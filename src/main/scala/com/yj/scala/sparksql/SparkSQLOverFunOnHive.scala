package com.yj.scala.sparksql

import org.apache.spark.sql.SparkSession

object SparkSQLOverFunOnHive {
  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder().master("local")
      //      .config("hive.metastore.uris", "thrift://mynode1:9083")
      //      .config("hive.metastore.warehouse.dir", "/user/hive_ha/warehouse")
      .appName("test")
      .enableHiveSupport()
      .getOrCreate()
    session.sql("use spark")
    session.sql(
      """
        | create table sales(riqi string,leibie string,jine double) row format delimited fields terminated by '\t'
      """.stripMargin)

    session.sql(
      """
        | load data local inpath 'T:/code/spark_scala/data/spark/sales' into table sales
      """.stripMargin)

    val result = session.sql(
      """
        | select riqi,leibie,jine ,row_number() over(partition by leibie order by jine desc) as rank
        | from sales
      """.stripMargin)
    result.write.saveAsTable("salesresult")
  }
}
