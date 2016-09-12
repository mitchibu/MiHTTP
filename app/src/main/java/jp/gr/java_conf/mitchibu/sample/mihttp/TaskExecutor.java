package jp.gr.java_conf.mitchibu.sample.mihttp;

import android.content.Context;
import android.os.Handler;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class TaskExecutor {
	private final static ExecutorService DEFAULT_EXECUTOR = Executors.newSingleThreadExecutor();

	private final ExecutorService executor;
	private final Handler handler;

	public TaskExecutor(Context context) {
		this(context, DEFAULT_EXECUTOR);
	}

	public TaskExecutor(Context context, ExecutorService executor) {
		this.executor = executor;
		handler = new Handler(context.getMainLooper());
	}

	public <E> Task<E> submit(Callable<E> callable, Listener<E> listener) {
		Task<E> task = new Task<>(callable, listener);
		executor.submit(task);
		return task;
	}

	public void shutdown() {
		executor.shutdown();
	}

	public interface Listener<E> {
		void onDone(Task<E> task);
		void onCancelled(Task<E> task);
	}

	public class Task<E> extends FutureTask<E> {
		private final Listener<E> listener;

		public Task(Callable<E> callable, Listener<E> listener) {
			super(callable);
			this.listener = listener;
		}

		@Override
		public void done() {
			if(listener != null) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if(isCancelled()) listener.onCancelled(Task.this);
						else listener.onDone(Task.this);
					}
				});
			}
		}
	}
}
