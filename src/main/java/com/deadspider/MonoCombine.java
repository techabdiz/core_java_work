package com.deadspider;


import java.util.stream.Stream;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class MonoCombine {

    public static void main(String[] args) {
        
        Mono.just("hello")
            .zipWith(Mono.just("world"), 
                    (first, second)-> first + " :GLUE: " + second)
            .subscribe(System.out::println);

        Mono.zip(arr-> Stream.of(arr).map(s -> Integer.valueOf((int)s)).reduce(0, (a,b)->a+b), 
            Mono.just(10),
            Mono.just(20),
            Mono.just(30)
        ).subscribeOn(Schedulers.parallel()).doOnSuccess(System.out::println).block();
    }

}
