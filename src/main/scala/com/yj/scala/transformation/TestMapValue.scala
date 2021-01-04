package com.yj.scala.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * groupByKey 把相同key的k，v格式的RDD分组
 */
object TestMapValue {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("map test")
      .setMaster("local"))
    val rdd1 = context.parallelize(List[(String, Int)](
      Tuple2("张三", 18),
      ("李四", 19),
      ("王5", 20),
      ("猪8戒", 500)
    ))
    val value: RDD[(String, String)] = rdd1.mapValues(_ + "hello")
    value.foreach(println)
  }
}
