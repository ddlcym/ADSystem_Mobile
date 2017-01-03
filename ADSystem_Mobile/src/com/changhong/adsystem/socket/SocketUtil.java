package com.changhong.adsystem.socket;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParserException;

import com.changhong.adsystem.model.JsonObject;
import com.changhong.adsystem.model.JsonSendModel;

public class SocketUtil {

	public static Vector<JsonObject> getSocketObjectVectorByCallingWS(
			Map<String, Object> params) throws IOException,
			XmlPullParserException {

		// return (Vector<SocketObject>) getObjectByCallingWS(params);
		return null;
	}

	public static JsonObject getObjectByCallingBox(Map<String, Object> params)
			throws IOException, XmlPullParserException {

		String host = SocketConfig.HOST;
		int port = SocketConfig.PORT;
		JsonObject socketPrimitiveResult = callBoxServer(host, port,params);
		return socketPrimitiveResult;
	}

	private static JsonObject callBoxServer(String host, int port,
			Map<String, Object> params) {

		JsonObject resultObject = null;

		// 第一步，创建client对象,并创建链接
		SocketClient client = SocketClient.geInstance();
		client.socketConnect(host,port);
		// 格式化传送数据
		JsonSendModel sendModel = JsonSendModel.getInstance();
		sendModel.init(params);

		// 客户端向服务器发送请求
		if (client.SClient(sendModel)) {
			// 接收来自服务器的数据
			resultObject = client.getResponse();
		} else {
			// 释放资源
			client.SocketClose();
		}

		return resultObject;
	}

	public static JsonSendModel constructRequestObject(Map<String, Object> params) {
		JsonSendModel request = new JsonSendModel();
		return request;
	}

	// 释放资源
	public static void free() {	
		SocketClient client = SocketClient.geInstance();
		client.SocketClose();
	}

}
