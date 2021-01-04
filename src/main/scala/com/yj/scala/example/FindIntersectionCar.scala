package com.yj.scala.example

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 取出同时出现01区域和05区域的车辆
 */
object FindIntersectionCar {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("FindIntersectionCar")
      .setMaster("local"))

    val lines: RDD[String] = context.textFile("T:/code/spark_scala/data/monitor_flow_action")

    val car01: RDD[String] = lines.filter(line => "01".equals(line.split("\t")(7)))
      .map(line => line.split("\t")(3))
      .distinct()
    val car05: RDD[String] = lines.filter(line => "05".equals(line.split("\t")(7)))
      .map(line => line.split("\t")(3))
      .distinct()
    car01.intersection(car05)
      .foreach(println)
  }
}
