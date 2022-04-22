package com.example.demo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class ScheduledExecutor {
	
	@PostConstruct
	public void test() throws InterruptedException {
//		while (true) {
//			test0();
//			test1();  // count req == count thread
//			test2();  // count req == count thread
//			test3();
//			test4();  // can not count execute
//		}
	}
	
	public void test0() throws InterruptedException {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
		List<Callable<Void>> task = new ArrayList<>();
		for (int i = 0 ; i < 20; i++) {
			String value = Integer.toString(i);
			task.add(new Callable<Void>() {
				public Void call() throws Exception {
					process(value);
			        return null;
			    }
			});
		}
		System.out.println((Calendar.getInstance()).getTime() + " Start!!");
		executorService.invokeAll(task);
		System.out.println((Calendar.getInstance()).getTime() + " End!!");
	}
	
	public void test1() throws InterruptedException {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
		System.out.println((Calendar.getInstance()).getTime() + " Start!!");
		CountDownLatch latch = new CountDownLatch(20);
		for (int i = 0 ; i < 20; i++) {
			String value = Integer.toString(i);
			Callable<Void> task = new Callable<Void>() {
				public Void call() throws Exception {
					process(value, latch);
			        return null;
			    }
			};
			executorService.schedule(task, 10, TimeUnit.SECONDS);
		}
		latch.await();
		System.out.println((Calendar.getInstance()).getTime() + " End!!");
	}
	
	public void test2() throws InterruptedException {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
		System.out.println((Calendar.getInstance()).getTime() + " Start!!");
		CountDownLatch latch = new CountDownLatch(20);
		for (int i = 0 ; i < 20; i++) {
			String value = Integer.toString(i);
			Runnable task = new Runnable() {
				public void run() {
					try {
						process(value, latch);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    }
			};
			executorService.schedule(task, 10, TimeUnit.SECONDS);
		}
		latch.await();
		System.out.println((Calendar.getInstance()).getTime() + " End!!");
	}
	
	public void test3() throws InterruptedException {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
		System.out.println((Calendar.getInstance()).getTime() + " Start!!");
		CountDownLatch latch = new CountDownLatch(20);
		for (int i = 0 ; i < 20; i++) {
			String value = Integer.toString(i);
			Runnable task = new Runnable() {
				public void run() {
					try {
						process(value, latch);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    }
			};
			executorService.scheduleAtFixedRate(task, 10, 10, TimeUnit.SECONDS);
		}
		latch.await();
		System.out.println((Calendar.getInstance()).getTime() + " End!!");
	}
	
	public void test4() throws InterruptedException {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
		System.out.println((Calendar.getInstance()).getTime() + " Start!!");
		CountDownLatch latch = new CountDownLatch(20);
		for (int i = 0 ; i < 20; i++) {
			String value = Integer.toString(i);
			Runnable task = new Runnable() {
				public void run(){
					try {
						process(value, latch);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    }
			};
			executorService.scheduleWithFixedDelay(task, 10, 10, TimeUnit.SECONDS);
		}
		latch.await();
		System.out.println((Calendar.getInstance()).getTime() + " End!!");
	}
	
	private void process(String i) throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " " + (Calendar.getInstance()).getTime() + " : " + i);
		TimeUnit.SECONDS.sleep(2);
	}
	
	private void process(String i, CountDownLatch latch) throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " " + (Calendar.getInstance()).getTime() + " : " + i);
		TimeUnit.SECONDS.sleep(2);
		latch.countDown();
	}

}
