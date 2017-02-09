package com.changhong.adsystem.http;

import java.io.IOException;
import java.net.ServerSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.changhong.adsystem.utils.Configure;
import com.changhong.adsystem.utils.ServiceConfig;

public class FileDownLoadService {

	private static final String Tag = "FileDownLoadService::";
	private static FileDownLoadService intance;
	private ServerSocket mServerSocket;
	private int DEFAULT_PORT = 20147;
	private Handler mParentHandler;
	private Handler mMsgHandler;

	// 文件下载接收服务器：
	private Thread mFileDownLoadThread = null;

	private FileDownLoadService() {
		initFileDownLoadService();
	}

	public static FileDownLoadService creatFileEditServer() {
		if (null == intance) {
			intance = new FileDownLoadService();
		}
		return intance;
	}

	/**
	 * 初始化全局变量，启动那个服务器
	 */
	private void initFileDownLoadService() {

		// 启动接收线程:
		FileDownLoadTask commThread = new FileDownLoadTask();
		mFileDownLoadThread = new Thread(commThread);
		mFileDownLoadThread.start();

	}

	/**
	 * 接收端返回的信息
	 * 
	 * @return
	 */
	public void communicationWithServer(Handler handler, int communicationType,
			String param) {

		mParentHandler = handler;
		// 发送消息给子线程
		if (null != mMsgHandler) {
			Message sendMsg = mMsgHandler.obtainMessage();
			sendMsg.what = communicationType;
			sendMsg.obj = param;
			mMsgHandler.sendMessage(sendMsg);
		}
	}

	public void close() {
		try {
			if (null != mServerSocket) {
				mServerSocket.close();
				mServerSocket = null;
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/********************************************************** clientCommunicationThread *******************************************************************/

	private class FileDownLoadTask implements Runnable {

		int communicationType;

		@Override
		public void run() {

			// 创建handler之前先初始化Looper
			Looper.prepare();

			mMsgHandler = new Handler() {

				public void handleMessage(Message msg) {

					communicationType = msg.what;
					// 接收来之通讯线程的消息
					switch (communicationType) {

					case ServiceConfig.ACTION_HTTP_DOWNLOAD:
						String downLoadResult;
						String fileUrl = (String) msg.obj;
						if (fileUrl.toLowerCase().startsWith("http://")
								|| fileUrl.toLowerCase().startsWith("https://")) {
							try {
								downLoadResult = HttpDownloader
										.download(fileUrl);
								Message respondMsg = mParentHandler
										.obtainMessage();
								respondMsg.what = ServiceConfig.SHOW_ACTION_RESULT;
								Bundle bundle = new Bundle();
								bundle.putString("action", "copy");
								if (downLoadResult
										.equals(ServiceConfig.ACTION_SUCCESS)) {
									bundle.putString("result",
											"文件下载成功：服务器==>>手机");
								} else if (downLoadResult
										.equals(Configure.FILE_EXIST)) {
									bundle.putString("result", "文件已存在");
								} else if (downLoadResult
										.equals(Configure.FILE_LARGE)) {
									bundle.putString("result", "文件超大");
								} else {
									bundle.putString("result",
											"下载失败：文件不存在或网络异常");
								}
								respondMsg.setData(bundle);
								mParentHandler.sendMessage(respondMsg);
								Log.e(Tag, "finish download file " + fileUrl);
							} catch (Exception e) {
								e.printStackTrace();
								mParentHandler.sendEmptyMessage(ServiceConfig.ACTION_P2P_ERROR);
							}
						}
						break;
					default:
						break;

					}
					;

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					Looper.loop();
				}
			};
		}
	}

	/**
	 * 特殊字符还原
	 * 
	 * @param url
	 *            url字符串
	 * @return
	 */
	public String convertHttpURLToFileUrl(String url) {
		if (null != url && url.length() > 0) {
			return url.replace("%25", "%").replace("%20", " ")
					.replace("%2B", "+").replace("%23", "#")
					.replace("%26", "&").replace("%3D", "=")
					.replace("%3F", "?").replace("%5E", "^");
		}
		return url;
	}

}
