package com.yj.scala.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
 * 找出每个类别价格最高的前3名，并且找出对应的日期
 *
 * Over  开窗函数：
 * row_number() over(partition by X1 order by X2  ) as rank
 * 对表中的数据按照X1分组，按照X2排序，对每个分组内的数据标号，每个分组内的标号是连续的，标号在每个分组内从1开始。
 * rank() over(partition by X1 order by X2  ) as rank
 * 对表中的数据按照X1分组，按照X2排序，对每个分组内的数据标号，每个分组内的标号不连续且相同的数据标号相同，标号在每个分组内从1开始。
 * dense_rank() over(partition by X1 order by X2  ) as rank
 * 对表中的数据按照X1分组，按照X2排序，对每个分组内的数据标号，每个分组内的标号连续且相同的数据标号相同，标号在每个分组内从1开始。
 *
 * 1	A	100       ---1
 * 2	A	20        ---2
 * 7	A	18        ---3
 * 9	A	4         ---4
 * 4	B	500       ---1
 * 2	B	200       ---2
 * 8	B	300       ---3
 * 5	B	50        ---4
 *
 */
object SaleInfo {
  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder()
      .master("local")
      .appName("test")
      .getOrCreate()
    val sc = session.sparkContext
    sc.setLogLevel("Error")
    import session.implicits._
    val lines = sc.textFile("T:/code/spark_scala/data/spark/sales")
    val saleInfoRDD: RDD[SaleInfo] = lines.map(line => {
      val arr = line.split("\t")
      SaleInfo(arr(0), arr(1), arr(2).toDouble)
    })
    val frame = saleInfoRDD.toDF()
    frame.createTempView("t")

    session.sql(
      """
        | select * from
        | (select
        |   riqi,leibie,jine,rank() over(partition by leibie order by jine desc ) as rank
        | from t order by leibie) temp
        | where temp.rank <= 3
      """.stripMargin)
      .show(100)
  }
}

case class SaleInfo(riqi: String, leibie: String, jine: Double)