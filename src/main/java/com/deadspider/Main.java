package com.deadspider;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@FunctionalInterface
interface Eval<T> {
    boolean eval(T t);
}


public class Main {

    static Predicate<Integer> isNegative = i -> i < 0; 


    public static void main(String[] args) {
        
        Predicate<Integer> p = isNegative;


        UnaryOperator<String> upperCase = String::toUpperCase;
        BiPredicate<String, String> startsWith = (val, start) -> {
            Predicate<String> st = upperCase.apply(val)::startsWith;
            return st.test(upperCase.apply(start));
        };  // confusing




        for(int i = 0; i < 20; i++){ 
            int k = i;
            p = p.or( j -> {
                System.out.println("Evaluating: " + k);
                return j < k;
            });
        }

        boolean test = ((Predicate<String>) k -> {
                System.out.println("Evaluating first");
                return k.startsWith("s");
            }).and(k ->{
                System.out.println("Evaluating second");
                return k.contains("and");
            }).or(k->{
                System.out.println("Evaluating third");
                return false;
            })
            .test("sand");

            System.out.println(startsWith.test("sand", "s"));




    }


}