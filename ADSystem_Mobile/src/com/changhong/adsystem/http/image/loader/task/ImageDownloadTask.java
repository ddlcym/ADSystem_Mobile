package com.changhong.adsystem.http.image.loader.task;

import android.os.Handler;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.changhong.adsystem.http.RequestURL;
import com.changhong.adsystem.http.image.loader.cache.LruDiskCache;
import com.changhong.adsystem.http.image.loader.core.ImageHttpDownloader;
/**
 * this task is used for download for image for http server
 *
 */
public class ImageDownloadTask implements Runnable {

    private static final String TAG = "ImageLoadController";

    /**
     * handle which used for tell activity download is finish, you can display now
     */
    private final Handler handler;

    /**
     * message id for activity handle display image
     */
    private final int displayID;

    /**
     * the parameter which used for decide which http image is orignal image or just zoom in image
     */
    private final boolean orignalImage;

    /**
     * http image uuid
     */
    private final String imageUuid;
    
    /**
     * http image url
     */
    private final String imageUri;

    /**
     * thread id for current image downloading, because end user cna slide picture very fast and the system must kill
     * previous downloading threads, this parameter used for record current downloading
     */
    public static Long currentDownloadThreadID = -1L;

    /**
     * if this download is into sub download thread, this parameter is used for check all the sub task is finished or not
     * AtomicInteger is thread safe class which can grantee the result just change one time by one thread
     */
    public static Map<String, AtomicInteger> subCurrentDownloadThreadCounter = new HashMap<String, AtomicInteger>();

    public ImageDownloadTask(Handler handler, int displayID, boolean orignalImage,String uuid,  String imageUri) {
        this.handler = handler;
        this.displayID = displayID;
        this.orignalImage = orignalImage;
        this.imageUuid=uuid;
        this.imageUri = imageUri;
    }

    @Override
    public void run() {
        //tell controller manager current thread id for downloading, other thread should kill off
        if (orignalImage) {
            ImageDownloadTask.currentDownloadThreadID = Thread.currentThread().getId();
        }

        //download the image from http to local disk
        if (null != imageUuid && !imageUuid.isEmpty()) {
            try {
                ImageHttpDownloader.getStreamFromNetwork(orignalImage, imageUuid,imageUri);
                LruDiskCache.checkMaxFileItemExceedAndProcess();
                if (handler != null && displayID > 0) {
                    handler.sendEmptyMessage(displayID);
                }
                Log.e(TAG, "finish download image " + imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
