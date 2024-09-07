package com.deadspider.streams;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.foreign.Linker.Option;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
public class SimplePipeline {


    public static Function<String, Optional<List<String>>> getUrlDataInWords = (url)->{
        try {
            URL uri = new URL(url);
            return Optional.ofNullable(new BufferedReader(new InputStreamReader(uri.openConnection().getInputStream()))
                    .lines().flatMap(s-> Arrays.asList(s.split("\\ ")).stream())
                            .filter(s->!s.isBlank())
                    .toList());
        } catch (IOException e) {
        }
        return Optional.empty();
    };

    public static void customCollect() { 
        List<Integer> list = Stream.iterate(1, i->i+1).parallel()
            .limit(10)
            .collect(ArrayList::new,
                ArrayList::add,
                ArrayList::addAll
            );
        System.out.println(list);
    }


    public static void main(String[] args) {
        customCollect();
    }

    static void streamWithoutTerminal () { 
        IntStream stream = IntStream.range(1, 10)
            .filter(s-> { 
                System.out.println("filters are being executed.... ");
                return true;
            });
        stream.count();
        System.out.println("EOF");
    }

    static void find() { 
        Stream<Integer> stream  = List.of(23, 43 , 54, 65, 43 ,65)
            .parallelStream();
        
        System.out.println(stream.findFirst());
    }

    static void minMax() { 
       Stream.iterate(1, s-> s +1 ).limit(20)
            .min((s, t) -> { 
                System.out.println(s + " === " + t);
                if(t%2 == 0){ 
                    return -1;
                }
                return 1;
            }).ifPresent(System.out::println);
    }


    static void wordCountUsingReduce() { 
        getWordStream().ifPresent(words->{
                    words.map(s -> Map.of(s, 1))
                .reduce(( mainMap, map)->{
                    if(mainMap.entrySet().size() == 1) { 
                        mainMap = new HashMap<>(mainMap);
                    }
                    Entry<String, Integer> entry = map.entrySet().iterator().next();
                    String word = entry.getKey();
                    if(mainMap.containsKey(word)) { 
                        mainMap.put(word, mainMap.get(word)+1);
                    }else{ 
                        mainMap.put(word, 1);
                    }
                    return mainMap;
                }).ifPresent(reduced->{
                    reduced.entrySet().stream()
                        .sorted(Comparator.<Entry<String, Integer>>comparingInt(Entry::getValue).reversed())
                        .filter(entry-> entry.getValue() > 1 && entry.getValue() > 200).limit(20)
                        .forEach(System.out::println);
                });
        });
    }

    static void multipleTerminals() { 
        Stream<Integer> stream = Stream.iterate(1, i-> i+1).limit(20)
            .filter(i-> {
                System.out.println("FILTER: 1 ---> " + i);
                return i%2==0;
            });
        
        System.out.println(stream.count()); // stream closes once terminal operation is done... :(
        System.out.println(stream.filter(i->{
            System.out.println("FILTER: 2 ---> " + i);;
            return i%4 == 0;
        }).count());
        

    }

    static void terminalCount() { 
        Stream<Integer> stream  = Stream.iterate(1, i -> i + 1)
            .limit(10);
        System.out.println(stream.count());
    }
    
    static void reduce() { 
        Optional<String> total = Stream.of("A", "A" ,"B", "C", "D")
            .distinct()
            .reduce((a, b)->{
                System.out.println(a + "  " + b);
                return a+b;
            });
        System.out.println(total.get());
    }

    static void streamCreation()  {
        Arrays.stream(new Double[]{
            10.1, 3.2, 9.12
        })
        .skip(1)
        .limit(1)
        .forEach(System.out::println);

        Stream.of("A", "B", "C", "D")
            .skip(1)
            .forEach(System.out::println);


        try {
            Files.lines(
                new File("C:\\work\\source\\java\\core-java-work\\src\\main\\java\\com\\deadspider\\streams\\SimplePipeline.java")
                .toPath())
                .filter(s->s.contains("{"))
                .limit(5)
                .forEach(System.out::println);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static Optional<Stream<String>> getWordStream() { 
        return Optional.ofNullable(getUrlDataInWords.apply("https://www.ietf.org/rfc/rfc2616.txt").get().stream());
    }

    static void inifiniteStream() { 
        //Stream<Integer> stream = Stream.generate(()->10);
        Stream<Integer> stream = Stream.iterate(2, n -> n + 2).limit(200);
        stream.forEach(System.out::println);
    }

    void runBasicPipeline() { 
        getUrlDataInWords.apply("https://www.ietf.org/rfc/rfc2616.txt")
            .ifPresent(words->{ 
                words
                    .stream()
                        //.map(s->"A".concat(s))
                        .filter(s-> {
                            System.out.println("filter_1===" + s);
                            return s.toUpperCase().startsWith("A");
                        }).filter(s-> {
                            System.out.println("filter_1===" + s);
                            return s.length() < 5;
                        })
                        .limit(10)
                            .forEach(System.out::println);
            });
    }


    
}
