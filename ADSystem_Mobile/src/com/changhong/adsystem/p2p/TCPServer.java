package com.changhong.adsystem.p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import com.changhong.adsystem.model.JsonPackage;

/**
 * 服务器端，接收来自服务器的消息
 * 
 * @author YD
 * 
 */

public class TCPServer {

	
	
	
	
	public void startReceiveTask(Socket socket){
		 new Thread(new ReceiveMsgFromSTB(socket)).start(); 
	 }
			
	
	
	
	/**
	 * 发送返回信息
	 * @param socket
	 */
	private void sendResponse(Socket socket,String action,boolean isSuccess) {
		String response = "";
		OutputStream output = null;
		try {
			response=JsonPackage.socketRespMsg(action, isSuccess);
			output = socket.getOutputStream();
			output.write(response.getBytes("utf-8"));
			output.flush();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != output) {
					output.close();
					output = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

    /**
     * 处理从机顶盒获取的信息
     * @param msg
     */
	private void handleMsgFromStb(String msg) {
       
	}
	
	
	/**
	 * 获取通讯的action
	 * @param msg
	 * @return
	 */
	private String getCommAction(String msg) {
		String action="";
       
		return action;
	}

	class ReceiveMsgFromSTB implements Runnable {

		Socket socket = null;

		public ReceiveMsgFromSTB(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			// 向android客户端输出hello worild
			
			InputStream input = null;
			String response = "",action="";
			try {
				// 向客户端发送信息
				input = socket.getInputStream();
				BufferedReader bff = new BufferedReader(new InputStreamReader(input));
				// 获取客户端的信息
				String line = null;
				while ((line = bff.readLine()) != null) {
					response += line;
				}
				action = getCommAction(response);
				//发送返回信息
				sendResponse(socket,action,true);
				
				handleMsgFromStb(response);
				
				bff.close();
				input.close();
				socket.close();
				socket = null;
			} catch (IOException e) {
				sendResponse(socket,action,false);
				e.printStackTrace();
			}

		}
	}
}
