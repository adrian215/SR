package com.sr.endpoint;

import com.sr.common.model.Task;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CLIEndpoint implements CommandLineRunner {

    private final Pattern pattern = Pattern.compile("([0-9]*);(.*)");

    @Async
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Uruchomiono CLI");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.next();
            if ("exit".equals(input))
                return;
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                int timeout = Integer.parseInt(matcher.group(1));
                String path = matcher.group(2);
            }
        }
    }
}
