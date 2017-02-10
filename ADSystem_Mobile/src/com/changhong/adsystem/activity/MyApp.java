package com.changhong.adsystem.activity;

import java.io.File;

import com.changhong.adsystem.http.image.loader.core.ImageLoadController;
import com.changhong.adsystem.http.image.loader.core.ImageLoaderConfigure;
import com.changhong.adsystem.http.image.loader.utils.RepositoryUtils;

import android.app.Application;

public class MyApp extends Application {
	private static MyApp instance;
	public static File imageDownloadRootPath;

	public static MyApp getContext() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		/**
		 * 设置图片下载的路径
		 */
		imageDownloadRootPath = RepositoryUtils.getCacheDirectory(this);
		ImageLoaderConfigure configure = new ImageLoaderConfigure();
		ImageLoadController.getInstance().initConfiguration(configure);
	}

}
