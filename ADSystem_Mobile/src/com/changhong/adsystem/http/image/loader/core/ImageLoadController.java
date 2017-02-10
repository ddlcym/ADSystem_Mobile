package com.changhong.adsystem.http.image.loader.core;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import java.io.File;
import java.util.concurrent.Executor;

import com.changhong.adsystem.http.image.loader.cache.DiskCache;
import com.changhong.adsystem.http.image.loader.task.ImageDownloadTask;
import com.changhong.adsystem.http.image.loader.task.TaskExecutorFactory;
import com.changhong.adsystem.utils.ServiceConfig;
import com.changhong.common.utils.StringUtils;


public class ImageLoadController {

    private static final String TAG = "ImageLoadController";

    /**
     * image loader controller instance
     */
    private static ImageLoadController instance;

    /**
     * disk cache provider
     */
    private DiskCache diskCache;

    /**
     * image data download task executor
     */
    private Executor downloadTaskExecutor ;

    public static ImageLoadController getInstance() {
        if (instance == null) {
            synchronized (ImageLoadController.class) {
                if (instance == null) {
                    instance = new ImageLoadController();
                }
            }
        }
        return instance;
    }

    public void initConfiguration(ImageLoaderConfigure configuration) {
        configuration.initAllEmptyFields();
        this.diskCache = configuration.getDiskCache();
        this.downloadTaskExecutor = TaskExecutorFactory.createdDownloadExecutor();
    }

    /****************************************************image load part**********************************************/

    /**
     * this is entrence for image is exist
     * <p>
     *
     * @param imageUri the url for http image
     */
    public boolean imageIsExist(String imageUri) {
        /**
         * according to image url check is cached on disk
         */
        File cachedFile = diskCache.getDiskCachedFile(imageUri);
        return (cachedFile != null);
    }

    /**
     * this method will do two things
     *
     * 1 - check the max number for disk file number is exceed, if exceed, delete oldest fifty
     * 1 - image download task execute
     */
    public void gotoDownloadWay(Handler handler, boolean orignalImage, String imageUri) {
        if (StringUtils.hasLength(imageUri)) {
            ImageDownloadTask task = new ImageDownloadTask(handler,ServiceConfig.IMAGE_DOWNLOAD_FINISHED,  orignalImage, imageUri);
            downloadTaskExecutor.execute(task);
        }
    }

   
}
