package com.gzgamut.vivitar.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.StringDef;
import android.util.DisplayMetrics;

import com.gzgamut.vivitar.main.SettingActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

/**
 * Created by LGB on 2017/8/27.
 */

public class LanguageManager {


    public static String SP_LANGUAGE = "SP_LANGUAGE";
    public static final String LOCALE_EN = "en";
    public static final String LOCALE_ES = "es";
    @StringDef({LOCALE_EN, LOCALE_ES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type{}

    public static void initLanguage(Context context) {
        String locale = getLanguage(context);
        if (LOCALE_ES.equals(locale)) {
            Locale spanish = new Locale("es", "ES");
            switchLanguage(spanish);
        }
        else {switchLanguage(Locale.ENGLISH);}
    }

    public static void switchLanguage(Locale locale) {
        Configuration config = ViApplication.getInstance().getResources().getConfiguration();// 获得设置对象
        Resources resources = ViApplication.getInstance().getResources();// 获得res资源对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = locale; // 简体中文
        resources.updateConfiguration(config, dm);
    }

    public static void setLanguage(Context context, @Type String locale) {
        SharedPreferences sp = context.getSharedPreferences(SP_LANGUAGE, SettingActivity.MODE_PRIVATE);
        sp.edit().putString(SP_LANGUAGE, locale).commit();
        initLanguage(context);
    }

    public static String getLanguage(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_LANGUAGE, SettingActivity.MODE_PRIVATE);
        return sp.getString(SP_LANGUAGE, LOCALE_EN);
    }
}
