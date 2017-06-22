package com.exprotmeteexcel.utl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Utl {

	public static Properties getProperties(String path) {

		InputStream is = null;
		try {
			is = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		InputStream inputStream = is;
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return p;
	}

	public static List<List<Map<String, Object>>> createList(List<Map<String, Object>> targe, int size) {
		List<List<Map<String, Object>>> listArr = new ArrayList<List<Map<String, Object>>>();
		// 获取被拆分的数组个数
		int arrSize = targe.size() % size == 0 ? targe.size() / size : targe.size() / size + 1;
		for (int i = 0; i < arrSize; i++) {
			List<Map<String, Object>> sub = new ArrayList<Map<String, Object>>();
			// 把指定索引数据放入到list中
			for (int j = i * size; j <= size * (i + 1) - 1; j++) {
				if (j <= targe.size() - 1) {
					sub.add(targe.get(j));
				}
			}
			listArr.add(sub);
		}
		return listArr;
	}

	public static <E> boolean isEmpty(Collection<E> collection) {

		return collection == null || collection.isEmpty();

	}

	public static <K, V> boolean isEmpty(Map<K, V> map) {

		return map == null || map.isEmpty();

	}

	public static <T> boolean isEmpty(T t) {

		return t == null;

	}

	public static boolean isEmpty(String str) {

		return str == null || str.equals("");

	}

}
