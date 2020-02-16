package com.zili.logic;

import com.google.gson.Gson;
import com.zili.MyApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zili.bean.Person;

import net.sf.json.JSON;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @Created HaiyuKing
 * @Used   登录界面相关接口
 */
public class LoginLogic extends BaseLogic {

	private static LoginLogic _Instance = null;
	private String tojson;

	public static LoginLogic Instance() {
		if (_Instance == null)
			_Instance = new LoginLogic();
		return _Instance;
	}

	private LoginLogic() {
		this.context = MyApplication.getAppContext();//防止了内存泄漏
	}

	/**
	 * get请求测试
	 */
	public String getJsonApi(StringCallback callback)
			throws Exception {
		String result = "";
		OkHttpUtils
				.get()
				.url(getSpcyUrl(ServerApi.GET_URL))
				.id(100)
				.tag(context)
				.build()
				.execute(callback);
		return result;
	}

	/**
	 * post请求	.addParams("user.userName", userName)
	 * .addParams("user.password", password)
	 */
//	public String postJosnApi(String username, String password,StringCallback callback)
//			throws Exception {
//		String result = "";
//		OkHttpUtils
//				.post()
//				.url(getSpcyUrl(ServerApi.POST_URL))
//		        .addParams("username",username)
//		        .addParams("password",password)
//				.id(100)
//				.tag(context)
//				.build()
//				.execute(callback);
//		return result;
//	}
	public String postJsonApi(Person person, StringCallback callback) {
		String result = "";
		//MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
	//	RequestBody body=RequestBody.create(JSON,tojson);
		OkHttpUtils
				.postString()
				.url(getSpcyUrl(ServerApi.POST_URL))
				.content(new Gson().toJson(person))
				.mediaType(MediaType.parse("application/json; charset=utf-8"))
				.id(100)
				.tag(context)
				.build()
				.execute(callback);
		return result;

	}




}

