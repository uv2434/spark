package com.yj.scala.actions

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * foreach:对RDD数据进行遍历
 * count:对数据进行计数，统计RDD的行数
 * take:take(num) 可以获取RDD中前num条数据，会将数据获取到Driver端。
 * first:获取RDD中的第一条数据
 * collect:将数据回收到Driver端，放入集合中。
 */
object Foreach {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("foreach test")
    val context: SparkContext = new SparkContext(conf)
    val lines: RDD[String] = context.textFile("T:/code/spark_scala/data/words")

    //    lines.foreach(println)
    //    val count: Long = lines.count()
    //    println(count)
    //    lines.take(5).foreach(println)
    //    val first: String = lines.first()
    //    println(s"The first is $first")
    val strings: Array[String] = lines.collect()
    strings.foreach(println)
  }
}
