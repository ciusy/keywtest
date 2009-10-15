package com.mrj.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskDispatcher implements Runnable {
	private Object lock = new Object();
	
	private static TaskDispatcher instance=new TaskDispatcher();
	
	private BlockingQueue<Task> taskPool = new LinkedBlockingQueue<Task>();
	
	private List<TaskProcessor> taskProcessorList= new ArrayList<TaskProcessor>();
	
	private int processorNum=4;

	private TaskDispatcher(){
		for(int i=0;i<processorNum;i++){
			TaskProcessor tp=new TaskProcessor("TaskProcessor"+i);
			tp.start();
			taskProcessorList.add(tp);
			
		}
	}
	
	public static TaskDispatcher getInstance(){
		return instance;
	}
	
	public void addTask(Task task) {		
		synchronized(lock){
			taskPool.offer(task);
			lock.notify();
		}
	}

	public Task pollTask() {
		return taskPool.poll();
	}

	
	int minBusyValue=Integer.MAX_VALUE;
	int minBusyProcessorNum=0;
	public void run() {
		while (true) {
			Task task = null;
			task = pollTask();
			if (task == null){
				try {
					synchronized (lock) {
						lock.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				
				/*//ʹ���æ���߳�����
				for(int i=0;i<processorNum;i++){
					if(taskProcessorList.get(i).getTaskPoolSize()<minBusyValue){
						minBusyValue=taskProcessorList.get(i).getTaskPoolSize();
						minBusyProcessorNum=i;
					}
				}*/
				taskProcessorList.get(minBusyProcessorNum++).addTask(task);
				if(minBusyProcessorNum>=processorNum){
					minBusyProcessorNum=0;//归零
				}
				
			}
				
		}

	}

}
