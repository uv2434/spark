package com.yj.scala.actions

import org.apache.spark.{SparkConf, SparkContext}

/**
 * collectAsMap :作用于k v格式的rdd，回收至driver端形成一个map
 */
object TestCollectAsMap {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("collect as test")
      .setMaster("local"))
    val rdd1 = context.parallelize(List[(String, Int)](
      Tuple2("张三", 22),
      ("李四", 19),
      ("王5", 20),
      ("猪8戒", 500),
      ("张三", 24)
    ))
    val map: collection.Map[String, Int] = rdd1.collectAsMap()
    map.foreach(println)

  }
}
