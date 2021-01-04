package com.yj.scala.streaming

import org.apache.spark.{SparkConf}
import org.apache.spark.streaming.{Durations, StreamingContext}


/**
 * transform :
 * 1).DStream的Transformation算子，可以获取DStream中的RDD，对RDD进行RDD的Transformation类算子转换，也可以使用Action算子。
 * 但是一定最后需要返回一个RDD，将返回的RDD会包装到一个DStream中。
 * 2).与foreachRDD类似，算子内的代码获取的RDD算子外的代码是在Driver端执行的，可以通过这个算子来动态的改变广播变量的值
 */
object TestTransform {
  def main(args: Array[String]): Unit = {
    val context: StreamingContext = new StreamingContext(new SparkConf()
      .setMaster("local[2]")
      .setAppName("TestTransform"), Durations.seconds(5))

    val lines = context.socketTextStream("node4", 9999)

    lines.transform(rdd => {
      /**
       * 与foreachRDD类似，算子内的代码获取的RDD算子外的代码是在Driver端执行的，
       * 可以通过这个算子来动态的改变广播变量的值
       */
      println("***** Driver ******")
      rdd.filter(r => !r.contains("zhangsan"))
    }).print()
    context.start()
    context.awaitTermination()
  }
}
