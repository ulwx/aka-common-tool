package com.ulwx.tool;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.ulwx.type.Callback;

public class MapUtils {
	public static <T> Map<T, T[]> putMap(Map<T, T[]> map, T key, T value) {

		return CollectionUtils.putMap(map, key, value);
	}

	public static <T1, T2> T2[] mapToArray(Collection<T1> src,
			Callback<T1, T2> callback) {
		return CollectionUtils.mapToArray(src, callback);

	}

	public static <T1, T2> List<T2> mapToList(Collection<T1> src,
			Callback<T1, T2> callback) {
		return CollectionUtils.mapToList(src, callback);
	}

	public static boolean isEmpty(Map map) {
		return (map == null || map.isEmpty());
	}

	public static boolean isNotEmpty(Map map) {
		return !isEmpty(map);
	}

	public static Object getMaxKeyInMap(Map map) {
		return CollectionUtils.getMaxKeyInMap(map);
	}

	public static void mergePropertiesIntoMap(Properties props, Map map) {

		CollectionUtils.mergePropertiesIntoMap(props, map);
	}

	public static Map<Object, Object[]> mergeMapIntoGivenMap(Map givenMap,
			Map map) {

		return CollectionUtils.mergeMapIntoGivenMap(givenMap, map);
	}

	public static String toJavascriptString(Map map) throws Exception {

		String resultStr = "";
		if (MapUtils.isEmpty(map)) {
			return "{}";
		}
		Set set = map.keySet();

		for (Object key : set) {

			if (key == null)
				continue;
			Object fvalue = map.get(key);
			if (fvalue == null)
				continue;
			if (fvalue.getClass().isArray()) {//
				resultStr = resultStr + ",\"" + key + "\":"
						+ ArrayUtils.toJavascriptString(fvalue);
			} else if (fvalue instanceof Map) {
				resultStr = resultStr + ",\"" + key + "\":"
						+ MapUtils.toJavascriptString((Map)fvalue);
			} else if (fvalue instanceof Collection) { // 对象
				resultStr = resultStr
						+ ",\""
						+ key
						+ "\":"
						+ CollectionUtils
								.toJavascriptString((Collection)fvalue);
			} else {
				resultStr = resultStr + ",\"" + key + "\":"
						+ ObjectUtils.toJavascriptString(fvalue);
			}

		}

		return "{" + StringUtils.trimLeadingString(resultStr, ",") + "}";

	}
	
	public static <K,T> Map<K,T> list2map(List<T> list,String keyName){
		if(list==null || list.size()<=0){
			return null;
		}
		Map<K,T> map=new HashMap<>();
		for(int i=0; i<list.size(); i++){
			T el=list.get(i);
			K v=(K)ObjectUtils.getFieldValue(el, keyName);
			if(v!=null){
				map.put(v, el);
			}
		}
		
		return map;
	}

}
