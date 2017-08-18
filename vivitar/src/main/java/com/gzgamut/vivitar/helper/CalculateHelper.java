package com.gzgamut.vivitar.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gzgamut.vivitar.been.Info;

public class CalculateHelper {

	/**
	 * 周或者月份
	 * 
	 * @param map
	 * @param historyList
	 * @return
	 */
	public static List<Info> setInfoDay(Map<Calendar, Info> map, List<Info> historyList) {
		List<Info> infoList = new ArrayList<Info>();

		if (historyList != null && map != null) {
			// 循环数据库中的历史小时数据
			for (Info info : historyList) {
				Calendar history_datetime = Calendar.getInstance();
				history_datetime.setTimeInMillis(info.getDate().getTimeInMillis());
				// 日期格式yyyy.mm.dd
				history_datetime = CalendarHelper.setDayFormat(history_datetime);

				if (map.containsKey(history_datetime)) {
					Info infoDate = map.get(history_datetime);
					infoDate = new Info();
					infoDate.setDate(history_datetime);
					infoDate.setWeight(info.getWeight());
					infoDate.setFat(info.getFat());
					infoDate.setWater(info.getWater());
					infoDate.setMuscle(info.getMuscle());
					infoDate.setBone(info.getBone());
					infoDate.setKcal(info.getKcal());
					infoDate.setBmi(info.getBmi());
					map.put(history_datetime, infoDate);
				}
			}

			// 将完整的数据添加到新 list
			Set<Calendar> keySet = map.keySet();
			for (Calendar key : keySet) {
				Info history = map.get(key);
				if (history == null) {
					history = new Info();
					history.setDate(key);

				}

				infoList.add(history);
			}
		}

		return infoList;
	}

	/*
	 * cm转换成inch英寸
	 */
	public static double cmToInch(double cm) {
		return cm * 0.39370078740157;
	}

	/*
	 * inch转换成cm
	 */
	public static double inchToCm(double inch) {
		return 2.54 * inch;
	}

	/**
	 * 
	 * @param cm
	 * @return
	 */
	public static double cmToFeet(double cm) {
		return cm / 100 / 0.3048;
	}

	/**
	 * 
	 * @param feet
	 * @return
	 */
	public static double feetToCm(double feet) {
		return feet * 100 * 0.3048;
	}

	/**
	 * kg 转换成 lbs
	 * 
	 */
	public static double kgToLbs(double kg) {
		return kg / 0.4536;
	}

	/**
	 * lbs 转换成 kg
	 * 
	 * @param lbs
	 * @return
	 */
	public static double LbsToKg(double lbs) {
		return lbs * 0.4536;
	}

	/**
	 * 保留两位小数
	 */
	public static double decimal(double decimal) {

		BigDecimal bigDecimal = new BigDecimal(decimal);

		return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
