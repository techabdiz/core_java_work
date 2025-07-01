package com.deadspider;

import java.time.Instant;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class FluxCombine {

    public static void main(String[] args) {
        Flux<String> f = Flux.from(Mono.fromCallable(FluxCombine::getDate))
            .subscribeOn(Schedulers.parallel())
            .repeat(10)
            .map(s -> "first: " + s)
        .mergeWith(  // doesn't preserve order
        //.concatWith(  // preserves order
            Flux.from(Mono.fromCallable(FluxCombine::getDate))
            .subscribeOn(Schedulers.parallel())
            .repeat(10)
            .map(s -> "second: " + s)
        );

        f.subscribe(System.out::println, System.out::println, ()->{
            System.out.println("completed");
        });

        f.blockLast();
        
        
    }

    public static String getDate() { 
        return Instant.now().toString();
    }
}
