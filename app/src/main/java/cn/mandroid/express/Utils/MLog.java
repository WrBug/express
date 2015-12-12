package cn.mandroid.express.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MLog {
	private static boolean display = true;
	private static String LOG_TAG="aaa";
	public static void i(Object log) {
		if (display) {
			Log.i(LOG_TAG, log + "");
		}

	}
	public static void v(String log) {
		if (display) {
			Log.v(LOG_TAG, log);
		}

	}

	public static void d(String log) {
		if (display) {
			Log.d(LOG_TAG, log);
		}

	}

	public static void e(String log) {
		if (display) {
			Log.e(LOG_TAG, log);
		}

	}

	public static void w(String log) {
		if (display) {
			Log.w(LOG_TAG, log);
		}

	}
}
