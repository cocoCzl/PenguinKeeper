package com.dollar.penguin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DollarPenguinStarter {

    public void start() {
        try {
            initAndStart();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            System.exit(-1);
        }
    }

    private void initAndStart() {
        // TODO
    }
}
