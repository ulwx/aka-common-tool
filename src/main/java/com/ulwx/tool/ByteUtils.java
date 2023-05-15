package com.ulwx.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class ByteUtils { 
	private static final Logger log = LoggerFactory.getLogger(ByteUtils.class);
	public final static short UNSIGNED_MAX_VALUE = (Byte.MAX_VALUE * 2) + 1;

	public static byte[] convertInt2Bytes(int value) {

		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte)(value >> 8 * ((b.length - 1) - i) & 0x00FF);
		}
		return b;
	}

	/**
	 * 把int数组转换成字节数组
	 * 
	 * @param values
	 * @return
	 */
	public static byte[] convertIntArray2Bytes(int[] values) {
		ByteBuffer bb = ByteBuffer.allocate(4 * values.length);
		bb.asIntBuffer().put(values);
		return bb.array();

	}

	public static byte[] convertDouble2Bytes(double value) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.asDoubleBuffer().put(value);
		return bb.array();

	}

	public static byte[] convertDoubleArray2Bytes(double[] values) {
		ByteBuffer bb = ByteBuffer.allocate(8 * values.length);
		bb.asDoubleBuffer().put(values);
		return bb.array();

	}

	public static byte[] convertLong2Bytes(long value) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.asLongBuffer().put(value);
		return bb.array();

	}

	public static byte[] convertLongArray2Bytes(long[] value) {
		ByteBuffer bb = ByteBuffer.allocate(8 * value.length);
		bb.asLongBuffer().put(value);
		return bb.array();

	}

	public static byte[] convertFloat2Bytes(float value) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.asFloatBuffer().put(value);
		return bb.array();

	}

	public static byte[] convertFloatArray2Bytes(float[] value) {
		ByteBuffer bb = ByteBuffer.allocate(4 * value.length);
		bb.asFloatBuffer().put(value);
		return bb.array();

	}

	public static byte[] convertChar2Bytes(char value) {

		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.asCharBuffer().put(value);
		return bb.array();
	}

	public static byte[] convertCharArray2Bytes(char[] value) {

		ByteBuffer bb = ByteBuffer.allocate(2 * value.length);
		bb.asCharBuffer().put(value);
		return bb.array();
	}

	public static long convertBytes2Long(byte[] bs) {

		Assert.state(bs.length == 8, "long must be 8 bytes!");
		ByteBuffer bb = ByteBuffer.wrap(bs);
		long value = bb.getLong();
		return value;

	}

	public static long[] convertBytes2LongArray(byte[] bs) {
		ByteBuffer bb = ByteBuffer.wrap(bs);
		long[] value = bb.asLongBuffer().array();
		return value;

	}

	public static float convertBytes2Float(byte[] bs) {

		Assert.state(bs.length == 4, "float must be 4 bytes!");

		ByteBuffer bb = ByteBuffer.wrap(bs);
		float value = bb.getFloat();
		return value;

	}

	public static float[] convertBytes2FloatArray(byte[] bs) {
		ByteBuffer bb = ByteBuffer.wrap(bs);
		float[] value = bb.asFloatBuffer().array();
		return value;

		// FileChannel

	}

	public static char convertFromUTF16beBytes2Char(byte[] bs) {

		Assert.state(bs.length == 2, "char must be 2 bytes!");

		ByteBuffer bb = ByteBuffer.wrap(bs);
		char value = bb.getChar();
		return value;

	}

	public static char[] convertFromUTF16beBytes2CharArray(byte[] bs) {
		ByteBuffer bb = ByteBuffer.wrap(bs);
		char[] value = bb.asCharBuffer().array();
		return value;

	}

	public static double convertBytes2Double(byte[] bs) {

		Assert.state(bs.length == 8, "double must be 8 bytes!");
		ByteBuffer bb = ByteBuffer.wrap(bs);
		double value = bb.getDouble();
		return value;

	}

	public static double[] convertBytes2DoubleArray(byte[] bs) {
		ByteBuffer bb = ByteBuffer.wrap(bs);
		double[] value = bb.asDoubleBuffer().array();
		return value;

	}

	/**
	 * 字节数组转化成int
	 * 
	 * @param b
	 * @return
	 */
	public static int convertBytes2Int(byte[] b) {
		int intValue = 0;

		Assert.state(b.length == 4, "int must have 4 bytes!");

		for (int i = 0; i < b.length; i++) {

			intValue = intValue | (b[i] & 0x000000FF);
			if (i < b.length - 1) {
				intValue <<= 8;
			}
			// intValue=(intValue|b[i])<<8;
		}
		return intValue;
	}

	public static int[] convertBytes2IntArray(byte[] bs) {
		ByteBuffer bb = ByteBuffer.wrap(bs);
		int[] value = bb.asIntBuffer().array();
		return value;

	}

	public static String toDecimalAscii(byte b) {
		return b + "";
	}

	public static String toDecimalAscii(byte[] bs, String c) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bs.length; i++) {
			sb.append(toDecimalAscii(bs[i]));
			if (i < bs.length - 1)
				sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 八进制
	 * 
	 * @param b
	 * @return
	 */
	public static String toOctalAscii(byte b) {
		String s = Integer.toOctalString(b);
		// System.out.println(s);
		return StringUtils.paddingToFixedString(s, '0', 3, true);

	}

	public static String toOctalAscii(byte[] bs, String c) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bs.length; i++) {
			sb.append(toOctalAscii(bs[i]));
			if (i < bs.length - 1)
				sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 按每16个字节一行打出字节数组的16进制格式
	 * 
	 * @param data
	 * @return
	 */
	public static String format(byte[] data) {
		StringBuilder result = new StringBuilder();
		int n = 0;
		for (byte b : data) {
			if (n % 16 == 0)
				result.append(String.format("%05X: ", n));
			result.append(String.format("%02X ", b));
			n++;
			if (n % 16 == 0)
				result.append("\n");
		}
		result.append("\n");
		return result.toString();
	}

	public static short toUnsigned(byte b) {
		return (short)(b < 0 ? (UNSIGNED_MAX_VALUE + 1) + b : b);
	}

	public static String toHexAscii(byte b) {
		// StringWriter sw = new StringWriter(2);
		// addHexAscii(b, sw);
		// return sw.toString();
		String s = Integer.toHexString(b);
		return StringUtils.paddingToFixedString(s, '0', 2, true);
	}

	/**
	 * 
	 * @param bs
	 * @param c
	 *            为分隔符
	 * @return
	 */
	public static String toHexAscii(byte[] bs, String c) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bs.length; i++) {
			sb.append(toHexAscii(bs[i]));
			if (i < bs.length - 1)
				sb.append(c);
		}
		return sb.toString();
	}

	public static String toBinaryAscii(byte b) {
		String s = Integer.toBinaryString(b);

		return StringUtils.paddingToFixedString(s, '0', 8, true);

	}

	public static String toBinaryAscii(byte[] bs, String c) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bs.length; i++) {
			sb.append(toBinaryAscii(bs[i]));
			if (i < bs.length - 1)
				sb.append(c);
		}
		return sb.toString();
	}

	public static byte[] xor(byte[] bytes, byte key) {

		byte[] result = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			result[i] = (byte)((bytes[i]) ^ key);
		}
		return result;
	}

	/**
	 * 循环异或，一般用于加密
	 * 
	 * @param bytes
	 * @param key
	 * @return
	 */
	public static byte[] xor(byte[] bytes, byte[] key) {
		byte[] result = bytes;
		for (int i = 0; i < key.length; i++) {
			result = xor(result, key[i]);
		}
		return result;
	}

	public static byte[] fromHexAscii(String s, String delimiterStr)
			throws Exception {

		s = s.replaceAll(delimiterStr, "");
		return fromHexAscii(s);
	}

	public static byte parseByte(String s, int radix) {
		int value = Integer.parseInt(s, radix);
		return (byte)(0x0ff & value);

	}

	/**
	 * 从16进制的字符串形式转换成对应的字节数组，每两个字符表示一个字节
	 * 
	 * @param s
	 * @return
	 * @throws NumberFormatException
	 */
	public static byte[] fromHexAscii(String s) throws Exception {
		try {
			int len = s.length();
			if ((len % 2) != 0)
				throw new NumberFormatException(
						"Hex ascii must be exactly two digits per byte.");

			int out_len = len / 2;
			byte[] out = new byte[out_len];
			int i = 0;
			StringReader sr = new StringReader(s);
			while (i < out_len) {
				// int val = (16 * fromHexDigit(sr.read()))
				// + fromHexDigit(sr.read());
				char[] cs = IOUtils.readFully(sr, 2, false);
				String b = cs[0] + "" + cs[1];
				// System.out.println("---"+b);
				out[i++] = parseByte(b, 16);
			}

			return out;
		} catch (IOException e) {
			// log.error("",e);
			throw e;

		}
	}

	/**
	 * 解析以delimiterStr分隔的s字符串，注意三个数字字符表示一个字节
	 * 
	 * @param s
	 * @param delimiterStr
	 * @return
	 * @throws NumberFormatException
	 */
	public static byte[] fromOctalAscii(String s, String delimiterStr)
			throws Exception {

		s = s.replaceAll(delimiterStr, "");
		return fromOctalAscii(s);
	}

	/**
	 * 解析以三位表示一个字节的八进制字符串，每三个字符决定一个字节，如012034, 表示012和034两个字节
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] fromOctalAscii(String s) throws Exception {
		try {
			int len = s.length();
			if ((len % 8) != 0)
				throw new NumberFormatException(
						"octal ascii must be exactly 3 digits per byte.");

			int out_len = len / 3;
			byte[] out = new byte[out_len];
			int i = 0;
			StringReader sr = new StringReader(s);
			while (i < out_len) {
				// int val = (16 * fromHexDigit(sr.read()))
				// + fromHexDigit(sr.read());
				char[] cs = IOUtils.readFully(sr, 3, false);

				out[i++] = parseByte(ArrayUtils.toString(cs, ""), 8);
				;
			}

			return out;
		} catch (IOException e) {
			throw e;
		}

	}

	/**
	 * 按指定的间隔字符串，指定的进制解析s
	 * 
	 * @param s
	 * @param delimiterStr
	 * @param radix
	 * @return
	 */
	public static byte[] fromRadixAscii(String s, String delimiterStr, int radix)
			throws Exception {
		try {
			int len = s.length();

			Scanner scan = new Scanner(s);
			scan.useDelimiter(delimiterStr);
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			while (scan.hasNext()) {
				byte b = parseByte(scan.next(), radix);
				bo.write(b);
			}

			return bo.toByteArray();
		} catch (Exception e) {
			throw e;
		}
	}

	public static byte[] fromDecimalAscii(String s, String delimiterStr)
			throws Exception {
		try {
			int len = s.length();

			Scanner scan = new Scanner(s);
			scan.useDelimiter(delimiterStr);
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			while (scan.hasNext()) {
				byte b = parseByte(scan.next(), 10);
				bo.write(b);
			}

			return bo.toByteArray();
		} catch (Exception e) {
			throw e;
		}
	}

	public static byte[] getDataOutputStreamBytes(String s) throws Exception {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();// 缓存
		DataOutputStream dos = new DataOutputStream(byteOut);
		try {

			if (s == null)
				s = "";
			dos.writeUTF(s);

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				dos.close();
			} catch (IOException e) {
				throw e;
			}
		}
		return byteOut.toByteArray();
	}

	public static byte[] getDataOutputStreamBytes(int i) throws Exception {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();// 缓存
		DataOutputStream dos = new DataOutputStream(byteOut);
		try {
			dos.writeInt(i);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				dos.close();
			} catch (IOException e) {
				throw e;
			}
		}
		return byteOut.toByteArray();
	}

	public static byte[] getDataOutputStreamBytes(short i) throws Exception {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();// 缓存
		DataOutputStream dos = new DataOutputStream(byteOut);
		try {
			dos.writeShort(i);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				dos.close();
			} catch (IOException e) {
				throw e;
			}
		}
		return byteOut.toByteArray();
	}

	public static byte[] getDataOutputStreamBytes(byte[] b_data)
			throws Exception {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();// 缓存
		DataOutputStream dos = new DataOutputStream(byteOut);
		try {
			if (ArrayUtils.isEmpty(b_data)) {
				dos.writeInt(0);
			} else {
				dos.writeInt(b_data.length);
				dos.write(b_data);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				dos.close();
			} catch (IOException e) {
				throw e;
			}
		}
		return byteOut.toByteArray();
	}

	public static byte[] fromBinaryAscii(String s, String delimiterStr)
			throws Exception {

		s = s.replaceAll(delimiterStr, "");
		return ByteUtils.fromBinaryAscii(s);

	}

	public static byte[] fromBinaryAscii(String s) throws Exception {

		try {
			int len = s.length();
			if ((len % 8) != 0)
				throw new NumberFormatException(
						"Binary ascii must be exactly 8 digits per byte.");

			int out_len = len / 8;
			byte[] out = new byte[out_len];
			int i = 0;
			StringReader sr = new StringReader(s);
			while (i < out_len) {
				// int val = (16 * fromHexDigit(sr.read()))
				// + fromHexDigit(sr.read());
				char[] cs = IOUtils.readFully(sr, 8, false);

				out[i++] = parseByte(ArrayUtils.toString(cs, ""), 2);
				;
			}

			return out;
		} catch (IOException e) {
			throw e;
		}
	}

	// static void addHexAscii(byte b, StringWriter sw) {
	// short ub = toUnsigned(b);
	// int h1 = ub / 16;
	// int h2 = ub % 16;
	// sw.write(toHexDigit(h1));
	// sw.write(toHexDigit(h2));
	// }

	// public int fromHexDigit(int c) throws NumberFormatException {
	// if (c >= 0x30 && c < 0x3A)
	// return c - 0x30;
	// else if (c >= 0x41 && c < 0x47)
	// return c - 0x37;
	// else if (c >= 0x61 && c < 0x67)
	// return c - 0x57;
	// else
	// throw new NumberFormatException('\'' + c
	// + "' is not a valid hexadecimal digit.");
	// }

	public byte fromHexDigit(String cs) {
		return Byte.parseByte(cs, 16);
	}

	/* note: we do no arg. checking, because */
	/* we only ever call this from addHexAscii() */
	/* above, and we are sure the args are okay */
	private static char toHexDigit(int h) {
		char out;
		if (h <= 9)
			out = (char)(h + 0x30);
		else
			out = (char)(h + 0x37);
		// System.err.println(h + ": " + out);
		return out;
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(toDecimalAscii(new byte[] { 45, -125, 67 }, " "));
		// System.out.println(toBinaryAscii(new byte[] { 45, -125, 67 }, " "));
		// byte[] bs=convertDouble2Bytes(-5499.13f);
		// System.out.println(convertBytes2Double(bs));
		// //ConcurrentSkipListMap,
		// bs=convertChar2Bytes('0');
		// System.out.println(convertBytes2Char(bs));
		// System.out.println(ArrayUtils.toString(fromHexAscii(toHexAscii(new
		// byte[] { 45, -125, 67 }," ")," "),","));
		//
		System.out.println(toBinaryAscii(new byte[] { 45, -125, 67 }, " "));
		System.out.println(ArrayUtils.toString(
				fromBinaryAscii(
						toBinaryAscii(new byte[] { 45, -125, 67 }, " "), " "),
				","));
		String s = toOctalAscii(new byte[] { 45, -125, 67 }, " ");
		System.out.println(s);
		s = "055 603 77";
		System.out.println(ByteUtils.toOctalAscii(fromRadixAscii(s, " ", 8),
				" "));
		String ss = toDecimalAscii(new byte[] { 45, -125, (byte)245 }, ",");
		System.out.println(ss);
		ss = "45,-12,-11";
		byte[] bss = ByteUtils.fromDecimalAscii(ss, ",");
		System.out.println(ArrayUtils.toString(bss));
		System.out.println(ByteUtils.toDecimalAscii(bss, ","));
		// Byte.decode("-0x10");
		// Scanner sc
		// System.out.println(format(new byte[]{1, -125, 67}));
		// SocketChannel
		// System.out.println(parseByte("12",16));

		System.out.println(ArrayUtils.toString("中".getBytes("utf-16le"), ""));
		byte[] bss2 = "中".getBytes("utf-16be");
		System.out.println(ByteUtils.convertFromUTF16beBytes2Char(bss2));

	}
}
