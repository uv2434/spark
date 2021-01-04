package com.yj.scala.actions

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

object TestForeachPartition {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("ForeachPartition test")
      .setMaster("local"))
    val rdd1 = context.parallelize(List(
      "张三",
      "李四",
      "王5",
      "猪8戒"
    ), 2)

    /**
     * foreach一般不会把数据全部迭代出来一起处理，数据量不确定，容易内存溢出。
     * 一般就是拿到一条数据就进行下处理
     */
    rdd1.foreach(r => {
      val list = new ListBuffer[String]()
      list += r
      if (list.size >= 10000) {
        println("插入数据库连接" + list.toString())
        println("关闭数据库连接")
        //丢失了如果list<10000时的数据，因此一般每条数据逐一处理
      }
      println("插入数据库连接" + r)
      println("关闭数据库连接")
    })

    /**
     * 最后说下这两个action的区别:
     *
     * Foreach与foreachPartition都是在每个partition中对iterator进行操作,
     *
     * 不同的是,foreach是直接在每个partition中直接对iterator执行foreach操作,而传入的function只是在foreach内部使用,
     *
     * 而foreachPartition是在每个partition中把iterator给传入的function,让function自己对iterator进行处理.
     */
    rdd1.foreachPartition(iter => {
      val list = new ListBuffer[String]()
      println("创建数据库连接")
      iter.foreach(i => {
        list += i
        if (list.size > 10000) {
          println("插入数据库连接" + list.toString())
          println("关闭数据库连接")

        }
      })
      if (list.size > 0) {
        println("插入数据库连接" + list.toString())
        println("关闭数据库连接")
      }
    })
  }
}
