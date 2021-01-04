package com.yj.scala.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * groupByKey 把相同key的k，v格式的RDD分组
 */
object TestZip {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("zip test")
      .setMaster("local"))
    context.setLogLevel("error")
    val rdd1: RDD[Int] = context.parallelize(List(1, 2, 3, 4, 5))
    val rdd2 = context.parallelize(List("张三", "李四", "王5", "赵6", "田七"))
    val value1: RDD[(Int, String)] = rdd1.zip(rdd2)
    value1.foreach(println)
    val value2: RDD[(Int, Long)] = rdd1.zipWithIndex()
    value2.foreach(println)
  }
}
