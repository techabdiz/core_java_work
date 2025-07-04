package com.deadspider;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class FluxFlatMap {


    public static void main(String[] args) {
        Flux<String> f = Flux.just("apple", "mango", "banana", "cherry", "strawberries");

        Flux<String> f2 = Flux.just("T1", "T2", "T3")
            .flatMap(t -> f.subscribeOn(Schedulers.parallel()).map(s -> String.format("[%s] --> %s", t, s)));
        
        f2.subscribe(System.out::println);

        f2.blockLast();

    }

}
