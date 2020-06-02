package me.ryan.netty.example;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduleExamples {
    private final static Channel CHANNEL_FROM_SOMEWHRE = new NioSocketChannel();

    public static void schedule() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

        ScheduledFuture<?> future = executor.schedule(() -> {
            System.out.println("60 seconds later");
        }, 60, TimeUnit.SECONDS);

        executor.shutdown();
    }

    public static void scheduleViaEventLoop() {
        Channel ch = CHANNEL_FROM_SOMEWHRE;

        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(() -> System.out.println("60 seconds later"),
                60, 60, TimeUnit.SECONDS);
    }

    public static void cancelingTaskUsingScheduledFuture() {
        Channel ch = CHANNEL_FROM_SOMEWHRE;
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(() -> System.out.println("60 seconds later"),
                60, 60, TimeUnit.SECONDS);
        boolean mayInterruptIfRunning = false;
        future.cancel(mayInterruptIfRunning);
    }
}

