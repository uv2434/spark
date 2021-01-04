package com.yj.scala.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object TestSample {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
    conf.setAppName("sample tes")
    conf.setMaster("local")

    val context: SparkContext = new SparkContext(conf)
    val lines: RDD[String] = context.textFile("T:/code/spark_scala/data/words")
    val result: RDD[String] = lines.sample(true, 0.1, 5l)
    result.foreach(println)
  }
}
