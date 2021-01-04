package com.yj.it;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class WordCount {

    public static void main(String[] args) {
//        SparkConf conf = new SparkConf();
//        conf.setAppName("java-spark-wordCount");
//        conf.setMaster("local");
//
//        JavaRDD<String> words = new JavaSparkContext(conf)
//                .textFile("T:/code/spark_scala/data/words")
//                .flatMap(line -> Arrays.asList(line.split(" ")).iterator());
//        JavaPairRDD<String, Integer> pairRDD = words.mapToPair(word -> new Tuple2<>(word, 1));
//        JavaPairRDD<String, Integer> result = pairRDD.reduceByKey((v1, v2) -> v1 + v2);
//        result.foreach(tuple2 -> System.out.println(tuple2));

    }
}
