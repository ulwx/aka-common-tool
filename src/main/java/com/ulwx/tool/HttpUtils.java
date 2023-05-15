package com.ulwx.tool;

import com.ulwx.tool.http.MultiThreadHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

	synchronized public static void setProxy(String host, int port) {
		MultiThreadHttpClient.setProxy(host, port);

	}
	public static InputStream poolPostAndGetStream(String url, Map parms) throws Exception {
		return postAndGetStream(url, parms);
	}

	public static InputStream postAndGetStream(String url, Map parms) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.setBody(parms);
			client.post(url);
			return client.getInputStream();

		} catch (Exception e) {
			log.error(":" + url + ":" + NetUtils.urlMapToQueryStr(parms));
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + "参数:" + ObjectUtils.toJsonString(parms) + " 使用时间:"
						+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static InputStream postAndGetStream(String url, Map parms, int timeout) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.setBody(parms);
			client.post(url, timeout, timeout);
			return client.getInputStream();

		} catch (Exception e) {
			log.error(":" + url + ":" + NetUtils.urlMapToQueryStr(parms));
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
				
			}

			log.debug("url=" + url + ";" + "参数:" + ObjectUtils.toJsonString(parms) + " 使用时间:"
					+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static InputStream poolPostAndGetStream(String url, byte[] postBody, String requestContentType)
			throws Exception {
		return postAndGetStream(url, postBody, requestContentType);
	}

	public static InputStream postAndGetStream(String url, byte[] postBody, String requestContentType)
			throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.setRequestContentType(requestContentType);
			client.setBody(postBody);
			client.post(url);
			return client.getInputStream();

		} catch (Exception e) {
			log.error(":" + url + ":requestContentType=" + requestContentType);
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + "参数 postBody.length:" + postBody.length + " 使用时间:"
						+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static String poolPost(String url, Map parms, String requestCharset, String ReturnDefaultCharset)
			throws Exception {
		return post(url, parms, requestCharset, ReturnDefaultCharset, MultiThreadHttpClient.CONNECTTION_TIMEOUT);
	}

	public static String poolPost(String url, Map parms, String requestCharset, String ReturnDefaultCharset,
			int timeout) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {

			client = new MultiThreadHttpClient();
			client.setRequestContentCharset(requestCharset);
			client.setBody(parms);
			client.post(url, timeout, timeout);
			return client.getString(ReturnDefaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":" + NetUtils.urlMapToQueryStr(parms));
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + "参数:" + ObjectUtils.toJsonString(parms) + " 使用时间:"
						+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static String poolPost(String url, Map parms, String requestCharset, String ReturnDefaultCharset,
			int timeout, Map<String, String> requestHeaders,Map<String, String> responseHeaders) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {

			client = new MultiThreadHttpClient();
			client.setRequestContentCharset(requestCharset);
			if(requestHeaders!=null) {
				client.setRequestHeaders(requestHeaders);
			}
		
			client.setBody(parms);
			client.post(url, timeout, timeout);
			if(responseHeaders!=null) {
				responseHeaders.putAll(client.getResponseHeaders());
			}
			return client.getString(ReturnDefaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":" + NetUtils.urlMapToQueryStr(parms));
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + "参数:" + ObjectUtils.toJsonString(parms) + " 使用时间:"
						+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static String post(String url, Map parms, String requestCharset, String ReturnDefaultCharset)
			throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {

			client = new MultiThreadHttpClient();
			client.setRequestContentCharset(requestCharset);
			client.setBody(parms);
			client.post(url);
			return client.getString(ReturnDefaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":" + NetUtils.urlMapToQueryStr(parms));
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + "参数:" + ObjectUtils.toJsonString(parms) + " 使用时间:"
						+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static String post(String url, Map parms, String requestCharset, String ReturnDefaultCharset, int timeout)
			throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();

			client.setBody(parms);
			client.post(url, timeout, timeout);
			return client.getString(ReturnDefaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":" + NetUtils.urlMapToQueryStr(parms));
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			
			}
			log.debug("url=" + url + ";" + "参数:" + ObjectUtils.toJsonString(parms) + " 使用时间:"
					+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static String post(String url, Map parms, String requestCharset, String ReturnDefaultCharset, int timeout,
			Map<String, String> returnHeaders) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.setBody(parms);
			client.post(url, timeout, timeout);
			returnHeaders.putAll(client.getResponseHeaders());
			return client.getString(ReturnDefaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":" + NetUtils.urlMapToQueryStr(parms));
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
				
			}

			log.debug("url=" + url + ";" + "参数:" + ObjectUtils.toJsonString(parms) + " 使用时间:"
					+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static String post(String url, Map parms, String requestCharset, String ReturnDefaultCharset, int timeout,
			HashMap<String, String> requestHeaders) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.setBody(parms);
			client.setRequestHeaders(requestHeaders);
			client.post(url, timeout, timeout);
			return client.getString(ReturnDefaultCharset);
		} catch (Exception e) {
			log.error(":" + url + ":" + NetUtils.urlMapToQueryStr(parms));
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
				
			}
			log.debug("url=" + url + ";" + "参数:" + ObjectUtils.toJsonString(parms) + " 使用时间:"
					+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static String poolPost(String url, Map parms, String requestCharset, String ReturnDefaultCharset,
			boolean isLikeExplorerPost) throws Exception {
		return post(url, parms, requestCharset, ReturnDefaultCharset, isLikeExplorerPost);
	}

	/**
	 * 
	 * @param url
	 *            请求的url地址
	 * @param parms
	 *            <p>
	 *            <blockquote>
	 * 
	 *            <pre>
	 * 模拟的窗体的参数,如：
	 * map.put("name",new String[]{"11","22"});
	 * map.put("code","456");
	 * map.put("class",new String("67"));
	 * 设置的值可以为数组或单个值
	 *            </pre>
	 * 
	 *            </blockquote>
	 *            </p>
	 * @param requestCharset
	 *            请求参数编码
	 * @param isLikeExplorerPost
	 *            是否为模仿浏览器的提交方式。如果为模仿浏览器的提交方式，服务器端必须转码，并且支持重定向。
	 * @return
	 */
	public static String post(String url, Map parms, String requestCharset, String ReturnDefaultCharset,
			boolean isLikeExplorerPost) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.setBody(parms);
			client.post(url);
			return client.getString(ReturnDefaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":" + NetUtils.urlMapToQueryStr(parms));
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + "参数:" + ObjectUtils.toJsonString(parms) + " 使用时间:"
						+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static String postFiles(String postUrl, Map<String, Object> params) throws Exception {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		String result = null;
		try {
			SSLContextBuilder builder = new SSLContextBuilder();
			// 全部信任 不做身份鉴定
			builder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			});

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
					new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
			LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();  
			httpclient= HttpClients.custom().setRedirectStrategy(redirectStrategy).
					setSSLSocketFactory(sslsf).build();
			
			HttpPost httpPost = new HttpPost(postUrl);

			MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
			for (String param : params.keySet()) {
				Object val = params.get(param);
				if (val instanceof File) {
					mEntityBuilder.addBinaryBody(param, (File) val);
				} else {
					mEntityBuilder.addTextBody(param, val + "");
				}

			}

			httpPost.setEntity(mEntityBuilder.build());
			response = httpclient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				result = EntityUtils.toString(resEntity);
				// 消耗掉response
				EntityUtils.consume(resEntity);
			}
		} catch (Exception e) {
			log.error(e + "", e);
		} finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpclient);

		}
		return result;

	}

	public static String post(String url, Map parms, String requestCharset, String ReturnDefaultCharset,
			boolean isLikeExplorerPost, int timeout) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();

			client.setBody(parms);
			client.post(url, timeout, timeout);
			return client.getString(ReturnDefaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":" + NetUtils.urlMapToQueryStr(parms));
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
				
			}

			log.debug("url=" + url + ";" + "参数:" + ObjectUtils.toJsonString(parms) + " 使用时间:"
					+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static String poolPost(String url, byte[] postBody, String requestContentType, String ReturnDefaultCharset)
			throws Exception {
		return post(url, postBody, requestContentType, ReturnDefaultCharset);
	}

	public static String post(String url, byte[] postBody, String requestContentType, String ReturnDefaultCharset)
			throws Exception {

		return HttpUtils.post(url, postBody, null, requestContentType, ReturnDefaultCharset);

	}
	public static String post(String url, byte[] postBody, Map<String,String> requestHeaders,String requestContentType, String ReturnDefaultCharset)
			throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			if(requestHeaders!=null && !requestHeaders.isEmpty()) {
				client.setRequestHeaders(requestHeaders);
			}
			client.setRequestContentType(requestContentType);
			client.setBody(postBody);
			client.post(url);
			return client.getString(ReturnDefaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":ReturnDefaultCharset=" + ReturnDefaultCharset);
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + "参数 postBody.length:" + postBody.length + " 使用时间:"
						+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}
	public static String post(String url, byte[] postBody, String requestContentType, String ReturnDefaultCharset,
			int timeout) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.setRequestContentType(requestContentType);
			client.setBody(postBody);
			client.post(url, timeout, timeout);
			return client.getString(ReturnDefaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":requestContentType=" + requestContentType, e);
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
				
			}
			log.debug("url=" + url + ";" + "参数postBody.length" + postBody.length + " 使用时间:"
					+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static String poolGet(String url, String defaultCharset) throws Exception {
		return get(url, defaultCharset);
	}

	public static String get(String url, String defaultCharset) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();

			client.get(url);
			return client.getString(defaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":defaultCharset=" + defaultCharset, e);
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + " 使用时间:" + (System.currentTimeMillis() - start) + "毫秒");

		}

	}

	public static String poolGet(String url, String defaultCharset, Map<String, String> requestHeaders) throws Exception {
		return get(url, defaultCharset, requestHeaders);
	}
	
	public static void close() {
		MultiThreadHttpClient.shutdown();
		
	}

	public static String get(String url, String defaultCharset, Map<String, String> requestHeaders) throws Exception {
		return HttpUtils.get(url, defaultCharset, requestHeaders, null);

	}
	public static String get(String url, String defaultCharset, Map<String, String> requestHeaders, Map<String, String> responseHeaders) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.setRequestHeaders(requestHeaders);
			client.get(url);
			if(responseHeaders!=null) {
				responseHeaders.putAll(client.getResponseHeaders());
			}
			return client.getString(defaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":defaultCharset=" + defaultCharset, e);
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + "参数:" + ObjectUtils.toJsonString(requestHeaders) + " 使用时间:"
						+ (System.currentTimeMillis() - start) + "毫秒");
		}

	}
	public static byte[] poolGetBytes(String url) throws Exception {
		return getBytes(url);
	}

	public static byte[] getBytes(String url) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.get(url);
			return client.getBytes();

		} catch (Exception e) {
			log.error("" + url + "", e);
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + " 使用时间:" + (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static byte[] getBytes(String url, int timeout) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;

		try {
			client = new MultiThreadHttpClient();
			client.get(url, timeout, timeout);
			return client.getBytes();

		} catch (Exception e) {
			log.error("" + url + "", e);

			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
				
			}

			log.debug("url=" + url + ";" + " 使用时间:" + (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static InputStream poolGet(String url) throws Exception {
		return get(url);
	}

	public static InputStream get(String url) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.get(url);
			return client.getInputStream();

		} catch (Exception e) {
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + " 使用时间:" + (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static int poolGetResponseStatusCode(String url) throws Exception {
		return getResponseStatusCode(url);
	}

	public static int getResponseStatusCode(String url) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.get(url);
			client.consumeContent();
			return client.getReponseStatusCode();

		} catch (Exception e) {
			log.error(":" + url + ":", e);
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
			}
			if (log.isDebugEnabled())
				log.debug("url=" + url + ";" + " 使用时间:" + (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static InputStream get(String url, int timeout) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.get(url, timeout, timeout);
			return client.getInputStream();

		} catch (Exception e) {
			log.error(":" + url + ":", e);
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
				
			}

			log.debug("url=" + url + ";" + " 使用时间:" + (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static String get(String url, String defaultCharset, int timeout) throws Exception {
		long start = System.currentTimeMillis();
		MultiThreadHttpClient client = null;
		try {
			client = new MultiThreadHttpClient();
			client.get(url, timeout, timeout);
			return client.getString(defaultCharset);

		} catch (Exception e) {
			log.error(":" + url + ":defaultCharset=" + defaultCharset, e);
			throw e;
		} finally {
			if (client != null) {
				client.closeResponse();
				
			}

			log.debug("url=" + url + ";" + " 使用时间:" + (System.currentTimeMillis() - start) + "毫秒");
		}

	}

	public static void main(String[] args) throws Exception {

		String url = "http://61.221.181.18";

		byte[] ret = HttpUtils.getBytes(url);
		System.out.println("ret=" + ret);

	}
}
