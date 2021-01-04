package com.yj.scala.sparksql

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
 * Tuple格式的DataSet加载DataFrame
 */
object ReadTupleDataSetToDF {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local")
      .appName("ReadTupleDataSetToDF")
      .getOrCreate()
    session.sparkContext.setLogLevel("Error")
    val ds: Dataset[String] = session.read.textFile("T:/code/spark_scala/data/pvuvdata")
    import session.implicits._
    val tupleDs: Dataset[(String, String, String, String, String, String, String)] = ds.map(line => {
      //126.54.121.136	浙江	2020-07-13	1594648118250	4218643484448902621	www.jd.com	Comment
      val arr: Array[String] = line.split("\t")
      (arr(0), arr(1), arr(2), arr(3), arr(4), arr(5), arr(6))
    })
    val frame: DataFrame = tupleDs.toDF("ip", "local", "date", "ts", "uid", "site", "operator")
    frame.createTempView("t")
    // pv
    session.sql(
      """
        | select site ,count(*) as pv from t group by site order by pv
        |""".stripMargin).show()
    // uv
    session.sql(
      """
        |select site,count(*) uv from (select distinct ip,site from t) t1 group by site order by uv
        |""".stripMargin).show()
  }
}
