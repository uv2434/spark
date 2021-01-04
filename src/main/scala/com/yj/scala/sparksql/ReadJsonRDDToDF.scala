package com.yj.scala.sparksql

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
 * 读取json格式的RDD就是RDD中的String是一个json字符串。
 * Spark2.0 之前 session.read.json(JSONRDD)
 * Spark2.0之后 session.read.json(JSONDATASET)
 */
object ReadJsonRDDToDF {
  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder().master("local").appName("test").getOrCreate()

    val jsonArr: Array[String] = Array[String](
      "{\"name\":\"zhangsan\",\"age\":18}",
      "{\"name\":\"lisi\",\"age\":19}",
      "{\"name\":\"wangwu\",\"age\":20}",
      "{\"name\":\"maliu\",\"age\":21}",
      "{\"name\":\"tianqi\",\"age\":22}"
    )
    import session.implicits._
    val jsonDataset: Dataset[String] = jsonArr.toList.toDS()

    val df1: DataFrame = session.read.json(jsonDataset)
    df1.createTempView("t")
    val df2: DataFrame = session.sql("select name,age from t where name like '%zhangsan%'")
    df2.show()

    //    frame.show()

    /**
     * Spark2.0 之前处理方式
     */
    //    val sc: SparkContext = session.sparkContext
    //    sc.setLogLevel("Error")
    //    val jsonRDD: RDD[String] = sc.parallelize(jsonArr)
    //    val frame: DataFrame = session.read.json(jsonRDD)
    //    frame.show()
  }
}
