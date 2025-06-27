package com.deadspider;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Main {

    public static void main(String[] args) {

        Flux.just(returnString())
           .subscribe(System.out::println);

      Flux.from((s)->returnString())
        .subscribe(System.out::println);

        
    }

    public static String returnString() {
        System.out.println("function executed... ");
        return "phyak u";
    }


}