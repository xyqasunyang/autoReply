package com.sun.discuz;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.params.CookiePolicy;

/**
 * 
 * @author sun
 *
 */
public class WebClient {

	public static String sgSendPost(String url, HashMap<String, Object> param, String cookie) {
		String responseMsg = "";

		// 1.构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset("UTF-8");

		// 2.构造PostMethod的实例
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		postMethod.setRequestHeader("Cookie", cookie);
		// 3.把参数值放入到PostMethod对象中
		if (param != null) {
			for (String key : param.keySet()) {
				postMethod.addParameter(key, String.valueOf(param.get(key)));
			}
		}
		try { // 4.执行postMethod,调用http接口
			httpClient.executeMethod(postMethod);// 200

			// 5.读取内容
			responseMsg = postMethod.getResponseBodyAsString().trim();

			// 6.处理返回的内容

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // 7.释放连接
			postMethod.releaseConnection();
		}
		return responseMsg;
	}

	public static String sgSendGet(String url, HashMap<String, Object> param) {
		String responseMsg = "";

		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset("GBK");
		String str = "";
		if (param != null) {
			for (String key : param.keySet()) {
				if (str.equals("")) {
					str = "?" + str + key + "=" + param.get(key);
				} else {
					str = str + "&" + key + "=" + param.get(key);
				}
			}
		}
		GetMethod getMethod = new GetMethod(url + str);
		getMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		try {
			httpClient.executeMethod(getMethod);// 200

			responseMsg = getMethod.getResponseBodyAsString().trim();

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // 7.释放连接
			getMethod.releaseConnection();
		}
		return responseMsg;
	}
}
