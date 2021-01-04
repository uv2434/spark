package com.yj.scala.sparksql

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * 读取Parquet格式的数据加载DataFrame
 * 注意：
 * 1).parquet是一种列式存储格式，默认是有压缩。Spark中常用的一种数据格式
 * 2).读取Parquet格式的数据两种方式
 * 3).可以将DataFrame保存成Json或者Pauquet格式数据,注意保存模式
 */
object ReadParquetFileToDF {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local")
      .appName("ReadParquetFileToDF")
      .getOrCreate()

    session.sparkContext.setLogLevel("Error")
    val frame: DataFrame = session.read.json("T:/code/spark_scala/data/spark/jsondata")

    /**
     * SaveMode:
     * Append: 追加写数据
     * ErrorIfExists : 存在就报错
     * Ignore : 忽略
     * Overwrite : 覆盖写数据
     */
    frame.write.mode(SaveMode.Overwrite).parquet("T:/code/spark_scala/data/spark/parquet")
    //读取Parquet格式的数据加载DataFrame
    val df = session.read.format("parquet").load("T:/code/spark_scala/data/spark/parquet")
//    df.write.json("T:/code/spark_scala/data/spark/resultJson")
    df.show(100)
    val l: Long = df.count()
    println(s"total count = $l")
  }
}
