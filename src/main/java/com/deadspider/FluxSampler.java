package com.deadspider;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxSampler {

    public static void main(String[] args) {
       /* MonoSampler
            .getUriWords("https://www.ietf.org/rfc/rfc2616.txt")
            .subscribe(words -> { 
                Flux.fromIterable(words)
                    .subscribe(System.out::println);
            });
            */

            Flux.from((s)->Mono.just("initial value: " + s))
                .subscribe(System.out::println);
    }

}
