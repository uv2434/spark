package com.yj.scala.transformation

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

/**
 * 按照需要重新分区，默认有shuffle
 */
object TestRepartition {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("TestRepartition")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array[String](
      "love1", "love2", "love3", "love4", "love5", "love6", "love7", "love8", "love9", "love10", "love11", "love12"
    ), 3)
    val rdd2 = rdd1.mapPartitionsWithIndex((index, iter) => {
      val listBuffer = new ListBuffer[String]()
      while (iter.hasNext) {
        val one = iter.next()
        listBuffer.append(s"rdd1 partition index = $index ,value = $one")
      }
      listBuffer.iterator
    })
    rdd2.foreach(println)

    val repartition = rdd2.repartition(2)
    val result = repartition.mapPartitionsWithIndex((index, iter) => {
      val listBuffer = new ListBuffer[String]()
      while (iter.hasNext) {
        val one = iter.next()
        listBuffer.append(s"repartition partition index = $index ,value = $one")
      }
      listBuffer.iterator
    })
    //    val arr = result.collect()
    result.foreach(println)

  }
}