package com.deadspider.cfuture;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
public class CompFut {

    static <T> CompletableFuture<T> customFuture(T t) { 
        CompletableFuture<T> future = new CompletableFuture<>();
        Thread.ofPlatform().daemon().name("custom-thread")
            .start(()-> { 
                try {
                    Thread.sleep(Duration.ofSeconds(2));
                } catch (InterruptedException e) {
                    future.completeExceptionally(e);
                }
                future.complete(t);
            });
        return future;
    } 


    static void combineEx() { 

        Supplier<String> task1 = () -> blockThreadAndReturn("task_1", 3, false);
        Supplier<String> task2 = () -> blockThreadAndReturn("task_2", 4, false);
        Supplier<String> task3 = () -> blockThreadAndReturn("task_3", 5, false);
        Supplier<String> task4 = () -> blockThreadAndReturn("task_4", 6, false);

        CompletableFuture 
            .supplyAsync(task1)
                .thenCombine(CompletableFuture.supplyAsync(task2), CompFut::fuse)
            .thenCompose((s) -> { 
                return CompletableFuture.supplyAsync(task3)
                        .thenCombine(CompletableFuture.supplyAsync(task4), CompFut::fuse);
            })
            .thenAccept(System.out::println).join();

    }


    public static void main(String[] args) {
        customFuture("return-same-value")
            .thenApply(s-> s + "GLUE::")
                .thenAccept(System.out::println).join();
    }

    public static String fuse(String s1, String s2) { 
        return s1+s2;
    }

    public static <T> T blockThreadAndReturn(T value, int seconds, boolean fail) { 
        System.out.println(String.format("INFO: blocking %s thread for %d seconds to return '%s' value", 
            Thread.currentThread().getName(), seconds, value));
        if(fail){ 
            System.out.println(String.format("ERROR: thrown error in %s thread after %d seconds while returning '%s' value", 
            Thread.currentThread().getName(), seconds, value));
             throw new IllegalStateException("failed intentionally");
        }
        try {
            Thread.sleep(Duration.ofSeconds(seconds));
        } catch (InterruptedException e) {
            throw new IllegalStateException(e.getMessage());
        }
        System.out.println(String.format("INFO: released %s thread after %d seconds and returning '%s' value", 
            Thread.currentThread().getName(), seconds, value));
        return value;
    }
}
