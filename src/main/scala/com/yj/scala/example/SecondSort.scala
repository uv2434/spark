package com.yj.scala.example

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

case class SecondSortKey(first: Int, second: Int) extends Ordered[SecondSortKey] {
  override def compare(that: SecondSortKey): Int = {
    if (this.first == that.first) {
      this.second - that.second
    } else {
      this.first - that.first
    }
  }
}

object SecondSort {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("second sort")
      .setMaster("local"))
    val lines: RDD[String] = context.textFile("T:/code/spark_scala/data/secondSort.txt")

    lines.map(line => {
      (SecondSortKey(
        line.split(" ")(0).toInt,
        line.split(" ")(1).toInt), line)
    }).sortByKey()
      .map(_._2)
      .foreach(println)
  }
}
