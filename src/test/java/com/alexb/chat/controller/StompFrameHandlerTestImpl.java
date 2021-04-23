package com.alexb.chat.controller;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

@SuppressWarnings("unchecked")
public class StompFrameHandlerTestImpl<T> implements StompFrameHandler {

    private final Class<T> payloadType;

    private final BlockingQueue<T> blockingQueue = new ArrayBlockingQueue<>(1);

    public StompFrameHandlerTestImpl(Class<T> payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return payloadType;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("\n\t" + Objects.requireNonNull(payload) + "\n");
        blockingQueue.add((T) payload);
    }

    public <P> P getSubscriptionPayload() throws InterruptedException {
        return (P) blockingQueue.poll(1, SECONDS);
    }

}
