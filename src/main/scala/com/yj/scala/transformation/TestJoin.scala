package com.yj.scala.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 父RDD的分区数与子RDD中分区数较多的那个保持一致
 *
 */
/**
 * leftOuterJoin
 * rightOuterJoin
 * fullOuterJoin
 */
case object TestJoin {
  def main(args: Array[String]): Unit = {

    val context: SparkContext = new SparkContext(new SparkConf()
      .setMaster("local")
      .setAppName("Join test"))
    val ageRDD: RDD[(String, Int)] = context.parallelize(Array[(String, Int)](
      ("张三", 18),
      ("李四", 19),
      ("王5", 20),
      ("猪8戒", 500)
    ))
    val scoreRDD: RDD[(String, Int)] = context.parallelize(Array[(String, Int)](
      ("张三", 300),
      ("李四", 500),
      ("王5", 800),
      ("马六", 700)
    ))
    val join: RDD[(String, (Int, Option[Int]))] = ageRDD.leftOuterJoin(scoreRDD)
    join.foreach(println)
    context.stop()
  }
}
