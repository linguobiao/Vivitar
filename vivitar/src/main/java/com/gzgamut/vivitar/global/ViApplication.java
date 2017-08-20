package com.gzgamut.vivitar.global;

import android.app.Application;

import com.gzgamut.vivitar.logger.LogLevel;
import com.gzgamut.vivitar.logger.Logger;
import com.gzgamut.vivitar.logger.Settings;
import com.pgyersdk.crash.PgyCrashManager;


/**
 * Created by LGB on 2017/7/19.
 */

public class ViApplication extends Application {

    private static ViApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //设置日志级别
        Settings settings = Logger.init(Global.LOG_TAG);
        settings.logLevel(Global.DEBUG ? LogLevel.FULL : LogLevel.NONE);
        PgyCrashManager.register(this);
    }

    public static ViApplication getInstance() {
        return instance;
    }

}
