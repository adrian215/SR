package com.sr.endpoint;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CLIEndpoint implements CommandLineRunner {

    @Async
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Uruchomiono ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.next();
            System.out.println("Wypisano " + input);
            if ("exit".equals(input))
                return;
        }
    }
}
