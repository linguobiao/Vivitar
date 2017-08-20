package com.gzgamut.vivitar.logger;

/**
 * Created by LGB on 2017/07/01.
 * 日志接口
 */
public interface LogTool {

  void d(String tag, String message);

  void e(String tag, String message);

  void w(String tag, String message);

  void i(String tag, String message);

  void v(String tag, String message);

  void wtf(String tag, String message);
}