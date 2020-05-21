package com.app.jzapp.videoapps.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    /**
     * 返回缓存文件夹
     */
    public static File getCacheFile(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = null;
            file = context.getExternalCacheDir();//获取系统管理的sd卡缓存文件
            if (file == null) {//如果获取的为空,就是用自己定义的缓存文件夹做缓存路径
                file = new File(getCacheFilePath(context));
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            return file;
        } else {
            return context.getCacheDir();
        }
    }

    /**
     * 获取自定义缓存文件地址
     *
     * @param context
     * @return
     */
    public static String getCacheFilePath(Context context) {
        String packageName = context.getPackageName();
        return "/mnt/sdcard/" + packageName;
    }

    private static String SDPATH = "";




//    public static String getSDPath() {
//        File sdDir = null;
//        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
//        if (sdCardExist) {
//            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
//        }
//        return sdDir.toString();
//    }
//    /**
//     * 获取到sd卡的根目录，并以String形式返回
//     *
//     * @return
//     */
//    public static String getSDCardPath() {
//        SDPATH = Environment.getExternalStorageDirectory() + "/";
//        return SDPATH;
//    }
//
//    /**
//     * 创建文件或文件夹
//     *
//     * @param fileName 文件名或问文件夹名
//     */
//    public static void createFile(String fileName) {
//        File file = new File(getSDPath() + fileName);
//        if (fileName.indexOf(".") != -1) {
//            // 说明包含，即使创建文件, 返回值为-1就说明不包含.,即使文件
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            System.out.println("创建了文件");
//        } else {
//            // 创建文件夹
//            file.mkdir();
//            System.out.println("创建了文件夹");
//        }
//
//    }
}
