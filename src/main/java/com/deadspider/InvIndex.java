package com.deadspider;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

public class InvIndex {
    /**
     * PLAN: 
     *  get list of seeds
     *  fetch data for all seeds
     *  tokenize to words
     *  convert to meta object freq, document, term (count)
     *  merge all metas
     *  ready to search
     */

    private final List<Meta> index;
    public Function<String, List<String>> commaSeperateString = s -> Arrays.asList(s.split(","));

    public InvIndex() { 
        index = new LinkedList<Meta>();
        initIndex();
        //System.out.println(index);
    }


    public static void main(String[] args) {
        InvIndex invertedIndex = new InvIndex();
       System.out.println( invertedIndex.search("text"));;
    }

    public Meta search(String term){ 
        return index.get(index.indexOf(Meta.builder().term(term).build()));
    }

    private void initIndex() { 
        MonoSampler.readFile("seeds.txt")
            .map(commaSeperateString)
            .map(ulist -> { 
                return ulist.stream().map(this::uriToMetas).toList();
            })
        .blockOptional()
        .ifPresent(list -> { 
            list.forEach(m-> { 
                m
                .blockOptional()
                .ifPresent(metalist-> { 
                    metalist.forEach(meta->{ 
                        if(index.contains(meta)) { 
                            Meta result = index.get(index.indexOf(meta));
                            result.setFreq(result.getFreq()+meta.getFreq());
                            result.getDocs().addAll(meta.getDocs());
                        }else { 
                            index.add(meta);
                        }
                    });
                });
            });
        });
    }

    private Mono<List<Meta>> uriToMetas(String uri) { 
        return MonoSampler
            .uriWordCount(uri)
                .map(wcount -> {
                    return wcount.entrySet()
                        .stream()
                            .map(e -> { 
                                List<String> uriList = new LinkedList<>();
                                uriList.add(uri);
                                return  new Meta(e.getKey(), e.getValue(), uriList);
                            })
                            .toList();
                });
    }


}


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Meta { 
    
    private String term;
    private Long freq;
    private List<String> docs;


    @Override
    public boolean equals(Object m){ 
        Meta meta = (Meta)m;
        return meta.term.equals(this.term);
    }
}