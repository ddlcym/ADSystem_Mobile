package com.changhong.adsystem.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;
import android.os.Environment;
import android.util.Log;

public class FileUtil {

	private String SDCARDPATH = "";
	/**
	 * 广告策略文件路径
	 */
	public static final String FILE_PATH = "baseFile";
	/**
	 * 资源文件路径
	 */
	public static final String IMAGE_PATH = "res";

	// 文件读取Buffer size
	private static final int BUFFER_SIZE = 4 * 1024; // 4K

	public final static int MAX_FILE_ITEM_SIZE = 10;

	public final static int DELETE_ITEM_SIZE = 2;

	public FileUtil() {
		SDCARDPATH = getSDCARDPATH() + File.separator;
	}

	/**
	 * 获取sdcard根目录
	 * 
	 * @return SDcard根目录路径
	 */
	public String getSDCARDPATH() {
		boolean exist = isSdCardExist();
		String sdpath = "";
		if (exist) {
			sdpath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		} else {
			sdpath = "不适用";
		}
		return sdpath;
	}

	/**
	 * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
	 * 
	 * @return
	 */
	public static boolean isSdCardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 在SDcard上创建文件
	 * 
	 * @param fileName
	 * @return File
	 */
	public File creatSDFile(String fileName) {

		String dirPath = "";
		String[] tempPath = fileName.split(File.separator);
		if (tempPath.length > 2) {
			for (int i = 0; i < tempPath.length - 1; i++) {
				dirPath += File.separator + tempPath[i];
				if (null != dirPath && !dirPath.isEmpty()) {
					createSDDir(dirPath);
				}
			}
		}
		File file = new File(fileName);
		return file;
	}

	/**
	 * 在SDcard上创建文件
	 * 
	 * @param fileName
	 * @return File
	 */
	public File creatSDFileA(String fileName) {
		File file = new File(SDCARDPATH + fileName);
		return file;
	}

	/**
	 * 在SDcard上创建目录
	 * 
	 * @param dirName
	 */
	public void createSDDir(String dirName) {
		File dir = new File(dirName);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return boolean
	 */
	public boolean isFileExist(String fileName) {

		String path = IMAGE_PATH;
		// 根据文件类型修改保存路径
		fileName = path + File.separator + fileName;

		File file = new File(SDCARDPATH + fileName);
		return file.exists();
	}

	/**
	 * @param path
	 *            存放目录
	 * @param fileName
	 *            文件名字
	 * @param input
	 *            数据来源
	 * @return
	 */
	public long writeToSDCard(String fileName, InputStream input) {

		int byteCount = 0;
		File file = null;
		BufferedInputStream in = null;
		BufferedOutputStream output = null;

		try {

			// 创建文件
			file = creatSDFile(fileName);
			in = new BufferedInputStream(input, BUFFER_SIZE);
			output = new BufferedOutputStream(new FileOutputStream(file),
					BUFFER_SIZE);
			// 以4K为单位，每次写4k
			byte buffer[] = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			// 清除缓存
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				if (null != output) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return byteCount;
	}

	/**
	 * @param fileName
	 *            文件名字
	 * @param content
	 *            保存内容
	 * @return
	 */
	public long writeToSDCard(String fileName, String content) {

		int byteCount = 0;
		File file = null;
		FileOutputStream outStream = null;
		OutputStreamWriter writer = null;
		try {
			fileName = fileName.trim();
			// 创建文件
			file = creatSDFile(fileName);
			outStream = new FileOutputStream(file);
			writer = new OutputStreamWriter(outStream, "utf-8");
			writer.write(content);
			writer.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				if (null != writer) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				// 关闭流
				if (null != outStream) {
					outStream.close();
					outStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return byteCount;
	}

	/**
	 * 读取指定路径的文件
	 * 
	 * @param fileName
	 * @return
	 */
	public String readFileFromSDCard(String filePath) {
		BufferedReader bufRead = null;
		StringBuffer strBuffer = new StringBuffer();
		try {
			File file = new File(filePath);
			bufRead = new BufferedReader(new FileReader(file));
			String readline = "";
			while ((readline = bufRead.readLine()) != null) {
				strBuffer.append(readline);
			}
			System.out.println("读取成功：" + strBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != bufRead) {
					bufRead.close();
					bufRead = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return strBuffer.toString();
	}

	/**
	 * 从SDcard中删除指定的文件
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public boolean removeFileFromSDCard(String filePath) {
		boolean reValue = false;
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
			reValue = true;
		}
		return reValue;
	}

	/**
	 * 重命名文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param newName
	 *            新的文件名
	 * @return true=重命名成功； false=重命名失败
	 */
	public boolean reNameFile(String filePath, String newName) {
		boolean reValue = false;
		File file = new File(filePath);
		if (file.exists()) {
			String newFilePath = getNewFilePath(filePath, newName);
			File newFile = new File(newFilePath);
			reValue = file.renameTo(newFile);
		}
		return reValue;
	}

	/**
	 * 根据新的文件名，重新获取文件路径
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public String getNewFilePath(String filePath, String newName) {
		String newPath = "";
		int startIndex = filePath.lastIndexOf(File.separator);
		int endIndex = filePath.lastIndexOf(".");
		if (startIndex > 0 && endIndex > (startIndex + 1)) {
			String oldName = filePath.substring(startIndex + 1, endIndex);
			if (StrLength(oldName) > 0) {
				newPath = filePath.replace(oldName, newName);
			}
		}
		return newPath;
	}

	/**
	 * 根据httpdownLoad文件URL，获取文件本地保存路径
	 * 
	 * @param fileURL
	 *            *文件远程定位符
	 * @return 本地保存文件路径
	 */
	public String convertHttpUrlToLocalFilePath(String fileUrl) {

		String localFilePath = Configure.adResFilePath + File.separator
				+ fileUrl;
		return localFilePath;
	}

	public String getFileName(String filePath) {
		String fileName = "";
		int startIndex = filePath.lastIndexOf(File.separator);
		int endIndex = filePath.lastIndexOf(".");
		if (startIndex > 0 && endIndex > (startIndex + 1)) {
			fileName = filePath.substring(startIndex + 1);
		}
		return fileName;
	}

	public String getFileNameWithoutSubfix(String filePath) {
		String fileName = "";
		int startIndex = filePath.lastIndexOf(File.separator);
		int endIndex = filePath.lastIndexOf(".");
		if (startIndex > 0 && endIndex > (startIndex + 1)) {
			fileName = filePath.substring(startIndex + 1, endIndex);
		}
		return fileName;
	}

	private int StrLength(String str) {
		int reLength = 0;
		if (null != str && str.length() > 0) {
			reLength = str.length();
		}
		return reLength;
	}

	/**
	 * 检查文件是否超过设定文件个数，超过，则删除2个文件
	 * 
	 * @param fileType
	 *            文件类型
	 */
	public void checkMaxFileItemExceedAndProcess() {

		File fileList = new File(getSDCARDPATH() + File.separator + IMAGE_PATH);
		String[] list = fileList.list();
		if (list != null && list.length > MAX_FILE_ITEM_SIZE) {
			Log.e("FILE_DELETE", "now small picture number is  " + list.length);
			int alreadyDeleteNumber = 0;
			File[] files = fileList.listFiles();
			Arrays.sort(files, new FileComparator());

			for (File file : files) {
				try {
					if (!file.isDirectory()) {
						file.delete();
						alreadyDeleteNumber++;
						if (alreadyDeleteNumber >= DELETE_ITEM_SIZE) {
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}

	static class FileComparator implements Comparator<File> {
		@Override
		public int compare(File f1, File f2) {
			Long last1 = f1.lastModified();
			Long last2 = f2.lastModified();
			return last1.compareTo(last2);
		}
	}

	public String getLocalFileDir() {
		return SDCARDPATH + IMAGE_PATH + File.separator;
	}

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
