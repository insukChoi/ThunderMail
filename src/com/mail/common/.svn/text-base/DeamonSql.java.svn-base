package com.mail.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;

import com.mail.daoimpl.MailDBImpl;

public class DeamonSql {

	DropLog dropLog = null;

	public DeamonSql() {
		dropLog = new DropLog();
	}

	/*
	 * Description : 데몬메일 PID PYN인지 ? N이면 P로 변경
	 * Parameter   : void
	 * Return Type : ResultMap
	 */
	public ResultMap mail_select_yn() {
		ResultMap outMap = new ResultMap();
		ResultMap resMap = new ResultMap();
		SqlSession sqlSession = null;
		int resultCd = 0;
		
		try {
			dropLog.delLog();

			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
			String yesterday     = fmt.format(new Date(new Date().getTime() - 1000 * 60 * 60 * 24));
			String today         = fmt.format(new Date(new Date().getTime()));

			MailDBImpl mailImpl = new MailDBImpl();
			sqlSession = WSqlSessionManager.getOracleSqlSession().openSession();

			HashMap requestMap = new HashMap();
			requestMap.clear();
			requestMap.put("SND_YN"    ,"N");
			requestMap.put("INS_DATE_S",yesterday);
			requestMap.put("INS_DATE_E",today);

			//ETAXBILL_SND_MAIL 조회
			outMap.setList(mailImpl.sqlSelect(sqlSession, "Taxbill.TAXBILL_SNDMAIL_R000", requestMap));
			outMap.beforeFirst();

			while (outMap.next()) {
				if (!"".equals(StringUtil.null2void(outMap.getString("ISSU_ID")))){
					requestMap.clear();
					requestMap.put("ISSU_ID"    ,StringUtil.null2void(outMap.getString("ISSU_ID").trim()));
					requestMap.put("SEQ_NO"     ,StringUtil.null2void(outMap.getString("SEQ_NO").trim()));
					requestMap.put("SND_TYPE1"  ,"EML");
					requestMap.put("SND_TYPE2"  ,"SYS");

					//ETAXBILL_BIZMAIL 조회
					resMap.setList(mailImpl.sqlSelect(sqlSession, "Taxbill.TAXBILL_BIZMAIL_R002", requestMap));
					resMap.first();

					outMap.putMap("SND_STAT"    , StringUtil.null2void(resMap.getString("SND_STAT")));
					outMap.putMap("USE_MON"     , StringUtil.null2void(resMap.getString("USE_MON")));
					outMap.putMap("USE_DATETIME", StringUtil.null2void(resMap.getString("USE_DATETIME")));
					outMap.putMap("RCV_GB"      , StringUtil.null2void(resMap.getString("RCV_GB")));
					outMap.putMap("SND_GB"      , StringUtil.null2void(resMap.getString("SND_GB")));
					outMap.putMap("CORP_NO"     , StringUtil.null2void(resMap.getString("CORP_NO")));
					outMap.putMap("USER_ID"     , StringUtil.null2void(resMap.getString("USER_ID")));
					outMap.putMap("SND_TYPE"    , StringUtil.null2void(resMap.getString("SND_TYPE")));
				}
				if ("N".equals(outMap.getString("SND_YN"))) {
					requestMap.clear();
					requestMap.put("SND_YN"     ,"P");
					requestMap.put("ISSU_ID"    ,StringUtil.null2void(outMap.getString("ISSU_ID")));
					requestMap.put("SEQ_NO"     ,StringUtil.null2void(outMap.getString("SEQ_NO")));
					requestMap.put("SND_DATE"  ,StringUtil.getDate("YYYYMMDD"));
					requestMap.put("SND_TIME"  ,StringUtil.getDate("HH24MISS"));

					//ETAXBILL_SND_MAIL의 SND_YN을 'P'로 업데이트
					resultCd = mailImpl.sqlUpdate(sqlSession, "Taxbill.TAXBILL_SNDMAIL_U001", requestMap);
				}
				if (resultCd > 0) {
					outMap.putMap("SND_YN", "P");
					outMap.putMap("SND_DATE"  ,StringUtil.getDate("YYYYMMDD"));
					outMap.putMap("SND_TIME"  ,StringUtil.getDate("HH24MISS"));
					mailImpl.sqlSessionCommit(sqlSession);
				} else {
					mailImpl.sqlSessionRollback(sqlSession);
				}
			}
		}catch(Exception e) {
			dropLog.write_file(e.toString());
		} finally {
			sqlSession.close();
		}
		return outMap;
	}
	
	

	/*
	 * Description : Maria DB에서 전송된 값의 코드를 가져온다.
	 * Parameter   : ResultMap
	 * Return Type : ResultMap
	 */
	public ResultMap getSendedMailInfo(ResultMap infoMap, SqlSession sqlSession){
		ResultMap outMap    = new ResultMap();
		MailDBImpl mailImpl = new MailDBImpl();

		try {
			outMap = sts_mstr(infoMap); // SELR_CORP_NM 구하기 위해

			HashMap requestMap = new HashMap();
			requestMap.clear();
			requestMap.put("ISSU_ID",StringUtil.null2void(infoMap.getString("ISSU_ID")));
			requestMap.put("SEQ_NO",StringUtil.null2void(infoMap.getString("SEQ_NO")));
			
			//보내진 메일 결과를 썬더메일에서 가져온다
			outMap.setList(mailImpl.sqlSelect(sqlSession, "ThunderMail.THUNDER_R001", requestMap));
		
			if(outMap.size() < 1){
				outMap.setList(mailImpl.sqlSelect(sqlSession, "ThunderMail.THUNDER_R002", requestMap));
			}else{
				outMap.first();
				if("1".equals(StringUtil.null2void(outMap.getString("smtpCodeType")))  ||  "3".equals(StringUtil.null2void(outMap.getString("smtpCodeType"))) || "4".equals(StringUtil.null2void(outMap.getString("smtpCodeType")))   ){ //retry 대상
					if(Integer.parseInt( StringUtil.null2zero( outMap.getString("retrySendedCount"))) < 3){
						outMap = new ResultMap();
					}
				}
			}
			
			
		} catch(Exception e){			
			dropLog.write_file("getSendedMailInfo (MariaDB 조회) 메소드 오류 [" + e.toString() + "]");
		}
		return outMap;
	}
	
	/*
	 * Description : 메일을 보내기 위해 썬더메일 DB에 Insert
	 * Parameter   : int , String[] , ResultMap
	 * Return Type : boolean
	 */

	public boolean insertMailToThunder(String mailTempletID, String[] mailContent, ResultMap inMap, SqlSession SqlSession){
		MailDBImpl mailImpl = new MailDBImpl();
		boolean bool_result = false;
		inMap.first();

		try {
			HashMap requestMap = new HashMap();
			requestMap.clear();
			requestMap.put("AUTOMAILID", mailTempletID);
			requestMap.put("MAILTITLE" , mailContent[0]);
			requestMap.put("CONTENT"   , StringUtil.replace(mailContent[1], "\n", "<br>"));
			requestMap.put("SND_ADDR"  , inMap.getString("SND_ADDR"));
			requestMap.put("USERNAME"  , inMap.getString("RES_USERNM"));
			requestMap.put("RES_ADDR"  , inMap.getString("RES_ADDR"));
			
			String oneToOneInfo = "[$MAILTITLE]Ð"+mailContent[0]+"æ"
								 +"[$USERNAME]Ð"+ inMap.getString("RES_USERNM")+"æ"
								 +"[$CONTENT]Ð"+ StringUtil.replace(mailContent[1], "\n", "<br>")+"æ"
								 +"[$LINKURL]Ð"+ mailContent[2]+"æ"
								 +"[$MAILTYPE]Ð"+ mailContent[3]+"æ"
								 +"[$LINKNAME]Ð"+ mailContent[4];
			
			requestMap.put("ONETOONEINFO", oneToOneInfo);
			requestMap.put("ISSU_ID", inMap.getString("ISSU_ID"));
			requestMap.put("SEQ_NO" , inMap.getString("SEQ_NO"));
			
			//tm6_automail_sendqueue 테이블에 메일 Insert
			SqlSession.clearCache();
			int intResultVal = mailImpl.sqlInsert(SqlSession, "ThunderMail.THUNDER_C001", requestMap);
			
			if(intResultVal > 0){
				bool_result = true;
				mailImpl.sqlSessionCommit(SqlSession);
			}else {
				mailImpl.sqlSessionRollback(SqlSession);
			}

		} catch(Exception e){
			dropLog.write_file("insertMailToThunder (MariaDB 등록) 메소드 오류 [" + e.toString() + "]");
		}
		
		return bool_result;
	}
	
	
	/*
	 * Description : 이메일 정상 또는 에러 전송 결과를 Maria DB에 Insert
	 * Parameter   : ResultMap, String, String
	 * Return Type : boolean
	 */
	public boolean setTaxMailInfoUpdate(ResultMap infoMap, String failCauseCode, String failcauseTypeName, SqlSession sqlSessionOracle){
		boolean bool_result = false;
		int intUpdateBizMailCd = 0;
		int intUpdateSndMailCd = 0;
		
		try {
			MailDBImpl mailImpl = new MailDBImpl();

			// ETAXBILL_BIZMAIL Update
			HashMap requestMap = new HashMap();
			requestMap.clear();
			if("".equals(failCauseCode)){  // 정상 전송일 경우
				requestMap.put("SND_STAT"   ,"01");
			}else{                       // 비정상 전송일 경우
				requestMap.put("SND_STAT"   ,"02");
			}
			requestMap.put("SND_ADDR"   ,infoMap.getString("SND_ADDR"));
			requestMap.put("ERR_CODE"   ,failCauseCode);    //정상일 경우는 code값 없음 ,  비정상일 경우는 실패 코드
			requestMap.put("ERR_MSG"    ,failcauseTypeName);//정상일 경우는 msg값 없음 ,  비정상일 경우는 실패 한글 메시지
			requestMap.put("ISSU_ID"    ,infoMap.getString("ISSU_ID"));
			requestMap.put("SEQ_NO"     ,infoMap.getString("SEQ_NO"));
			
			intUpdateBizMailCd = mailImpl.sqlUpdate(sqlSessionOracle, "Taxbill.TAXBILL_BIZMAIL_U001", requestMap);
			
			// ETAXBILL_SND_MAIL Update
			requestMap = new HashMap();
			requestMap.clear();

			requestMap.put("SND_YN"   ,"Y");
			requestMap.put("SND_ADDR"  ,infoMap.getString("SND_ADDR"));
			requestMap.put("SND_DATE"  ,StringUtil.getDate("YYYYMMDD"));
			requestMap.put("SND_TIME"  ,StringUtil.getDate("HH24MISS"));
			requestMap.put("ISSU_ID"   ,infoMap.getString("ISSU_ID"));
			requestMap.put("SEQ_NO"    ,infoMap.getString("SEQ_NO"));
			
			intUpdateSndMailCd = mailImpl.sqlUpdate(sqlSessionOracle, "Taxbill.TAXBILL_SNDMAIL_U002", requestMap);
			
			if(intUpdateBizMailCd > 0 && intUpdateSndMailCd > 0){
				bool_result = true;
				mailImpl.sqlSessionCommit(sqlSessionOracle);
			}else {
				mailImpl.sqlSessionRollback(sqlSessionOracle);
			}
			
		} catch(Exception e){
			dropLog.write_file("썬더메일 결과 Taxbill365 DB 등록 오류 - ISSU_ID ["+infoMap.getString("ISSU_ID")+"] :  "+ e.toString());
		} finally {
		}
		return bool_result;
	}
	
	
	

	/*
	 * Description : 세금계산서 종류 리턴 메소드
	 * Parameter   : String
	 * Return Type : ResultMap
	 */
	public ResultMap getTaxType(String issu_id){
		ResultMap outMap      = new ResultMap();
		SqlSession sqlSession = null;

		try {
			MailDBImpl mailImpl = new MailDBImpl();
			sqlSession = WSqlSessionManager.getOracleSqlSession().openSession();

			HashMap requestMap = new HashMap();
			requestMap.clear();
			requestMap.put("ISSU_ID"    ,issu_id);

			//ETAXBILL_MSTR 테이블에서 TAX_TYPE 조회
			outMap.setList(mailImpl.sqlSelect(sqlSession, "Taxbill.TAXBILL_MSTR_R003", requestMap));
			
		} catch(Exception e){
			dropLog.write_file(e.toString());
		} finally {
			sqlSession.close();
		}
		return outMap;
	}

	
	/*
	 * Description : 해당 승인번호에 거래명세서 정보를 리턴한다.
	 * Parameter   : ResultMap
	 * Return Type : ResultMap
	 */
	public ResultMap sts_mstr(ResultMap inMap){

		ResultMap outMap      = new ResultMap();
		SqlSession sqlSession = null;
		
		try {
			MailDBImpl mailImpl = new MailDBImpl();
			sqlSession = WSqlSessionManager.getOracleSqlSession().openSession();

			HashMap requestMap = new HashMap();
			requestMap.clear();
			requestMap.put("ISSU_ID"    ,StringUtil.null2void(inMap.getString("ISSU_ID1")));
			requestMap.put("MSTR_YN"    ,"Y");

			//ETAXBILL_STS_MSTR, ETAXBILL_USER  테이블에서 SELR_CORP_NM 조회
			outMap.setList(mailImpl.sqlSelect(sqlSession, "Taxbill.TAXBILL_STS_R004", requestMap));
			
		} catch(Exception e) {
			dropLog.write_file(e.toString());
		} finally {
			sqlSession.close();
		}
		return outMap;
	}
	
	
	

}