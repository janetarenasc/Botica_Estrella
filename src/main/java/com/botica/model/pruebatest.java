package com.botica.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class pruebatest {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("1234"));
    }
}
