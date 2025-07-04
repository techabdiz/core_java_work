package com.deadspider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

public class MonoSampler {
    
    static Function<String, List<String>> toWords = (s)-> Arrays.asList(s.split(" "));

    public static void main(String[] args) {
        String term = "protocol";

        Mono<Optional<Entry<String, Long>>> mono = readFile("seeds.txt")
            .map(s -> Arrays.asList(s.split(",")))
            .map(s -> s.stream().collect(Collectors.toMap(t->t, t-> getCount(term, t))))
            .map(s -> s.entrySet().stream().map(e-> Map.entry(e.getKey(), e.getValue().block())).max(Map.Entry.comparingByValue()));

        mono.subscribe(v -> {
             v.ifPresent(val -> { 
                if (val.getValue() != 0) { 
                    System.out.println(String.format("found term '%s' in %s %d times out of all seeds", 
                        term, val.getKey(), val.getValue()));
                }else{ 
                    System.out.println(String.format("unable to find term ['%s'] not found in any of the seeds", term));
                }
             });
        });
    }

    public static Mono<List<String>> getUriWords(String uri) { 
        return Mono.fromCallable(()->uri)
                   // .subscribeOn(Schedulers.single())
                    .map(MonoSampler::getBody)
                        .map(String::toLowerCase)
                        .map(String::trim)
                        .filter(Predicate.not(String::isBlank))
                    .map(toWords);
    }


    public static Mono<Map<String, Long>> uriWordCount(String uri) { 
        return getUriWords(uri)
            .map(MonoSampler::wordCount);
    }

    public static Mono<Long> getCount(String word, String uri) { 
        return uriWordCount(uri)
            .map(s -> s.get(word))
            .onErrorReturn(0l);
    }

    public static String getBody(String uri)  { 
        HttpRequest request;
        try {
            request = HttpRequest
                .newBuilder(new URI(uri)).GET().build();
            return HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
                .build()
                    .send(request, BodyHandlers.ofString())
                        .body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public static Map<String, Long> wordCount(List<String> s) { 
        return s.stream()
            .collect(Collectors.groupingBy(t->t, Collectors.counting()));
    }

    public static Mono<String> readFile(String name){ 
        return Mono.fromCallable(() -> { 
                try(InputStream is = MonoSampler.class.getClassLoader().getResourceAsStream(name)){ 
                    return new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining(""));
                }
            }
        );
    }

}
