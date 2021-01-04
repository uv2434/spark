package com.yj.scala.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object TestFilter {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("filter test")
    val sc = new SparkContext(conf)
    val lines: RDD[String] = sc.textFile("T:/code/spark_scala/data/words")
    //filter
    val result: RDD[String] = lines.filter(line => {
      "hello spark".equals(line)
    })
    result.foreach(println)

  }
}
