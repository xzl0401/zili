package com.zili.logic;

/**
 * Created by HaiyuKing
 * Used 接口地址
 */

public interface ServerApi {

	//String SERVER_URL = "http://www.weather.com.cn/data/sk/";// 域名（开发环境）192.168.1.100 10.10.0.107192.168.1.100
	String SERVER_URL = "http://192.168.3.95:8080/JsonProject/";// 域名（开发环境）
	// String GET_URL = "101010100.html";
	// get请求地址http://localhost:8080/JsonProject/servlet /Test

	String GET_URL = "servlet/Test";//servlet/LoginServlet
	//String SERVER_URL = "http://localhost:8080/JsonProject/servlet/Text";
	//String GET_URL = "";

	//String POST_URL = "servlet/LoginServlet";
	String POST_URL = "servlet/Test";

}
