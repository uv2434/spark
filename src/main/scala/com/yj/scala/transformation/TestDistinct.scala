package com.yj.scala.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * union:合并两个RDD
 * 分区数亦合并，物理合并
 */
object TestDistinct {
  /**
   * 去重
   *
   * @param args
   */
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("test distinct")
      .setMaster("local"))
    val rdd: RDD[String] = context.parallelize(List("a", "b", "e", "f", "a", "e"))
    val rdd2: RDD[String] = rdd.distinct()
    rdd2.foreach(println)

    context.stop()
  }
}
