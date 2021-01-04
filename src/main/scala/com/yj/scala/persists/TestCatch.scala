package com.yj.scala.persists

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * cache:将rdd数据持久化到内存中，懒执行算子，需要action触发执行
 */
case object TestCatch {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("cache test")
    val sc = new SparkContext(conf)
    var lines: RDD[String] = sc.textFile("T:/code/spark_scala/data/words")
    lines = lines.cache()
    val s1: Long = System.currentTimeMillis()
    val count1 = lines.count()
    val e1: Long = System.currentTimeMillis()
    println(s"从磁盘读取数据：count1 = $count1, time = ${e1 - s1}ms")
    val s2 = System.currentTimeMillis()
    val count2: Long = lines.count()
    val e2 = System.currentTimeMillis()
    println(s"从内存读取数据：count2 = $count2,time = ${e2 - s2}ms")


  }
}
