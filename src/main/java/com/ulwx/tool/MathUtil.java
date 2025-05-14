package com.ulwx.tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.bouncycastle.util.Arrays;

public class MathUtil {
	private static final int DEF_DIV_SCALE = 0;

	public MathUtil() {

	}
	public static Math math(double d) {
		return new Math(d);
	}
	public static class Math{
		private double d=0;
		public Math(double d) {
			this.d=d;
		}
		public double doubleValue() {
			return d;
		}
		public Math add(double v1) {
			d=MathUtil.add(d, v1);
			return this;
		}
		
		public Math sub(double v1) {
			d=MathUtil.sub(d, v1);
			return this;
		}
		
		public Math mul(double v1) {
			d=MathUtil.mul(d, v1);
			return this;
		}
		
		public Math div(double v1, int scale) {
			d=MathUtil.div(d, v1,scale);
			return this;
		}
		
		public Math div(double v1) {
			d=MathUtil.div(d, v1);
			return this;
		}
		

	}
	

	
	public static double add(String v1, String v2) {

		BigDecimal b1 = new BigDecimal(v1);

		BigDecimal b2 = new BigDecimal(v2);

		return b1.add(b2).doubleValue();

	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */

	public static double add(double v1, double v2) {

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.add(b2).doubleValue();

	}
	public static boolean numberEquals(String d1,String d2) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.compareTo(b2)==0?true:false;
	}
	
	public static int numberCompare(String d1,String d2) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return  b1.compareTo(b2);
	}
	public static boolean doubleEquals(double d1,double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.compareTo(b2)==0?true:false;
	}
	public static int doubleCompare(double d1,double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return  b1.compareTo(b2);
	}
	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */

	public static double sub(double v1, double v2) {

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.subtract(b2).doubleValue();

	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */

	public static double mul(double v1, double v2) {

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.multiply(b2).doubleValue();

	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */

	public static double div(double v1, double v2) {

		return div(v1, v2, 10);

	}

	public static double div(long v1, long v2) {

		return div(v1, v2, 10);

	}

	public static long div2(long v1, long v2) {

		return div2(v1, v2, 10);

	}

	public static long div2(long v1, long v2, int scale) {

		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1);

		BigDecimal b2 = new BigDecimal(v2);

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).longValue();

	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */

	public static double div(double v1, double v2, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		}

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	public static double div(long v1, long v2, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		}

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */

	public static double round(double v, int scale) {

		if (scale < 0)

		{

			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		}

		BigDecimal b = new BigDecimal(Double.toString(v));

		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}
	public static double round(double v, int scale, int roundingMode) {

		if (scale < 0)

		{

			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		}

		BigDecimal b = new BigDecimal(Double.toString(v));
		//BigDecimal.ROUND_HALF_UP
		return b.setScale(scale, roundingMode).doubleValue();

	}
	public static double max(double[] numbers) {
		Max max = new Max();
		return max.evaluate(numbers, 0, numbers.length);

	}

	public static long max(long[] values) {
		Max max = new Max();
		double[] ds = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			ds[i] = (double)values[i];
		}
		return (long)max(ds);

	}

	public static long min(long[] values) {
		Min min = new Min();
		double[] ds = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			ds[i] = (double)values[i];
		}
		return (long)min(ds);

	}

	public static double min(double[] numbers) {
		Min min = new Min();
		return min.evaluate(numbers, 0, numbers.length);

	}

	/**
	 * 固定7位小数，固定小数位后截取
	 * @param db
	 * @return
	 */
	public static String doubleToString(double db) {
		return numberToStringFixPoint(db,2);
       
	}

	public static String doubleToString(double db,String format) {
		DecimalFormat decimalFormat = new DecimalFormat(format);//格式化设置  
        return decimalFormat.format(db);
       
	}
	/**
	 * 固定小数位，固定小数位后截取
	 * @param db
	 * @param fixPointNum
	 * @return
	 */
	public static String doubleToString(double db,int fixPointNum) {
		return numberToStringFixPoint(db,fixPointNum);
       
	}


	/**
	 * 用非科学计数法
	 * @param llR
	 * @return
	 */

	public static String longToString(long llR) {
		NumberFormat nf=NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		return nf.format(llR);
       
	}

	/**
	 * 默认最大7位小数，四舍五入
	 * @param value
	 * @return
	 */
	public static String numberToString(Object value) {
		NumberFormat nf=NumberFormat.getInstance();  
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(2);
		return nf.format(value);
	}
	/**
	 * 四舍五入
	 * @param value
	 * @param maxPointNum
	 * @return
	 */
	public static String numberToString(Object value, int maxPointNum) {
		NumberFormat nf=NumberFormat.getInstance();  
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(maxPointNum);
		return nf.format(value);
	}
	
	/**
	 * 固定小数位，固定小数位后截取
	 * @param value
	 * @param fixPointNum
	 * @return
	 */
	public static String numberToStringFixPoint(Object value, int fixPointNum) {
		char[] chs=new char[fixPointNum];
		Arrays.fill(chs, '0');
		String s=new String(chs);
		String format="0."+s;
		DecimalFormat decimalFormat = new DecimalFormat(format);//格式化设置  
		decimalFormat.setRoundingMode(RoundingMode.DOWN);
        return decimalFormat.format(value);
	}
	public static String numberToString(Object value, int maxPointNum,int minPointNum, boolean group,RoundingMode rmode) {
		NumberFormat nf=NumberFormat.getInstance();  
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(maxPointNum);
		nf.setMinimumFractionDigits(minPointNum);
		nf.setGroupingUsed(group);
		nf.setRoundingMode(rmode);
		return nf.format(value);
	}
	/**
	 * 四舍五入
	 * @param value
	 * @param group
	 * @param maxPointNum
	 * @return
	 */
	public static String numberToString(Object value, boolean group,int maxPointNum) {
		NumberFormat nf=NumberFormat.getInstance();  
		nf.setGroupingUsed(group);
		nf.setMaximumFractionDigits(maxPointNum);

		return nf.format(value);
	}
	/**
	 * 非四舍五入，向下取整
	 * @param val  数值
	 * @param pointNum  小数点位数
	 * @return
	 */
	public static Double formatDoubleNotFourFive(double val,int pointNum){
		BigDecimal b = new BigDecimal(Double.toString(val));
		double f1 = b.setScale(pointNum, BigDecimal.ROUND_DOWN).doubleValue();
		return f1;
	}
	/**
	 * 四舍五入
	 * @param val
	 * @param pointNum 小数的位数
	 * @return
	 */
	public static Double formatDouble(Double val,int pointNum){
		BigDecimal b = new BigDecimal(Double.toString(val));
		double f1 = b.setScale(pointNum, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}
	
	public static Double formatDouble(Double val,int pointNum,int roundingMode){
		BigDecimal b = new BigDecimal(Double.toString(val));
		double f1 = b.setScale(pointNum, roundingMode).doubleValue();
		return f1;
	}
	
	/**
	 * 四舍五入，取两位
	 * @param val
	 * @return
	 */
	public static Double formatDoubleForDouble(Double val){
    	DecimalFormat df = new DecimalFormat("0.00");
    	return Double.parseDouble(df.format(val));
	}
	public static void main(String[] args) {
//		System.out.println(max(new double[] { 1.2, 34, 66, 0.1 }));
//		System.out.println(min(new long[] { 2, 34, 66, 134455666 }));
//		System.out.println(max(new long[] { 2, 34, 66, 134455666 }));
//
//		System.out.println(round(1.632, 2));
//		BigDecimal b = new BigDecimal(1.632);
//		double d= b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
//		System.out.println(d);
//		
//		double db=1234556778888888d;
//		BigDecimal b1 = new BigDecimal(Double.toString(db));
//		System.out.println("sssss="+doubleToString(5008910d));
//		
		double ddd=17888999.33333;
//		System.out.println(""+ddd);
//		BigDecimal bbbb = new BigDecimal(Double.toString(ddd));
//		double dr= bbbb.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
//		
		System.out.println(MathUtil.math(10d).div(3d,16).add(3d).doubleValue());
//		
//		System.out.println(formatDouble(12.3414,2));
//		System.out.println(formatDoubleForDouble(12.3414));
//		
//		NumberFormat nf=NumberFormat.getInstance();  
//		nf.setGroupingUsed(true);
//		nf.setMaximumFractionDigits(2);
//		System.out.println(nf.format(12345678));
//		System.out.println(formatDoubleNotFourFive(5.6999,2));
	//	Double d=123455555555d;
		System.out.println(MathUtil.numberToString(18565574709.12344d,9)+"");
		
//		System.out.println(MathUtil.numberEquals("0.0", "0"));
//		
//		System.out.println(MathUtil.doubleEquals(0, 0.0));
//	    double d1=1.100d;
//	    double d2=1.1000d;
//		if(d1==d2) {
//			System.out.println("s"+true);
//		}
		
	}

}
