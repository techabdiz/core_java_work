package com.deadspider.streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SimplePipeline {


    public static void main(String[] args) {
        List<Double> tempReadings = Arrays.asList(23.5, 60.4, 99.34, 34.12);

        long finteredCount = tempReadings.stream()
            .peek(System.out::println)
            .filter(temp-> temp > 30)
          .peek(System.out::println)
                .count();

        System.out.printf("temprature went up %d times\n", finteredCount);
    }
}
