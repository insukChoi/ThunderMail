package com.mail.thread;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;

import com.mail.common.DeamonSql;
import com.mail.common.DropLog;
import com.mail.common.ResultMap;
import com.mail.common.StringUtil;
import com.mail.common.WSqlSessionManager;
import com.mail.daoimpl.MailDBImpl;

public class ThunderMailToTaxbillThread implements Runnable{

	private boolean flag = false;
	private static boolean orderByFlag = false;   // 0은 ASC , 1은 DESC
	
	/*
	 *  ThunderMailToTaxbill 쓰레드 역할
	 *  1. [Taxbill Side] etaxbill_snd_mail 테이블에서 SND_YN 값이 P 인것을 조회
	 *  2. [ThunderMail Side] tm6_part_automail_sendresult 테이블에서 smtpCode , smtpMsg 조회
	 *  3. [Taxbill Side] 
	 *    1) smtpCode 코드가 정상이면  , etaxbill_snd_mail 의 SND_YN = Y , etaxbill_bizmail 의 SND_STAT = 01 (정상전송) Update
	 *    2) smtpCode 코드가 비정상이면, etaxbill_snd_mail 의 SND_YN = Y , etaxbill_bizmail 의 SND_STAT = 02 (전송실패) Update
	 */
	public void run() {
		try {
            while (!flag) {
				Thread.sleep(30000);
				
				if(orderByFlag){
					orderByFlag = !orderByFlag;
				}else{
					orderByFlag = !orderByFlag;
				}
				
            	DeamonSql ds      = new DeamonSql();
            	ResultMap resMap  = new ResultMap();
            	DropLog   dropLog = new DropLog();
            	boolean boolCd    = false;
                ResultMap infoMap = null;
                
            	resMap = getSendedMailSelect();
            	resMap.beforeFirst();
            	
            	int taskCount = resMap.size();
            	
            	SqlSession sqlSessionOracle = WSqlSessionManager.getOracleSqlSession().openSession();
            	SqlSession sqlSessionMaria  =  WSqlSessionManager.getMariaSqlSession().openSession();
            	
            	long start = System.currentTimeMillis(); // 시작시간 
            	
                while (resMap.next()) { //맵에 담겨있는 리스트만큼 반복
                	if ("P".equals(StringUtil.null2void(resMap.getString("SND_YN")))) {
                		infoMap = new ResultMap();
	                	infoMap = ds.getSendedMailInfo(resMap, sqlSessionMaria);
	                	infoMap.first();
	                	
	                	if(infoMap.size() == 0){ //아직 전송되지 않은 이메일일 경우 continue;
	                		continue;
	                	}else{
	                		boolCd = ds.setTaxMailInfoUpdate(resMap, StringUtil.null2void(infoMap.getString("failCauseCode")), StringUtil.null2void(infoMap.getString("failcauseTypeName")), sqlSessionOracle );
	                		if(!boolCd){
	                			dropLog.write_file("Taxbill 결과값 Update 오류 - sendMail : ISSU_ID[" + StringUtil.null2void(resMap.getString("ISSU_ID")) + "]");
	                		}
	                	}
                	}
				}
                sqlSessionOracle.close();
                sqlSessionMaria.close();
                
                long end = System.currentTimeMillis();  //종료시간
                
                //현재시간
                long time = System.currentTimeMillis(); 
        		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        		String str = dayTime.format(new Date(time));
        		
            	dropLog.write_file(str+" : 2 ThunderMail To Taxbill [task count ="+taskCount+"] total time = "+(end-start)+" milliseconds");
            }
        } catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ThreadDeath ouch) {
			ouch.printStackTrace();
            //throw(ouch);
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {}
	}
	
	public static synchronized ResultMap getSendedMailSelect(){
		ResultMap outMap    = new ResultMap();
		MailDBImpl mailImpl = new MailDBImpl();
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		String yesterday     = fmt.format(new Date(new Date().getTime() - 1000 * 60 * 60 * 24));
		String today         = fmt.format(new Date(new Date().getTime()));
		
		try {
            SqlSession sqlSession = WSqlSessionManager.getOracleSqlSession().openSession(ExecutorType.REUSE , false);
            
			HashMap requestMap = new HashMap();
			requestMap.clear();
			requestMap.put("SND_YN"    ,"P");
			requestMap.put("INS_DATE_S",yesterday);
			requestMap.put("INS_DATE_E",today);
			
			if(orderByFlag){
				requestMap.put("ORDERBYFLAG","ASC");
			}else{
				requestMap.put("ORDERBYFLAG","DESC");
			}

			//ETAXBILL_SND_MAIL 에서 SND_YN = P 조회
			outMap.setList(mailImpl.sqlSelect(sqlSession, "Taxbill.TAXBILL_SNDMAIL_R001", requestMap));
			sqlSession.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return outMap;
	}
}
