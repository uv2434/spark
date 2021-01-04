package com.yj.scala.example

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 根据车流量监控表数据，找出
 * 1.当天通过车辆数最多的top5卡扣信息
 * 2.找出这top5卡扣下通过所有车辆的信息
 */
object FindTop5MonitorInfos {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("FindTop5MonitorInfos")
      .setMaster("local"))
    val lines: RDD[String] = context.textFile("T:/code/spark_scala/data/monitor_flow_action")
    val top5: Array[String] = lines.map(line => (line.split("\t")(1), 1))
      .reduceByKey((v1, v2) => v1 + v2)
      .sortBy(tp => tp._2, false)
      .map(_._1)
      .take(5)
    top5.foreach(println)

    /**
     * 0001
     * 0007
     * 0005
     * 0006
     * 0002
     */
    //根据通过车辆数最高top5卡扣信息，找出这些卡扣下通过的所有车辆信息
    lines.filter(line => {
      top5.contains(line.split("\t")(1))
    }).foreach(println)
  }

}
