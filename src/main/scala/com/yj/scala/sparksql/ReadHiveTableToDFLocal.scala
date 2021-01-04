package com.yj.scala.sparksql

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * 本地操作Hive 读取Hive表数据加载DataFrame:
 * 注意：
 * 1).如果Hive表中的数据量大，不建议使用本地方式读取，可以用于测试。
 * 2).在本地连接Hive ,操作Hive中的数据加载DataFrame,需要配置 hive.metastore.uris 配置项。
 *
 */
object ReadHiveTableToDFLocal {
  def main(args: Array[String]): Unit = {
    //    val warehouseLocation = new File("spark-warehouse").getAbsolutePath
    val session = SparkSession.builder()
      .master("local")
      .appName("test")
      //      .config("hive.metastore.uris", "thrift://node1:9083")
      //      .config("hive.metastore.warehouse.dir", "/user/hive_ha/warehouse")
      .enableHiveSupport()
      .getOrCreate()

    session.sql("use spark")

    //创建 students 表
    session.sql(
      """
        | create table students(id int ,name string,age int) row format delimited fields terminated by ','
      """.stripMargin)
    //创建 score 表
    session.sql(
      """
        | create table scores(id int ,name string,score int) row format delimited fields terminated by ','
      """.stripMargin)
    //给students 表加载数据
    session.sql(
      """
        | load data local inpath 'T:/code/spark_scala/data/spark/students' into table students
      """.stripMargin)
    //给scores表加载数据
    session.sql(
      """
        | load data local inpath 'T:/code/spark_scala/data/spark/scores' into table scores
      """.stripMargin)

    //读取Hive表的数据，进行统计分析
    val result = session.sql(
      """
        | select a.id,a.name,a.age,b.score from students a join scores b on a.id = b.id where b.score >= 200
      """.stripMargin)
    result.show()

    //将结果数据保存在Hive表中
    result.write.mode(SaveMode.Overwrite).saveAsTable("goodinfos")
  }
}
