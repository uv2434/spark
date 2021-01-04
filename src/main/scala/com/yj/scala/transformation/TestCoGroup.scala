package com.yj.scala.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * cogroup:分组展示吧
 */
object TestCoGroup {
  def main(args: Array[String]): Unit = {

    val context: SparkContext = new SparkContext(new SparkConf()
      .setMaster("local")
      .setAppName("cogroup test"))
    val rdd1: RDD[(String, Int)] = context.parallelize(Array[(String, Int)](
      ("张三", 19),
      ("张三", 20),
      ("李四", 19),
      ("王5", 20),
      ("猪8戒", 500)
    ))
    val rdd2: RDD[(String, Int)] = context.parallelize(Array[(String, Int)](
      ("张三", 302),
      ("李四", 500),
      ("王5", 800),
      ("马六", 700)
    ))

    val value: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd1.cogroup(rdd2)
    value.foreach(v => {
      println(v)
    })

    context.stop()
  }
}
