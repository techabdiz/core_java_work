package com.deadspider;

import java.util.Map;
import java.util.Map.Entry;

class User { 
    private String username;
    private String password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Entry<String, String> entry) { 
        this.username = entry.getKey();
        this.password = entry.getValue();
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + "]";
    }

}

public class ConstructorRef {
    

    public static void main(String[] args) {
        
        Map.of("ABC", "DEF")
            .entrySet().stream()
                .map(User::new).forEach(System.out::println);

    }
}
