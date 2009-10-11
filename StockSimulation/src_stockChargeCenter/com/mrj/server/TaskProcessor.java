package com.mrj.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskProcessor extends Thread{
	private Object lock = new Object();
	
	
	
	private BlockingQueue<Task> taskPool = new LinkedBlockingQueue<Task>();
	
	public TaskProcessor(String string) {
		super(string);
	}

	public int getTaskPoolSize(){
		return taskPool.size();
	}
	
	public void addTask(Task task) {
		taskPool.offer(task);
		synchronized(lock){
			lock.notify();
		}
	}

	public Task peekTask() {
		return taskPool.peek();
	}
	
	@Override
	public void run() {
		while (true) {
			Task task = null;
			task = peekTask();
			if (task == null){
				try {					
					synchronized (lock) {
						lock.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				task.dotask();	
				taskPool.remove();
			}
				
		}
	}


}
