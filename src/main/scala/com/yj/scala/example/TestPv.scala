package com.yj.scala.example

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object TestPv {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("pv test")
      .setMaster("local"))
    val lines: RDD[String] = context.textFile("T:/code/spark_scala/data/pvuvdata")
    // 获取网站
    //    210.197.131.72	海南	2020-10-28	1603879315603	3761743856363679839	www.suning.com	Login

    val pairSiteInfo: RDD[(String, Int)] = lines.map(line => (line.split("\t")(5), 1))
    val reduceInfo: RDD[(String, Int)] = pairSiteInfo.reduceByKey((v1, v2) => {
      v1 + v2
    })
    val pv: RDD[(String, Int)] = reduceInfo.sortBy(_._2, false)
    pv.foreach(println)

    /**
     * uv  去掉相同ip的网址累计
     */
    lines.map(line =>
      (line.split("\t")(0) + "_" + line.split("\t")(5), 1))
      .distinct()
      .map(one => (one._1.split("_")(1), 1))
      .reduceByKey((v1, v2) => v1 + v2)
      .sortBy(_._2, false)
      .foreach(println)
  }
}
