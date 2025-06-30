package com.deadspider.cfuture;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class Executor {

    public static void main(String[] args) {
        ExecutorService serv = Executors.newFixedThreadPool(5);
        ExecutorCompletionService<TaskResult> completionService = new ExecutorCompletionService<>(serv);

        IntStream.range(0, 10)
            .forEach(i -> {
                completionService.submit(()->doSomething("task_"+i, false));
            });
            
        IntStream.range(0, 10).forEach(i-> { 
            try {
                Future<TaskResult> result = completionService.take();
                System.out.println(result.get());
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
        serv.shutdown();
    }

    public static TaskResult doSomething(String name, boolean fail) { 
        try { 
            Thread.sleep(Duration.ofMillis(300));
        }catch (InterruptedException e ){ 
            e.printStackTrace();
        }
        if(fail){ 
            throw new IllegalStateException("thread exeuction failed");
        }
        return TaskResult.builder()
            .name(name)
            .val("executed by thread: " + Thread.currentThread().getName())
        .build();

    }



}

@Data
@Builder
@AllArgsConstructor
class TaskResult {
    
    private String name;
    private String val;
}
