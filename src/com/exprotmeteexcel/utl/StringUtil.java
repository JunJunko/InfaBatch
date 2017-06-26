package com.exprotmeteexcel.utl;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.sql.CLOB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 字符串处理工具类
 * 
 * @author lb
 * 
 */

public class StringUtil {
	
	private StringUtil(){
		super();
	}

	private static final Logger log = LoggerFactory.getLogger(StringUtil.class);

	/**
	 * 判断是否匹配正则表达式
	 * 
	 * @param src
	 *            源字符串
	 * @param pattern
	 *            正则表达式字符串，参见本类的静态常量
	 * @return 是否数字的标志
	 */
	public static boolean isNumeric(String src, String pattern) {
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = Pattern.compile(pattern).matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}

	/**
	 * 把string list用给定的符号symbol连接成一个字符串
	 * 
	 * @param array
	 *            字符串list
	 * @param symbol
	 *            连接符号
	 * @return
	 */
	public static String joinString(List<String> array, String symbol) {
		String result = "";
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				String temp = array.get(i).toString();
				if (temp != null && temp.trim().length() > 0)
					result += (temp + symbol);
			}
			if (result.length() > 1)
				result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * 截取字符串超出的字符用symbol代替,如标题过长用‘...’　　
	 * 
	 * @param len
	 *            字符串长度，长度计量单位为一个GBK汉字，两个英文字母计算为一个单位长度
	 * @param str
	 *            源字符串
	 * @param symbol
	 *            替代的字符串
	 * @return
	 */
	public static String getLimitLengthString(String str, int len, String symbol) {
		int iLen = len * 2;
		int counterOfDoubleByte = 0;
		String strRet = "";
		try {
			if (str != null) {
				byte[] b = str.getBytes("GBK");
				if (b.length <= iLen) {
					return str;
				}
				for (int i = 0; i < iLen; i++) {
					if (b[i] < 0) {
						counterOfDoubleByte++;
					}
				}
				if (counterOfDoubleByte % 2 == 0) {
					strRet = new String(b, 0, iLen, "GBK") + symbol;
					return strRet;
				} else {
					strRet = new String(b, 0, iLen - 1, "GBK") + symbol;
					return strRet;
				}
			} else {
				return "";
			}
		} catch (Exception ex) {
			log.error("错误信息", ex);
			return str.substring(0, len);
		} finally {
			strRet = null;
		}
	}

	/**
	 * 把string array用给定的符号symbol连接成一个字符串
	 * 
	 * @param array
	 *            字符串array
	 * @param symbol
	 *            连接字符串
	 * @return
	 */
	public static String joinString(String[] array, String symbol) {
		String result = "";
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				String temp = array[i];
				if (temp != null && temp.trim().length() > 0)
					result += (temp + symbol);
			}
			if (result.length() > 1)
				result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * 取得字符串的实际长度（考虑了汉字的情况）
	 * 
	 * @param SrcStr
	 *            源字符串
	 * @return 字符串的实际长度
	 */
	public static int getStringLen(String SrcStr) {
		int return_value = 0;
		if (SrcStr != null) {
			char[] theChars = SrcStr.toCharArray();
			for (int i = 0; i < theChars.length; i++) {
				return_value += (theChars[i] <= 255) ? 1 : 2;
			}
		}
		return return_value;
	}

	/**
	 * 自定义的分隔字符串函数 例如: 1,2,3 =>[1,2,3] 3个元素 ,2,3=>[,2,3] 3个元素 ,2,3,=>[,2,3,]
	 * 4个元素 ,,,=>[,,,] 4个元素 5.22算法修改，为提高速度不用正则表达式 两个间隔符,,返回""元素
	 * 
	 * @param split
	 *            分割字符 默认,
	 * @param src
	 *            输入字符串
	 * @return 分隔后的list
	 */
	public static List<String> splitToList(String split, String src) {
		// 默认,
		String sp = ",";
		if (split != null && split.length() == 1) {
			sp = split;
		}
		List<String> r = new ArrayList<String>();
		int lastIndex = -1;
		int index = src.indexOf(sp);
		if (-1 == index && src != null) {
			r.add(src);
			return r;
		}
		while (index >= 0) {
			if (index > lastIndex) {
				r.add(src.substring(lastIndex + 1, index));
			} else {
				r.add("");
			}

			lastIndex = index;
			index = src.indexOf(sp, index + 1);
			if (index == -1) {
				r.add(src.substring(lastIndex + 1, src.length()));
			}
		}
		return r;
	}

	/**
	 * 把 名=值 参数表转换成字符串 (a=1,b=2 =>a=1&b=2)
	 * 
	 * @param map
	 *            名=值map
	 * @return
	 */
	public static String linkedHashMapToString(Map<String, String> map) {
		if (map != null && !map.isEmpty()) {
			String result = "";
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String name = (String) it.next();
				String value = (String) map.get(name);
				result += "".equals(result) ? "" : "&";
				result += String.format("%s=%s", name, value);
			}
			return result;
		}
		return null;
	}

	

	/**
	 * 数字转字符串,如果num<=0 则输出"";
	 * 
	 * @param num
	 * @return
	 */
	public static String numberToString(Object num) {
		if (num == null) {
			return null;
		} else if (num instanceof Integer && (Integer) num > 0) {
			return Integer.toString((Integer) num);
		} else if (num instanceof Long && (Long) num > 0) {
			return Long.toString((Long) num);
		} else if (num instanceof Float && (Float) num > 0) {
			return Float.toString((Float) num);
		} else if (num instanceof Double && (Double) num > 0) {
			return Double.toString((Double) num);
		} else {
			return "";
		}
	}

	/**
	 * 货币转字符串
	 * 
	 * @param money
	 * @param style
	 *            样式 [default]要格式化成的格式 such as #.00, #.#
	 * @return
	 */
	public static String moneyToString(Object money, String style) {
		if (money != null && style != null
				&& (money instanceof Double || money instanceof Float)) {
			Double num = (Double) money;

			if ("default".equalsIgnoreCase(style)) {
				// 缺省样式 0 不输出 ,如果没有输出小数位则不输出.0
				if (num == 0) {
					// 不输出0
					return "";
				} else if ((int) (num * 10 % 10) == 0) {
					// 没有小数
					return Integer.toString((int) num.intValue());
				} else {
					// 有小数
					return num.toString();
				}
			} else {
				DecimalFormat df = new DecimalFormat(style);
				return df.format(num);
			}
		}
		return null;
	}

	/**
	 * 在sou中是否存在finds 如果指定的finds字符串有一个在sou中找到,返回true;
	 * 
	 * @param sou
	 *            源字符串
	 * @param find
	 *            可能包含的字符串数组
	 * @return
	 */
	public static boolean strPos(String sou, String... finds) {
		if (sou != null && finds != null && finds.length > 0) {
			for (int i = 0; i < finds.length; i++) {
				if (sou.indexOf(finds[i]) > -1)
					return true;
			}
		}
		return false;
	}

	/**
	 * 在sou中是否存在finds 如果指定的finds字符串有一个在sou中找到,返回true;
	 * 
	 * @param sou
	 *            源字符串
	 * @param find
	 *            可能包含的字符串list
	 * @return
	 */
	public static boolean strPos(String sou, List<String> finds) {
		if (sou != null && finds != null && !finds.isEmpty()) {
			for (String s : finds) {
				if (sou.indexOf(s) > -1)
					return true;
			}
		}
		return false;
	}

	/**
	 * 把xml 转为object
	 * 
	 * @param xml
	 *            xml格式的字符串
	 * @return
	 */
	public static Object xmlToObject(String xml) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					xml.getBytes("UTF8"));
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(in));
			return decoder.readObject();
		} catch (Exception e) {
			log.error("错误信息", e);
		}
		return null;
	}

	/**
	 * 过滤用户输入的URL地址（防治用户广告） 目前只针对以http或www开头的URL地址
	 * 本方法调用的正则表达式，不建议用在对性能严格的地方例如:循环及list页面等
	 * 
	 * @param str
	 *            需要处理的字符串
	 * @return 返回处理后的字符串
	 */
	public static String removeURL(String str) {
		String ss = str;
		if (str != null)
			ss = str.toLowerCase().replaceAll("(http|www|com|cn|org|\\.)+", "");
		return ss;
	}

	/**
	 * Wap页面的非法字符检查替换
	 * 
	 * @param str
	 *            源字符串
	 * @return
	 */
	public static String replaceWapStr(String str) {
		String ss = str;
		if (str != null) {
			ss = str.replaceAll("<span class=\"keyword\">", "");
			ss = ss.replaceAll("</span>", "");
			ss = ss.replaceAll("<strong class=\"keyword\">", "");
			ss = ss.replaceAll("<strong>", "");
			ss = ss.replaceAll("</strong>", "");
			ss = ss.replace('$', '＄');
			ss = ss.replaceAll("&amp;", "＆");
			ss = ss.replace('&', '＆');
			ss = ss.replace('<', '＜');
			ss = ss.replace('>', '＞');

		}
		return ss;
	}

	/**
	 * 去除字符串中的匹配正则表达式的字符串部分
	 * 
	 * @param str
	 *            源字符串
	 * @return
	 */
	public static String replaceBlank(String str, String pattern) {
		String ss = str;
		if (str != null) {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(str);
			ss = m.replaceAll("");
		}
		return ss;
	}

	/**
	 * 将带有html格式的文本格式化成武html格式文本
	 * 
	 * @param str
	 *            源字符串
	 * @return 目标字符串
	 */
	public static String removeHTMLLable(String str) {
		String ss = str;
		ss = stringReplace(str, ">\\s*<", "><");
		ss = stringReplace(ss, "&nbsp;", " ");// 替换空格
		ss = stringReplace(ss, "<br ?/?>", "\n");// 去<br><br />
		ss = stringReplace(ss, "<([^<>]+)>", "");// 去掉<>内的字符
		ss = stringReplace(ss, "\\s\\s\\s*", " ");// 将多个空白变成一个空格
		ss = stringReplace(ss, "^\\s*", "");// 去掉头的空白
		ss = stringReplace(ss, "\\s*$", "");// 去掉尾的空白
		ss = stringReplace(ss, " +", " ");
		return ss;
	}

	/**
	 * 
	 * 正则表达式字符串替换
	 * 
	 * @param str
	 *            源字符串
	 * @param sr
	 *            正则表达式样式
	 * @param sd
	 *            替换文本
	 * @return 结果串
	 */
	public static String stringReplace(String str, String sr, String sd) {
		String regEx = sr;
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		return m.replaceAll(sd);
	}

	/**
	 * 
	 * 将html的指定标签省略写法替换成非省略写法如： <input/>改成<input></input>
	 * 
	 * @param str
	 *            html字符串
	 * @param pt
	 *            标签如table
	 * @return 结果串
	 */
	public static String fomateToFullForm(String str, String pt) {
		String regEx = "<" + pt + "\\s+([\\S&&[^<>]]*)/>";
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		String[] sa = null;
		String sf = "";
		String sf2 = "";
		String sf3 = "";
		String ss = str;
		for (; m.find();) {
			sa = p.split(str);
			if (sa == null) {
				break;
			}
			sf = str.substring(sa[0].length(),
					str.indexOf("/>", sa[0].length()));
			sf2 = sf + "></" + pt + ">";
			sf3 = str.substring(sa[0].length() + sf.length() + 2);
			ss = sa[0] + sf2 + sf3;
			sa = null;
		}
		return ss;
	}

	/**
	 * 根据正则表达式提取字符串,相同的字符串只返回一个
	 * 
	 * @param str源字符串
	 * @param pattern
	 *            正则表达式
	 * @return 目标字符串数据组
	 */
	public static String[] getStringArrayByPattern(String str, String pattern) {
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(str);
		// 范型
		Set<String> result = new HashSet<String>();// 目的是：相同的字符串只返回一个。。。 不重复元素
		while (matcher.find()) {
			for (int i = 0; i < matcher.groupCount(); i++) { // int groupCount()
				result.add(matcher.group(i));
			}
		}
		String[] resultStr = null;
		if (!result.isEmpty()) {
			resultStr = new String[result.size()];
			return result.toArray(resultStr);
		}
		return resultStr;
	}

	/**
	 * 判断是否与给定字符串样式匹配
	 * 
	 * @param str
	 *            字符串
	 * @param pattern
	 *            正则表达式样式
	 * @return 是否匹配是true,否false
	 */
	public static boolean isMatch(String str, String pattern) {
		Pattern pattern_hand = Pattern.compile(pattern);
		Matcher matcher_hand = pattern_hand.matcher(str);
		boolean b = matcher_hand.matches();
		return b;
	}

	/**
	 * 截取字符串
	 * 
	 * @param s
	 *            源字符串
	 * @param jmp
	 *            跳过jmp
	 * @param sb
	 *            取在sb
	 * @param se
	 *            于se
	 * @return 之间的字符串
	 */
	public static String subStringExe(String s, String jmp, String sb, String se) {
		String ss = s;
		if (isEmpty(s)) {
			return "";
		}
		int i = ss.indexOf(jmp);
		if (i >= 0 && i < ss.length()) {
			ss = ss.substring(i + 1);
		}
		i = ss.indexOf(sb);
		if (i >= 0 && i < ss.length()) {
			ss = ss.substring(i + 1);
		}
		if (se == "") {
			return ss;
		} else {
			i = ss.indexOf(se);
			if (i >= 0 && i < ss.length()) {
				ss = ss.substring(i + 1);
			}
			return ss;
		}
	}

	/**
	 * 用要通过URL传输的内容进行编码
	 * 
	 * @param 源字符串
	 * @return 经过编码的内容
	 */
	public static String URLEncode(String src) {
		String return_value = "";
		try {
			if (src != null) {
				return_value = URLEncoder.encode(src, "GBK");

			}
		} catch (UnsupportedEncodingException e) {
			log.error("错误信息", e);
			return_value = src;
		}
		return return_value;
	}

	/**
	 * 泛型方法(通用)，把list转换成以“,”相隔的字符串
	 * 
	 * @param <T>
	 *            泛型
	 * @param list
	 *            list列表
	 * @return 以“,”相隔的字符串
	 */
	public static <T> String listTtoString(List<T> list) {
		if (list == null || list.isEmpty())
			return "";
		Iterator<T> i = list.iterator();
		if (!i.hasNext())
			return "";
		StringBuilder sb = new StringBuilder();
		for (;;) {
			T e = i.next();
			sb.append(e);
			if (!i.hasNext())
				return sb.toString();
			sb.append(",");
		}
	}

	/**
	 * 把整形数组转换成以“,”相隔的字符串
	 * 
	 * @param a
	 *            数组a
	 * @return 以“,”相隔的字符串
	 */
	public static String intArraytoString(int[] a) {
		if (a == null)
			return "";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "";
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < iMax; i++) {
			b.append(a[i]);
			b.append(",");
		}
		b.append(a[iMax]);
		return b.toString();
	}

	/**
	 * 将list 用传入的分隔符组装为String
	 * 
	 * @param list
	 * @param slipStr
	 * @return String
	 */
	public static String listToStringSlipStr(List<String> list, String slipStr) {
		StringBuilder returnStr = new StringBuilder();
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				returnStr.append(list.get(i)).append(slipStr);
			}
		}
		if (returnStr.toString().length() > 0)
			return returnStr.toString().substring(0,
					returnStr.toString().lastIndexOf(slipStr));
		else
			return "";
	}

	/**
	 * 获取从start开始用*替换len个长度后的字符串
	 * 
	 * @param str
	 *            要替换的字符串
	 * @param start
	 *            开始位置
	 * @param len
	 *            长度
	 * @return 替换后的字符串
	 */
	public static String getMaskStr(String str, int start, int len) {
		if (isEmpty(str)) {
			return str;
		}
		if (str.length() < start) {
			return str;
		}
		// 获取*之前的字符串
		String ret = str.substring(0, start);
		// 获取最多能打的*个数
		int strLen = str.length();
		int lens = len;
		if (strLen < start + lens) {
			lens = strLen - start;
		}
		// 替换成*
		for (int i = 0; i < lens; i++) {
			ret += "*";
		}
		// 加上*之后的字符串
		if (strLen > start + lens) {
			ret += str.substring(start + lens);
		}
		return ret;
	}

	/**
	 * 根据传入的分割符号,把传入的字符串分割为List字符串
	 * 
	 * @param slipStr
	 *            分隔的字符串
	 * @param src
	 *            字符串
	 * @return 列表
	 */
	public static List<String> stringToStringListBySlipStr(String slipStr,
			String src) {
		List<String> list = new ArrayList<String>();
		if (src != null) {
			String[] result = src.split(slipStr);
			for (int i = 0; i < result.length; i++) {
				list.add(result[i]);
			}
		}
		return list;
	}

	/**
	 * 判断某个字符串是否存在于数组中
	 * 
	 * @param stringArray
	 *            原数组
	 * @param source
	 *            查找的字符串
	 * @return 是否找到
	 */
	public static boolean contains(String[] stringArray, String source) {
		List<String> tempList = Arrays.asList(stringArray);
		if (tempList.contains(source)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 按特定的格式获取当前时间格式化后的字符串
	 * 
	 * @param dateStr
	 *            时间格式
	 * @return
	 */
	public static String turnTimeStringToString(String dateStr) {
		Date date = new Date();
		SimpleDateFormat dfs = new SimpleDateFormat(dateStr);
		String time = dfs.format(date);
		return time;
	}

	/**
	 * 格式化字符串 例：formateString("xxx{0}bbb",1) = xxx1bbb
	 * 
	 * @param str
	 *            源字符串
	 * @param params
	 *            参数数组
	 * @return
	 */
	public static String formateString(String str, String... params) {
		String ss = str;
		for (int i = 0; i < params.length; i++) {
			ss = ss.replace("{" + i + "}", params[i] == null ? "" : params[i]);
		}
		return ss;
	}

	/**
	 * 处理拼接字符串ids 例：idsUtils(1,2,3) = "'1','2','3'"
	 * 
	 * @param ids
	 *            源字符串
	 * @return
	 */
	public static String idsUtils(String ids) {
		StringBuilder idStr = new StringBuilder();
		String[] idsArray = ids.split(",");
		for (int i = 0; i < idsArray.length; i++) {
			if (i < idsArray.length - 1) {
				idStr.append("'").append(idsArray[i]).append("'").append(",");
			} else {
				idStr.append("'").append(idsArray[i]).append("'");
			}
		}
		return idStr.toString();
	}

	/**
	 * 判断是不是含有特殊字符。如%_'
	 * 
	 * @return
	 */
	public static boolean decideSpecial(String condition) {
		if (condition.contains("%")) {
			return true;
		} else if (condition.contains("_")) {
			return true;
		} else if (condition.contains("'")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 转义hql条件中的特殊字符。如%_'
	 * 
	 * @param condition
	 * @return
	 */
	public static String turnHqlCondition(String condition) {
		String ss = condition;
		if (ss.contains("%")) {
			ss = ss.replaceAll("%", "/%");
		}

		if (ss.contains("_")) {
			ss = ss.replaceAll("_", "/_");
		}

		if (ss.contains("'")) {
			ss = ss.replaceAll("'", "''");
		}
		return ss;
	}

	/**
	 * 将GBK的中午转换成ISO8859_1
	 * 
	 * @param str
	 *            源字符串
	 * @return
	 */
	public static String encodeISO(String str) {
		if (str != null) {
			byte[] tmpbyte = null;
			try {
				tmpbyte = str.getBytes("GBK");
			} catch (Exception e) {
				log.error("错误信息", e);
			}
			try {
				str = new String(tmpbyte, "ISO8859_1");
			} catch (Exception e) {
				log.error("错误信息", e);
			}
		}
		return str;
	}

	/**
	 * 普判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 字符串编码转换
	 * 
	 * @param str
	 * @return
	 */
	public static String decodeUTF8(String str) {
		String ss = str;
		try {
			ss = java.net.URLDecoder.decode(ss, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("错误信息", e);
		}
		return ss;
	}


	/*
	 * clob to string 大字符串格式转换STRING
	 * 
	 * @param clob
	 * 
	 * @return 大字符串
	 */
	public static String Clob2String(CLOB clob) {// Clob转换成String 的方法
		String content = null;
		StringBuilder stringBuf = new StringBuilder();
		try {
			int length = 0;
			Reader inStream = clob.getCharacterStream(); // 取得大字侧段对象数据输出流
			char[] buffer = new char[10];
			while ((length = inStream.read(buffer)) != -1) // 读取数据库 //每10个10个读取
			{
				for (int i = 0; i < length; i++) {
					stringBuf.append(buffer[i]);
				}
			}

			inStream.close();
			content = stringBuf.toString();
		} catch (Exception e) {
			log.error("ClobUtil.Clob2String:", e);
		}
		return content;
	}
	
	public static String turnTimeToString(Date date) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time= dfs.format(date);  
		return time;		
	}
	
	public static String turnTimeToStringByFormat(Date date, String format) {
		SimpleDateFormat dfs = new SimpleDateFormat(format);
		String time= dfs.format(date);  
		return time;		
	}
	
	
}
