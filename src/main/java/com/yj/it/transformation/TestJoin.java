package com.yj.it.transformation;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;

/**
 * join : 作用在K,V格式的RDD上，（K,V）.join(K,W) => (K,(V,W)) 两个RDD相同的key才能关联在一起
 */
public class TestJoin {
    public static void main(String[] args) {
        JavaSparkContext context = new JavaSparkContext(new SparkConf()
                .setMaster("local")
                .setAppName("join test"));

        JavaPairRDD<String, Integer> ageRDD = context.parallelizePairs(Arrays.asList(
                new Tuple2<>("张三", 18),
                new Tuple2<>("李四", 25),
                new Tuple2<>("王5", 19)
        ));
        JavaPairRDD<String, Integer> scoreRDD = context.parallelizePairs(Arrays.asList(
                new Tuple2<>("张三", 89),
                new Tuple2<>("李四", 100),
                new Tuple2<>("王5", 55)
        ));

        JavaPairRDD<String, Tuple2<Integer, Integer>> join = ageRDD.join(scoreRDD);
        join.foreach(new VoidFunction<Tuple2<String, Tuple2<Integer, Integer>>>() {
            @Override
            public void call(Tuple2<String, Tuple2<Integer, Integer>> stringTuple2Tuple2) throws Exception {
                System.out.println(stringTuple2Tuple2);
            }
        });

        context.stop();
    }
}
