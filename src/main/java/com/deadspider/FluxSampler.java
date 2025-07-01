package com.deadspider;


import reactor.core.publisher.Flux;

public class FluxSampler {

    public static void main(String[] args) {
        MonoSampler.getUriWords("https://www.ietf.org/rfc/rfc2616.txt")
            .flatMapMany(Flux::fromIterable)
            .mapNotNull(String::trim)
            .filter(s -> !s.equals(System.lineSeparator()))
            .filter(s -> !s.isBlank())
            .subscribe(System.out::println);
            

    }

}
