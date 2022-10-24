package com.icezhg.athena.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("test")
public class TestTask {

    public void test() {
        log.info("test run...");
    }
}
