package com.yj.scala.streaming

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
 * SparkStreaming 3.0.1 + Kafka 0.12 Direct模式整合
 */
object SparkStreamingReadKafka {
  def main(args: Array[String]): Unit = {
    val context: StreamingContext = new StreamingContext(new SparkConf()
      .setMaster("local[2]")
      .setAppName("SparkStreamingReadKafka"), Durations.seconds(5))
    context.sparkContext.setLogLevel("Error")

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "node1:9092,node2:9092,node3:9092", // kafka集群
      "key.deserializer" -> classOf[StringDeserializer], //指定读取kafka数据 key的序列化格式
      "value.deserializer" -> classOf[StringDeserializer], //指定读取Kafka 数据value的序列化格式
      "group.id" -> "firstGroup", // 指定消费者组，利用kafka管理消费者offset时，需要以组为单位存储。
      /**
       * latest :连接kafka之后,读取向kafka中生产的数据
       * earliest : 如果kafka中有当前消费者组存储的消费者offset,就接着这个位置读取数据继续消费。如果kafka中没有当前消费者组对应的消费offset,
       * 就从最早的位置消费数据。
       */
      "auto.offset.reset" -> "earliest",
      //是否开启自动向Kafka 提交消费者offset,周期5s
      // 一般设置为false，而采用数据处理完成后异步提交方法，确保数据不会未处理完成而丢失
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val topics = Array[String]("myTopic")
    val ds: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
      context,
      PreferConsistent, //接收Kafka数据的策略，这种策略是均匀将Kafka中的数据接收到Executor中处理。
      Subscribe[String, String](topics, kafkaParams)
    )

    val lines: DStream[String] = ds.map(cr => {
      println(s"message key = ${cr.key()}")
      println(s"message value = ${cr.value()}")
      cr.value()
    })
    lines.flatMap(line => line.split("\t"))
      .map((_, 1))
      .reduceByKey((v1, v2) => v1 + v2)
      .print()

    //保证业务逻辑处理完成的情况下，异步将当前批次offset提交给kafka,异步提交DStream需要使用源头的DStream
    ds.foreachRDD { rdd =>
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      // some time later, after outputs have completed
      ds.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges) // 异步向Kafka中提交消费者offset
    }
    context.start()
    context.awaitTermination()
  }
}
