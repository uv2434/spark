package com.yj.scala

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf()
      .setAppName("spark-scala-wordCount")
      .setMaster("local"))

    val result: RDD[(String, Int)] = sc.textFile("T:/code/spark_scala/data/words")
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)

    result.foreach(println)

  }

  //    val conf = new SparkConf()
  //    conf.setMaster("local")
  //    conf.setAppName("spark_scala_wc")
  //    val sc = new SparkContext(conf)
  //    val lines: RDD[String] = sc.textFile("./data/words")
  //    val words: RDD[String] = lines.flatMap(line=>{line.split(" ")})
  //    val pairWords: RDD[(String, Int)] = words.map(word=>{new Tuple2(word,1)})
  //
  //    /**
  //      * reduceByKey
  //      *  1.按照key对数据进行分组
  //      *  2.对每个组内的value进行操作
  //      */
  //
  //    val result: RDD[(String, Int)] = pairWords.reduceByKey((v1, v2)=>{v1+v2})
  //
  //    result.foreach(tp=>{println(tp)})
}
