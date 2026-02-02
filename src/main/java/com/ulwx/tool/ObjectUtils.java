/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ulwx.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.*;
import com.esotericsoftware.kryo.Kryo;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.*;
import com.rits.cloning.Cloner;
import com.ulwx.tool.deepequal.DeepEquals;
import com.ulwx.type.TInteger;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Consumer;


/**
 * Miscellaneous object utility methods. Mainly for internal use within the
 * framework; consider Jakarta's Commons Lang for a more comprehensive suite of
 * object utilities.
 * 
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rod Johnson
 * @author Rob Harrop
 * @author Alex Ruiz
 * @since 19.03.2004
 */
public abstract class ObjectUtils {

	public static Set<Class> simpleType = new HashSet<Class>();

	static {
		simpleType.add(boolean.class);
		simpleType.add(Boolean.class);
		simpleType.add(String.class);
		simpleType.add(char.class);
		simpleType.add(Character.class);
		simpleType.add(byte.class);
		simpleType.add(Byte.class);
		simpleType.add(Integer.class);
		simpleType.add(int.class);
		simpleType.add(Long.class);
		simpleType.add(long.class);
		simpleType.add(Short.class);
		simpleType.add(short.class);
		simpleType.add(Float.class);
		simpleType.add(float.class);
		simpleType.add(Double.class);
		simpleType.add(double.class);
		simpleType.add(LocalDate.class);
		simpleType.add(LocalDateTime.class);
		simpleType.add(LocalTime.class);
		simpleType.add(java.util.Date.class);

	}
	static final Logger log = LoggerFactory.getLogger(ObjectUtils.class);
	static final int INITIAL_HASH = 7;
	static final int MULTIPLIER = 31;

	static final String EMPTY_STRING = "";
	static final String NULL_STRING = "null";
	static final String ARRAY_START = "{";
	static final String ARRAY_END = "}";
	static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;
	static final String ARRAY_ELEMENT_SEPARATOR = ", ";

	/**
	 * Return whether the given throwable is a checked exception: that is, neither a
	 * RuntimeException nor an Error.
	 * 
	 * @param ex the throwable to check
	 * @return whether the throwable is a checked exception
	 * @see java.lang.Exception
	 * @see java.lang.RuntimeException
	 * @see java.lang.Error
	 */
	public static boolean isCheckedException(Throwable ex) {
		return !(ex instanceof RuntimeException || ex instanceof Error);
	}

	/**
	 * 
	 * @param o 要序列化的对象，必须遵循java序列化对象的要求,即对象是可序列化的
	 * @return 对象对象的字节数组
	 * @throws Exception
	 */
	public static byte[] serializeObject(Object o) throws Exception {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		if (o == null) {
			return null;
		}

		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(o);
			oos.flush();
			return bos.toByteArray();
		} finally {
			if (oos != null) {
				oos.close();
			}
		}
	}

	/**
	 * 
	 * @param data 对象序列化字节流
	 * @return 反序列化的对象
	 * @throws Exception
	 */
	public static Object deSerializeObject(byte[] data) throws Exception {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		if (data == null || data.length == 0) {
			return null;
		}
		try {
			bis = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(bis);
			return ois.readObject();
		} finally {
			if (ois != null) {
				ois.close();
			}
		}
	}

	public static boolean checkedSimpleType(Class t) {
		if (simpleType.contains(t)) {
			return true;
		}
		return false;
	}

	// 序列化对象
	public static String converToString(Object value, boolean isCompress) throws Exception {
		if (value != null)// 如果要设置的值非空，那么就需要先序列化，然后转化为String
		{
			try {
				byte[] b = serializeObject(value);
				if (isCompress) {
					b = ZipUtils.gzip(b);
				}
				String result = new String(b, "iso-8859-1");
				log.error("converToString===" + result);
				return result;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				throw e;
			}

		} else {
			return null;
		}

	}
	public static boolean deepEquals(Object src, Object dest) {
		return DeepEquals.deepEquals(src, dest);
	}
	// 反序列化对象
	public static Object converToObject(String value, boolean isCompress) throws Exception {
		log.error("converToObject1111111111===" + value);
		try {
			if (value != null) {
				byte[] b = value.getBytes("iso-8859-1");
				if (isCompress) {
					b = ZipUtils.ungzip(b);
				}
				// Object
				// result=deSerializeObject(ZipUtils.ungzip(value.getBytes("iso-8859-1")));
				Object result = deSerializeObject(b);
				return result;
			} else {
				return null;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Check whether the given exception is compatible with the exceptions declared
	 * in a throws clause.
	 * 
	 * @param ex                 the exception to checked
	 * @param declaredExceptions the exceptions declared in the throws clause
	 * @return whether the given exception is compatible
	 */
	public static boolean isCompatibleWithThrowsClause(Throwable ex, Class[] declaredExceptions) {
		if (!isCheckedException(ex)) {
			return true;
		}
		if (declaredExceptions != null) {
			for (int i = 0; i < declaredExceptions.length; i++) {
				if (declaredExceptions[i].isAssignableFrom(ex.getClass())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return whether the given array is empty: that is, <code>null</code> or of
	 * zero length.
	 * 
	 * @param array the array to check
	 * @return whether the given array is empty
	 */
	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * Append the given Object to the given array, returning a new array consisting
	 * of the input array contents plus the given Object.
	 * 
	 * @param array the array to append to (can be <code>null</code>)
	 * @param obj   the Object to append
	 * @return the new array (of the same component type; never <code>null</code>)
	 */
	public static Object[] addObjectToArray(Object[] array, Object obj) {
		Class compType = Object.class;
		if (array != null) {
			compType = array.getClass().getComponentType();
		} else if (obj != null) {
			compType = obj.getClass();
		}
		int newArrLength = (array != null ? array.length + 1 : 1);
		Object[] newArr = (Object[]) Array.newInstance(compType, newArrLength);
		if (array != null) {
			System.arraycopy(array, 0, newArr, 0, array.length);
		}
		newArr[newArr.length - 1] = obj;
		return newArr;
	}

	/**
	 * Convert the given array (which may be a primitive array) to an object array
	 * (if necessary of primitive wrapper objects).
	 * <p>
	 * A <code>null</code> source value will be converted to an empty Object array.
	 * 
	 * @param source the (potentially primitive) array
	 * @return the corresponding object array (never <code>null</code>)
	 * @throws IllegalArgumentException if the parameter is not an array
	 */
	public static Object[] toObjectArray(Object source) {
		if (source instanceof Object[]) {
			return (Object[]) source;
		}
		if (source == null) {
			return new Object[0];
		}
		if (!source.getClass().isArray()) {
			throw new IllegalArgumentException("Source is not an array: " + source);
		}

		int length = Array.getLength(source);
		if (length == 0) {
			return new Object[0];
		}
		Class wrapperType = Array.get(source, 0).getClass();
		Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
		for (int i = 0; i < length; i++) {
			newArray[i] = Array.get(source, i);
		}
		return newArray;
	}

	//
	/**
	 * 把java对象转换成一个map对象，javaBean对象里的日期属性转换成了字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> fromJavaBeanToMap(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(moduleForMapper);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		@SuppressWarnings("unchecked")
		Map<String, Object> fieldMap = mapper.convertValue(obj, Map.class);
		return fieldMap;
	}


	public static Map<String,Object> fromJavaBeanToMapByPubMethod(Object obj)throws Exception{
		return PropertyUtil.describe(obj); 
	}

	/**
	 * 通过反射实现obj到map的转换
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> fromJavaBeanToMap2(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();

		Class cls = obj.getClass();
		Field[] fields = FieldUtils.getAllFields(cls);
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				map.put(field.getName(), field.get(obj));
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return map;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> fromJavaBeanToMapNoNull(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();

		Class cls = obj.getClass();
		//Field[] fields = cls.getDeclaredFields();
		Field[] fields = FieldUtils.getAllFields(cls);
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				if (field.get(obj) != null) {
					map.put(field.getName(), field.get(obj));
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return map;
	}

	/**
	 * 把json字符串转换成指定type的对象
	 * 
	 * @param <T>
	 * @param json
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static <T> T fromJsonToObject(String json, Class<T> type) {

		GsonBuilder builder = new GsonBuilder();
		registGsonBuilder(builder);
		builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
		Gson gson = builder.create();
		T target = gson.fromJson(json, type);
		return target;

	}

	public static <T> T fromJsonToObjectUseFastJosn(String json, Class<T> type) throws Exception {

		return JSON.parseObject(json, type, Feature.AllowComment, Feature.DisableCircularReferenceDetect,
				Feature.AllowUnQuotedFieldNames, Feature.IgnoreNotMatch);

	}

	/**
	 * Type type = new TypeReference<Collection<Foo>>(){}.getType();
	 * 
	 * @param <T>
	 * @param json
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static <T> T fromJsonToObjectUseFastJosn(String json, Type type) throws Exception {

		return JSON.parseObject(json, type, Feature.AllowComment, Feature.DisableCircularReferenceDetect,
				Feature.AllowUnQuotedFieldNames, Feature.IgnoreNotMatch);

	}

	/**
	 * 利用阿里的解析器
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static Map fromJsonToMap(String json) {

		return JSON.parseObject(json);

	}

	public static Map fromJsonToMapWithFastJson(String json) {

		return JSON.parseObject(json);

	}

	/**
	 *
	 * @param json
	 * @param type
	 * <xmp>
	 *   Type typeOfT = new com.google.gson.reflect.TypeToken<Collection<Foo>>(){}.getType();
	 * </xmp>
	 * @return
	 * @throws Exception
	 */
	public static <T> T fromJsonToObject(String json, Type type) {

		GsonBuilder builder = new GsonBuilder();
		registGsonBuilder(builder);
		builder.setDateFormat("yyyy-MM-dd HH:mm:ss");

		Gson gson = builder.create();

		T target = gson.fromJson(json, type);
		return target;

	}

	/**
	 * 使json字符串转换成java的jdk内部对象 <blockquote>
	 * 
	 * <pre>
	 * Object -->LinkedHashMap<String,Object>, 
	 * array-->ArrayList<Object> ,
	 * string-->String , 
	 * number (no fraction)-->Integer, Long or BigInteger(smallest applicable) ,
	 * number (fraction) -->Double(configurable to use BigDecimal) , 
	 * true|false -->Boolean null --> null </code>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static Object fromJsonToObject(String json) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(moduleForMapper);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		Object obj = mapper.readValue(json, Object.class);

		return obj;
	}

	// public static SimpleModule getModule(){
	//
	//
	//
	//
	// }
	public static SimpleModule moduleForMapper = new SimpleModule();
	static {

		moduleForMapper.addSerializer(LocalDate.class, Converter.LocalDateSerializer.instance);
		moduleForMapper.addDeserializer(LocalDate.class, Converter.LocalDateDeSerializer.instance);

		moduleForMapper.addSerializer(LocalDateTime.class, Converter.LocalDateTimeSerializer.instance);
		moduleForMapper.addDeserializer(LocalDateTime.class, Converter.LocalDateTimeDeSerializer.instance);

		moduleForMapper.addSerializer(LocalTime.class, Converter.LocalTimeSerializer.instance);
		moduleForMapper.addDeserializer(LocalTime.class, Converter.LocalTimeDeSerializer.instance);

	}

	public static Object fromJsonToObject2(String json, Class t) throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		mapper.registerModule(moduleForMapper);

		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		Object obj = mapper.readValue(json, t);

		return obj;
	}

	// public static Object fromJsonTobject(String json) throws Exception {
	//
	// return fromJsonToObject(json);
	// }

	/**
	 * 把javaben输出成json 如果父接口或父类 和子类存在相同的属性，会报错，可使用toJsonString2(Object obj)方法来解决
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJsonString(Object obj) {
		return toJsonString(obj, true);

	}

	public static String toJsonString2(Object obj) {
		return toJsonString2(obj, false);
	}

	/**
	 * 解决toJsonString方法不能输出父子类相同属性的问题
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJsonString2(Object obj, boolean includeNull) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.registerModule(moduleForMapper);
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

			if (!includeNull) {
				// SerializationFeature.FAIL_ON_EMPTY_BEANS
				mapper.setSerializationInclusion(Include.NON_NULL);
			}

			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.error("", e);
		}
		return "";
	}

	public static String toJsonString(Object obj, boolean includeNull) {
		GsonBuilder builder = new GsonBuilder();
		registGsonBuilder(builder);

		if (includeNull)
			builder.serializeNulls();

		Gson gson = builder.create();

		String target = gson.toJson(obj);
		return target;

	}

	public static String toJsonString2(Object obj, boolean includeNull, boolean ifNullToDefault) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.registerModule(moduleForMapper);
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

			if (!includeNull) {
				mapper.setSerializationInclusion(Include.NON_NULL);

			} else {
				if (ifNullToDefault) {

					mapper.setSerializerFactory(mapper.getSerializerFactory()
							.withSerializerModifier(new Converter.MyBeanSerializerModifier()));
				}
			}
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.error("", e);
		}
		return "";

	}

	/**
	 * 
	 * @param obj
	 * @param t        用法如下：<p><pre class="code">Type typeOfT = new com.google.gson.reflect.TypeToken<Collection<Foo>>(){}.getType();</pre></p>
	 *
	 * @param includeNull
	 * @return
	 */
	public static String toJsonString(Object obj, Type t, boolean includeNull) {
		GsonBuilder builder = new GsonBuilder();
		registGsonBuilder(builder);

		Gson gson = builder.create();
		if (includeNull)
			builder.serializeNulls();
		String target = gson.toJson(obj, t);
		return target;

	}

	public static String toJsonString(Object obj, Type t) {
		return toJsonString(obj, t, false);
	}

	public static boolean isPrimitiveWapper(Class t) {
		if (t == Integer.class || t == Boolean.class || t == Long.class

				|| t == Short.class || t == Float.class || t == Double.class || t == Byte.class
				|| t == Character.class) {
			return true;
		}
		return false;

	}

	/**
	 * 
	 * @param javaBean
	 * @param fromMap
	 * @param mapStr:映射字符串，例如："a:b,c:null" 表明键a映射到javaBean的b属性;javaBean的c属性不用映射
	 * @return
	 */
	public static <T> T fromMapToJavaBean(T javaBean, Map fromMap, String mapStr) {
		if (fromMap == null)
			return javaBean;
		Map newMap = new HashMap();
		newMap.putAll(fromMap);

		if (StringUtils.hasText(mapStr)) {
			String[] strs = mapStr.split(",");
			strs = ArrayUtils.trim(strs);
			for (int i = 0; i < strs.length; i++) {
				String[] tow = strs[i].split(":");
				if (newMap.keySet().contains(tow[0])) {
					Object val = newMap.get(tow[0]);
					newMap.remove(tow[0]);
					if (!tow[1].equals("null")) {
						newMap.put(tow[1], val);
					}

				}

			}
		}
		return fromMapToJavaBean(javaBean, newMap);
	}

	public static <T> T fromMapToBeanWithField(T javaBean, Map fromMap, Class superClass) {
		try {

			T bean = javaBean;

			TInteger superLen = new TInteger();
			Field[] fields = getFields(javaBean.getClass(), superLen, superClass);

			for (int n = fields.length - 1; n >= 0; n--) {

				String name = (String) fields[n].getName();

				Class<?> t = fields[n].getType();
				Object value = null;
				Object obj = fromMap.get(name);
				// name为javabean属性名
				// 简单类型
				if (obj != null) {
					if (obj.getClass().isArray()) {// 源是数组

						if (!t.isArray()) {// 目标为非数组

							Object objval = null;
							if (Array.getLength(obj) > 0) {// 源数组的元素个数
								objval = Array.get(obj, 0);
								if (objval instanceof String) {// 源数组元素为string
									if (t == String.class) {// 目标元素为String
										String des = ArrayUtils.toString(obj, ",");
										ObjectUtils.setFieldValue(bean, fields[n], des);

									} else {// 目标元素为非String
										Object des = convertStringToTargetClassObject(objval + "", t);
										ObjectUtils.setFieldValue(bean, fields[n], des);
									}

								} else {// 源数组元素为非string
									if (t == String.class) {// 目标为string
										if (objval != null) {
											String des = ArrayUtils.toString(obj, ",");
											ObjectUtils.setFieldValue(bean, fields[n], des);

										} else {

											ObjectUtils.setFieldValue(bean, fields[n], (String) null);
										}
									} else {// 目标非string
										ObjectUtils.setFieldValue(bean, fields[n], objval);
									}

								}
							} else {
								ObjectUtils.setFieldValue(bean, fields[n], objval);
							}
						} else {// 目标为数组
							Class ct = t.getComponentType();
							Object arrays = Array.newInstance(ct, Array.getLength(obj));
							for (int m = 0; m < Array.getLength(obj); m++) {
								Object v = Array.get(obj, m);// 源数组元素
								if (v instanceof String) {// 源数组元素为string
									Object des = convertStringToTargetClassObject(v + "", ct);
									Array.set(arrays, m, des);

								} else {// 源数组元素为非string
									if (ct == String.class) {// 目标为String
										if (v != null) {
											Array.set(arrays, m, v.toString());
										} else {
											Array.set(arrays, m, null);
										}

									} else {
										Array.set(arrays, m, v);
									}

								}
							}
							ObjectUtils.setFieldValue(bean, fields[n], arrays);

						}

					} else {// 源为非数组
						if (!t.isArray()) {// 目标为非数组

							if (obj instanceof String) {// 目标为string
								String objStr = null;
								objStr = obj.toString();
								Object des = convertStringToTargetClassObject(objStr, t);
								ObjectUtils.setFieldValue(bean, fields[n], des);

							} else {// 目标为非string
								ObjectUtils.setFieldValue(bean, fields[n], obj);
							}
						} else {// 目标为数组
							Class ct = t.getComponentType();
							Object arrays = Array.newInstance(ct, 1);

							Object v = obj;
							if (v instanceof String) {
								Object des = convertStringToTargetClassObject(v + "", ct);
								Array.set(arrays, 0, des);

							} else {
								if (ct == String.class) {
									Array.set(arrays, 0, v.toString());
								} else {
									Array.set(arrays, 0, v);
								}

							}

							ObjectUtils.setFieldValue(bean, fields[n], arrays);

						}
					}
				} else {
					if (!t.isArray()) {//// 目标为非数组
						ObjectUtils.setFieldValue(bean, fields[n], null);
					} else {
						//
					}
				}

			}

			return bean;
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}

	/**
	 * 
	 * @param javaBean 转换的目标javaBean
	 * @param fromMap  带需要转换的Map
	 * @return
	 */
	public static <T> T fromMapToJavaBean(T javaBean, Map fromMap) {

		try {

			T bean = javaBean;
			// Map<String, Object> nestedObjs = new HashMap<String,
			// Object>();
			Map<?, ?> map = PropertyUtil.describe(bean);
			Set<?> set = map.keySet();
			Iterator<?> i = set.iterator();
			Map newFromMap = new HashMap();
			Set fromMapKeys = fromMap.keySet();
			for (Object key : fromMapKeys) {
				String key2 = StringUtils.firstCharLowCase(key.toString());
				newFromMap.put(key2, fromMap.get(key));
			}

			while (i.hasNext()) {

				String name = (String) i.next();
				if (name.equals("class"))
					continue;

				Class<?> t = PropertyUtil.getPropertyType(bean, name);
				Object value = null;
				Object obj = newFromMap.get(name);
				// name为javabean属性名
				// 简单类型

				if (obj != null) {
					if (obj.getClass().isArray()) {// 源是数组

						if (!t.isArray()) {// 目标为非数组

							Object objval = null;
							if (Array.getLength(obj) > 0) {// 源数组的元素个数
								objval = Array.get(obj, 0);
								if (objval instanceof String) {// 源数组元素为string
									if (t == String.class) {// 目标元素为String
										String des = ArrayUtils.toString(obj, ",");
										PropertyUtil.setProperty(bean, name, des);
									} else {// 目标元素为非String
										Object des = convertStringToTargetClassObject(objval + "", t);
										PropertyUtil.setProperty(bean, name, des);
									}

								} else {// 源数组元素为非string
									if (t == String.class) {// 目标为string
										if (objval != null) {
											String des = ArrayUtils.toString(obj, ",");
											PropertyUtil.setProperty(bean, name, des);

										} else {
											PropertyUtil.setProperty(bean, name, (String) null);
										}
									} else {// 目标非string
										PropertyUtil.setProperty(bean, name, objval);
									}

								}
							} else {
								PropertyUtil.setProperty(bean, name, objval);
							}
						} else {// 目标为数组
							Class ct = t.getComponentType();
							Object arrays = Array.newInstance(ct, Array.getLength(obj));
							for (int m = 0; m < Array.getLength(obj); m++) {
								Object v = Array.get(obj, m);// 源数组元素
								if (v instanceof String) {// 源数组元素为string
									Object des = convertStringToTargetClassObject(v + "", ct);
									Array.set(arrays, m, des);

								} else {// 源数组元素为非string
									if (ct == String.class) {// 目标为String
										if (v != null) {
											Array.set(arrays, m, v.toString());
										} else {
											Array.set(arrays, m, null);
										}

									} else {
										Array.set(arrays, m, v);
									}

								}
							}
							PropertyUtil.setProperty(bean, name, arrays);

						}

					} else {// 源为非数组
						if (!t.isArray()) {// 目标为非数组

							if (obj instanceof String) {// 目标为string
								String objStr = null;
								objStr = obj.toString();
								Object des = convertStringToTargetClassObject(objStr, t);
								PropertyUtil.setProperty(bean, name, des);

							} else {// 目标为非string
								PropertyUtil.setProperty(bean, name, obj);
							}
						} else {// 目标为数组
							Class ct = t.getComponentType();
							Object arrays = Array.newInstance(ct, 1);

							Object v = obj;
							if (v instanceof String) {
								Object des = convertStringToTargetClassObject(v + "", ct);
								Array.set(arrays, 0, des);

							} else {
								if (ct == String.class) {
									Array.set(arrays, 0, v.toString());
								} else {
									Array.set(arrays, 0, v);
								}

							}

							PropertyUtil.setProperty(bean, name, arrays);

						}
					}
				} else {
					if (!t.isArray()) {//// 目标为非数组
						PropertyUtil.setProperty(bean, name, null);
					} else {
						//
					}
				}

			} // while

			return bean;
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}

	public static Object convertStringToTargetClassObject(String src, Class targetClass) {

		if (src == null) {
			return null;
		}
		if (ObjectUtils.checkedSimpleType(targetClass)) {
			if (targetClass == String.class) {
				return src;
			} else if (targetClass == char.class || targetClass == Character.class) {
				if (src.length() >= 1)
					return src.charAt(0);
			} else if (targetClass == java.util.Date.class) {
				try {
					if (StringUtils.hasText(src))
						return CTime.parseDateTime(src);
				} catch (Exception e) {
					return null;
				}
			}
			else if (targetClass == LocalDate.class) {
				try {
					if (StringUtils.hasText(src))
						return CTime.parseToLocalDate(src);
				} catch (Exception e) {
					return null;
				}
			}
			else if (targetClass == LocalDateTime.class) {
				try {
					if (StringUtils.hasText(src))
						return CTime.parseToLocalDateTimeWithCommon(src);
				} catch (Exception e) {
					return null;
				}
			}
			else if (targetClass == LocalTime.class) {
				try {
					if (StringUtils.hasText(src))
						return CTime.parseToLocalTime(src);
				} catch (Exception e) {
					return null;
				}
			}
			else {
				if (StringUtils.hasText(src)) {
					return NumberUtils.convertNumberToTargetClass(src.trim(), targetClass);
				} else {
					return null;
				}
			}
		}
		return null;

	}

	public static Map<String, Object> getMapFromResultSet(ResultSet rs) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {

			ResultSetMetaData rsMeta = rs.getMetaData();

			for (int i = 0; i < rsMeta.getColumnCount(); i++) {
				String columnName = rsMeta.getColumnLabel(i + 1);
				log.debug("columnName=" + columnName + ",val=" + rs.getObject(columnName));

				map.put(columnName, rs.getObject(columnName));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("", e);
		}
		return map;

	}

	/**
	 * 把一个JavaBean转换成一个javabean，第一个JavaBean里的所有和第二个JavaBean相同名称
	 * 属性都会赋予第二个JavaBean的相应属性. 注意：属性类型只能是简单类型才能赋值
	 *
	 * @param clazz
	 * @param fromBean
	 * @return
	 */
	public static <T1, T2> T1 getBeanFromBean(Class<T1> clazz, T2 fromBean) {

		try {

			if (fromBean == null)
				return null;
			T1 bean = clazz.newInstance();
			return PropertyUtil.copyProperties(bean, fromBean);

		} catch (Exception e) {
			log.error("", e);
		}

		return null;

	}

	/**
	 * 属性必须要有get，set方法
	 * 
	 * @param clazz
	 * @param fromBean
	 * @return
	 */
	public static <T1, T2> T1 fromBeanToBean(Class<T1> clazz, T2 fromBean) {
		return getBeanFromBean(clazz, fromBean);
	}

	/**
	 * 搜索本类或父类的Method,从本类开始查找，再一级级搜索父类
	 * 
	 * @param object         : javabean对象
	 * @param methodName     : 类中的方法名
	 * @param parameterTypes : 父类中的方法参数类型
	 * @return 父类中的方法对象
	 */
	public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
		Method method = null;
		Method[] methods = null;
		for (Class<?> clazz = object.getClass(); clazz != null
				&& clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
				return method;
			} catch (Exception e) {
				// 这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
				// 如果这里的异常打印或者往外抛，则就不会执行clazz =
				// clazz.getSuperclass(),最后就不会进入到父类中了
			}
		}
		return null;
	}

	/**
	 * 调用对象方法, 包含(private, protected, default)，从本类开始搜索，没有的化再一级级搜索父类，存在执行后返回
	 * 
	 * @param object         : javabean对象
	 * @param methodName     : 方法名
	 * @param parameterTypes : 参数类型
	 * @param parameters     : 方法参数
	 * @return 父类中方法的执行结果
	 */
	public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
			Object[] parameters) {
		// 根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象
		Method method = getDeclaredMethod(object, methodName, parameterTypes);

		// 抑制Java对方法进行检查,主要是针对私有方法而言
		method.setAccessible(true);

		try {
			if (null != method) {
				// 调用object 的 method 所代表的方法，其方法的参数是 parameters
				return method.invoke(object, parameters);
			}
		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		} catch (InvocationTargetException e) {

		}
		return null;
	}

	/**
	 * 搜索本类或所有父类里指定的属性 ，从本类开始找，再一级级搜索父类
	 * 
	 * @param object         : javabean对象
	 * @param fieldName      : 属性名
	 * @param fromParentClass 从哪个基类开始查找
	 * @return 父类中的属性对象
	 */
	public static Field getDeclaredField(Object object, String fieldName, Class fromParentClass) {
		Field field = null;
		Class<?> clazz = object.getClass();
		for (; clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
			if (fromParentClass != null && !clazz.isAssignableFrom(fromParentClass)) {
				continue;
			}
			try {
				field = clazz.getDeclaredField(fieldName);
				return field;
			} catch (Exception e) {
			}

		}
		return null;
	}

	/**
	 * 设置对象属性值,包含private/protected 修饰符，从本类开始查找，再一级级搜索父类，存在的化就设置完退出
	 * 
	 * @param object    : javabean对象
	 * @param fieldName : 属性名
	 * @param value     : 将要设置的值
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) {
		// 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(object, fieldName, null);
		if (field != null) {
			// 抑制Java对其的检查
			field.setAccessible(true);
			try {
				// 将 object 中 field 所代表的值 设置为 value
				if (value instanceof Number) {
					Class fc = field.getType();

					if (NumberUtils.isNumber(fc)) {
						try {
							value = NumberUtils.convertNumberToTargetClass((Number) value, fc);
						} catch (Exception e) {
							log.error("", e);
						}
					}

				}
				field.set(object, value);
			} catch (IllegalArgumentException e) {

			} catch (IllegalAccessException e) {
			}
		}
	}

	public static void setFieldValue(Object object, String fieldName, Object value, Class fromParentClass) {
		// 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(object, fieldName, fromParentClass);
		// 抑制Java对其的检查
		if (field != null) {
			field.setAccessible(true);
			try {
				// 将 object 中 field 所代表的值 设置为 value
				if (value instanceof Number) {
					Class fc = field.getType();
					;

					if (NumberUtils.isNumber(fc)) {
						try {
							value = NumberUtils.convertNumberToTargetClass((Number) value, fc);
						} catch (Exception e) {
							log.error("", e);
						}
					}

				}
				field.set(object, value);
			} catch (Exception e) {

			}
		}
	}

	public static void setFieldValue(Object object, Field field, Object value) {
		// 抑制Java对其的检查
		if (field != null) {
			field.setAccessible(true);
			try {

				if (value instanceof Number) {
					Class fc = field.getType();

					if (NumberUtils.isNumber(fc)) {
						try {
							value = NumberUtils.convertNumberToTargetClass((Number) value, fc);
						} catch (Exception e) {
							log.error("", e);
						}
					}

				}

				field.set(object, value);
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	/**
	 * 读取对象的属性值，从本类开始查找，再一级级搜索父类，存在就返回, 包含 private/protected 修饰符
	 * 
	 * @param object    : javabean对象
	 * @param fieldName : 属性名
	 * @return : 父类中的属性值
	 */
	public static Object getFieldValue(Object object, String fieldName) {

		// 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(object, fieldName, null);
		if (field != null) {
			// 抑制Java对其的检查
			field.setAccessible(true);
			try {
				// 获取 object 中 field 所代表的属性值
				return field.get(object);
			} catch (Exception e) {

			}
		}
		return null;
	}

	public static Object getFieldValue(Object object, String fieldName, Class fromParentClass) {

		// 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(object, fieldName, fromParentClass);
		if (field != null) {
			// 抑制Java对其的检查
			field.setAccessible(true);
			try {
				// 获取 object 中 field 所代表的属性值
				return field.get(object);
			} catch (Exception e) {

			}
		}
		return null;
	}

	public static List<Field> getPublicDeclaredFields(Class c) {

		List<Field> list = new ArrayList<Field>();
		Field[] fields = c.getDeclaredFields();/////
		for (int m = 0; m < fields.length; m++) {
			if (Modifier.isPublic(fields[m].getModifiers()) && !Modifier.isStatic(fields[m].getModifiers())
					&& !Modifier.isFinal(fields[m].getModifiers())) {
				list.add(fields[m]);
			}
		}
		return list;
	}

	/**
	 * 获取指定class的所有公共域
	 * 
	 * @param c
	 * @param superLen
	 * @param fromParentClass
	 * @return
	 */
	public static Field[] getPublicFileds(Class c, TInteger superLen, Class fromParentClass) {

		String resultStr = "";

		List<Class> listClazz = new ArrayList<Class>();
		List<Field> list = new ArrayList<Field>();
		Class superClazz = c.getSuperclass();
		if (superClazz == null) {// 表名是Object
			return new Field[0];
		}
		while (superClazz != null && superClazz != Object.class) {
			listClazz.add(superClazz);
			superClazz = superClazz.getSuperclass();

		} //////
		List<Field> includeClassesFields = new ArrayList<Field>();
		for (int i = listClazz.size() - 1; i >= 0; i--) {
			Class clazz = listClazz.get(i);
			List<Field> fdList = getPublicDeclaredFields(clazz);
			if (fromParentClass != null && clazz.isAssignableFrom(fromParentClass)) {
				list.addAll(fdList);
			} else {
				includeClassesFields.addAll(fdList);

			}

		}
		int superClassFieldLen = list.size();
		superLen.setValue(superClassFieldLen);

		list.addAll(includeClassesFields);

		List<Field> fdList = getPublicDeclaredFields(c);
		list.addAll(fdList);

		Field[] curFields = list.toArray(new Field[0]);

		return curFields;

	}

	/**
	 * 
	 * @param targetClazz    目标类
	 * @param fromBean 带需要转换的源javaBean
	 * @param  fromParentClass
	 */
	public static <T1, T2> T1 fromBeanWithPublicFieldToBean(Class<T1> targetClazz, T2 fromBean, Class fromParentClass) {
		TInteger superLen = new TInteger();
		Field[] fields = getPublicFileds(fromBean.getClass(), superLen, fromParentClass);

		try {
			T1 target = targetClazz.newInstance();
			for (Field field : fields) {
				field.setAccessible(true);
				try {
					ObjectUtils.setFieldValue(target, field.getName(), field.get(fromBean), fromParentClass);
				} catch (Exception e) {
				}
			}
			return target;
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}

	/**
	 * 得到类里的public的Filed
	 * 
	 * @param c        ：javabean对象
	 * @param superLen ：返回的数组里父类占的个数，从数组索引0开始算
	 * @return
	 */
	public static Field[] getPublicFileds(Class c, TInteger superLen) {

		return getPublicFileds(c, superLen, null);

	}

	public static Field[] getFields(Class c, TInteger superLen) {
		return getFields(c, superLen, null);
	}

	/**
	 * 得到类和父类里的所有的Filed，包括共有的，私有的，protected，不包括final和static的域
	 * 
	 * @param c
	 * @param superLen ：返回的数组里父类占的个数，从数组索引0开始算
	 * @return
	 */
	public static Field[] getFields(Class c, TInteger superLen, Class fromParentClass) {

		String resultStr = "";

		List<Class> listClazz = new ArrayList<Class>();
		List<Field> list = new ArrayList<Field>();
		Class superClazz = c.getSuperclass();
		if (superClazz == null) {// 表名是Object
			return new Field[0];
		}
		while (superClazz != null && superClazz != Object.class) {

			if (fromParentClass != null && !superClazz.isAssignableFrom(fromParentClass)) {
				continue;
			}

			listClazz.add(superClazz);
			superClazz = superClazz.getSuperclass();

		} //////

		for (int i = listClazz.size() - 1; i >= 0; i--) {
			Class clazz = listClazz.get(i);

			Field[] fields = clazz.getDeclaredFields();/////
			for (int m = 0; m < fields.length; m++) {
				fields[m].setAccessible(true);

				if (!Modifier.isStatic(fields[m].getModifiers()) && !Modifier.isFinal(fields[m].getModifiers())) {
					list.add(fields[m]);
				}
			}
		}
		int superClassFieldLen = list.size();
		superLen.setValue(superClassFieldLen);
		// 当前类的声明属性
		Field[] pfields = c.getDeclaredFields();

		for (int m = 0; m < pfields.length; m++) {
			if (!Modifier.isStatic(pfields[m].getModifiers()) && !Modifier.isFinal(pfields[m].getModifiers())) {
				list.add(pfields[m]);
			}
		}
		Field[] curFields = list.toArray(new Field[0]);

		return curFields;

	}

	public static String toJavascriptString(Object obj) throws Exception {

		String resultStr = "";
		if (obj == null)
			return "{}";
		if (obj.getClass().isPrimitive() || isPrimitiveWapper(obj.getClass())) {
			return resultStr = resultStr + obj;
		} else if (obj.getClass() == String.class) {
			if (((String) obj).startsWith("[javascript]")) {
				return resultStr = StringUtils.trimLeadingString(obj + "", "[javascript]");
			} else {
				return resultStr = resultStr + "\"" + obj + "\"";
			}
		} else if (obj.getClass().isArray()) {
			return ArrayUtils.toJavascriptString(obj);
		} else if (obj instanceof Map) {
			return MapUtils.toJavascriptString((Map) obj);
		} else if (obj instanceof Collection) {
			return CollectionUtils.toJavascriptString((Collection) obj);
		}

		Class c = obj.getClass();

		Field[] fields = c.getDeclaredFields();
		Map<String, Object> fieldsNameValues = new HashMap<String, Object>();
		Map<String, Field> fieldsNameField = new HashMap<String, Field>();
		String[] values = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod))
				continue;

			values[i] = field.getName();
			fieldsNameValues.put(field.getName(), field.get(obj));
			fieldsNameField.put(field.getName(), field);
		}
		try {

			for (int i = 0; i < values.length; i++) {
				String fieldName = values[i];

				Field field = fieldsNameField.get(fieldName);

				Object fvalue = field.get(obj);
				if (fvalue == null) {
					continue;
				}

				Class<?> fieldType = field.getType();

				// 基本类型
				if (fieldType.isPrimitive() || isPrimitiveWapper(fieldType) || fieldType == String.class) {

					resultStr = resultStr + ",\"" + fieldName + "\":" + ObjectUtils.toJavascriptString(field.get(obj));

				} else if (fieldType.isArray()) {// 数组

					resultStr = resultStr + "," + ArrayUtils.toJavascriptString(field.get(obj));

				} else if (fvalue instanceof Map) {
					resultStr = resultStr + "," + MapUtils.toJavascriptString((Map) field.get(obj));

				} else if (fvalue instanceof Collection) {

					resultStr = resultStr + "," + CollectionUtils.toJavascriptString((Collection) field.get(obj));
				} else if (fieldType instanceof Object) {
					resultStr = resultStr + "," + ObjectUtils.toJavascriptString(obj);
				} else {
					throw new Exception("java bean里的" + fieldName + "属性的类型" + fieldType + "不支持！");
				}

			}
		} catch (Exception e) {
			throw new Exception("seq属性配置不正确或者配置的javabean与二进制流格式不匹配导致解析出错");
		}

		return "{" + StringUtils.trimLeadingString(resultStr, ",") + "}";
	}

	/**
	 * 使用Cloner实现 深拷贝
	 * 
	 * @param object
	 * @return
	 */
	public static <T> T CloneWithDeep(T object) {

		Cloner cloner = new Cloner();
		cloner.setNullTransient(true);
		return cloner.deepClone(object);

	}

	/**
	 * 使用Clone实现
	 * @param object
	 * @param setter
	 * @return
	 * @param <T>
	 */
	public static <T> T CloneWithDeep(T object, Consumer<Cloner> setter) {

		Cloner cloner = new Cloner();
		cloner.setNullTransient(true);
		setter.accept(cloner);
		return cloner.deepClone(object);

	}

	/**
	 * 使用Kryo实现深拷贝
	 * @param object
	 * @param shallow
	 * @return
	 * @param <T>
	 */
	public static <T> T CloneWith(T object,boolean shallow) {

		Kryo kryo = new Kryo();
		if(shallow){
			T deepCloned = kryo.copyShallow(object);
			return deepCloned;
		}else{
			T cloned = kryo.copy(object);
			return cloned;
		}


	}
	/**
	 * 反射机制实现的深拷贝
	 * 
	 * @param <T>
	 * @param object 需要深拷贝的类
	 * @param c      ：object中不需要拷贝的类,会设置为null
	 * @return
	 */
	public static <T> T CloneWithDeep(T object, java.lang.Class<?>... c) {

		Cloner cloner = new Cloner();
		cloner.registerConstant(object.getClass(),"serialVersionUID");
		cloner.nullInsteadOfClone(c);
		return cloner.deepClone(object);

	}

	public static String toString(Object obj) {
		return toString(obj, true);
	}

	public static String toString(Object obj, boolean includeNull) {

		GsonBuilder builder = new GsonBuilder();
		if (includeNull)
			builder.serializeNulls();
		builder.disableHtmlEscaping();
		registGsonBuilder(builder);
		builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		return gson.toJson(obj);

	}

	/**
	 * 
	 * @param obj
	 * @param type 用法如下： Type typeOfT = new
	 *             com.google.gson.reflect.TypeToken<Collection
	 *             <Foo>>(){}.getType();
	 * @return
	 */
	public static String toString(Object obj, Type type) {

		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();

		registGsonBuilder(builder);
		builder.disableHtmlEscaping();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		return gson.toJson(obj, type);
	}

	public static String toStringUseFastJson(Object obj) {
		return toStringUseFastJson(obj, false);
	}

	public static String toStringUseFastJson(Object obj, boolean includeNull) {

		return toStringUseFastJson(obj, includeNull, false, false);
	}

	public static String toStringUseFastJson(Object obj, boolean includeNull, boolean singleQuotes,
			boolean prettyForamt) {

		List<SerializerFeature> list = new ArrayList<>();
		list.add(SerializerFeature.IgnoreErrorGetter);
		if (prettyForamt)
			list.add(SerializerFeature.PrettyFormat);
		if (includeNull)
			list.add(SerializerFeature.WriteMapNullValue);
		if (singleQuotes) {
			list.add(SerializerFeature.UseSingleQuotes);
		}
		list.add(SerializerFeature.IgnoreNonFieldGetter);
		return JSON.toJSONString(obj, fastJsonConfig, list.toArray(new SerializerFeature[0]));

	}

	public static void registGsonBuilder(GsonBuilder builder) {
		builder.registerTypeAdapter(Date.class, dateGsonConverter);
		builder.registerTypeAdapter(Class.class, classGsonConverter);
		builder.registerTypeAdapter(LocalDateTime.class, localDateTimeGsonConverter);
		builder.registerTypeAdapter(LocalDate.class, localDateGsonConverter);
		builder.registerTypeAdapter(LocalTime.class, LocalTimeGsonConverter);
		builder.registerTypeAdapter(Double.class,emptyStringToDoubleDeserializer);
		builder.registerTypeAdapter(Float.class,emptyStringToFloatDeserializer);
		builder.registerTypeAdapter(Integer.class,emptyStringToIntegerDeserializer);
		builder.registerTypeAdapter(Long.class,emptyStringToLongDeserializer);
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		String json = "{\"month_1\":12,\"month_2\":12.0,\"month_3\":13.0,\"month_6\":14.0, \"month_9\":14.5,\"month_12\":15.0, \"month_15\":15.5, \"month_18\":15.5,\"month_24\":16.0}";
		Map map = (Map) ObjectUtils.fromJsonToObject(json);
		map.put("dt", LocalDateTime.now());
		map.put("dt2", LocalDate.now());
		System.out.println(ObjectUtils.toString(map));

		System.out.println(ObjectUtils.toStringUseFastJson(map));
		String sss = ObjectUtils.toStringUseFastJson(map);
		// System.out.println(JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd
		// HH:mm:ss.SSS"));

		TestBean m = ObjectUtils.fromJsonToObjectUseFastJosn(sss, TestBean.class);
		String s = ObjectUtils.toStringUseFastJson(m);
		System.out.println("m=" + s);
		Map mp = ObjectUtils.fromJsonToMap(s);

		mp.put("big", new BigDecimal(1233333.222));
		s = ObjectUtils.toStringUseFastJson(mp, false, true, true);
		System.out.println("mp=" + s);
		Map mmp = ObjectUtils.fromJavaBeanToMap(m);
		for (Object key : mmp.keySet()) {
			Object v = mmp.get(key);
			int ss = 00;
		}
		System.out.print(mmp);

	}

	public static Converter.DateGsonConverter dateGsonConverter = new Converter.DateGsonConverter();
	public static Converter.ClassGsonConverter classGsonConverter = new Converter.ClassGsonConverter();
	public static Converter.LocalDateTimeGsonConverter localDateTimeGsonConverter = new Converter.LocalDateTimeGsonConverter();
	public static Converter.LocalDateGsonConverter localDateGsonConverter = new Converter.LocalDateGsonConverter();
	public static Converter.LocalTimeGsonConverter LocalTimeGsonConverter = new Converter.LocalTimeGsonConverter();//
	public static Converter.EmptyStringToDoubleDeserializer emptyStringToDoubleDeserializer=new Converter.EmptyStringToDoubleDeserializer();
	public static Converter.EmptyStringToFloatDeserializer emptyStringToFloatDeserializer=new Converter.EmptyStringToFloatDeserializer();
	public static Converter.EmptyStringToIntegerDeserializer emptyStringToIntegerDeserializer=new Converter.EmptyStringToIntegerDeserializer();
	public static Converter.EmptyStringToLongDeserializer emptyStringToLongDeserializer=new Converter.EmptyStringToLongDeserializer();


	public static Converter.FalstLocalDateFormatSerializer ld = new Converter.FalstLocalDateFormatSerializer();
	public static Converter.FalstLocalDateTimeFormatSerializer ldt = new Converter.FalstLocalDateTimeFormatSerializer();
	public static Converter.FalstLocalTimeFormatSerializer lt = new Converter.FalstLocalTimeFormatSerializer();

	public static SerializeConfig fastJsonConfig = new SerializeConfig();
	static {
		fastJsonConfig.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		fastJsonConfig.put(LocalDate.class, ld);
		fastJsonConfig.put(LocalDateTime.class, ldt);
		fastJsonConfig.put(LocalTime.class, lt);
	}

	public static class TestBean {
		private LocalDateTime dt;
		private Double month_3;
		private Double month_6;
		private LocalDate dt2;
		private Double month_2;
		private Double month_1;
		private Double month_18;

		private Double month_12;
		private Double month_24;

		private Double month_9;
		// private Double month_15;
		private Double xxxx;

		public Double getXxxx() {
			return xxxx;
		}

		public void setXxxx(Double xxxx) {
			this.xxxx = xxxx;
		}

		public LocalDateTime getDt() {
			return dt;
		}

		public void setDt(LocalDateTime dt) {
			this.dt = dt;
		}

		public Double getMonth_3() {
			return month_3;
		}

		public void setMonth_3(Double month_3) {
			this.month_3 = month_3;
		}

		public Double getMonth_6() {
			return month_6;
		}

		public void setMonth_6(Double month_6) {
			this.month_6 = month_6;
		}

		public LocalDate getDt2() {
			return dt2;
		}

		public void setDt2(LocalDate dt2) {
			this.dt2 = dt2;
		}

		public Double getMonth_2() {
			return month_2;
		}

		public void setMonth_2(Double month_2) {
			this.month_2 = month_2;
		}

		public Double getMonth_1() {
			return month_1;
		}

		public void setMonth_1(Double month_1) {
			this.month_1 = month_1;
		}

		public Double getMonth_18() {
			return month_18;
		}

		public void setMonth_18(Double month_18) {
			this.month_18 = month_18;
		}

		public Double getMonth_12() {
			return month_12;
		}

		public void setMonth_12(Double month_12) {
			this.month_12 = month_12;
		}

		public Double getMonth_24() {
			return month_24;
		}

		public void setMonth_24(Double month_24) {
			this.month_24 = month_24;
		}

		public Double getMonth_9() {
			return month_9;
		}

		public void setMonth_9(Double month_9) {
			this.month_9 = month_9;
		}
//		public Double getMonth_15() {
//			return month_15;
//		}
//		public void setMonth_15(Double month_15) {
//			this.month_15 = month_15;
//		}

	}

}

class Converter {
	public static class DateGsonConverter implements JsonSerializer<Date>, JsonDeserializer<Date> {

		@Override
		public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			// TODO Auto-generated method stub
			if (json == null) {
				return null;
			} else {

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				java.util.Date date;
				try {
					date = formatter.parse(json.getAsString());
				} catch (ParseException e) {

				}
				return null;
			}
		}

		@Override
		public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
			// TODO Auto-generated method stub
			if (src == null) {
				return null;
			} else {

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String ret = null;
				ret = formatter.format(src);
				return new JsonPrimitive(ret);

			}
		}

	}

	public static class LocalDateTimeGsonConverter
			implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

		@Override
		public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			// TODO Auto-generated method stub
			if (json == null) {
				return null;
			} else {

				LocalDateTime date;
				try {
					date = LocalDateTime.parse(json.getAsString(), CTime.DTF_YMD_HH_MM_SS_SSS);
					return date;
				} catch (Exception e) {

				}
				return null;
			}
		}

		@Override
		public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
			// TODO Auto-generated method stub
			if (src == null) {
				return null;
			} else {

				String ret = null;
				ret = src.format(CTime.DTF_YMD_HH_MM_SS);
				return new JsonPrimitive(ret);

			}
		}

	}

	public static class LocalDateGsonConverter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

		@Override
		public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			// TODO Auto-generated method stub
			if (json == null) {
				return null;
			} else {

				LocalDate date;
				try {
					date = LocalDate.parse(json.getAsString());
					return date;
				} catch (Exception e) {
				}
				return null;
			}
		}

		@Override
		public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
			// TODO Auto-generated method stub
			if (src == null) {
				return null;
			} else {

				String ret = null;
				ret = src.toString();
				return new JsonPrimitive(ret);

			}
		}

	}

	public static class EmptyStringToIntegerDeserializer implements JsonDeserializer<Integer> {
		@Override
		public Integer deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			String stringValue = json.getAsString();
			try{
				return Integer.valueOf(stringValue);
			}catch (Exception e){
				return null;
			}
		}

	}
	public static class EmptyStringToDoubleDeserializer implements JsonDeserializer<Double>,JsonSerializer<Double>  {
		@Override
		public Double deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			String stringValue = json.getAsString();
			try{
				return Double.valueOf(stringValue);
			}catch (Exception e){
				return null;
			}
		}
		@Override
		public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
			if (src == src.longValue())
				return new JsonPrimitive(src.longValue());
			return new JsonPrimitive(src);
		}
	}
	public static class EmptyStringToFloatDeserializer implements JsonDeserializer<Float> {
		@Override
		public Float deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			String stringValue = json.getAsString();
			try{
				return Float.valueOf(stringValue);
			}catch (Exception e){
				return null;
			}
		}
	}
	public static class EmptyStringToLongDeserializer implements JsonDeserializer<Long> {
		@Override
		public Long deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			String stringValue = json.getAsString();
			try{
				return Long.valueOf(stringValue);
			}catch (Exception e){
				return null;
			}
		}
	}

	public static class LocalTimeGsonConverter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

		@Override
		public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			// TODO Auto-generated method stub
			if (json == null) {
				return null;
			} else {

				LocalTime date=null;
				try {
					date = LocalTime.parse(json.getAsString());
				} catch (Exception e) {
				}
				return date;
			}
		}

		@Override
		public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
			// TODO Auto-generated method stub
			if (src == null) {
				return null;
			} else {

				String ret = null;
				ret = src.toString();
				return new JsonPrimitive(ret);

			}
		}

	}

	public static class ClassGsonConverter implements JsonSerializer<Class>, JsonDeserializer<Class> {

		@Override
		public Class deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			// TODO Auto-generated method stub
			if (json == null) {
				return null;
			} else {

				String t = json.getAsString();
				try {
					return Class.forName(t);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					return null;
				}
			}
		}

		@Override
		public JsonElement serialize(Class src, Type typeOfSrc, JsonSerializationContext context) {
			// TODO Auto-generated method stub
			if (src == null) {
				return null;
			} else {

				String ret = src.getName();
				return new JsonPrimitive(ret);

			}
		}

	}

	public static class MyNullArrayJsonSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Object> {

		@Override
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {

			if (value == null) {
				jgen.writeStartArray();
				jgen.writeEndArray();
			} else {
				jgen.writeObject(value);
			}
		}
	}

	public static class MyNullStringJsonSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Object> {

		@Override
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			if (value == null) {
				jgen.writeString("");
			} else {
				jgen.writeObject(value);
			}
		}
	}

	public static class MyNullObjectJsonSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Object> {

		@Override
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			if (value == null) {
				jgen.writeStartObject();
				jgen.writeEndObject();
			} else {
				jgen.writeObject(value);
			}
		}
	}

	public static class MyNullNumberJsonSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Object> {

		@Override
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			if (value == null) {
				jgen.writeNumber(0);
			} else {
				jgen.writeObject(value);
			}
		}
	}

	public static class MyNullBooleanJsonSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Object> {

		@Override
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			if (value == null) {
				jgen.writeBoolean(false);
			} else {
				jgen.writeObject(value);
			}
		}
	}

	public static class MyNullLocalDateJsonSerializer extends com.fasterxml.jackson.databind.JsonSerializer<LocalDate> {

		@Override
		public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			if (value == null) {
				jgen.writeString("0001-01-01");

			} else {
				jgen.writeObject(value);
			}
		}
	}

	public static class MyNullLocalDateTimeJsonSerializer
			extends com.fasterxml.jackson.databind.JsonSerializer<LocalDateTime> {

		@Override
		public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			if (value == null) {
				jgen.writeString("0001-01-01 00:00:00");

			} else {
				jgen.writeObject(value);
			}
		}
	}

	public static class MyNullLocalTImeJsonSerializer extends com.fasterxml.jackson.databind.JsonSerializer<LocalTime> {

		@Override
		public void serialize(LocalTime value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			if (value == null) {
				jgen.writeString("00:00:00");

			} else {
				jgen.writeObject(value);
			}
		}
	}

	public static class MyBeanSerializerModifier extends BeanSerializerModifier {

		public static com.fasterxml.jackson.databind.JsonSerializer<Object> nullArrayJsonSerializer = new Converter.MyNullArrayJsonSerializer();
		public static com.fasterxml.jackson.databind.JsonSerializer<Object> nullStringJsonSerializer = new Converter.MyNullStringJsonSerializer();
		public static com.fasterxml.jackson.databind.JsonSerializer<Object> nullObjectJsonSerializer = new Converter.MyNullObjectJsonSerializer();
		public static com.fasterxml.jackson.databind.JsonSerializer<Object> nullBooleanJsonSerializer = new Converter.MyNullBooleanJsonSerializer();
		public static com.fasterxml.jackson.databind.JsonSerializer<Object> nullNumberJsonSerializer = new Converter.MyNullNumberJsonSerializer();

		public static com.fasterxml.jackson.databind.JsonSerializer nullLocalDateJsonSerializer = new Converter.MyNullLocalDateJsonSerializer();
		public static com.fasterxml.jackson.databind.JsonSerializer nullLocalDateTimeJsonSerializer = new Converter.MyNullLocalDateTimeJsonSerializer();
		public static com.fasterxml.jackson.databind.JsonSerializer nullLocalTImeJsonSerializer = new Converter.MyNullLocalTImeJsonSerializer();


		@Override
		public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
				List<BeanPropertyWriter> beanProperties) {
			// 循环所有的beanPropertyWriter
			for (int i = 0; i < beanProperties.size(); i++) {
				BeanPropertyWriter writer = beanProperties.get(i);
				// 判断字段的类型，如果是array，list，set则注册nullSerializer
				if (isArrayType(writer)) {

					writer.assignNullSerializer(nullArrayJsonSerializer);
				} else if (isStringType(writer)) {
					writer.assignNullSerializer(nullStringJsonSerializer);
				} else if (isNumberType(writer)) {
					writer.assignNullSerializer(nullNumberJsonSerializer);
				} else if (isBooleanType(writer)) {
					writer.assignNullSerializer(nullBooleanJsonSerializer);
				} else if (writer.getType().getRawClass() == LocalDate.class) {
					writer.assignNullSerializer(nullLocalDateJsonSerializer);
				} else if (writer.getType().getRawClass() == LocalDateTime.class) {
					writer.assignNullSerializer(nullLocalDateTimeJsonSerializer);
				} else if (writer.getType().getRawClass() == LocalTime.class) {
					writer.assignNullSerializer(nullLocalTImeJsonSerializer);
				} else {
					writer.assignNullSerializer(nullObjectJsonSerializer);
				}
			}
			return beanProperties;
		}

		// 判断是什么类型
		protected boolean isArrayType(BeanPropertyWriter writer) {
			Class<?> clazz = writer.getType().getRawClass();
			return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class);

		}

		protected boolean isStringType(BeanPropertyWriter writer) {
			Class<?> clazz = writer.getType().getRawClass();
			return clazz == String.class || clazz == Character.class || clazz == char.class;

		}

		protected boolean isNumberType(BeanPropertyWriter writer) {
			Class<?> t = writer.getType().getRawClass();

			return (t == Integer.class || t == int.class || t == Long.class || t == long.class || t == Short.class
					|| t == short.class || t == Float.class || t == float.class || t == Double.class
					|| t == double.class || t == java.math.BigDecimal.class || t == java.math.BigInteger.class);
		}

		protected boolean isObjectType(BeanPropertyWriter writer) {
			Class<?> clazz = writer.getType().getRawClass();
			return Object.class.isAssignableFrom(clazz);

		}

		protected boolean isBooleanType(BeanPropertyWriter writer) {
			Class<?> clazz = writer.getType().getRawClass();
			return clazz == Boolean.class || clazz == boolean.class;

		}

	}

	public static class LocalDateSerializer extends com.fasterxml.jackson.databind.JsonSerializer<LocalDate> {

		public static LocalDateSerializer instance = new LocalDateSerializer();

		@Override
		public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException, JsonProcessingException {
			if (value != null) {
				gen.writeString(value.toString());
			}

		}

	}

	public static class LocalDateDeSerializer extends com.fasterxml.jackson.databind.JsonDeserializer<LocalDate> {
		public static LocalDateDeSerializer instance = new LocalDateDeSerializer();

		@Override
		public LocalDate deserialize(JsonParser p, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {

			LocalDate date;
			try {
				if (StringUtils.hasText(p.getText())) {
					date = LocalDate.parse(p.getText());
					return date;
				} else {
					return null;
				}
			} catch (Exception e) {

			}
			return null;

		}
	}

	public static class LocalDateTimeSerializer extends com.fasterxml.jackson.databind.JsonSerializer<LocalDateTime> {

		public static LocalDateTimeSerializer instance = new LocalDateTimeSerializer();

		@Override
		public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException, JsonProcessingException {
			if (value != null) {
				gen.writeString(value.format(CTime.DTF_YMD_HH_MM_SS));
			}

		}

	}

	public static class LocalDateTimeDeSerializer
			extends com.fasterxml.jackson.databind.JsonDeserializer<LocalDateTime> {
		public static LocalDateTimeDeSerializer instance = new LocalDateTimeDeSerializer();

		@Override
		public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {

			LocalDateTime date=null;
			try {
				if (StringUtils.hasText(p.getText())) {
					date = LocalDateTime.parse(p.getText(), CTime.DTF_YMD_HH_MM_SS);
					return date;
				}
			} catch (Exception e) {
			}
			return null;

		}

	}

	public static class LocalTimeSerializer extends com.fasterxml.jackson.databind.JsonSerializer<LocalTime> {

		public static LocalTimeSerializer instance = new LocalTimeSerializer();

		@Override
		public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException, JsonProcessingException {
			if (value != null) {
				gen.writeString(value.toString());
			} else {
				gen.writeString("00:00:00");
			}

		}

	}

	public static class LocalTimeDeSerializer extends com.fasterxml.jackson.databind.JsonDeserializer<LocalTime> {
		public static LocalTimeDeSerializer instance = new LocalTimeDeSerializer();

		@Override
		public LocalTime deserialize(JsonParser p, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {

			LocalTime date;
			try {
				if (StringUtils.hasText(p.getText())) {
					date = LocalTime.parse(p.getText());
					return date;
				} else {
					return null;
				}
			} catch (Exception e) {

			}
			return null;

		}

	}

	public static class FalstLocalDateFormatSerializer implements ObjectSerializer {

		public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
				throws IOException {
			if (object == null) {
				serializer.out.writeNull();
				return;
			}

			LocalDate date = (LocalDate) object;

			serializer.write(CTime.formatLocalDate(date));
		}
	}

	public static class FalstLocalDateTimeFormatSerializer implements ObjectSerializer {

		public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
				throws IOException {
			if (object == null) {
				serializer.out.writeNull();
				return;
			}

			LocalDateTime date = (LocalDateTime) object;
			serializer.write(CTime.formatDateTime(date,CTime.DTF_YMD_HH_MM_SS));
		}
	}

	public static class FalstLocalTimeFormatSerializer implements ObjectSerializer {

		public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
				throws IOException {
			if (object == null) {
				serializer.out.writeNull();
				return;
			}

			LocalTime date = (LocalTime) object;
			serializer.write(CTime.formatLocalTime(date,CTime.DTF_HH_MM_SS));
		}
	}

	public boolean deepEqual(Object src,Object des){
		return DeepEquals.deepEquals(src, des);
	}

}