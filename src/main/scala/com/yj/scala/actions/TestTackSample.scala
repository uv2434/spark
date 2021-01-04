package com.yj.scala.actions

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

object TestTackSample {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("takeSample test")
      .setMaster("local"))
    val rdd1 = context.parallelize(List(
      "张三",
      "李四",
      "王5",
      "猪8戒"
    ), 2)
    val strings: Array[String] = rdd1.takeSample(true, 3, 100l)
    strings.foreach(println)
  }
}
