package com.yj.scala.example

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.util.control.Breaks

/**
 * 分组取topN问题，分好组之后，两种获取topN数据方式：
 * 1.原生集合排序,问题：数据量多占用Eecutor内存多，有可能导致Executor oom问题。
 * 2.定长数组方式
 *
 * spark的mapPartitionsWithIndex中iterator尽量不要使用toList，
 * 原因：toList相当于将迭代数据进行了缓存，容易导致OutOfMemory的异常，
 * iterator是流式的处理，处理完一条记录才会去读取下一条记录并且会丢弃已读的记录，无法重复使用；
 * 而iterator.toList会将所有的记录进行缓存，便于重复使用。
 */
object GroupTopN {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("group top to N")
      .setMaster("local"))
    val lines: RDD[String] = context.textFile("T:/code/spark_scala/data/score.txt")

    /**
     * 原生集合排序 有可能占用 Executor端的内存比较多，导致内存OOM问题
     */
    lines.map(line => (line.split(" ")(0), line.split(" ")(1).toInt))
      .groupByKey()
      .map(line => (line._1, line._2.toList.sorted(Ordering.Int.reverse)))
      .foreach(x => {
        println(s"class = ${x._1},socres = ${x._2.slice(0, 3)}")
      })

    /**
     * 定长数组方式,内存中的list最多只有定长的3条数据，建议在大数据量的时候使用
     */
    lines.map(line => (line.split(" ")(0), line.split(" ")(1).toInt))
      .groupByKey()
      .foreach(line => {
        val top = new Array[Int](3)
        val b = new Breaks
        val iter = line._2.iterator
        while (iter.hasNext) {
          val next = iter.next()
          b.breakable {
            for (i <- 0 until top.length) {
              if (top(i) == 0) {
                top(i) = next
                b.break()
              } else if (next > top(i)) {
                for (j <- 2 until(i, -1)) {
                  top(j) = top(j - 1)
                }
                top(i) = next
                b.break()
              }
            }
          }
        }
        println(s"class = ${line._1},socres = ${top.toList}")
      })
  }
}
