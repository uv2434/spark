package com.yj.scala.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

/**
 * mapPartitions: 比map算子性能更高
 * 便利RDD中每一个分区，对每个分区的数据进行处理
 */
object TestMapPartition {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("test MapPartition")
      .setMaster("local"))
    val rdd1: RDD[String] = context.parallelize(List("a", "b", "c", "d", "e"), 2)
    val strings: RDD[String] = rdd1.mapPartitions(iter => {
      val listBuffer = new ListBuffer[String]()
      println("建立数据库连接... ...")
      while (iter.hasNext) {
        val next = iter.next()
        listBuffer.append(next + "#")
      }
      println("批量插入数据库.. ..." + listBuffer.toString())
      println("关闭数据库连接... ...")
      listBuffer.iterator
    })
    strings.collect().foreach(println)
    context.stop()
  }
}
