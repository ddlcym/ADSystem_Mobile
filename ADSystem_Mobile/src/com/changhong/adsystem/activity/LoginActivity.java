package com.changhong.adsystem.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.changhong.adsystem.http.HttpRequest;
import com.changhong.adsystem.modle.Class_Constant;
import com.changhong.adsystem.modle.JsonResolve;
import com.changhong.adsystem_mobile.R;

/**
 * @author cym
 * @date 创建时间：2016年12月21日 下午1:22:09
 * @version 1.0
 * @parameter
 */
public class LoginActivity extends Activity implements OnClickListener {
	// 声明控件对象
	private int secuCodeTime=60000;//间隔多少秒可以获取一次验证码
	
	private EditText et_name, et_pass;
	private Button mLoginButton;
	int selectIndex = 1;
	int tempSelect = selectIndex;
	boolean isReLogin = false;
	// private String [] coutry_phone_sn_array,coutry_name_array;
	public final static int LOGIN_ENABLE = 0x01; // 注册完毕了
	public final static int LOGIN_UNABLE = 0x02; // 注册完毕了
	public final static int PASS_ERROR = 0x03; // 注册完毕了
	public final static int NAME_ERROR = 0x04; // 注册完毕了

	private Button bt_username_clear, get_security_code;
	private TimeCount time;
	private TextWatcher username_watcher;
	private TextWatcher password_watcher;
	
	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
	private String mobile=null;//手机号
	private String strContent;//验证码
	
	private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				byte[] pdu = (byte[]) obj;
				SmsMessage sms = SmsMessage.createFromPdu(pdu);
				// 短信的内容
				String message = sms.getMessageBody();
				Log.d("logo", "message     " + message);
				// 短息的手机号。。+86开头？
				String from = sms.getOriginatingAddress();
				Log.d("logo", "from     " + from);
				// Time time = new Time();
				// time.set(sms.getTimestampMillis());
				// String time2 = time.format3339(true);
				// Log.d("logo", from + "   " + message + "  " + time2);
				// strContent = from + "   " + message;
				// handler.sendEmptyMessage(1);
				if (!TextUtils.isEmpty(from)) {
					String code = patternCode(message);
					if (!TextUtils.isEmpty(code)) {
						strContent = code;
						UiMangerHandler.sendEmptyMessage(Class_Constant.RECEIVE_SECURITYCODE);
					}
				}
			}
		}
	};

	final Handler UiMangerHandler = new Handler() {
		JSONObject json=null;
		int status=0;
		String result=null;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
//			case LOGIN_ENABLE:
//				mLoginButton.setClickable(true);
//				// mLoginButton.setText(R.string.login);
//				break;
//			case LOGIN_UNABLE:
//				mLoginButton.setClickable(false);
//				break;
//			case PASS_ERROR:
//
//				break;
//			case NAME_ERROR:
//				break;
//			}
			case Class_Constant.POST_SECURITYCODE_RESULT:
				json=(JSONObject) msg.obj;
				status=JsonResolve.getJsonObjInt(json, "status");
				result=JsonResolve.getJsonObjectString(json, "message");
				Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
				
				break;
				
			case Class_Constant.RECEIVE_SECURITYCODE:
				et_pass.setText(strContent);
				login();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// //不显示系统的标题栏
		// getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN );

		setContentView(R.layout.activity_login);
		et_name = (EditText) findViewById(R.id.username);
		et_pass = (EditText) findViewById(R.id.password);

		bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
		get_security_code = (Button) findViewById(R.id.get_security_code);

		bt_username_clear.setOnClickListener(this);
		get_security_code.setOnClickListener(this);
		initWatcher();
		et_name.addTextChangedListener(username_watcher);

		mLoginButton = (Button) findViewById(R.id.login);
		mLoginButton.setOnClickListener(this);
		time = new TimeCount(secuCodeTime, 1000);
	}

	/**
	 * 手机号，密码输入控件公用这一个watcher
	 */
	private void initWatcher() {
		username_watcher = new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				et_pass.setText("");
				if (s.toString().length() > 0) {
					bt_username_clear.setVisibility(View.VISIBLE);
				} else {
					bt_username_clear.setVisibility(View.INVISIBLE);
				}
			}
		};

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.login: // 登陆
			login();

			break;

		case R.id.bt_username_clear:
			et_name.setText("");
			break;
		case R.id.get_security_code:
			
			// 发送验证码到服务器
			mobile=et_name.getText().toString().trim();
			if(mobile.length()==11){
				HttpRequest.getInstance().postSecurityCode(UiMangerHandler, mobile);
				time.start();
			}else{
				Toast.makeText(LoginActivity.this, "手机号码位数不对！", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			get_security_code.setBackgroundColor(Color.parseColor("#B6B6D8"));
			get_security_code.setClickable(false);
			get_security_code.setText("(" + millisUntilFinished / 1000
					+ ") 秒后可重新发送");
		}

		@Override
		public void onFinish() {
			get_security_code.setText("重新获取验证码");
			get_security_code.setClickable(true);
			get_security_code.setBackgroundColor(Color.parseColor("#4EB84A"));

		}
	}

	/**
	 * 登陆
	 */
	private void login() {

		Intent intent = new Intent(LoginActivity.this, ADMainActivity.class);
		startActivity(intent);
	}

	/**
	 * 监听Back键按下事件,方法2: 注意: 返回值表示:是否能完全处理该事件 在此处返回false,所以会继续传播该事件.
	 * 在具体项目中此处的返回值视情况而定.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (isReLogin) {
				Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
				mHomeIntent.addCategory(Intent.CATEGORY_HOME);
				mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				LoginActivity.this.startActivity(mHomeIntent);
			} else {
				LoginActivity.this.finish();
			}
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	/**
	 * 匹配短信中间的6个数字（验证码等）
	 * 
	 * @param patternContent
	 * @return
	 */
	private String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile(patternCoder);
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

}
