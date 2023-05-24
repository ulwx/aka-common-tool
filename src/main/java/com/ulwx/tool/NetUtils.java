package com.ulwx.tool;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class NetUtils {
	private static Logger log = LoggerFactory.getLogger(NetUtils.class);

	public static String getQueryStrFromURL(String url) {

		int index = url.indexOf("?");
		if (index == -1)
			return "";
		return url.substring(index + 1);

	}

	private static List<NameValuePair> getListParams(Map map) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();

		if (CollectionUtils.isEmpty(map))
			return formparams;
		Set keys = map.keySet();

		for (Object key : keys) {
			Object value = map.get(key);
			if(value==null) {
				continue;
			}
			if (value!=null && value.getClass().isArray()) {
				
				String[] values = (String[]) value;
				for (int i = 0; i < values.length; i++) {
					formparams.add(new BasicNameValuePair((String) key, values[i]));
				}
			} else {
				formparams.add(new BasicNameValuePair((String) key, value.toString()));
			}

		}
		return formparams;
	}

	public static String getAbsoluteURLFromGivenURL(String url, String url2) throws Exception {
		try {
			URI u = new URI(url);
			URI u2 = u.resolve(url2);
			return u2.toString();
		} catch (Exception ex) {
			throw ex;
		}

	}

	/**
	 * 用法：
	 * <p>
	 * <blockquote>
	 * 
	 * <pre>
	 * map.put("name","123"); 
	 * map.put("code",new String[]{"1234","4567"};
	 * map.put("uu",new String("567");
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param url
	 * @param map
	 *            可以为字符串数组
	 * @param requestCharset
	 * @return
	 * @throws Exception
	 */
	public static String getMethodUrl(String url, Map map, String requestCharset) throws Exception {
		// String result=null;
		try {
			String extParms = "";
			if (!CollectionUtils.isEmpty(map)) {
				List<NameValuePair> qparams = getListParams(map);

				extParms = URLEncodedUtils.format(qparams, requestCharset);
			}
			URI newUrl = new URI(url);

			String scheme = newUrl.getScheme();
			url = "";
			if (StringUtils.hasText(scheme)) {
				url = newUrl.getScheme() + "://";
			} else {
				url = "http://";
			}
			String auth = newUrl.getAuthority();
			if (StringUtils.hasText(auth)) {
				url = url + newUrl.getAuthority();
			}
			url = url + newUrl.getPath();

			String queryStr = newUrl.getRawQuery();

			if (StringUtils.hasText(queryStr)) {
				if (StringUtils.hasText(extParms)) {
					queryStr = queryStr + "&" + extParms;
				}

			} else {
				if (StringUtils.hasText(extParms)) {
					queryStr = extParms;
				}
			}
			if (StringUtils.hasText(queryStr)) {
				url = url + "?" + queryStr;
			}
			// log.debug("+++++++" + url);
		} catch (Exception ex) {

			// log.error("", ex);
			throw ex;
		}
		return url;
	}

	public static String urlMapToQueryStr(Map map, String encoding) {
		String extParms = "";
		if (!CollectionUtils.isEmpty(map)) {
			List<NameValuePair> qparams = getListParams(map);

			extParms = URLEncodedUtils.format(qparams, encoding);
		}
		return extParms;
	}

	/**
	 * 用法：
	 * <p>
	 * <blockquote>
	 * 
	 * <pre>
	 * Map map = new HashMap();
	 * map.put("usr", new String[] { "中国", "中国" });
	 * map.put("name","123"); 
	 * map.put("code",new String[]{"1234","4567"});
	 * System.out.println(NetUtils.getMethodUrl("192.168.112.55/ss/s.jsp",map, "utf-8"));
	 * ----------
	 * 输出结果为：
	 * http://192.168.112.55/ss/s.jsp?name=123&code=1234&code=4567&usr=%E4%B8%AD%E5%9B%BD&usr=%E4%B8%AD%E5%9B%BD
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param map
	 * @return
	 */
	public static String urlMapToQueryStr(Map map) {
		return urlMapToQueryStr(map, "utf-8");
	}

	/**
	 * <p>
	 * <blockquote>
	 * 
	 * <pre>
	 * query字符串转化成map 
	 * 如： Map map=urlQueryStrToMap("name=su&code=123&class=66&class=77");
	 * 形式的字符串转化成map里的元素为： name="su" class={"66","77"}
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * 
	 * @param s
	 * @return
	 */
	public static Map<String, String[]> urlQueryStrToMap(String s) {
		return StringUtils.urlQueryStrToMap(s, null);
	}

	public static Map<String, String[]> urlQueryStrToMap(String s, String urlEncoding) {
		return StringUtils.urlQueryStrToMap(s, urlEncoding);
	}

	/**
	 * 从contentType提取charset
	 * 
	 * @param contentType
	 * @return
	 */
	public static String fetchCharset(String contentType) {

		BasicHeader bh = new BasicHeader("Content-Type", contentType);
		return fetchCharset(bh);

	}

	private static String fetchCharset(Header contentType) {

		HeaderElement[] elments = contentType.getElements();
		String charset = "";
		boolean flag = false;
		for (int i = 0; i < elments.length; i++) {
			HeaderElement he = elments[i];
			NameValuePair[] nv = he.getParameters();
			for (int n = 0; n < nv.length; n++) {
				NameValuePair np = nv[n];
				String parmName = np.getName();
				if (parmName.equalsIgnoreCase("charset")) {
					charset = np.getValue();
					flag = true;
					break;
				}
			}
			if (flag) {
				break;
			}
			// he.getParameterByName(charset);
		}
		return charset;
	}

	public static String fetchContentType(Map headers) {

		if (headers == null || headers.size() == 0)
			return "";
		Set<String> keys = headers.keySet();

		String contentType = "";

		for (String key : keys) {
			if (key.trim().equalsIgnoreCase("content-type")) {
				contentType = (String) headers.get(key);
			}
		}
		return contentType;
	}

	public static String getAuthorityFromURL(String url) throws Exception {
		try {
			if (StringUtils.hasText(url)) {

				URL url2 = new URL(url);
				// System.out.println(url2.getAuthority());
				// System.out.println(url2.getFile());
				// System.out.println(url2.getPath());
				// System.out.println(url2.getProtocol());
				String s = url2.getProtocol() + "://" + url2.getAuthority();
				return s;
			}
			return "";
		} catch (Exception ex) {
			// log.error("", ex);
			throw ex;
		}

	}

	public static String getParentPathFromURL(String url) {
		return FileUtils.getFileParentPath(url);
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(getAuthorityFromURL("http://3g.sina.com.cn:8080/prog/wapsite/sss.jsp"));
		// System.out.println(getParentPathFromURL("http://3g.sina.com.cn:8080/prog/wapsite/sss/"));
		// Map map = new HashMap();
		// map.put("usr", new String[] { "中国", "中国" });
		// System.out.println(NetUtils.getMethodUrl("192.168.112.55/ss/s.jsp",
		// map, "utf-8"));
		//
		// System.out.println(NetUtils.urlMapToQueryStr(map, "gbk"));
		// System.out.println(NetUtils.getAbsoluteURLFromGivenURL(
		// "http://192.168.23.44/test/1.jsp", "/2.jsp"));
		// System.out.println("+++"+NetUtils.getQueryStrFromURL("/image?"));
		// String
		// body="Protocol=002003&PacketID=1000&SoftID=1&VersionID=1&MobileID=101604&IMSI=460022764350104&"
		// +
		// "SmsCenter=&ThemeType=0&ThemeId=200726&Account=12380&Password=<9=:90&";
		// Map bodyMap2=NetUtils.urlQueryStrToMap(body,"utf-8");
		// System.out.println(ObjectUtils.toJsonString(bodyMap2));
//		Map map = new HashMap();
//		map.put("usr", new String[] { "中国", "中国" });
//		map.put("name", "123");
//		map.put("code", new String[] { "1234", "4567" });
//		System.out.println(NetUtils.getMethodUrl("192.168.112.55/ss/s.jsp", map, "utf-8"));
//
//		System.out.println(getHostIP());
//		
//		System.out.print(getIp());
		
		String[] values = (String[]) null;
	}

	public static String getLocalHostName() {
		String hostName;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Exception ex) {
			hostName = "";
		}
		return hostName;
	}

	/**
	 * 多IP处理，可以得到最终ip
	 * 
	 * @return
	 */
	public static String getIp() {
		String localip = null;// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			boolean finded = false;// 是否找到外网IP
			while (netInterfaces.hasMoreElements() && !finded) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					ip = address.nextElement();

					if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
						netip = ip.getHostAddress();
						finded = true;
						break;
					} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
						localip = ip.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			log.error(e + "", e);
		}
		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}
	


	public static String getHostIP() {
		return getIp();

	}

}
