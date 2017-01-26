package com.sr.common.helper;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class IdGenerator {

    AtomicInteger counter = new AtomicInteger(0);

    public int getId() {
        return counter.getAndAdd(1);
    }
}
