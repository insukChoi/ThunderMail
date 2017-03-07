package com.mail.common;

/**
 * @(#) StringUtil.java
 * @author  최인석, cis172510@bizplay.co.kr.
 */
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class StringUtil { 
	public static String null2void(String value) {
		if (value == null || "null".equals(value))
			return "";
		return value.trim();
	}
	public static String null2zero(String value) {
		if (value == null || "null".equals(value) || "".equals(value))
			return "0";
		return value.trim();
	}
	public static String null2money(String value) {
		if (value == null || "null".equals(value))
			return "0";
		return value.trim().replaceAll(",", "");
	}
	public static String null2moneyFormat(String value) {
		if (value == null || "null".equals(value))
			return "0";
		return formatMoney(value.trim().replaceAll(",", ""));
	}
	public static String formatMoney(String _value) {

		if (_value == null || "".equals(_value) || "-".equals(_value))
			return _value;
		NumberFormat money = NumberFormat.getInstance();
		return money.format(Double.parseDouble(_value));
	}
	static String replace(String src, String org, String tar) {
		if (src == null) {
			return "";
		}

		if (org == null) {
			return src;
		}

		if (tar == null) {
			tar = "";
		}

		String tmp1 = src;
		String tmp2 = "";
		while (tmp1.indexOf(org) > -1) {
			tmp2 = tmp2 + tmp1.substring(0, tmp1.indexOf(org)) + tar;
			tmp1 = tmp1.substring(tmp1.indexOf(org) + org.length());
		}
		tmp2 = tmp2 + tmp1;
		return tmp2;
	}
	/**
	* 오늘 날자를 기준으로 format형식에 따라서 값을 반환해준다..
	* @param s String Format String
	*/
	public static String getDate(String s)
	{
		return getDate(Calendar.getInstance().getTime(), s);

	}
	/**
	 *사용자가 입력한 Date 를 format 형식에따라 값을 반환한다.
	 * @param date Date 사용자가 Date
	 * @param s    String Date Format
	*/
	public static String getDate(Date date, String s)
	{
		s = s.toLowerCase();
		int i = s.indexOf("hh24");
		if (i != -1)
			s = s.substring(0, i) + "HH" + s.substring(i + 4);
		i = s.indexOf("mm");
		if (i != -1)
			s = s.substring(0, i) + "MM" + s.substring(i + 2);
		i = s.indexOf("mi");
		if (i != -1)
			s = s.substring(0, i) + "mm" + s.substring(i + 2);
		return new SimpleDateFormat(s).format(date);
	}
}