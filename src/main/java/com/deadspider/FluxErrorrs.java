package com.deadspider;

import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class FluxErrorrs {


    public static void main(String[] args) {
        Flux<String> f = Flux
            .range(0, 10)
                .map(i -> {
                    if(i == 4) { 
                        throw new IllegalArgumentException(Thread.currentThread().getName() +": breaking code on 4");
                    }
                    return  String.format("[%d] --> %s", i, generateString());
                })
        .subscribeOn(Schedulers.parallel())
        .onErrorStop()
        .onErrorContinue((err, val)-> {
            System.out.println(err + " --- " + val);
        }) 
       .onErrorResume((err)-> { 
        return Flux.just("40"); // incase of error, previous flux values will be dropped and returned flux will continue, will be ignored if onErrorContinueInStack
       });

       System.out.println(f.reduce(new ArrayList<String>(), (accumulatedValue, nextValue)-> {
            accumulatedValue.add(nextValue);
            return accumulatedValue;
        }).block());;
       
       System.out.println(f.collectList().block()); // no need to blockLast, as we are already blocking on mono out of collectList

      // f.blockLast();
    }

    public static String generateString() { 
        return List.of("apple", "mango", "banana", "cherry", "strawberries")
            .parallelStream().findAny().get();
    }
}
