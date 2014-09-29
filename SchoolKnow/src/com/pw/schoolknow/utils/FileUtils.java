package com.pw.schoolknow.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import android.os.Environment;

public class FileUtils {
        private String SDPATH;       
        private int FILESIZE = 4 * 1024; 
        
        public String getSDPATH(){
                return SDPATH;
        }        
        public FileUtils(){
            //得到当前外部存储设备的目录( /SDCARD )
            SDPATH = Environment.getExternalStorageDirectory() + "/";
        }
        
        /**
         * 在SD卡上创建文件
         * @param fileName
         * @return
         * @throws IOException
         */
        public File createSDFile(String fileName) throws IOException{
                File file = new File(SDPATH + fileName);
                file.createNewFile();
                return file;
        }
        
        /**
         * 在SD卡上创建目录
         * @param dirName
         * @return
         */
        public File createSDDir(String dirName){
                File dir = new File(SDPATH + dirName);
                dir.mkdir();
                return dir;
        }
        
        /**
         * 创建路径
         * @param dirPath
         * @return
         */
        public static File createPath(String dirPath){
        	File dir = new File(dirPath);
        	if(!dir.exists()){
        		dir.mkdir();
        	}
            return dir;
        }
        
        /**
         * 判断SD卡上的文件夹是否存在
         * @param fileName
         * @return
         */
        public boolean isFileExist(String fileName){
                File file = new File(SDPATH + fileName);
                return file.exists();
        }
        
        /**
         * 将一个InputStream里面的数据写入到SD卡中
         * @param path
         * @param fileName
         * @param input
         * @return
         */
        public File write2SDFromInput(String path,String fileName,InputStream input){
                File file = null;
                OutputStream output = null;
                try {
                        createSDDir(path);
                        file = createSDFile(path + fileName);
                        output = new FileOutputStream(file);
                        byte[] buffer = new byte[FILESIZE];
                        while((input.read(buffer)) != -1){
                                output.write(buffer);
                        }
                        output.flush();
                } 
                catch (Exception e) {
                        e.printStackTrace();
                }
                finally{
                        try {
                                output.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                return file;
        }
        
        //随机生成图片文件名
        public static String createImgFileName(){
        	Random rd=new Random();
        	int n=rd.nextInt(1000);
        	String temp= System.currentTimeMillis()+n+"";
        	return new Sha1Util().getDigestOfString(temp.getBytes())+".jpg";
        }

}
