package com.yj.scala.highlevel

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 广播变量使用：
 * 1.不能将RDD广播出去，广播变量必须在Driver端定义
 * 2.在Executor端不能改变广播变量的值
 */
object BroadCastTest {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("BroadCastTest")
      .setMaster("local"))
    val lines: RDD[String] = context.textFile("T:/code/spark_scala/data/words", 10)
    val broadcastList: Broadcast[List[String]] = context.broadcast(List("疑是地上霜", "床前明月光"))
    lines.filter(line => broadcastList.value.contains(line))
      .foreach(println)
  }
}
