package com.mail.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DropLog {
	private static String log_file_path = "/taxbill_module/ThunderMail/logs/";
	private static String curDateTime = null;

	/*
	 *  로그 경로에 Write
	 */
	public static void write_file(String log){
		curDateTime = new SimpleDateFormat("yyyyMMdd").format(new Date()).trim();	//current time
		
		BufferedWriter file = null;

		try{
			File dir = new File(log_file_path);		
			
			if(!dir.isDirectory()){
				dir.mkdirs();
			}

			file = new BufferedWriter(new FileWriter(log_file_path+"thunder_log_"+curDateTime.substring(4, 8), true));
			file.write(log); file.newLine();
			file.flush();
		}
		catch(IOException e){
			write_file(curDateTime+"==>write_file Exception[" + e + "]");
		}finally {			 
			if (file       != null) { try { file .close(); } catch (Exception e) {} }
		}
	}
	
	/*
	 *  한달 전 로그 삭제
	 */
	public void delLog() {
		try {
			String CurrMon = new SimpleDateFormat("MM").format(new Date()).trim();
			String LogPath	= log_file_path;		
			File	file = new File(LogPath);
			String[] FileList = file.list();
			if(file.isDirectory())
			{
				for( int i = 0 ; i < FileList.length ; i++) {
					if(FileList[i].substring(0,4).equals("mail")) {
						if(!CurrMon.equals(FileList[i].substring(9,11))) {
							File del = new File(LogPath+"/"+FileList[i]);
							
							del.delete();
							System.out.println(Integer.parseInt(LogPath+"/"+FileList[i])+" DEL!!");
						}
					}
				}		
			}
		} catch ( Exception e) {
			curDateTime = new SimpleDateFormat("yyyyMMdd").format(new Date()).trim();	//current time
			write_file(curDateTime+"==>Del Log Exception[" + e + "]");
		}
	}
}
