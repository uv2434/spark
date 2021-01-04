package com.yj.scala.transformation

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object TestSortByKey {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("test")
    val sc = new SparkContext(conf)
    val lines: RDD[String] = sc.textFile("T:/code/spark_scala/data/words")
    val words = lines.flatMap(line => {
      line.split(" ")
    })
    val pairWords = words.map(word => {
      Tuple2(word, 1)
    })
    val reduceResult: RDD[(String, Int)] = pairWords.reduceByKey((v1, v2) => {
      v1 + v2
    })

    val transRDD1: RDD[(Int, String)] = reduceResult.map(tp => {
      tp.swap
    })
    val sortedRDD = transRDD1.sortByKey(false)
    val result: RDD[(String, Int)] = sortedRDD.map(tp => {
      tp.swap
    })
    result.foreach(println)

  }
}
