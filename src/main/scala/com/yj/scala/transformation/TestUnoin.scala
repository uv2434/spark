package com.yj.scala.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * union:合并两个RDD
 * 分区数亦合并，物理合并
 */
object TestUnoin {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("test union")
      .setMaster("local"))
    val RDD1: RDD[String] = context.parallelize(Array("a", "b", "c", "d"), 2)
    val RDD2: RDD[String] = context.parallelize(Array("a", "b", "e", "f"), 3)

    val result: RDD[String] = RDD1.union(RDD2)
    result.foreach(println)
    println(result.getNumPartitions)
    context.stop()
  }
}
