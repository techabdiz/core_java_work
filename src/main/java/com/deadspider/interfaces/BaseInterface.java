package com.deadspider.interfaces;

public interface BaseInterface {

    void doSomeThing();


    int a   = 200;

    static void runStatic() { 
        System.out.println("this requires an implementation");
    }

    default void run() { 
        System.out.println("print something");
    }


}
