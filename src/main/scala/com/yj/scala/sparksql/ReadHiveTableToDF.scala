package com.yj.scala.sparksql

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * 读取Hive表中的数据加载DataFrame
 * 注意：
 * 1).需要开启Hive支持 ： .enableHiveSupport()
 * 2).直接写SparkSQL操作的就是Hive中的表数据
 * 3).可以将分析结果再次保存到Hive中，表不需要自己创建，会自动创建。
 * 4).直接加载Hive中的数据表 可以使用 session.table(xx) 得到DataFrame
 */
object ReadHiveTableToDF {
  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder().appName("test").enableHiveSupport().getOrCreate()
    session.sql("use spark")
    val df: DataFrame = session.sql("select count(*) from person")
    df.show()

    //创建 students 表
    session.sql(
      """
        | create table students(id int ,name string,age int) row format delimited fields terminated by ','
      """.stripMargin
    )
    //创建 score 表
    session.sql(
      """
        | create table scores(id int ,name string,score int) row format delimited fields terminated by ','
      """.stripMargin)

    //给students 表加载数据
    session.sql(
      """
        | load data local inpath '/software/test/students' into table students
      """.stripMargin)

    //给scores表加载数据
    session.sql(
      """
        | load data local inpath '/software/test/scores' into table scores
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
