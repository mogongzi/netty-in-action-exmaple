package me.ryan.netty.example;

import java.util.Collections;
import java.util.List;

public class EventLoopExamples {

    public static void executeTaskInEventLoop() {
        boolean terminated = true;

        while (!terminated) {
            List<Runnable> readyEvents = blockUntilEventsReady();
            for (Runnable ev : readyEvents) {
                ev.run();
            }
        }

    }

    private static final List<Runnable> blockUntilEventsReady() {
        return Collections.singletonList(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
