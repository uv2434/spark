package com.yj.scala.persists

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 将RDD数据持久化到磁盘中，需要指定磁盘目录;
 * 懒执行，需要action算子触发
 * 2.checkpoint目录由外部的存储系统管理，不由Spark框架管理。当Spark application 执行完成之后，数据目录不会被清空。
 * persist 目录由Spark框架管理，当application执行完成之后数据是被清空的。
 * 3.checkpoint可以切断RDD的依赖关系，数据当application执行完成之后还会保留，多用于状态管理。
 * checkpoint执行流程：
 * 当Spark job执行完成之后，Spark框架会从后往前回溯，找到checkpointRDD 做标记，当回溯完成之后，Spark 重新启动一个job
 * 来计算checkpointRDD的结果，将结果持久化到指定的目录中。
 * 优化：对哪个RDD进行checkpoint时，可以先对这个RDD进行cache()。
 */
object TestCheckPoint {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("TestCheckPoint")
      .setMaster("local"))
    val lines: RDD[String] = context.textFile("T:/code/spark_scala/data/words")
    context.setCheckpointDir("T:/code/spark_scala/data")
    lines.checkpoint()
    lines.count()
  }
}
