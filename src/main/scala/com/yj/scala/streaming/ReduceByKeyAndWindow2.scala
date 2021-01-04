package com.yj.scala.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
 * reduceByKeyAndWindow : 优化方式处理数据,在集群处理数据能力不足时使用
 * 1).reduceByKeyAndWindow(fun1,fun2,窗口长度，滑动间隔)
 * 2).优化方式需要设置checkpoint 保存状态
 * 3).进来k对应的value值减去出去key对应的value值即可
 */
object ReduceByKeyAndWindow2 {
  def main(args: Array[String]): Unit = {
    val context: StreamingContext = new StreamingContext(new SparkConf()
      .setMaster("local[2]")
      .setAppName("ReduceByKeyAndWindow2"), Durations.seconds(5))
    context.sparkContext.setLogLevel("Error")
    context.checkpoint("T:/code/spark_scala/data/sparkstreaming/ck")
    val lines = context.socketTextStream("node4", 9999)
    lines.flatMap(line => line.split(" "))
      .map((_, 1))
      .reduceByKeyAndWindow((v1: Int, v2: Int) => {
        v1 + v2
        // 每隔 滑动间隔  统计最近 窗口长度内的数据，按照指定逻辑计算。
      }, (v1: Int, v2: Int) => {
        v1 - v2
      }, Durations.seconds(15), Durations.seconds(5))
      .print()
    context.start()
    context.awaitTermination()
  }
}
