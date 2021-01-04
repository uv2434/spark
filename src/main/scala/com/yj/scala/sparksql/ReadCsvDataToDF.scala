package com.yj.scala.sparksql

import org.apache.spark.sql.SparkSession

/**
 * 读取csv格式的文件。
 */
object ReadCsvDataToDF {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local[1]")
      .appName("ReadCsvDataToDF")
      .getOrCreate()
    // 添加头
    session.read.option("header", true)
      .csv("T:/code/spark_scala/data/spark/csvdata.csv")
      .show()
  }
}
