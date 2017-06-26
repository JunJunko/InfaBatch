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
 * �ַ�����������
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
	 * �ж��Ƿ�ƥ��������ʽ
	 * 
	 * @param src
	 *            Դ�ַ���
	 * @param pattern
	 *            ������ʽ�ַ������μ�����ľ�̬����
	 * @return �Ƿ����ֵı�־
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
	 * ��string list�ø����ķ���symbol���ӳ�һ���ַ���
	 * 
	 * @param array
	 *            �ַ���list
	 * @param symbol
	 *            ���ӷ���
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
	 * ��ȡ�ַ����������ַ���symbol����,���������á�...������
	 * 
	 * @param len
	 *            �ַ������ȣ����ȼ�����λΪһ��GBK���֣�����Ӣ����ĸ����Ϊһ����λ����
	 * @param str
	 *            Դ�ַ���
	 * @param symbol
	 *            ������ַ���
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
			log.error("������Ϣ", ex);
			return str.substring(0, len);
		} finally {
			strRet = null;
		}
	}

	/**
	 * ��string array�ø����ķ���symbol���ӳ�һ���ַ���
	 * 
	 * @param array
	 *            �ַ���array
	 * @param symbol
	 *            �����ַ���
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
	 * ȡ���ַ�����ʵ�ʳ��ȣ������˺��ֵ������
	 * 
	 * @param SrcStr
	 *            Դ�ַ���
	 * @return �ַ�����ʵ�ʳ���
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
	 * �Զ���ķָ��ַ������� ����: 1,2,3 =>[1,2,3] 3��Ԫ�� ,2,3=>[,2,3] 3��Ԫ�� ,2,3,=>[,2,3,]
	 * 4��Ԫ�� ,,,=>[,,,] 4��Ԫ�� 5.22�㷨�޸ģ�Ϊ����ٶȲ���������ʽ ���������,,����""Ԫ��
	 * 
	 * @param split
	 *            �ָ��ַ� Ĭ��,
	 * @param src
	 *            �����ַ���
	 * @return �ָ����list
	 */
	public static List<String> splitToList(String split, String src) {
		// Ĭ��,
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
	 * �� ��=ֵ ������ת�����ַ��� (a=1,b=2 =>a=1&b=2)
	 * 
	 * @param map
	 *            ��=ֵmap
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
	 * ����ת�ַ���,���num<=0 �����"";
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
	 * ����ת�ַ���
	 * 
	 * @param money
	 * @param style
	 *            ��ʽ [default]Ҫ��ʽ���ɵĸ�ʽ such as #.00, #.#
	 * @return
	 */
	public static String moneyToString(Object money, String style) {
		if (money != null && style != null
				&& (money instanceof Double || money instanceof Float)) {
			Double num = (Double) money;

			if ("default".equalsIgnoreCase(style)) {
				// ȱʡ��ʽ 0 ����� ,���û�����С��λ�����.0
				if (num == 0) {
					// �����0
					return "";
				} else if ((int) (num * 10 % 10) == 0) {
					// û��С��
					return Integer.toString((int) num.intValue());
				} else {
					// ��С��
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
	 * ��sou���Ƿ����finds ���ָ����finds�ַ�����һ����sou���ҵ�,����true;
	 * 
	 * @param sou
	 *            Դ�ַ���
	 * @param find
	 *            ���ܰ������ַ�������
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
	 * ��sou���Ƿ����finds ���ָ����finds�ַ�����һ����sou���ҵ�,����true;
	 * 
	 * @param sou
	 *            Դ�ַ���
	 * @param find
	 *            ���ܰ������ַ���list
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
	 * ��xml תΪobject
	 * 
	 * @param xml
	 *            xml��ʽ���ַ���
	 * @return
	 */
	public static Object xmlToObject(String xml) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					xml.getBytes("UTF8"));
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(in));
			return decoder.readObject();
		} catch (Exception e) {
			log.error("������Ϣ", e);
		}
		return null;
	}

	/**
	 * �����û������URL��ַ�������û���棩 Ŀǰֻ�����http��www��ͷ��URL��ַ
	 * ���������õ�������ʽ�����������ڶ������ϸ�ĵط�����:ѭ����listҳ���
	 * 
	 * @param str
	 *            ��Ҫ������ַ���
	 * @return ���ش������ַ���
	 */
	public static String removeURL(String str) {
		String ss = str;
		if (str != null)
			ss = str.toLowerCase().replaceAll("(http|www|com|cn|org|\\.)+", "");
		return ss;
	}

	/**
	 * Wapҳ��ķǷ��ַ�����滻
	 * 
	 * @param str
	 *            Դ�ַ���
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
			ss = ss.replace('$', '��');
			ss = ss.replaceAll("&amp;", "��");
			ss = ss.replace('&', '��');
			ss = ss.replace('<', '��');
			ss = ss.replace('>', '��');

		}
		return ss;
	}

	/**
	 * ȥ���ַ����е�ƥ��������ʽ���ַ�������
	 * 
	 * @param str
	 *            Դ�ַ���
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
	 * ������html��ʽ���ı���ʽ������html��ʽ�ı�
	 * 
	 * @param str
	 *            Դ�ַ���
	 * @return Ŀ���ַ���
	 */
	public static String removeHTMLLable(String str) {
		String ss = str;
		ss = stringReplace(str, ">\\s*<", "><");
		ss = stringReplace(ss, "&nbsp;", " ");// �滻�ո�
		ss = stringReplace(ss, "<br ?/?>", "\n");// ȥ<br><br />
		ss = stringReplace(ss, "<([^<>]+)>", "");// ȥ��<>�ڵ��ַ�
		ss = stringReplace(ss, "\\s\\s\\s*", " ");// ������հױ��һ���ո�
		ss = stringReplace(ss, "^\\s*", "");// ȥ��ͷ�Ŀհ�
		ss = stringReplace(ss, "\\s*$", "");// ȥ��β�Ŀհ�
		ss = stringReplace(ss, " +", " ");
		return ss;
	}

	/**
	 * 
	 * ������ʽ�ַ����滻
	 * 
	 * @param str
	 *            Դ�ַ���
	 * @param sr
	 *            ������ʽ��ʽ
	 * @param sd
	 *            �滻�ı�
	 * @return �����
	 */
	public static String stringReplace(String str, String sr, String sd) {
		String regEx = sr;
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		return m.replaceAll(sd);
	}

	/**
	 * 
	 * ��html��ָ����ǩʡ��д���滻�ɷ�ʡ��д���磺 <input/>�ĳ�<input></input>
	 * 
	 * @param str
	 *            html�ַ���
	 * @param pt
	 *            ��ǩ��table
	 * @return �����
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
	 * ����������ʽ��ȡ�ַ���,��ͬ���ַ���ֻ����һ��
	 * 
	 * @param strԴ�ַ���
	 * @param pattern
	 *            ������ʽ
	 * @return Ŀ���ַ���������
	 */
	public static String[] getStringArrayByPattern(String str, String pattern) {
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(str);
		// ����
		Set<String> result = new HashSet<String>();// Ŀ���ǣ���ͬ���ַ���ֻ����һ�������� ���ظ�Ԫ��
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
	 * �ж��Ƿ�������ַ�����ʽƥ��
	 * 
	 * @param str
	 *            �ַ���
	 * @param pattern
	 *            ������ʽ��ʽ
	 * @return �Ƿ�ƥ����true,��false
	 */
	public static boolean isMatch(String str, String pattern) {
		Pattern pattern_hand = Pattern.compile(pattern);
		Matcher matcher_hand = pattern_hand.matcher(str);
		boolean b = matcher_hand.matches();
		return b;
	}

	/**
	 * ��ȡ�ַ���
	 * 
	 * @param s
	 *            Դ�ַ���
	 * @param jmp
	 *            ����jmp
	 * @param sb
	 *            ȡ��sb
	 * @param se
	 *            ��se
	 * @return ֮����ַ���
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
	 * ��Ҫͨ��URL��������ݽ��б���
	 * 
	 * @param Դ�ַ���
	 * @return �������������
	 */
	public static String URLEncode(String src) {
		String return_value = "";
		try {
			if (src != null) {
				return_value = URLEncoder.encode(src, "GBK");

			}
		} catch (UnsupportedEncodingException e) {
			log.error("������Ϣ", e);
			return_value = src;
		}
		return return_value;
	}

	/**
	 * ���ͷ���(ͨ��)����listת�����ԡ�,��������ַ���
	 * 
	 * @param <T>
	 *            ����
	 * @param list
	 *            list�б�
	 * @return �ԡ�,��������ַ���
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
	 * ����������ת�����ԡ�,��������ַ���
	 * 
	 * @param a
	 *            ����a
	 * @return �ԡ�,��������ַ���
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
	 * ��list �ô���ķָ�����װΪString
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
	 * ��ȡ��start��ʼ��*�滻len�����Ⱥ���ַ���
	 * 
	 * @param str
	 *            Ҫ�滻���ַ���
	 * @param start
	 *            ��ʼλ��
	 * @param len
	 *            ����
	 * @return �滻����ַ���
	 */
	public static String getMaskStr(String str, int start, int len) {
		if (isEmpty(str)) {
			return str;
		}
		if (str.length() < start) {
			return str;
		}
		// ��ȡ*֮ǰ���ַ���
		String ret = str.substring(0, start);
		// ��ȡ����ܴ��*����
		int strLen = str.length();
		int lens = len;
		if (strLen < start + lens) {
			lens = strLen - start;
		}
		// �滻��*
		for (int i = 0; i < lens; i++) {
			ret += "*";
		}
		// ����*֮����ַ���
		if (strLen > start + lens) {
			ret += str.substring(start + lens);
		}
		return ret;
	}

	/**
	 * ���ݴ���ķָ����,�Ѵ�����ַ����ָ�ΪList�ַ���
	 * 
	 * @param slipStr
	 *            �ָ����ַ���
	 * @param src
	 *            �ַ���
	 * @return �б�
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
	 * �ж�ĳ���ַ����Ƿ������������
	 * 
	 * @param stringArray
	 *            ԭ����
	 * @param source
	 *            ���ҵ��ַ���
	 * @return �Ƿ��ҵ�
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
	 * ���ض��ĸ�ʽ��ȡ��ǰʱ���ʽ������ַ���
	 * 
	 * @param dateStr
	 *            ʱ���ʽ
	 * @return
	 */
	public static String turnTimeStringToString(String dateStr) {
		Date date = new Date();
		SimpleDateFormat dfs = new SimpleDateFormat(dateStr);
		String time = dfs.format(date);
		return time;
	}

	/**
	 * ��ʽ���ַ��� ����formateString("xxx{0}bbb",1) = xxx1bbb
	 * 
	 * @param str
	 *            Դ�ַ���
	 * @param params
	 *            ��������
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
	 * ����ƴ���ַ���ids ����idsUtils(1,2,3) = "'1','2','3'"
	 * 
	 * @param ids
	 *            Դ�ַ���
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
	 * �ж��ǲ��Ǻ��������ַ�����%_'
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
	 * ת��hql�����е������ַ�����%_'
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
	 * ��GBK������ת����ISO8859_1
	 * 
	 * @param str
	 *            Դ�ַ���
	 * @return
	 */
	public static String encodeISO(String str) {
		if (str != null) {
			byte[] tmpbyte = null;
			try {
				tmpbyte = str.getBytes("GBK");
			} catch (Exception e) {
				log.error("������Ϣ", e);
			}
			try {
				str = new String(tmpbyte, "ISO8859_1");
			} catch (Exception e) {
				log.error("������Ϣ", e);
			}
		}
		return str;
	}

	/**
	 * ���ж��ַ����Ƿ�Ϊ��
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
	 * �ַ�������ת��
	 * 
	 * @param str
	 * @return
	 */
	public static String decodeUTF8(String str) {
		String ss = str;
		try {
			ss = java.net.URLDecoder.decode(ss, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("������Ϣ", e);
		}
		return ss;
	}


	/*
	 * clob to string ���ַ�����ʽת��STRING
	 * 
	 * @param clob
	 * 
	 * @return ���ַ���
	 */
	public static String Clob2String(CLOB clob) {// Clobת����String �ķ���
		String content = null;
		StringBuilder stringBuf = new StringBuilder();
		try {
			int length = 0;
			Reader inStream = clob.getCharacterStream(); // ȡ�ô��ֲ�ζ������������
			char[] buffer = new char[10];
			while ((length = inStream.read(buffer)) != -1) // ��ȡ���ݿ� //ÿ10��10����ȡ
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
