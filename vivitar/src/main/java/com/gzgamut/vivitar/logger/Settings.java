package com.gzgamut.vivitar.logger;

/**
 *  Created by LGB on 2017/07/01.
 *  在Applaction中设置日志级别和TAG即可
 *  for ex:
 *      Settings settings = new Settings();
 *      settings.logLevel(CommonData.DEBUG ? LogLevel.FULL : LogLevel.NONE);
 *      Logger.init(EcopConstants.LOG_TAG);
 */
public final class Settings {

    private int methodCount = 2;

    private boolean showThreadInfo = true;

    private int methodOffset = 0;

    private LogTool logTool;

    /**
     * Determines how logs will printed
     */
    private LogLevel logLevel = LogLevel.FULL;

    public Settings hideThreadInfo() {
      showThreadInfo = false;
      return this;
    }

    public Settings methodCount(int methodCount) {
      if (methodCount < 0) {
        methodCount = 0;
      }
      this.methodCount = methodCount;
      return this;
    }

    public Settings logLevel(LogLevel logLevel) {
      this.logLevel = logLevel;
      return this;
    }

    public Settings methodOffset(int offset) {
      this.methodOffset = offset;
      return this;
    }

    public Settings logTool(LogTool logTool) {
      this.logTool = logTool;
      return this;
    }

    public int getMethodCount() {
      return methodCount;
    }

    public boolean isShowThreadInfo() {
      return showThreadInfo;
    }

    public LogLevel getLogLevel() {
      return logLevel;
    }

    public int getMethodOffset() {
      return methodOffset;
    }

    public LogTool getLogTool() {
      if (logTool == null) {
        logTool = new AndroidLogTool();
      }
      return logTool;
    }
}
