package com.zili;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zili.bean.Person;
import com.zili.logic.LoginLogic;
import com.zili.utils.ToastUtil;
import com.zili.utils.httputil.HttpUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import okhttp3.Call;
import okhttp3.Request;



/**
 * A login screen that offers login via email/password.
 */
public class  LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private TextView mBtnLogin;

    private Button mBtnFh;
    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;
    private TextView responseText;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private String tojson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        Log.d("LoginActivity","xxxxxxx");

        initView();
    }

   //输入错误，恢复初始界面重新输入
    private void recovery() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }


    private void initView() {
        mBtnFh = (Button) findViewById(R.id.fh);
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        responseText = (TextView) findViewById(R.id.response_text);
        usernameEdit =(EditText) findViewById(R.id.userEditText);
        passwordEdit = (EditText)findViewById(R.id.pwdEditText);
        usernameEdit.getText();
        passwordEdit.getText();
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);

      //设置监听
        mBtnLogin.setOnClickListener(this);
        mBtnFh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fh:
                //Intent intent = new Intent(LoginActivity.this,MainActivity.class);
               // startActivity(intent);
                recovery();
               // getJson();
               Toast.makeText(this, "xuuuu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_btn_login:
                // 计算出控件的高与宽
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();
                // 隐藏输入框
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);

                inputAnimator(mInputLayout, mWidth, mHeight);
                //v.getId()获取当前组件id属性的值，这是一个整型常量。
                //每一个View都有一个Id（R.id.viewId）、在你onclick（View v）这个方法中、v既是你点击对象的view、
                // 但是怎么去判断是不是你点击的那个view就直接可以由v.getId() == R.id.viewId来判断是否为点击事件的view
                if (v.getId() == R.id.main_btn_login) {//ContextCompat.checkSelfPermission方法判断是不是授权了，第二个参数是权限名
                    if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.INTERNET) !=
                            PackageManager.PERMISSION_GRANTED) {//申请授权：第一参数要求是Activity实例，第二参数String数组，申请的权限名放入其中，第三个参数请求码，是唯一值即可，这里传入是1。
                        ActivityCompat.requestPermissions(LoginActivity.this,
                                new String[]{Manifest.permission.INTERNET}, 1);
                    } else {
                       // sendRequestWithOkHttp();//经充许权限，执行相应操作。
                       // getJson();
                        if (usernameEdit.toString().isEmpty() || passwordEdit.toString().isEmpty()) {
                            System.out.println("学号或密码不能为空");
                        }else{
                            Person person = new Person();
                            person.setName(usernameEdit.getText().toString());
                            person.setPassword(passwordEdit.getText().toString());

                          //  Gson gson = new Gson();
                           // tojson = gson.toJson(person);
                            Toast.makeText(this,tojson,Toast.LENGTH_SHORT).show();
                           // postJosn(person.getName(),per son.getPassword());
                            postJosn(person);
                        }


                    }
                }
                break;
        }


    }

    //不论用户是否授权，最终回调到onRequestPermissionsResult(),授权结果会封装在grantResults参数中，
    //用户同意就调用sendRequestWithOkHttp(),否则弹出失败提示
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,
                                           int[] grantResults)
    {
        switch(requestCode){
            case  1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED){ ;//sendRequestWithOkHttp();getJson()
                }else {
                    Toast.makeText(this,"You denide the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }



    /**
     * 输入框的动画效果
     *
     * @param view
     *            控件
     * @param w
     *            宽
     * @param h
     *            高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }
/////////从此处加入以下部份private void postJosn(String username,String password){

    private void postJosn(Person person){
      //  String username = "xxx";
      //  String password = "sssxxxccccvvfddfggfgfg";
       // Person person = new Person();
        try {
            LoginLogic.Instance().postJsonApi(person, new PostJsonStringCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



/**
    private void getJson () {
        if (HttpUtil.isNetworkAvailable(this)) {
            //执行网络请求接口GetJsonStringCallback
            try {
                LoginLogic.Instance().getJsonApi(new GetJsonStringCallback());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.network_enable));
        }
    }
**/

    ///
    @Override
    public void onPause () {
        // TODO Auto-generated method stub
        super.onPause();
        //取消网络请求,根据tag取消请求
        OkHttpUtils.getInstance().cancelTag(this);
    }

/**
 * post接口自定义回调函数
 */
public class PostJsonStringCallback extends StringCallback
{
    @Override
    public void onBefore(Request request, int id)
    {//showProgressDialog("");//显示进度加载框
    }

    @Override
    public void onAfter(int id)
    {//dismissProgressDialog();//隐藏进度加载框
    }

    @Override
    public void onError(Call call, Exception e, int id)
    {
        ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_again));
        Log.w(TAG,"{onError}e="+e.toString());
    }

    @Override
    public void onResponse(String response, int id)
    {
        Log.e(TAG, "onResponse：response="+response);
        switch (id)
        {
            case 100://http
                try {
                    if (response != null && !"".equals(response)){
                        //解析登录成功信息
                       // JSONObject responseObj = new JSONObject(response);

                        String xu="";
                        JSONArray jsonArray =new JSONArray(response);
                        for (int i = 0; i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String number = jsonObject.getString("Number");
                            String name = jsonObject.getString("Name");
                            String password = jsonObject.getString("Password");
                            String age = jsonObject.getString("Age");
                            xu =xu+ "Number:"+number+"Name:"+name+"Password"+password+"Age:"+age;

                            responseText.setText(xu);
                        }
                    } else {
                        ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_null_exception));
                    }
                } catch (JSONException e) {
                    ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_json_exception));
                } catch (Exception e) {
                    ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_json_exception));
                } finally {
                }
                break;
            case 101://https
                break;
        }
    }
    @Override
    public void inProgress(float progress, long total, int id)
    {
        Log.e(TAG, "inProgress:" + progress);
    }
}






    /**
     * get接口的自定义回调函数*/
    /**
    private class GetJsonStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {//showProgressDialog("");//显示进度加载框
        }

        @Override
        public void onAfter(int id) {//dismissProgressDialog();//隐藏进度加载框
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_again));
            Log.w(TAG, "{onError}e=" + e.toString());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：response=" + response);
            switch (id) {
                case 100://http
                    try {
                        if (response != null && !"".equals(response)) {
                            //解析
                            // ToastUtil.showShortToast("xxx");
                            // JSONObject responseObj = new JSONObject(response);
                            String xu="";
                            JSONArray jsonArray =new JSONArray(response);
                            for (int i = 0; i<jsonArray.length();i++){
                                JSONObject jsonObject =jsonArray.getJSONObject(i);
                                String number = jsonObject.getString("Number");
                                String name = jsonObject.getString("Name");
                                String age = jsonObject.getString("Age");
                                // int  age = jsonObject.getInt("age");
                                // String age1=String.valueOf(age);

                               // xu = xu+number+name+age;
                               // responseText.setText(xu);
                            }


                            //tv_show.setText(responseObj.toString());



                        } else {
                            ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_null_exception));
                        }
                    } catch (JSONException e) {
                        ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_json_exception));
                    } catch (Exception e) {
                        ToastUtil.showShortToast(MyApplication.getAppContext().getResources().getString(R.string.login_json_exception));
                    } finally {
                    }
                    break;
                case 101://https
                    break;
            }
        }
        @Override
        public void inProgress(float progress, long total, int id) {
            Log.e(TAG, "inProgress:" + progress);
        }


    }
**/


}
