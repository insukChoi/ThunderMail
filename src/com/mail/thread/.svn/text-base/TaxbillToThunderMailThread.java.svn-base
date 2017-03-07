package com.mail.thread;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;

import com.mail.common.DeamonSql;
import com.mail.common.Deamonmail;
import com.mail.common.DropLog;
import com.mail.common.ResultMap;
import com.mail.common.StringUtil;
import com.mail.common.WSqlSessionManager;

public class TaxbillToThunderMailThread implements Runnable{
	
	private boolean flag = false;
	/*
	 *  TaxbillToThunderMail 쓰레드 역할
	 *  1. [Taxbill Side] etaxbill_snd_mail 테이블에서 SND_YN 값이 N 인것을 조회
	 *  2. [Taxbill Side] etaxbill_snd_mail 테이블에서 SND_YN 을 P로 Update
	 *  3. [ThunderMail Side] tm6_automail_sendqueue 테이블에 Insert
	 */
	public void run() {
		try {
            while (!flag) {
				Thread.sleep(60000);
				
            	Deamonmail dm    = new Deamonmail();
                ResultMap resMap = new ResultMap();
                DropLog dropLog  = new DropLog();
                
            	resMap = getMailSelect();
            	resMap.beforeFirst();
            	
            	//dropLog.write_file("1 TaxbillToThunderMailThread task map size = "+resMap.size());
            	int taskCount = resMap.size();
            	SqlSession sqlSession = WSqlSessionManager.getMariaSqlSession().openSession();
            	
            	long start = System.currentTimeMillis(); // 시작시간 
                while (resMap.next()) { //맵에 담겨있는 리스트만큼 반복

                	if ("".equals(StringUtil.null2void(resMap.getString("PID"))) && "P".equals(StringUtil.null2void(resMap.getString("SND_YN"))) && "03".equals(resMap.getString("SND_STAT"))) {
	                	if ("email_0008_01.htm".equals(resMap.getString("TPL_NM"))) {
	                		dm.sndStsMail(resMap, sqlSession);       //거래명세서
						} else {
							dm.deamonMailSend(resMap, sqlSession);  //세금계산서 등
						}
                	}
				}

                long end = System.currentTimeMillis();  //종료시간
                
                //현재시간
                long time = System.currentTimeMillis(); 
        		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        		String str = dayTime.format(new Date(time));
        		
            	dropLog.write_file(str+" : 1 Taxbill To ThunderMail [task count ="+taskCount+"] total time = "+(end-start)+" milliseconds");
                
                sqlSession.close();
            }
        } catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ThreadDeath ouch) {
            throw(ouch);
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{

        }
	}
	
	public static synchronized ResultMap getMailSelect(){
		DeamonSql des   = new DeamonSql(); 
		ResultMap value = new ResultMap();
		
		try { 
			value = des.mail_select_yn(); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		return value;
	}

}
