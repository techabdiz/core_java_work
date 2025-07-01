package com.deadspider;


import reactor.core.publisher.Flux;

public class FluxSampler {

    public static void main(String[] args) {
        Flux<String> flux = MonoSampler.getUriWords("https://www.ietf.org/rfc/rfc2616.txt")
            .flatMapMany(Flux::fromIterable)
            .mapNotNull(String::trim)
            .filter(s -> s.matches("[a-zA-Z0-9]+"))
            .filter(s -> s.length() > 1)
            .filter(s -> !s.equals(System.lineSeparator()))
            .filter(s -> !s.isBlank())
            .doOnNext(s -> { // same as peek
                System.out.println("Processing word: " + s);
            });
            flux.subscribe(System.out::println, 
                error -> System.err.println("Error: " + error.getMessage()),
                () -> System.out.println("Completed processing words.")) ;

    }

    public static String toUnicode(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(String.format("\\u%04x", (int) c));
        }
        return sb.toString();
    }
    
}
