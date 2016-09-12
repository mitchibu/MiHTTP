package jp.gr.java_conf.mitchibu.sample.mihttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.mitchibu.mihttp.MiHTTP;

public class MainActivity extends AppCompatActivity {
//	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	TaskExecutor test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		test = new TaskExecutor(this);
		MiHTTP.VERBOSE = true;
//		MiHTTP http = new MiHTTP<String>("http://www.yahoo.co.jp").receiver(new MiHTTP.Receiver<String>() {
//			@Override
//			public String processReceive(int code, String message, Map<String, List<String>> headers, InputStream body) throws Exception {
//				return null;
//			}
//		});
//		executor.submit(http);
		//new Yahoo().execute();
//		executor.submit(new Yahoo());
		TaskExecutor.Task<String> task = test.submit(new Yahoo(), new TaskExecutor.Listener<String>() {
			@Override
			public void onDone(TaskExecutor.Task<String> task) {
			}

			@Override
			public void onCancelled(TaskExecutor.Task<String> task) {
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		executor.shutdown();
		test.shutdown();
	}

	class Yahoo extends Base<String> {
		public Yahoo() {
			super("http://www.yahoo.co.jp");
		}
	}

	class Base<E> extends MiHTTP<E> {
		private final MiHTTP.Receiver<E> receiver = new MiHTTP.Receiver<E>() {
			@Override
			public E processReceive(int code, String message, Map<String, List<String>> headers, InputStream body) throws Exception {
				return null;
			}
		};

		public Base(String url) {
			super(url);
			receiver(receiver);
		}
	}
}
