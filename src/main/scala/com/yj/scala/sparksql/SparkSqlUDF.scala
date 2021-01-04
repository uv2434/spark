package com.yj.scala.sparksql

import org.apache.spark.sql.SparkSession

/**
 * SparkSQL  UDF : User Defined Function - 用户自定义函数
 */
object SparkSqlUDF {
  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder().master("local").appName("test").getOrCreate()
    val nameList = List[String]("zhangsan", "lisi", "wangwu", "maliu")
    import session.implicits._
    val frame = nameList.toDF("name")
    frame.createTempView("info")

    /**
     * 自定义 namelength 函数来计算每个name的长度。
     *
     */
    session.udf.register("namelength", (n: String, l: Int) => {
      l + n.length
    })
    session.sql(
      """
        | select name,namelength(name,10) as nl from info order by nl desc
        |""".stripMargin)
      .show()
  }
}
