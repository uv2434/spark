package com.yj.scala.test

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 24.写代码计算聚合平均值
 * 当数据集以键值对形式组织的时候，聚合具有相同键的元素进行统计是很常见的操作。根据给出的示例，求每个键平均值的代码及结果。
 * key	value
 * panda	0
 * pink	3
 * pirate	3
 * panda	1
 * pink	4
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("test")
    val sc = new SparkContext(conf)
    sc.setLogLevel("Error")

    sc.parallelize(List(
      ("a", 10),
      ("b", 4),
      ("a", 10),
      ("b", 20)))
      .mapValues(x => (x, 1))
      //      .map(x => (x._1, (x._2, 1)))
      .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))
      //      .map(x => (x._1, x._2._1 / x._2._2))
      .mapValues(x => x._1 / x._2)
      .foreach(println)

  }
}
