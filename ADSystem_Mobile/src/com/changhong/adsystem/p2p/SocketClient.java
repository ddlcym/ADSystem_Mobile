package com.changhong.adsystem.p2p;

/**
 * socket通讯 用于手机与盒子端通讯
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import org.apache.http.conn.util.InetAddressUtils;
import android.util.Log;
import com.changhong.adsystem.model.JsonObject;
import com.changhong.adsystem.model.JsonSendModel;

public class SocketClient {

	String TAG = "SocketClient";

	private static Socket mSocketClient = null;
	private static SocketClient instance = null;
	public boolean mClientFlag = false;
	private String hostName;
	private int portNum;
	JsonObject resultObject;
	byte[] theData = null;
	boolean isReqMedia = false;
	PrintWriter mOutput = null;
	BufferedReader mInput = null;

	private SocketClient() {
		mSocketClient = new Socket();
		resultObject = JsonObject.getJsonObject();
	}

	public static SocketClient geInstance() {
		if (null == instance) {
			instance = new SocketClient();
		}
		return instance;
	}

	public boolean socketConnect(String HostName, int iPort) {

		SocketAddress socketAddress = new InetSocketAddress(HostName, iPort);

		try {
			Log.d("WineStock", "WineStock SocketConnect connect ");

			mSocketClient.connect(socketAddress);
			mSocketClient.setSoTimeout(3000);// 设置阻塞时间

			mInput = new BufferedReader(new InputStreamReader(
					mSocketClient.getInputStream()));
			mOutput = new PrintWriter(mSocketClient.getOutputStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("WineStock", "WineStock SocketConnect IOException ");

		} catch (IllegalArgumentException e1) {
			// TODO: handle exception
			Log.d("WineStock",
					"WineStock SocketConnect IllegalArgumentException ");
		}
		return true;
	}

	/**
	 * 发送数据
	 * 
	 * @param SendModel
	 * @return
	 */
	public boolean SClient(JsonSendModel model) {

		boolean bRet = false;
		try {
			if (mSocketClient.isConnected()) {
				mOutput.print(model.toString());
				mOutput.flush();
				bRet = true;
			}
		} catch (IllegalArgumentException e1) {
			Log.d(TAG, "SClient sendMSG IllegalArgumentException ");
		}
		return bRet;
	}

	// 接收服务器数据
	public String readMessage() {
		String revMsg = "";
		String readstr = "";
		try {
			while ((readstr = mInput.readLine()) != null) {
				revMsg += readstr;
			}
		} catch (UnknownHostException e) {
			Log.e(TAG, "---->出现未知主机错误! 主机信息=" + hostName + " 端口号=" + portNum
					+ " 出错信息=" + e.getMessage());

		} catch (IOException e) {
			Log.e(TAG, "---->出现IO异常! 主机信息=" + this.hostName + " 端口号="
					+ this.portNum + " 出错信息=" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(TAG, "---->出现未知异常" + e.getMessage());
		} finally {
			try {
				if (null != mInput) {
					mInput.close();
					mInput = null;
				}
				if (null != mOutput) {
					mOutput.close();
					mOutput = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return revMsg;
	}

	/**
	 * 接收并解析 从服务器返回的数据
	 * 
	 * @return
	 */
	public JsonObject getResponse() {
		String result = readMessage();
		resultObject.ParserMsgFromStr(result);
		return resultObject;
	}

	/**
	 * 释放资源，关闭网络链接
	 */
	public void SocketClose() {
		if (null == mSocketClient)
			return;

		try {
			mSocketClient.close();
			mSocketClient = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取本地网络IP地址。
	 * 
	 * @return
	 */
	private String getLocalHostIp() {
		String ipaddress = "";
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			// 遍历所用的网络接口
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// 遍历每一个接口绑定的所有ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					if (!ip.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ip
									.getHostAddress())) {
						return ipaddress = ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			// Log.e(TAG, "获取本地ip地址失败");
			e.printStackTrace();
		}
		return ipaddress;
	}

}
