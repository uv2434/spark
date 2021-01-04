package com.yj.scala.actions

import org.apache.spark.{SparkConf, SparkContext}

/**
 * takeOrdered:针对RDD，返回最小的前num个值，放在driver端
 * top
 */
object TestTakeOrder {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("takeOrder test")
      .setMaster("local"))
    val rdd = context.parallelize(List(
      1, 3, 6, 6, 9, 10, 6, 38, 100
    ), 2)
    val ints1: Array[Int] = rdd.takeOrdered(3)
    ints1.foreach(println)
    val ints2: Array[Int] = rdd.top(3)
    ints2.foreach(println)

  }
}
