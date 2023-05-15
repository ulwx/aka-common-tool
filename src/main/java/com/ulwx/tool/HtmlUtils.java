package com.ulwx.tool;

import java.util.ArrayList;
import java.util.List;

public class HtmlUtils {

	public static String nl2br(String value) {
		return value.replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>")
				.replaceAll("\r", "<br/>");
	}

	/**
	 * textarea框的换行 textarea标签的wrap属性的值为"hard"，并且赋予textarea的内容文本是调用此方法替换过的
	 * 
	 * @param value
	 * @return
	 */
	public static String nl2nl(String value) {
		String s = value.replaceAll("\r\n", "\\\\r").replaceAll("\n", "\\\\r")
				.replaceAll("\r", "\\\\r");
		return s;
	}

	public static String toJsonString(Object obj) {

		return ObjectUtils.toJsonString(obj);
	}

	/**
	 * 此方法不支持本身就是泛型的对象，如type为 List<String>
	 * 
	 * @param json
	 * @param type
	 *            ，type不能为本身是泛型类型的类型，如List<Stirng>
	 * @return
	 */
	public static Object fromJsonToObject(String json, Class<?> type)
			throws Exception {

		Object t = ObjectUtils.fromJsonToObject(json, type);
		return t;
	}

	public static String htmlEscape(String src) {
		return EscapeUtil.escapeHtml(src);
	}

	/**
	 * 只反转义html实体
	 * 
	 * @param src
	 * @return
	 */
	public static String htmlUnEscapeOnlyForHtmlEntity(String src) {
		return EscapeUtil.unescapeOnlyHtmlEntity(src);
	}

	/**
	 * 此方法具有容错功能
	 * 
	 * @param src
	 * @return
	 */
	public static String htmlUnEscape(String src) {
		return Encode.unicode2gb(src); 
	}

	/**
	 * 把html片段的字符串 进行 分页,此方法具有简单的容错功能
	 * 
	 * @param htmlfragment
	 *            html片段
	 * @return 每页的大小
	 */

	public static List<String> deal(String htmlfragment, int pageSize) {
		htmlfragment = htmlfragment.trim();
		List<String> pageSet = new ArrayList<String>();
		int totallength = htmlfragment.length();// 文字总长度
		// int pageSize =2000;// 每页的显示的长度

		StringBuilder sb = new StringBuilder(htmlfragment);

		int pos = 0;
		// 如果总长度小于 分页的长度===没有分页
		if (totallength <= pageSize) {
			String str = htmlfragment.substring(0, totallength);
			pageSet.add(str);
		}// 有分页
		else if (totallength > pageSize) {
			int count = 0;
			while (sb.length() > 0)// 循环处理分页技术
			{
				if (count++ > 40)
					break;
				String str = "";
				pos = pageSize;
				if (sb.length() < pageSize) {
					pos = sb.length();
				}
				int lastlt = sb.lastIndexOf("<", pos);
				int gt = -1;
				if (lastlt != -1) {
					gt = sb.indexOf(">", lastlt);
					if (gt == -1) {
						sb.deleteCharAt(lastlt);
						continue;
					} else {
						if (gt >= pos) {
							if (sb.charAt(lastlt + 1) == '/') {
								pos = gt;
							} else {
								pos = lastlt;
							}

						} else {

							if (sb.charAt(lastlt + 1) != '/'
									&& sb.charAt(gt - 1) != '/') {

								pos = lastlt;
							}
						}

					}
				}

				if (pos == 0) {
					if (gt == -1) {
						sb.deleteCharAt(lastlt);
						continue;
					} else {
						pos = gt + 1;
					}
				}
				str = sb.substring(0, pos);
				if (StringUtils.hasText(str)) {
					pageSet.add(str.trim());
				}
				sb.delete(0, pos);
			}
		}



		return pageSet;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// System.out.println(nl2nl("aaa\r\nsssss"));
		try {
//			String s = "发生地&a/df?";
//			byte[] bs = s.getBytes("utf-8");
//			System.out.println(ByteUtils.toHexAscii(bs, ""));
//			String t = new String(bs, "iso-8859-1");
//			String ss = URLEncoder.encode(t, "utf8");
//			System.out.println(ss);
//			String m = URLDecoder.decode(ss, "utf8");
//			byte[] sss = m.getBytes("iso-8859-1");
//
//			System.out.println(Integer.toHexString(-1));
//			System.out.println(new String(sss, "utf-8"));
//			StringBuilder sb = new StringBuilder();
//			for (int i = 0; i < bs.length; i++) {
//				sb.append(Integer.toHexString(bs[i] & 0x00ff));
//				sb.append(",");
//			}
//			byte b = Byte.valueOf("15", 16);
//			System.out.println(b);
//			System.out.println(sb.toString());
          String s="\\u0022\\u007d";
         // System.out.println(org.owasp.encoder.Encode.forHtml(s));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
