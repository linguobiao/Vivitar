package com.gzgamut.vivitar.helper;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.widget.TextView;

import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.been.Info;
import com.gzgamut.vivitar.global.Global;


public class CalendarHelper {

	/**
	 * 获取今天日期
	 * 
	 * @return
	 */
	public static Calendar getToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static String getYyyy_MM_dd(Calendar cal) {
		if (cal != null) {
			// int year = cal.get(Calendar.YEAR);
			// int month = cal.get(Calendar.MONTH) + 1;
			// int day = cal.get(Calendar.DATE);
			//
			// return Global.df_int_4.format(year) + "-" +
			// Global.df_1.format(month) + "-" + Global.df_1.format(day);
			return Global.sdf_2.format(cal.getTime());
		}

		return "2000-01-01";
	}

	public static String getYyyy_MM_dd_HH_mm(Calendar cal) {
		if (cal != null) {
			// int year = cal.get(Calendar.YEAR);
			// int month = cal.get(Calendar.MONTH) + 1;
			// int day = cal.get(Calendar.DATE);
			//
			// return Global.df_int_4.format(year) + "-" +
			// Global.df_1.format(month) + "-" + Global.df_1.format(day);
			return Global.sdf_1.format(cal.getTime());
		}

		return "2000-01-01";
	}
	
	public static String getYyyy_MM_dd_HH_mm_ss(Calendar cal) {
		if (cal != null) {
			// int year = cal.get(Calendar.YEAR);
			// int month = cal.get(Calendar.MONTH) + 1;
			// int day = cal.get(Calendar.DATE);
			//
			// return Global.df_int_4.format(year) + "-" +
			// Global.df_1.format(month) + "-" + Global.df_1.format(day);
			return Global.sdf_4.format(cal.getTime());
		}

		return "2000-01-01";
	}
	/**
	 * 判断x天
	 * @param cal_from
	 * @return
	 */
	public static int getXDays(Calendar cal) {
		int days = 0;
		Calendar today = Calendar.getInstance();
		if (cal != null && today != null) {
			 days = today.get(Calendar.DATE) - cal.get(Calendar.DATE);
		}
		return days;
	}
	
	/**
	 * 减少X个月
	 * @return
	 */
	public static Calendar minXMonth(Calendar cal, int minMonth) {
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - minMonth);
		
		return cal;
	}
	
	/**
	 * 加一日
	 * @param cal
	 * @return
	 */
	public static Calendar addADay(Calendar cal) {
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
		
		return cal;
	}
	
	/**
	 * 上个月日期、明天日期
	 * @param today
	 * @return
	 */
	public static Calendar[] getLastMonthToTomorrow(Calendar today) {
		// 上个月
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.setTimeInMillis(today.getTimeInMillis());
		lastMonth.set(Calendar.MONTH, lastMonth.get(Calendar.MONTH) - 1);
//		lastMonth = minADay(lastMonth);
//		lastMonth.set(Calendar.DATE, lastMonth.get(Calendar.DATE) - 31);
		// yyyy.mm.dd
		lastMonth = setDayFormat(lastMonth);
		
		// 明天
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTimeInMillis(today.getTimeInMillis());
		tomorrow = addADay(tomorrow);
		// yyyy.mm.dd
		tomorrow = setDayFormat(tomorrow);		
		
		Calendar[] calArray = new Calendar[2];
		calArray[0] = lastMonth;
		calArray[1] = tomorrow;
		
		return calArray;		
	}
	
	/**
	 * 三个月日期、明天日期
	 * @param today
	 * @return
	 */
	public static Calendar[] getLast3MonthToTomorrow(Calendar today) {
		// 上个月
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.setTimeInMillis(today.getTimeInMillis());
		lastMonth.set(Calendar.MONTH, lastMonth.get(Calendar.MONTH) - 3);
//		lastMonth = minADay(lastMonth);
//		lastMonth.set(Calendar.DATE, lastMonth.get(Calendar.DATE) - 31);
		// yyyy.mm.dd
		lastMonth = setDayFormat(lastMonth);
		
		// 明天
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTimeInMillis(today.getTimeInMillis());
		tomorrow = addADay(tomorrow);
		// yyyy.mm.dd
		tomorrow = setDayFormat(tomorrow);		
		
		Calendar[] calArray = new Calendar[2];
		calArray[0] = lastMonth;
		calArray[1] = tomorrow;
		
		return calArray;		
	}
	
	/**
	 * 六个月日期、明天日期
	 * @param today
	 * @return
	 */
	public static Calendar[] getLast6MonthToTomorrow(Calendar today) {
		// 上个月
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.setTimeInMillis(today.getTimeInMillis());
		lastMonth.set(Calendar.MONTH, lastMonth.get(Calendar.MONTH) - 6);
//		lastMonth = minADay(lastMonth);
//		lastMonth.set(Calendar.DATE, lastMonth.get(Calendar.DATE) - 31);
		// yyyy.mm.dd
		lastMonth = setDayFormat(lastMonth);
		
		// 明天
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTimeInMillis(today.getTimeInMillis());
		tomorrow = addADay(tomorrow);
		// yyyy.mm.dd
		tomorrow = setDayFormat(tomorrow);		
		
		Calendar[] calArray = new Calendar[2];
		calArray[0] = lastMonth;
		calArray[1] = tomorrow;
		
		return calArray;		
	}
	
	/**
	 * 12个月日期、明天日期
	 * @param today
	 * @return
	 */
	public static Calendar[] getLast12MonthToTomorrow(Calendar today) {
		// 上个月
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.setTimeInMillis(today.getTimeInMillis());
		lastMonth.set(Calendar.YEAR, lastMonth.get(Calendar.YEAR) - 1);
//		lastMonth = minADay(lastMonth);
//		lastMonth.set(Calendar.DATE, lastMonth.get(Calendar.DATE) - 31);
		// yyyy.mm.dd
		lastMonth = setDayFormat(lastMonth);
		
		// 明天
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTimeInMillis(today.getTimeInMillis());
		tomorrow = addADay(tomorrow);
		// yyyy.mm.dd
		tomorrow = setDayFormat(tomorrow);		
		
		Calendar[] calArray = new Calendar[2];
		calArray[0] = lastMonth;
		calArray[1] = tomorrow;
		
		return calArray;		
	}
	
	/**
	 * 格式 yyyy.mm.dd
	 * @param cal
	 * @return
	 */
	public static Calendar setDayFormat(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal;
	}
	
	/**
	 * 获取两个日期间的日map
	 * @param calArray
	 * @return
	 */
	public static Map<Calendar, Info> getDayMap(Calendar[] calArray) {
		
		Map<Calendar, Info> map = new LinkedHashMap<Calendar, Info>();
		
		if (calArray != null && calArray.length == 2) {
			Calendar begin = calArray[0];
			Calendar end = calArray[1];

			Calendar temp = Calendar.getInstance();
			temp.setTimeInMillis(begin.getTimeInMillis());
			
//			map.put(begin, null);
			
			while(true) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(addADay(temp).getTimeInMillis());
				
				if (cal.equals(end)) {
					break;
				} else {
					map.put(cal, null);
				}
			}
		}
		
		return map;
	}
	
	/**
	 * 设置日期，格式 'July-29'
	 * @param cal
	 * @param text_date
	 */
	public static void setDateInformation(Context context, Calendar cal, TextView text_date) {
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		// 星期几
		String dayOfWeekStr = CalendarHelper.getDayOfWeekStr(context, dayOfWeek);
		// 月份
		String monthStr = CalendarHelper.getMonthStr(context, month);
		if (dayOfWeekStr != null && monthStr != null) {
			text_date.setText(monthStr + "-" + Global.df_int_2.format(day));
		}
//		Log.i(this.getClass().getName(), "year:" + year + ", month:" + month);
	}
	
	/**
	 * 获取英文星期缩写
	 * @param dayOfWeek
	 * @return
	 */
	public static String getDayOfWeekStr(Context context, int dayOfWeek) {
		String dayOfWeekStr = null;
		if (dayOfWeek == Calendar.MONDAY) {
			dayOfWeekStr = context.getString(R.string.MON);
		} else if (dayOfWeek == Calendar.TUESDAY) {
			dayOfWeekStr = context.getString(R.string.TUE);
		} else if (dayOfWeek == Calendar.WEDNESDAY) {
			dayOfWeekStr = context.getString(R.string.WED);
		} else if (dayOfWeek == Calendar.THURSDAY) {
			dayOfWeekStr = context.getString(R.string.THU);
		} else if (dayOfWeek == Calendar.FRIDAY) {
			dayOfWeekStr = context.getString(R.string.FRI);
		} else if (dayOfWeek == Calendar.SATURDAY) {
			dayOfWeekStr = context.getString(R.string.SAT);
		} else if (dayOfWeek == Calendar.SUNDAY) {
			dayOfWeekStr = context.getString(R.string.SUN);
		}
		
		return dayOfWeekStr;
	}
	
	/**
	 * 返回英文月份缩写
	 * @param month
	 * @return
	 */
	public static String getMonthStr(Context context, int month) {
		
		String monthStr = null;
		if (month == Calendar.JANUARY) {
			monthStr = context.getString(R.string.JAN);
		} else if (month == Calendar.FEBRUARY) {
			monthStr = context.getString(R.string.FEB);
		} else if (month == Calendar.MARCH) {
			monthStr = context.getString(R.string.MAR);
		} else if (month == Calendar.APRIL) {
			monthStr = context.getString(R.string.APR);
		} else if (month == Calendar.MAY) {
			monthStr = context.getString(R.string.MAY);
		} else if (month == Calendar.JUNE) {
			monthStr = context.getString(R.string.JUN);
		} else if (month == Calendar.JULY) {
			monthStr = context.getString(R.string.JUL);
		} else if (month == Calendar.AUGUST) {
			monthStr = context.getString(R.string.AUG);
		} else if (month == Calendar.SEPTEMBER) {
			monthStr = context.getString(R.string.SEP);
		} else if (month == Calendar.OCTOBER) {
			monthStr = context.getString(R.string.OCT);
		} else if (month == Calendar.NOVEMBER) {
			monthStr = context.getString(R.string.NOV);
		} else if (month == Calendar.DECEMBER) {
			monthStr = context.getString(R.string.DEC);
		} 
		
		return monthStr;
	}
}
