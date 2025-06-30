package com.deadspider;

import java.util.Date;
import reactor.core.publisher.Mono;

public class Main {
    public static void main(String[] args) {
        Mono<String> mono = Mono.just(getDate())
            .doOnNext(val -> {
                System.out.println("value : " + val + " " + Thread.currentThread().getName());
            });

        mono
        .map(s-> s + " mmy value ").subscribe();
    }

    public static String getDate(){ 
        return new Date().toString();
    }
}