package com.yj.scala.highlevel

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}


/**
 * 累加器的代码
 * 累加器只能在Driver端定义，在Executor端累加，获取累加器的值 ： acc.value()
 */
object Accumulator {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("Accumulator")
      .setMaster("local"))
    /**
     * words中真实数量为10
     */
    val lines: RDD[String] = context.textFile("T:/code/spark_scala/data/words", 2)
    val accumulator: LongAccumulator = context.longAccumulator
    var i = 0
    lines.map(line => {
      i += 1
      println(i) // 两个分区分别打印1,2,3,4,5
      line
    }).collect() // collect触发懒算子执行
    println(s" i = $i") // 打印结果：i = 0

    lines.map(line => accumulator.add(1l))
      .collect()
    println(s"accumulator = ${accumulator.value}") // 打印结果为words中真实数量10
  }
}
