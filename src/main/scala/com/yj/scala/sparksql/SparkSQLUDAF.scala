package com.yj.scala.sparksql


import org.apache.spark.SparkConf
import org.apache.spark.sql.{Dataset, Encoder, Encoders, SparkSession}
import org.apache.spark.sql.expressions.Aggregator


case class Employee(name: String, salary: Long)

case class Aver(var sum: Long, var count: Int)

/**
 * 求平均工资
 */
class Average extends Aggregator[Employee, Aver, Double] {
  //初始化每个分区中的共享变量
  override def zero: Aver = Aver(0L, 0)

  //每个分区中每一条数据聚合的时候需要调的方法
  override def reduce(b: Aver, a: Employee): Aver = {
    b.sum += a.salary
    b.count += 1
    b
  }

  //将每个分区的输出 合并 形成最后的数据
  override def merge(b1: Aver, b2: Aver): Aver = {
    b1.sum += b2.sum
    b1.count += b2.count
    b1
  }

  //给出计算结果
  override def finish(reduction: Aver): Double = reduction.sum.toDouble / reduction.count

  //主要用于对共享变量进行编码
  override def bufferEncoder: Encoder[Aver] = Encoders.product

  //主要用于将输出进行编码
  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}

/**
 * 求平均工资
 */
object Average {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("udaf").setMaster("local[*]")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    //读入数据并创建DataSet变量
    import spark.implicits._
    val aver = new Average().toColumn.name("average")
    //    val employee = spark.read.json("D:\\idea_workspace2020\\spark\\sparksql\\doc\\employees.json").as[Employee]
    val employee: Dataset[Employee] = List[Employee](
      Employee("张三", 20000),
      Employee("李四", 18000),
      Employee("王五", 21000),
      Employee("赵六", 22000),
      Employee("田七", 25000))
      .toDS()
      .as[Employee]
    employee.select(aver).show()
    spark.stop()
  }
}