package com.yj.scala.sparksql.example

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * +---+----------+---+
 * |uid|   accDate|dur|
 * +---+----------+---+
 * |111|2019-06-20|  1|
 * |111|2019-06-21|  2|
 * |111|2019-06-22|  3|
 * |222|2019-06-20|  4|
 * |222|2019-06-21|  5|
 * |222|2019-06-22|  6|
 * |333|2019-06-20|  7|
 * |333|2019-06-21|  8|
 * |333|2019-06-22|  9|
 * |444|2019-06-23| 10|
 * +---+----------+---+
 * 根据用户访问网站的浏览时长统计以下信息：
 * 1.统计每个用户每天访问网站的总时长（当天总时长是累加之前日期的）
 *
 * 2.统计每个用户当前天及前一天访问网站总时长
 *
 * 3.统计每个用户当前天访问的网站时长（当前天统计的时长除了当前天访问的总时长还包含前一天和后一天的访问总时长）
 *
 * 4.统计每个用户访问网站的总时长
 */
object AccInfo {
  def main(args: Array[String]): Unit = {
    val session = SparkSession
      .builder()
      .master("local")
      .appName("AccInfo")
      .getOrCreate()

    val sc = session.sparkContext
    sc.setLogLevel("Error")
    val infos = sc.textFile("T:/code/spark_scala/data/spark/userAccInfo.txt")

    val accInfoRDD: RDD[AccInfo] = infos.map(info => {
      val arr = info.split("\t")
      val uid = arr(0)
      val accDate = arr(1)
      val dur = arr(2).toInt
      AccInfo(uid, accDate, dur)
    })
    import session.implicits._
    val frame = accInfoRDD.toDF()
    frame.createTempView("accInfo")

    session.sql(
      """
        | select uid,accDate,sum(dur) over(partition by uid order by accDate) as current_day_dur
        | from accInfo
      """.stripMargin).show()

    session.sql(
      """
        | select uid,accDate,sum(dur) over(partition by uid order by accDate rows between 1 preceding and current row ) as totalDur
        | from accInfo
      """.stripMargin).show()

    session.sql(
      """
        | select uid,accDate,sum(dur) over(partition by uid order by accDate rows between 1 preceding and 1 following) as totalDur
        | from accInfo
      """.stripMargin).show()

    session.sql(
      """
        | select uid,sum(dur) as totaldur
        | from accInfo
        | group by uid
      """.stripMargin).show()

    session.sql(
      """
        | select uid,accdate,sum(dur) over(partition by uid ) as totaldur
        | from accInfo
      """.stripMargin).show()

  }
}

case class AccInfo(uid: String, accDate: String, dur: Int)
