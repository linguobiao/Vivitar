package com.gzgamut.vivitar.logger;

import android.util.Log;

/**
 * Created by LGB on 2017/07/01.
 * 最终采用Android底层Log实现
 */
public class AndroidLogTool implements LogTool {

    @Override
    public void d(String tag, String message) {
      Log.d(tag, message);
    }

    @Override
    public void e(String tag, String message) {
      Log.e(tag, message);
    }

    @Override
    public void w(String tag, String message) {
      Log.w(tag, message);
    }

    @Override
    public void i(String tag, String message) {
      Log.i(tag, message);
    }

    @Override
    public void v(String tag, String message) {
      Log.v(tag, message);
    }

    @Override
    public void wtf(String tag, String message) {
      Log.wtf(tag, message);
    }
}
