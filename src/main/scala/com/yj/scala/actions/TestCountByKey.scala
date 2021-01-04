package com.yj.scala.actions

import org.apache.spark.{SparkConf, SparkContext}

/**
 * countByKey ： 针对K,V格式的RDD 进行操作，统计相同的key对应的数据条数有多少，返回一个Map<Key,keyCount>,放在Driver端。
 */
/**
 * countByValue : 可以针对K,V格式的RDD，也可以是非K,V格式的RDD。统计整体相同value的个数，返回一个Map<currentValue,currentValueCount>
 */
object TestCountByKey {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("count by key as test")
      .setMaster("local"))
    val rdd = context.parallelize(List[(String, Int)](
      Tuple2("张三", 24),
      ("李四", 19),
      ("王5", 20),
      ("猪8戒", 500),
      ("张三", 24)
    ))
    val map1: collection.Map[String, Long] = rdd.countByKey()
    map1.foreach(println)
    val map2: collection.Map[(String, Int), Long] = rdd.countByValue()
    map2.foreach(println)
  }
}
