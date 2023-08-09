package com.excentria_it.wamya.common.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ConsumerUtils {

	private ConsumerUtils() {

	}

	public static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
		AtomicInteger counter = new AtomicInteger(0);
		return item -> consumer.accept(counter.getAndIncrement(), item);
	}
}
