package com.yj.scala.sparksql

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SQLContext, SparkSession}

object ReadJsonDataToDF {
  def main(args: Array[String]): Unit = {
    //        val conf = new SparkConf()
    //        conf.setMaster("local")
    //        conf.setAppName("test")
    //        val sc = new SparkContext(conf)
    //        val sqlContext = new SQLContext(sc)
    //        val hiveContext = new HiveContext(sc)
    //        val frame: DataFrame = sqlContext.read.json("./data/jsondata")

    val session: SparkSession = SparkSession.builder()
      .master("local")
      .appName("ReadJsonDataToDF")
      .getOrCreate()
    session.sparkContext.setLogLevel("Error")

    val frame: DataFrame = session.read.json("T:/code/spark_scala/data/spark/jsondata")
    //    frame.show()

    //    frame.select("name", "age").show(10)
    /**
     * 条件过滤
     */
    frame.select("name", "age")
      .filter("age is not null")
      .show()

    /**
     * select name ,age from table where age >= 19
     */
    frame.filter("age >= 19 and age is not null")
      .select(col("name"), col("age"))
      .show()

    /**
     * select name ,age + 10 as addage from table where age is not null
     */
    frame.select(col("name"), col("age").plus(10).alias("addage"))
      .filter("age is not null")
      .show()

    /**
     * 使用sql
     */
    frame.createTempView("t")
    frame.createOrReplaceTempView("t1")
    session.sql("select name ,age + 10 as addAge from t")
      .show()

    /**
     * 在其他session中访问全局session
     */
    val session2: SparkSession = session.newSession()
    session2.sql("select name ,age from t").show()

  }
}
