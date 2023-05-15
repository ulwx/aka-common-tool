package com.ulwx.tool.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ulwx.tool.ArrayUtils;
import com.ulwx.tool.CollectionUtils;
import com.ulwx.tool.NetUtils;
import com.ulwx.tool.StringUtils;

public class HttpClient {
	private static Logger log = LoggerFactory.getLogger(HttpClient.class);

	public static int connecttion_timeout = 8000;
	public static int so_timeout = 8000;
	private CloseableHttpClient httpClient = null;
	private volatile static HttpHost proxy = null;
	private HttpEntity requestEntity = null;
	private HttpEntity responseEntity = null;
	private HttpRequestBase method = null;
	private String requestURL = "";
	private int reponseStatusCode = 0;
	private CloseableHttpResponse response=null;

	private boolean consumeContent = true;
	private HashMap<String, String> requestHeaders = new HashMap<String, String>();
	private static HashMap<String, String> defaultHeaders = new HashMap<String, String>();
	static {
		defaultHeaders.put("User-Agent", "Opera/9.80 (Windows NT 5.1; U; zh-cn) " + "Presto/2.2.15 Version/10.10");
		defaultHeaders.put("Accept",
				"text/html, application/xml;q=0.9, application/xhtml+xml, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1");
		defaultHeaders.put("Accept-Language", "zh-cn,en;q=0.9,zh;q=0.8");
		defaultHeaders.put("Accept-Encoding", "identity, *;q=0");
		defaultHeaders.put("Accept-Charset", "iso-8859-1, utf-8, utf-16, *;q=0.1");
		defaultHeaders.put("Content-Type", "text/html; charset=utf-8");

	}
	{
		requestHeaders.putAll(defaultHeaders);
	}
	private HashMap<String, String> responseHeaders = new HashMap<String, String>();

	public HttpClient() {
		try {
            // 在调用SSL之前需要重写验证方法，取消检测SSL
            X509TrustManager trustManager = new X509TrustManager() {
                @Override public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override public void checkClientTrusted(X509Certificate[] xcs, String str) {}
                @Override public void checkServerTrusted(X509Certificate[] xcs, String str) {}
            };
            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            ctx.init(null, new TrustManager[] { trustManager }, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);

			LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();  
			this.httpClient = HttpClients.custom().setRedirectStrategy(redirectStrategy).setSSLSocketFactory(socketFactory).build();
			


		} catch (Exception e) {
			log.error("", e);
		}

	}


	synchronized public static void setProxy(String host, int port) {
		proxy= new HttpHost(host, port,"http");  

	}
	public CloseableHttpClient getClient() {
		return httpClient;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public int getReponseStatusCode() {
		return reponseStatusCode;
	}

	public static HttpClient getHttpClient() {

		return new HttpClient();
	}

	public void setRequestHeaders(Map<String, String> headers) {
		this.requestHeaders.putAll(headers);
	}

	public HashMap<String, String> getRequestHeaders() {
		return this.requestHeaders;
	}

	public void setRequestHeader(String header, String value) {
		this.requestHeaders.put(header, value);
	}

	public HashMap<String, String> getResponseHeaders() {
		return responseHeaders;
	}

	public long getResponseContentLength() {
		String len = this.getResponseHeaders().get("Content-Length");
		return Long.valueOf(len);
	}

	public String getResponseContentType() {
		String type = this.getResponseHeaders().get("Content-Type");
		return type;
	}

	public String getResponseLocation() {
		String s = this.getResponseHeaders().get("Location");
		return s;
	}

	public String getResponseServer() {
		String s = this.getResponseHeaders().get("Server");
		return s;
	}

	public String getRequestHeaderValue(String header) {
		return this.getRequestHeaders().get(header);
	}

	public void reset() {
		this.requestHeaders.clear();
		this.requestHeaders.putAll(defaultHeaders);
		this.reponseStatusCode = 0;
		this.requestURL = "";
		this.requestEntity = null;
		this.responseEntity = null;
		this.responseHeaders.clear();
		this.method = null;
		this.consumeContent = true;
	}

	public void setRequestContentType(String value) {
		this.requestHeaders.put("Content-Type", value);
	}

	public String getRequestContentType() {
		return this.requestHeaders.get("Content-Type");
	}

	public void setRequestContentCharset(String value) {
		String contentType = StringUtils.getNotNullString(this.requestHeaders.get("Content-Type"));

		int pos = contentType.indexOf(";");
		if (pos != -1) {
			contentType = contentType.substring(0, pos).trim();
		}
		this.requestHeaders.put("Content-Type", contentType + ";charset=" + value);
	}

	// Content-Encoding
	public String getRequestContentEncoding() {
		return this.requestHeaders.get("Content-Encoding");
	}

	public String getResponseContentEncoding() {
		return this.responseHeaders.get("Content-Encoding");
	}

	public String getRequestContentCharset() {
		String contentType = StringUtils.getNotNullString(this.requestHeaders.get("Content-Type"));
		// log.debug("contentType="+contentType);
		String charset = NetUtils.fetchCharset(contentType);
		// log.debug("charset="+charset);
		return charset;
	}

	public void setRequestUserAgent(String value) {
		this.requestHeaders.put("User-Agent", value);
	}

	public String getRequestUserAgent() {
		return this.requestHeaders.get("User-Agent");
	}

	public void setRequestAccept(String value) {
		this.requestHeaders.put("Accept", value);
	}

	public String getRequestAccept() {
		return this.requestHeaders.get("Accept");
	}

	public void setRequestAcceptLanguage(String value) {
		this.requestHeaders.put("Accept-Language", value);
	}

	public String getRequestAcceptLanguage() {
		return this.getRequestHeaders().get("Accept-Language");
	}

	public void setRequestAcceptEncoding(String value) {
		this.requestHeaders.put("Accept-Encoding", value);
	}

	public String getRequestAcceptEncoding() {
		return this.getRequestHeaders().get("Accept-Encoding");
	}

	public void setRequestAcceptCharset(String value) {
		this.requestHeaders.put("Accept-Charset", value);
	}

	public String getRequestAcceptCharset() {
		return this.getRequestHeaders().get("Accept-Charset");
	}

	public void setRequestHost(String value) {
		this.requestHeaders.put("Host", value);
	}

	public String getRequestHost() {
		return this.getRequestHeaders().get("Host");
	}

	public void setRequestReferer(String value) {
		this.requestHeaders.put("Referer", value);
	}

	public String getRequestReferer() {
		return this.getRequestHeaders().get("Referer");
	}

	/**
	 * 提交字节数组
	 * 
	 * @param body
	 */
	public void setBody(byte[] body) {
		if (ArrayUtils.isEmpty(body)) {
			body = new byte[0];
		}
		ByteArrayEntity requestEntity = new ByteArrayEntity(body);
		requestEntity.setChunked(false);
		this.requestEntity = requestEntity;
		if (this.requestEntity.getContentType() != null) {
			this.setRequestContentType(this.requestEntity.getContentType().getValue());
		}
	}

	/**
	 * 提交窗体的参数
	 * 
	 * @param formParamMap
	 */
	public void setBody(Map formParamMap) throws Exception {
		List<NameValuePair> formparams = getListParams(formParamMap);
		try {
			// log.debug("===="+ this.getRequestContentCharset());
			UrlEncodedFormEntity entityParms = new UrlEncodedFormEntity(formparams, this.getRequestContentCharset());
			entityParms.setChunked(false);

			this.requestEntity = entityParms;
			// log.debug("==contentType="+this.requestEntity.getContentType().getValue());
			if (this.requestEntity.getContentType() != null)
				this.setRequestContentType(this.requestEntity.getContentType().getValue());

		} catch (Exception ex) {
			throw ex;
		}

	}

	/**
	 * 提交流
	 * 
	 * @param stream
	 */
	public void setBody(InputStream stream) {
		InputStreamEntity entity = new InputStreamEntity(stream, -1);// 消费所有内容
		entity.setChunked(false);
		this.requestEntity = entity;
		if (this.requestEntity.getContentType() != null)
			this.setRequestContentType(this.requestEntity.getContentType().getValue());
	}

	/**
	 * 提交字符串
	 * 
	 * @param content
	 */
	public void setBody(StringBuilder content) throws Exception {
		try {
			StringEntity entity = new StringEntity(content.toString(), this.getRequestContentCharset());
			this.requestEntity = entity;
			if (this.requestEntity.getContentType() != null)
				this.setRequestContentType(this.requestEntity.getContentType().getValue());
		} catch (Exception ex) {
			throw ex;
		}
	}

	private HttpEntity getRequestEntity() {
		return this.requestEntity;
	}

	private void checkIsConsumeContent() throws Exception {

		if (!consumeContent) {
			if (this.requestEntity != null) {

				EntityUtils.consume(this.requestEntity);
				this.requestEntity = null;
				// this.requestEntity.consumeContent();
			}
			if (this.responseEntity != null) {

				EntityUtils.consume(this.responseEntity);
				// this.requestEntity.consumeContent();
				this.responseEntity = null;
			}
			this.consumeContent = true;
		}
	}



	private CloseableHttpResponse executePost(String url,int connectTimeout,int conRequestTimeout,int socketTimeout) throws Exception {

		this.checkIsConsumeContent();
		this.setRequestURL(url);
		HttpPost httppost = new HttpPost(url);
		
		RequestConfig requestConfig = RequestConfig.custom().setRedirectsEnabled(true)
				.setCircularRedirectsAllowed(true).setMaxRedirects(10)
				.setProxy(proxy)
	                .setConnectTimeout(connectTimeout).setConnectionRequestTimeout(conRequestTimeout)  
	                .setSocketTimeout(socketTimeout).build();
		httppost.setConfig(requestConfig);
		this.method = httppost;
		httppost.setHeaders(getHeadersFromMap(this.getRequestHeaders()));
		httppost.setEntity(this.getRequestEntity());
		CloseableHttpClient client = getClient();
		CloseableHttpResponse response = client.execute(httppost);

		//response = this.redirect(response);
		this.responseHeaders = getMapFromHeaderArray(response.getAllHeaders());
		this.reponseStatusCode = response.getStatusLine().getStatusCode();
		consumeContent = false;
		return response;
	}

	private HashMap<String, String> getMapFromHeaderArray(Header[] responseHeaders) {
		HashMap<String, String> responseHeadersMap = new HashMap<String, String>();
		if (!ArrayUtils.isEmpty(responseHeaders)) {
			for (int i = 0; i < responseHeaders.length; i++) {
				Header head = responseHeaders[i];
				responseHeadersMap.put(head.getName(), head.getValue());
			}
		}
		return responseHeadersMap;
	}

	private CloseableHttpResponse executeGet(String url,int connectTimeout,int conRequestTimeout,int socketTimeout) throws Exception {

		this.checkIsConsumeContent();
		this.setRequestURL(url);
		// log.debug("headers="+CollectionUtils.toString(this.getRequestHeaders()));
		HttpGet httpget = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setRedirectsEnabled(true)
				.setProxy(proxy)
				.setCircularRedirectsAllowed(true).setMaxRedirects(10)  
                .setConnectTimeout(connectTimeout).setConnectionRequestTimeout(conRequestTimeout)  
                .setSocketTimeout(socketTimeout).build();
		httpget.setConfig(requestConfig);
		this.method = httpget;
		httpget.setHeaders(getHeadersFromMap(this.getRequestHeaders()));

		CloseableHttpClient client = getClient();
		CloseableHttpResponse response = client.execute(httpget);
		//response = this.redirect(response);
		this.responseHeaders = getMapFromHeaderArray(response.getAllHeaders());
		this.reponseStatusCode = response.getStatusLine().getStatusCode();
		consumeContent = false;
		return response;
	}

	public void post(String url) throws Exception {
		this.post(url, connecttion_timeout, so_timeout);

	}
	public void post(String url,int connectTimeout,int socketTimeout) throws Exception {
		try {
			CloseableHttpResponse response = this.executePost(url,connectTimeout,connectTimeout,socketTimeout);
			this.response=response;
			this.responseEntity = response.getEntity();
			
		} catch (Exception e) {
			if (this.method != null) {
				this.method.abort();
				this.consumeContent = true;

			}
			this.closeResponse();
			throw e;
		}

	}
	public void get(String url,int connectTimeout,int socketTimeout) throws Exception {
		try {
			CloseableHttpResponse response = this.executeGet(url,connectTimeout,connectTimeout,socketTimeout);
			this.response=response;
			this.responseEntity = response.getEntity();
		} catch (Exception e) {
			if (this.method != null) {
				this.method.abort();
				this.consumeContent = true;
			}
			this.closeResponse();
			throw e;
		}
	}
	
	public void get(String  url) throws Exception  {
		 this.get(url, connecttion_timeout, so_timeout);
	}

	private Header[] getHeadersFromMap(Map map) {
		Header[] requestHeaders = new Header[0];

		if (!CollectionUtils.isEmpty(map)) {
			Set<String> keys = map.keySet();
			requestHeaders = new BasicHeader[map.size()];

			int i = 0;
			for (String key : keys) {
				Header header = new BasicHeader(key, (String) map.get(key));
				requestHeaders[i++] = header;

			}
			return requestHeaders;

		}
		return requestHeaders;
	}

	public void consumeContent() throws Exception {
		if (consumeContent) {
			throw new Exception("内容已经消费！不能再此调用此方法，必须重新执行get或post!");
		}
		EntityUtils.consume(this.responseEntity);
		// this.responseEntity.consumeContent();

	}

	public String getString(String defalutCharset) throws Exception {
		if (consumeContent) {
			throw new Exception("内容已经消费！不能再此调用此方法，必须重新执行get或post!");
		}
		try {
			if (this.responseEntity == null)
				return null;
			String s = EntityUtils.toString(this.responseEntity, defalutCharset);

			return s;
		} catch (Exception e) {
			if (this.method != null) {
				this.method.abort();
			}
			throw e;
		} finally {
			this.consumeContent = true;
			this.closeResponse();

		}
	}

	public byte[] getBytes() throws Exception {
		if (consumeContent) {
			throw new Exception("内容已经消费！不能再此调用此方法，必须重新执行get或post!");
		}
		try {
			if (this.responseEntity == null)
				return null;
			byte[] temps = EntityUtils.toByteArray(this.responseEntity);

			return temps;
		} catch (Exception e) {
			if (this.method != null) {
				this.method.abort();
			}
			throw e;
		} finally {
			this.consumeContent = true;
			this.closeResponse();

		}

	}

	public InputStream getInputStream() throws Exception {
		if (consumeContent) {
			throw new Exception("内容已经消费！不能再此调用此方法，必须重新执行get或post!");
		}
		try {
			if (this.responseEntity == null)
				return null;
			byte[] temps = EntityUtils.toByteArray(this.responseEntity);
			return new ByteArrayInputStream(temps);
		} catch (Exception e) {
			if (this.method != null) {
				this.method.abort();
			}
			throw e;
		} finally {
			this.consumeContent = true;
			requestHeaders.clear();
			requestHeaders.putAll(defaultHeaders);

			this.closeResponse();
		}
	}

	public void shutdown() {
		if (httpClient != null) {

			try {
				httpClient.close();
			} catch (IOException e) {
				log.error("", e);
			}
		}
	}
	
	public void closeResponse() {
		if(this.response!=null) {
			try {
				this.response.close();
			} catch (IOException e) {
				log.error(e+"",e);
			}finally {
				this.response=null;
			}
		}
	}

	private static List<NameValuePair> getListParams(Map map) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();

		if (CollectionUtils.isEmpty(map))
			return formparams;
		Set keys = map.keySet();

		for (Object key : keys) {
			Object value = map.get(key);
			if (value.getClass().isArray()) {
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("11", "222");
		Map map2 = new HashMap(map);
		map2.put("22", "333");
		System.out.println(CollectionUtils.toString(map));

	}

}
