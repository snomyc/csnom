package com.snomyc.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
/**
 * 删除文件夹及文件操作
 * **/
public class FileUtils {
	
	/**
	* 删除文件夹及文件夹下的所有文件filepath结尾不需带上/
	**/
	public static boolean deletePaperFiles(String filepath){
		
		//创建一个文件对象
		File file = new File(filepath);
		
		//判断是文件还是文件夹
		if(!file.isDirectory()) {
			//是文件则删除
			//System.out.println("删除文件"+file.getName()+"成功!");
			file.delete();
			return true;
		}else {
			//是目录，获得该目录下的所有文件列表
			File[] files = file.listFiles();
			for(int i=0; i<files.length; i++) {
				
				File delfile = new File(filepath+"\\"+files[i].getName());
				//System.out.println(delfile.getPath());
				
				if(!delfile.isDirectory()) {
					//是文件
					//System.out.println("删除文件"+delfile.getName()+"成功!");
					delfile.delete();
				}else {
					
					deleteFiles(delfile.getPath());
					//删除目录
					//System.out.println("删除目录为:"+delfile.getPath());
					delfile.delete();
				}
				
			}
			
		}
		
		//当所有文件都删除完毕，然后删除当前文件夹 如果不要file.delete()则是删除文件夹下的所有文件
		//System.out.println("删除总目录:"+file.getPath()+"成功!");
		file.delete();
		return true;
	}
	
	/**
	* 删除文件夹下的所有文件不包括文件夹filepath结尾不需带上/
	**/
	public static boolean deleteFiles(String filepath){
		//创建一个文件对象
		File file = new File(filepath);
		//判断是文件还是文件夹
		if(!file.isDirectory()) {
			//是文件则删除
			file.delete();
			return true;
		}else {
			//是目录，获得该目录下的所有文件列表
			File[] files = file.listFiles();
			for(int i=0; i<files.length; i++) {
				File delfile = new File(filepath+"\\"+files[i].getName());
				if(!delfile.isDirectory()) {
					//是文件 删除
					delfile.delete();
				}else {
					
					deleteFiles(delfile.getPath());
					//删除目录
					delfile.delete();
				}
			}
		}
		return true;
	}
	
	
	/***
	 * 复制文件(单个文件)且不能复制文件夹
	 * start:需要复制的文件地址
	 * end:需要复制到哪个位置的路径结尾不需带上/
	 * **/
	public static boolean CopyFile(String start, String end) {  
		File source = new File(start);
		File target = new File(end);
		if(!source.exists() || !target.isDirectory()) {
			return false;
		}
		if(source.isDirectory()) {
			return false;
		}
		target = new File(end+"\\"+source.getName());
	    FileChannel in = null;  
	    FileChannel out = null;  
	    FileInputStream inStream = null;  
	    FileOutputStream outStream = null;  
	    try {  
	        inStream = new FileInputStream(source);  
	        outStream = new FileOutputStream(target);  
	        in = inStream.getChannel();  
	        out = outStream.getChannel();  
	        in.transferTo(0, in.size(), out);  
	        
			inStream.close();
			in.close();
	    	outStream.close();
	    	out.close();
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
	    return true;
	}
	
	/***
	 * 剪切文件(单个文件)
	 * start:需要剪切的文件地址
	 * end:需要剪切到哪个位置的路径结尾不需带上/
	 * **/
	public static boolean CutFile(String start, String end) {  
		File source = new File(start);
		File target = new File(end);
		if(!source.exists() || !target.isDirectory()) {
			return false;
		}
		if(source.isDirectory()) {
			return false;
		}
		target = new File(end+"\\"+source.getName());
	    FileChannel in = null;  
	    FileChannel out = null;  
	    FileInputStream inStream = null;  
	    FileOutputStream outStream = null;  
	    try {  
	        inStream = new FileInputStream(source);  
	        outStream = new FileOutputStream(target);  
	        in = inStream.getChannel();  
	        out = outStream.getChannel();  
	        in.transferTo(0, in.size(), out);  
	        
	        inStream.close();
			in.close();
	    	outStream.close();
	    	out.close();
	    	
	    	//删除原文件
	    	source.delete();
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
	    return true;
	}
	
	
	public static void main(String[] args) {
		
	}
}
