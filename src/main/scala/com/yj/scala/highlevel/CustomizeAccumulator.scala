package com.yj.scala.highlevel

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.util.AccumulatorV2

case class Student(var studentCount: Int, var studentTotalScore: Int)

class CustomizeAccumulator extends AccumulatorV2[Student, Student] {
  /**
   * 这是driver侧
   */
  private var student = Student(10, 0)

  /**
   * 判断每个分区中的累加器对象是否是初始值，需要和reset保持一致
   */
  override def isZero: Boolean = {
    student.studentCount == 0 && student.studentTotalScore == 0
  }

  /**
   * 复制累加器，
   *
   * @return
   */
  override def copy(): AccumulatorV2[Student, Student] = {
    val accumulator: CustomizeAccumulator = new CustomizeAccumulator()
    accumulator.student = this.student
    accumulator
  }

  /**
   * 给每个分区的student设置初始值
   */
  override def reset(): Unit = {
    student = Student(0, 0)
  }

  /**
   * 在每个RDD分区里处理
   *
   * @param v
   */
  override def add(v: Student): Unit = {
    student.studentCount += v.studentCount
    student.studentTotalScore += v.studentTotalScore
  }

  /**
   * 将每个分区的结果与driver端的自定义累加器进行累加
   *
   * @param other
   */
  override def merge(other: AccumulatorV2[Student, Student]): Unit = {
    val accumulator: CustomizeAccumulator = other.asInstanceOf[CustomizeAccumulator]
    student.studentCount += accumulator.student.studentCount
    student.studentTotalScore += accumulator.student.studentTotalScore
  }

  /**
   * 返回累加器最终返回结果值
   *
   * @return
   */
  override def value: Student = student
}

object CustomizeAccumulatorTest {
  def main(args: Array[String]): Unit = {
    val context: SparkContext = new SparkContext(new SparkConf()
      .setAppName("CustomizeAccumulatorTest")
      .setMaster("local[2]"))
    val acc: CustomizeAccumulator = new CustomizeAccumulator()
    context.register(acc)
    val students: RDD[String] = context.parallelize(List("A 88", "B 89", "C 48", "D 66", "E 99", "F 60"), 3)
    students.map(s => {
      val score: Int = s.split(" ")(1).toInt
      acc.add(Student(1, score))
      s
    }).collect()
    println(s"student count = ${acc.value.studentCount},student total scores = ${acc.value.studentTotalScore}")

    StorageLevel
  }
}
