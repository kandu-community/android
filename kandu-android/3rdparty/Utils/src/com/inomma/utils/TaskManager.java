package com.inomma.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Class for collecting tasks with priorities and running them one after another in a new thread.<br/>
 * Tasks are queue is thread safe so tasks can be added to queue any time from any thread.
 */
public class TaskManager {
	
	public static final int PRIORITY_HIGHEST = 0;
	public static final int PRIORITY_HIGH = 10;
	public static final int PRIORITY_MEDIUM = 0;
	public static final int PRIORITY_LOW = 0;

	private static TaskManager instance = new TaskManager();

	private volatile Map<Integer, List<Runnable>> tasks;
	private boolean executing;

	public static TaskManager getInstance() {
		return instance;
	}

	private TaskManager() {
		tasks = new TreeMap<Integer, List<Runnable>>();
	}
	
	private void logState()
	{
		StringBuilder builder = new StringBuilder();
		for(int key : tasks.keySet())
		{
			builder.append("priority: " + key + ", count: " + tasks.get(key).size() + "\n");
		}
	}

	/**
	 * Executes runnables in tasks lists.<br/>
	 * Finds the task with the highest priority (the smallest number), executes it, removes from tasks list and does the same until tasks are empty.
	 */
	private void executeAll() {
		while (true) {
			List<Runnable> current = null;
			for(int key : tasks.keySet())
			{
				current = tasks.get(key);
				if(!current.isEmpty())
				{
					logState();
					break;
				}

				current = null;
			}
			if(current == null)
				break;
			
			current.get(0).run();
			current.remove(0);
		}
		executing = false;
	}

	/**
	 * Starts executing tasks.
	 * Does nothing if is already executing.
	 */
	public void startExecuting() {
		if (executing)
			return;
		executing = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				executeAll();
			}
		}).start();
	}
	
	/**
	 * Adds runnable to tasks queue with given priority.
	 * If there was not any task starts executing immediately.
	 * 
	 * @param task a runnable object
	 * @param priority priority of task (lower is important)
	 */
	public void addTask(Runnable task, int priority)
	{
		addTask(task, priority, true);
	}
	
	/**
	 * Adds runnable to tasks queue with given priority.
	 * If there was not any task starts executing immediately.
	 * 
	 * @param task a runnable object
	 * @param priority priority of task (lower is important)
	 * @param startImmediately starts executing if there was no any task and this parameter is true
	 */
	public void addTask(Runnable task, int priority, boolean startImmediately)
	{
		List<Runnable> tasksForPriority = tasks.get(priority);
		if(tasksForPriority == null)
		{
			tasksForPriority = new ArrayList<Runnable>();
			tasks.put(priority, tasksForPriority);
		}
		tasksForPriority.add(task);
		if (!executing && startImmediately)
			startExecuting();
	}
}