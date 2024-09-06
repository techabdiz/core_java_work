package com.deadspider;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ArgsCounter {
    

    public static void main(String[] args) {
        Supplier<Integer> supplier = ArgsCounter::countArgs; 
        Function<Integer, Integer> consumer = ArgsCounter::countArgs; 
        BiFunction<Integer, Integer, Integer> bif = ArgsCounter::countArgs; 

        System.out.println(supplier.get());
        System.out.println(consumer.apply(1));
        System.out.println(bif.apply(1,2));
    }


    public static Integer countArgs(Object... args) { 
        return args.length;
    }
}
