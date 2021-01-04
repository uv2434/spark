package com.yj.scala.example

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * 根据 卡扣与摄像头基本关系数据 与  车流量监控数据  获取正常的卡扣信息。
 */
object FindNormalMonitorInfo {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("FindNormalMonitorInfo")
      .setMaster("local"))
    // 0006	68871
    val base: RDD[String] = context.textFile("T:/code/spark_scala/data/monitor_camera_info")
    //2020-07-14	0005	27430	鲁A17158	2020-07-14 14:39:48	194	2	05
    val monitor = context.textFile("T:/code/spark_scala/data/monitor_flow_action")

    val baseCount: RDD[(String, Int)] = base.map(line => (line.split("\t")(0), 1))
      .reduceByKey((v1, v2) => v1 + v2)
    val monitorCount: RDD[(String, Int)] = monitor.map(line => (line.split("\t")(1), line.split("\t")(2)))
      .distinct()
      .map(k => (k._1, 1))
      .reduceByKey((v1, v2) => v1 + v2)

    baseCount.join(monitorCount)
      .filter(tp => tp._2._1 == tp._2._2)
      .foreach(tp => println(s"正常的卡扣信息：${tp._1}"))

  }
}
