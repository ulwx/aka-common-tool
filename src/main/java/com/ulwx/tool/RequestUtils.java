package com.ulwx.tool;

import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils {
	public static final String REQUEST_BODY_STR="REQUEST_BODY_STR";
	private Map<String, Object[]> rParms = new HashMap<String, Object[]>();
	private Map<String, Object[]> objs = new HashMap<String, Object[]>();

	private static Logger log = Logger.getLogger(RequestUtils.class);

	public RequestUtils(Map<String, Object[]> requestParameters) {
		rParms = requestParameters;
	}

	public RequestUtils() {
		rParms = new HashMap<String, Object[]>();
	}

	public void setString(String name, String value) {
		rParms.put(name, new String[] { value });
	}

	public void setValue(String name, Object value) {
		if(value!=null) {
		  Object array=Array.newInstance(value.getClass(), 1);
		  Array.set(array, 0, value);
		  Object[] obs=(Object[]) array;
		  rParms.put(name, obs);
		}
		
	}
	public void setValues(String name, Object[] values) {
		 rParms.put(name, values);
		
	}
	public void setStrings(String name, String[] values) {
		rParms.put(name, values);
	}

	public Map<String, Object[]> getrParms() {

		return rParms;
	}

	public void setrParms(Map<String, Object[]> rParms) {
		this.rParms = rParms;
	}

	public void setInt(String name, Integer value) {
		rParms.put(name, new Integer[] { value });
	}

	public void setFloat(String name, Float value) {
		rParms.put(name, new Float[] { value });
	}

	public void setLong(String name, Long value) {
		rParms.put(name, new Long[] { value });
	}

	public void setDouble(String name, Double value) {
		rParms.put(name, new Double[] { value });
	}

	public void setLocalTime(String name, LocalTime value) {
		rParms.put(name, new LocalTime[] { value });
	}

	public void setLocalDateTime(String name, LocalDateTime value) {
		rParms.put(name, new LocalDateTime[] { value });
	}

	public void setLocalDate(String name, LocalDate value) {
		rParms.put(name, new LocalDate[] { value });
	}

	public void setLocalTimes(String name, LocalTime[] value) {
		rParms.put(name, value);
	}

	public void setLocalDateTimes(String name, LocalDateTime[] value) {
		rParms.put(name, value);
	}

	public void setLocalDates(String name, LocalDate[] value) {
		rParms.put(name, value);
	}

	public void setObject(String name, Object value) {
		objs.put(name, new Object[] { value });
	}

	public void setObjects(String name, Object[] values) {
		objs.put(name, values);
	}

	public Object getObject(String name) {
		try {
			Object value = ArrayUtils.getFirst(objs.get(name));
			if (value == null)
				return null;
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	public Object[] getObjects(String name) {
		try {
			Object[] value = objs.get(name);
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	public String getString(String name) {
		Object value = ArrayUtils.getFirst(rParms.get(name));
		if (value == null)
			return "";
		try {
			if (value instanceof String) {
				return (String) value;
			} else {
				return value.toString();
			}
		} catch (Exception e) {
			return null;
		}

	}
	public Object getValue(String name) {
		Object value = ArrayUtils.getFirst(rParms.get(name));
		return value;
		
	}
	public Object[] getValues(String name) {
		return rParms.get(name);
		
	}
	public String getTrimString(String name){
		return StringUtils.trim(this.getString(name));
	}

	public File getFile(String name) {
		try {
			File value = ArrayUtils.getFirst((File[]) rParms.get(name));
			return value;
		} catch (Exception e) {
			return null;
		}

	}
	public void setBody(String bodyStr){

	}
	public <T> T getJson(String name,Class<T> c) throws Exception{
		String str=this.getString(name);
		return (T)ObjectUtils.fromJsonToObject(str, c);
	}

	public <T> T getBody(Class<T> c){
		try {
			return this.getJson(REQUEST_BODY_STR, c);
		}catch (Exception e){
			log.error(""+e,e);
			return null;
		}
	}
	/**
	 * 
	 * @param name
	 * @param t
	 * 	      <xmp> Type typeOfT = new
	 * 		 com.google.gson.reflect.TypeToken<Collection<Foo>>(){}.getType();</xmp>
	 * @return
	 * @throws Exception
	 */
	public <T> T getJson(String name,Type t) throws Exception{
		String str=this.getString(name);
		return (T)ObjectUtils.fromJsonToObject(str, t);
	}
	/**
	 *
	 * @param t
	 * 	      <xmp> Type typeOfT = new
	 * 		 com.google.gson.reflect.TypeToken<Collection<Foo>>(){}.getType();</xmp>
	 * @return
	 * @throws Exception
	 */
	public <T> T getBody(Type t)throws Exception{
		try {
			return  this.getJson(REQUEST_BODY_STR, t);
		}catch (Exception e){
			log.error(""+e,e);
			return null;
		}

	}
	public String getFileContentType(String name) {
		return this.getString(name + "ContentType");
	}

	public String[] getFileContentTypes(String name) {
		return this.getStrings(name + "ContentType");
	}

	public String getFileName(String name) {
		return this.getString(name + "FileName");
	}


	public String[] getFileNames(String name) {
		return this.getStrings(name + "FileName");
	}

	public File[] getFiles(String name) {
		try {
			return (File[]) rParms.get(name);
		} catch (Exception e) {
			return null;
		}

	}

	public <T> T getJavaBean(T javabean) {
		return ObjectUtils.fromMapToJavaBean(javabean, this.getrParms());

	}

	public Integer getInt(String name) {
		Object value = ArrayUtils.getFirst(rParms.get(name));
		if (value == null)
			return null;
		try {
			if (value instanceof Integer) {
				return (Integer) value;
			} else {
				return Integer.parseInt(value.toString().trim());
			}
		} catch (Exception e) {
			// log.error("", e);
			return null;
		}
	}

	public Float getFloat(String name) {
		Object value = ArrayUtils.getFirst(rParms.get(name));
		if (value == null)
			return null;
		try {
			if (value instanceof Float) {
				return (Float) value;
			} else {
				return Float.parseFloat(value.toString().trim());
			}
		} catch (Exception e) {
			// log.error("", e);
			return null;
		}
	}

	public Double getDouble(String name) {
		Object value = ArrayUtils.getFirst(rParms.get(name));
		if (value == null)
			return null;
		try {

			if (value instanceof Double) {
				return (Double) value;
			} else {
				return Double.parseDouble(value.toString().trim());
			}
		} catch (Exception e) {
			// log.error("", e);
			return null;
		}
	}

	public Long getLong(String name) {
		Object value = ArrayUtils.getFirst(rParms.get(name));
		if (value == null)
			return null;
		try {
			if (value instanceof Long) {
				return (Long) value;
			} else {
				return Long.parseLong(value.toString().trim());
			}
		} catch (Exception e) {
			// log.error("", e);
			return null;
		}
	}

	public Byte getByte(String name) {
		Object value = ArrayUtils.getFirst(rParms.get(name));
		if (value == null)
			return null;
		try {
			if (value instanceof Byte) {
				return (Byte) value;
			} else {
				return Byte.parseByte(value.toString().trim());
			}
		} catch (Exception e) {
			// log.error("", e);
			// TODO Auto-generated catch block
			return null;
		}
	}

	public Boolean getBoolean(String name) {
		Object value = ArrayUtils.getFirst(rParms.get(name));
		if (value == null)
			return null;
		try {
			if (value instanceof Boolean) {
				return (Boolean) value;
			} else {
				return Boolean.parseBoolean(value.toString().trim());
			}
		} catch (Exception e) {
			// log.error("", e);
			return null;
		}

	}

	public String[] getStrings(String name) {
		try {
			Object[] value = rParms.get(name);
			if (value == null) {
				return new String[0];
			}
			if (value.getClass().getComponentType() == String.class) {
				return (String[]) value;
			} else {
				return ArrayUtils.toStringArray(value);
			}

		} catch (Exception e) {
			// log.error("", e);
			return null;
		}
	}

	public void setFloats(String name, Float[] values) {
		rParms.put(name, values);
	}

	public void setDoubles(String name, Double[] values) {
		rParms.put(name, values);
	}

	public void setLongs(String name, Long[] values) {
		rParms.put(name, values);
	}

	public void setInts(String name, Integer[] values) {
		rParms.put(name, values);
	}

	public void setBoolean(String name, Boolean value) {
		rParms.put(name, new Boolean[] { value });
	}
	public void setFileName(String name,String fileName){
		rParms.put(name+"FileName",new String[]{fileName});
	}
	public void setFileName(String name,String[] fileNames){
		rParms.put(name+"FileName",fileNames);
	}
	public void setFileContentType(String name,String contentType){
		rParms.put(name+"ContentType",new String[]{contentType});
	}
	public void setFileContentTypes(String name,String[] contentTypes){
		rParms.put(name+"ContentType",contentTypes);
	}
	public void setBooleans(String name, Boolean[] values) {
		rParms.put(name, values);
	}

	public void setBytes(String name, Byte[] values) {
		rParms.put(name, values);
	}

	public void setByte(String name, Byte value) {
		rParms.put(name, new Byte[] { value });
	}

	public Date getDayDate(String name) {
		Object value = ArrayUtils.getFirst(rParms.get(name));
		if (value == null)
			return null;
		try {
			if (value instanceof Date) {
				return (Date) value;
			} else {
				return CTime.parseDayDate(value.toString().trim());
			}
		} catch (Exception e) {
			return null;
		}
	}

	public LocalDate getLocalDate(String name) {
		Object value = ArrayUtils.getFirst(rParms.get(name));

		if (value == null)
			return null;
		try {
			if (value instanceof LocalDate) {
				return (LocalDate) value;
			} else {
				return LocalDate.parse(value.toString().trim());
			}
		} catch (Exception e) {
			return null;
		}

	}

	public LocalTime getLocalTime(String name) {

		Object value = ArrayUtils.getFirst(rParms.get(name));

		if (value == null)
			return null;
		try {
			if (value instanceof LocalTime) {
				return (LocalTime) value;
			} else {
				return LocalTime.parse(value.toString().trim());
			}
		} catch (Exception e) {
			return null;
		}

	}

	public LocalDateTime getLocalDateTime(String name) {

		Object value = ArrayUtils.getFirst(rParms.get(name));

		if (value == null)
			return null;
		try {
			if (value instanceof LocalDateTime) {
				return (LocalDateTime) value;
			} else {
				return LocalDateTime.parse(value.toString().trim(), CTime.DTF_YMD_HH_MM_SS);
			}
		} catch (Exception e) {
			return null;
		}

	}

	public Date[] getDayDates(String name) {
		Object[] value = rParms.get(name);
		if (value == null)
			return null;

		if (value.getClass().getComponentType() == Date.class) {
			return (Date[]) value;
		} else {
			Date[] res = new Date[value.length];
			for (int i = 0; i < value.length; i++) {
				try {
					res[i] = CTime.parseDayDate(value[i].toString().trim());
				} catch (Exception e) {
					res[i] = null;
				}
			}
			return res;
		}

	}

	public LocalDate[] getLocalDates(String name) {
		Object[] value = rParms.get(name);
		if (value == null)
			return null;

		if (value.getClass().getComponentType() == LocalDate.class) {
			return (LocalDate[]) value;
		} else {
			LocalDate[] res = new LocalDate[value.length];
			for (int i = 0; i < value.length; i++) {
				try {
					res[i] = LocalDate.parse(value[i].toString().trim());
				} catch (Exception e) {
					res[i] = null;
				}
			}
			return res;
		}

	}

	public LocalTime[] getLocalTimes(String name) {
		Object[] value = rParms.get(name);
		if (value == null)
			return null;
		if (value.getClass().getComponentType() == LocalTime.class) {
			return (LocalTime[]) value;
		} else {
			LocalTime[] res = new LocalTime[value.length];
			for (int i = 0; i < value.length; i++) {
				try {
					res[i] = LocalTime.parse(value[i].toString().trim());
				} catch (Exception e) {
					res[i] = null;
				}
			}

			return res;
		}
	}

	public LocalDateTime[] getLocalDateTimes(String name) {
		Object[] value = rParms.get(name);
		if (value == null)
			return null;
		if (value.getClass().getComponentType() == LocalDateTime.class) {
			return (LocalDateTime[]) value;
		} else {
			LocalDateTime[] res = new LocalDateTime[value.length];
			for (int i = 0; i < value.length; i++) {
				try {
					res[i] = LocalDateTime.parse(value[i].toString(), CTime.DTF_YMD_HH_MM_SS);
				} catch (Exception e) {

					res[i] = null;
				}
			}

			return res;
		}
	}

	public void setWholeDate(String name, Date value) {
		rParms.put(name, new Date[] { value });
	}

	public void setWholeDates(String name, Date[] values) {
		rParms.put(name, values);
	}

	public Date getWholeDate(String name) {
		Object value = ArrayUtils.getFirst(rParms.get(name));
		if (value == null)
			return null;
		try {
			if (value instanceof Date) {
				return (Date) value;
			} else {
				return CTime.parseWholeDate(value.toString());
			}
		} catch (Exception e) {

			return null;
		}
	}

	public Date[] getWholeDates(String name) {
		Object[] value = rParms.get(name);
		if (value == null)
			return null;
		if (value.getClass().getComponentType() == Date.class) {
			return (Date[]) value;
		} else {
			Date[] res = new Date[value.length];
			for (int i = 0; i < value.length; i++) {
				try {
					res[i] = CTime.parseWholeDate(value[i].toString());
				} catch (Exception e) {
			
					res[i] = null;
				}
			}

			return res;
		}
	}

	public Integer[] getInts(String name) {
		Object[] value = rParms.get(name);
		if (value == null)
			return null;
		if (value.getClass().getComponentType() == Integer.class) {
			return (Integer[]) value;
		} else {
			Integer[] res = new Integer[value.length];
			for (int i = 0; i < value.length; i++) {
				try {
					res[i] = Integer.parseInt(value[i].toString().trim());
				} catch (Exception e) {
					// TODO Auto-generated catch block

					res[i] = null;
				}
			}

			return res;
		}
	}

	public Long[] getLongs(String name) {
		Object[] value = rParms.get(name);
		if (value == null)
			return null;
		if (value.getClass().getComponentType() == Long.class) {
			return (Long[]) value;
		} else {
			Long[] res = new Long[value.length];
			for (int i = 0; i < value.length; i++) {
				try {
					res[i] = Long.parseLong(value[i].toString().trim());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// log.error("", e);
					res[i] = null;
				}
			}

			return res;
		}
	}

	public Boolean[] getBooleans(String name) {
		Object[] value = rParms.get(name);
		if (value == null)
			return null;
		if (value.getClass().getComponentType() == Boolean.class) {
			return (Boolean[]) value;
		} else {
			Boolean[] res = new Boolean[value.length];
			for (int i = 0; i < value.length; i++) {
				try {
					res[i] = Boolean.valueOf(value[i].toString().trim());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					res[i] = null;
				}
			}

			return res;
		}
	}

	public Byte[] getBytes(String name) {
		Object[] value = rParms.get(name);
		if (value == null)
			return null;
		if (value.getClass().getComponentType() == Byte.class) {
			return (Byte[]) value;
		} else {
			Byte[] res = new Byte[value.length];
			for (int i = 0; i < value.length; i++) {
				try {
					res[i] = Byte.parseByte(value[i].toString().trim());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					res[i] = null;
				}
			}

			return res;
		}
	}

	public Double[] getDoubles(String name) {
		Object[] value = rParms.get(name);
		if (value == null)
			return null;
		if (value.getClass().getComponentType() == Double.class) {
			return (Double[]) value;
		} else {
			Double[] res = new Double[value.length];
			for (int i = 0; i < value.length; i++) {
				try {
					res[i] = Double.parseDouble(value[i].toString().trim());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					res[i] = null;
				}
			}

			return res;
		}
	}

	public Float[] getFloats(String name) {
		Object[] value = rParms.get(name);
		if (value == null)
			return null;
		if (value.getClass().getComponentType() == Float.class) {
			return (Float[]) value;
		} else {
			Float[] res = new Float[value.length];
			for (int i = 0; i < value.length; i++) {
				try {
					res[i] = Float.parseFloat(value[i].toString().trim());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					res[i] = null;
				}
			}

			return res;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		RequestUtils ru = new RequestUtils();
//		ru.setBooleans("sss", new Boolean[] { true, false });
//		ru.setInts("aa", new Integer[]{123,444});
//		System.out.println(ArrayUtils.toJsonString(ru.getInts("aa")));
//		Map<String,String[]> map=new HashMap<String,String[]>();
//		map.put("aa", new String[]{123+"",444+""});
//		map.put("bb", new String[]{"sss","bb"});
//		ru.setrParms((Map)map);
//		System.out.println(ArrayUtils.toJsonString(ru.getString("aa")));
		
		  Object array=Array.newInstance(Integer.class, 1);
		  Array.set(array, 0, 1);
		  Object[] obs=(Object[]) array;
		 int i=(Integer)obs[0];
		 int tt=22;

	}

}
