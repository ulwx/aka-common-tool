package com.ulwx.tool;

import com.ulwx.type.TResult2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;

public class PropertyUtil {
	private static Logger log = LoggerFactory.getLogger(PropertyUtil.class);

	public static <T1, T2> T1 copyProperties(T1 toBean, T2 fromBean) throws Exception {
		Map<String, Object> map = PropertyUtil.describe(fromBean);
		Set<String> keys = map.keySet();
		for (String key : keys) {
			Object val = map.get(key);
			try {
				PropertyUtil.setProperty(toBean, key, val);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}

		return toBean;

	}

	public static void setProperty(Object bean, String name, Object value) {

		Class cls = bean.getClass();
		try {
			Method[] methods = cls.getMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("set")) {
					if (method.getParameterTypes().length == 1) {
						if (method.getName().compareToIgnoreCase("set" + name) == 0) {
							if(value!=null){
								if(!method.getParameterTypes()[0].isAssignableFrom(value.getClass())){
									if(NumberUtils.isNumber(value)){
										value=NumberUtils.convertNumberToTargetClass((Number)value,method.getParameterTypes()[0]);
									}
								}
							}
							method.invoke(bean, value);
						}
					}
				}

			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

	}

	public static Object getProperty(Object bean, String name) {
		Class cls = bean.getClass();

		try {
			Method[] methods = cls.getMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("get")) {
					if (method.getParameterTypes().length == 0) {
						if (method.getName().compareToIgnoreCase("get" + name) == 0) {
							Object val = method.invoke(bean);
							return val;
						}
					}
				} else if (method.getName().startsWith("is")) {
					if (method.getParameterTypes().length == 0 && method.getReturnType() == boolean.class) {
						if (method.getName().compareToIgnoreCase("is" + name) == 0) {
							Object val = method.invoke(bean);
							return val;

						}
					}
				}

			}

		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return null;

	}
	public static Map<String, Method> getSetterMethods(Class<?> clazz) {
		Map<String, Method> methodMap = new HashMap<>();
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
				Method writeMethod = pd.getWriteMethod();
				if (writeMethod != null) {
					if(pd.getName().equals("class")) continue;
					methodMap.put(pd.getName(), writeMethod);
				}
			}
		} catch (Exception ignore) {
		}
		return methodMap;
	}


	public static Object merge(Object src, Object des) throws Exception {

		Object finalObj = des.getClass().getConstructor().newInstance();
		Map<String, Method> setmap = getSetterMethods(des.getClass());
		Map<String, Method> getmap = getGetterMethods(des.getClass());
		for (String name : getmap.keySet()) {
			Method getmethod = getmap.get(name);
			if(src!=null) {
				Object srcValue = getmethod.invoke(src);
				if (srcValue != null) {
					if(setmap.get(name)!=null) {
						log.debug(name+":"+srcValue);
						setmap.get(name).invoke(finalObj, srcValue);
					}
				}
			}
			Object desValue=getmethod.invoke(des);
			if(desValue!=null){
				if(setmap.get(name)!=null) {
					setmap.get(name).invoke(finalObj, desValue);
				}
			}

		}

		return finalObj;
	}
	public static Map<String, Method> getGetterMethods(Class<?> clazz) {
		Map<String, Method> methodMap = new HashMap<>();
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
				Method readMethod = pd.getReadMethod();
				if (readMethod != null) {
					if(pd.getName().equals("class")) continue;
					methodMap.put(pd.getName(), readMethod);
				}
			}
		} catch (Exception ignore) {
		}
		return methodMap;
	}
	public static Object convertValue(Method method, Object value) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes.length == 1) {
			Class<?> parameterType = parameterTypes[0];
			String propertyValue = String.valueOf(value);
			if (parameterType == String.class) {
				return propertyValue;
			}
			if (parameterType == Integer.class || parameterType == int.class) {
				return Integer.valueOf(propertyValue).intValue();
			}
			if (parameterType == Long.class || parameterType == long.class) {
				return Long.valueOf(propertyValue).longValue();
			}
			if (parameterType == Boolean.class || parameterType == boolean.class) {
				return Boolean.valueOf(propertyValue).booleanValue();
			}
			if (parameterType == Double.class || parameterType == double.class) {
				return Double.valueOf(propertyValue).doubleValue();
			}
			if (parameterType == Float.class || parameterType == float.class) {
				return Float.valueOf(propertyValue).floatValue();
			}
		}
		return value;
	}
	public static Class getPropertyType(Object bean, String name) {
		Class cls = bean.getClass();

		try {
			Method[] methods = cls.getMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("get")) {
					if (method.getParameterTypes().length == 0) {
						if (method.getName().compareToIgnoreCase("get" + name) == 0) {
							return method.getReturnType();
						}
					}
				} else if (method.getName().startsWith("is")) {
					if (method.getParameterTypes().length == 0 && method.getReturnType() == boolean.class) {
						if (method.getName().compareToIgnoreCase("is" + name) == 0) {
							return method.getReturnType();

						}
					}
				}

			}

		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return null;

	}

	public static void setSimpleProperty(Object bean, String name, Object value) {
		setProperty(bean, name, value);
	}

	/**
	 * 对bean进行反射，变成一个Map，通过get方法识别属性
	 * @param bean  反射的对象
	 * @param t    实际需要反射的类，bean存在继承的情况，t可以指定继承层级里某个层级类，提供这个层级类的反射
	 * @return 属性对应的类型和值的map
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, TResult2<Method, Object>> describeForTypes(Object bean, Class t) throws Exception {
		Map<String, TResult2<Method, Object>> map = new TreeMap<>();
		if(bean==null) return map;

		Method[] methods = t.getMethods();
		for (Method method : methods) {
			try {
				method.setAccessible(true);
				String key = "";
				Class returnType = method.getReturnType();
				if (method.getName().startsWith("get")) {
					key = StringUtils.trimLeadingString(method.getName(), "get");
					key = StringUtils.firstCharLowCase(key);
					if (key.equals("class")) {
						continue;
					}
				} else if (returnType == boolean.class && method.getName().startsWith("is")) {
					key = StringUtils.trimLeadingString(method.getName(), "is");
					key = StringUtils.firstCharLowCase(key);
				} else {
					continue;
				}
				Method beanMethod= bean.getClass().getMethod(method.getName());
				Object val = beanMethod.invoke(bean);
				TResult2<Method, Object> tr = new TResult2<Method, Object>(beanMethod, val);
				map.put(key, tr);
			} catch (Exception e) {
				throw e;
			}
		}
		return map;
	}

	/**
	 * javabean转换成一个map对象
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> describe(Object obj) throws Exception {
		Map<String, Object> map = new TreeMap<>();
		Class cls = obj.getClass();
		Method[] methods = cls.getMethods();
		for (Method method : methods) {
			try {
				String key = "";
				method.setAccessible(true);
				Class returnType = method.getReturnType();
				if (method.getName().startsWith("get")) {
					key = StringUtils.trimLeadingString(method.getName(), "get");
					key = StringUtils.firstCharLowCase(key);
					if (key.equals("class")) {
						continue;
					}
				} else if (returnType == boolean.class && method.getName().startsWith("is")) {
					key = StringUtils.trimLeadingString(method.getName(), "is");
					key = StringUtils.firstCharLowCase(key);
				} else {
					continue;
				}
				Object val = method.invoke(obj);
				map.put(key, val);
			} catch (Exception e) {
				throw e;
			}
		}
		return map;
	}

	public static Map<String, String> loadAsMap(InputStream input, String charsetName) {
		Properties ps = new Properties();
		if (input == null) {
			return null;
		}
		InputStreamReader inr = null;
		try {
			inr = new InputStreamReader(input, charsetName);
			ps.load(inr);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (inr != null) {
				try {
					inr.close();
				} catch (Exception e) {

				}
			}
		}
		Map<String, String> value = (Map) ps;// PropertyUtils.loadAsMap(file);
		// System.out.println(ObjectUtils.toString(value));;
		return value;
	}


	public static void main(String[] args) throws Exception {
		System.out.println(ObjectUtils.toString(loadAsMap(new FileInputStream("e:/xxx.property"))));
		// PropertyUtils.l
	}

	public static Map<String, String> loadAsMap(InputStream input) {
		return loadAsMap(input, "utf-8");
	}


}
