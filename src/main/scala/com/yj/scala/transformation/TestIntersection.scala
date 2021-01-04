package com.yj.scala.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * intersection: 两个RDD取交集
 * 分区数和较多的一个RDD保持一致
 */
object TestIntersection {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("test intersection")
      .setMaster("local"))
    val RDD1: RDD[String] = context.parallelize(Array("a", "b", "c", "d", "u"), 2)
    val RDD2: RDD[String] = context.parallelize(Array("a", "b", "e", "f", "u"), 3)

    val result: RDD[String] = RDD1.intersection(RDD2)
    result.foreach(println)
    println(result.getNumPartitions)
    context.stop()
  }
}
