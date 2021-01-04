package com.yj.scala.sparksql.example

import org.apache.spark.sql.SparkSession

/**
 * 读取分析json格式的Array ：
 * {"name":"zhangsan","age":18,"scores":[{"xueqi":1,"yuwen":98,"shuxue":90,"yingyu":100},{"xueqi":2,"yuwen":98,"shuxue":78,"yingyu":100}]}
 * {"name":"lisi","age":19,"scores":[{"xueqi":1,"yuwen":58,"shuxue":50,"yingyu":78},{"xueqi":2,"yuwen":56,"shuxue":76,"yingyu":13}]}
 * {"name":"wangwu","age":17,"scores":[{"xueqi":1,"yuwen":18,"shuxue":90,"yingyu":45},{"xueqi":2,"yuwen":76,"shuxue":42,"yingyu":45}]}
 * {"name":"zhaoliu","age":20,"scores":[{"xueqi":1,"yuwen":68,"shuxue":23,"yingyu":63},{"xueqi":2,"yuwen":23,"shuxue":45,"yingyu":87}]}
 * {"name":"tianqi","age":22,"scores":[{"xueqi":1,"yuwen":88,"shuxue":91,"yingyu":41},{"xueqi":2,"yuwen":56,"shuxue":79,"yingyu":45}]}
 * explode(集合) : 一对多将集合中数据转换成一行行的数据
 */
object ReadJsonArrayData {
  def main(args: Array[String]): Unit = {
    val session = SparkSession
      .builder()
      .appName("test")
      .master("local")
      .getOrCreate()
    val frame = session.read.json("T:/code/spark_scala/data/spark/jsonArrayFile")
    import session.implicits._
    import org.apache.spark.sql.functions._

    val df1 = frame.select(col("name"), frame.col("age"), explode(frame.col("scores")).as("el"))
    df1.select($"name",
      $"age",
      col("el.xueqi"),
      col("el.yuwen"),
      col("el.shuxue"),
      col("el.yingyu"))
      .show()

    //    frame.createTempView("temp")
    //    val df = session.sql(
    //      """
    //        | select
    //        |   name,age ,el.xueqi,el.yuwen,el.shuxue,el.yingyu
    //        | from
    //        | (select name,age,explode(scores) as el  from temp ) t
    //      """.stripMargin).show()
  }
}
