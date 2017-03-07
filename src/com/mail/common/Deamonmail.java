package com.mail.common;

import java.io.FileReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.ibatis.session.SqlSession;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Deamonmail {

	// 데몬메일 전송
	public void deamonMailSend(ResultMap inMap, SqlSession sqlSession){ 
		
		TaxbillUtil taxUtil = new TaxbillUtil();
		DropLog dropLog     = new DropLog();
		String serp_yn      = "";   //전자발주서 Y,N
		String taxType      = "";   //세금계산서 종류

		serp_yn = (("email_0015_01.htm".equals(inMap.getString("TPL_NM"))) ? "Y" : "N");  //전자발주서 일 경우
		DeamonSql ds = new DeamonSql();
 
		if(!"email_0012_01.htm".equals(inMap.getString("TPL_NM"))){
			if(!"SYS".equals(inMap.getString("SND_TYPE"))){
				// 세금계산서 종류를 구한다.
				taxType = taxUtil.getStrTaxCode(inMap.getString("TAX_TYPE"));  //세금계산서 종류
			}
		}
		
		String[] mailContent = new String[5];

		mailContent[0] = StringUtil.null2void(inMap.getString("TITL"));			 	 // 메일제목
		mailContent[1] = StringUtil.null2void(inMap.getString("CONTS")); 			 // 메일내용
		mailContent[2] = StringUtil.null2void(inMap.getString("LINK_URL")); 		 // 링크URL
		if ("Y".equals(serp_yn)) {						 
			mailContent[3] = "전자발주 메일안내";			 // 버튼제목 = 전자발주서
		} else if("email_0012_01.htm".equals(inMap.getString("TPL_NM"))){
			mailContent[3] = "사이트 바로가기";			 	 // 버튼제목 = 사이트바로가기
		} else if("email_0013_01.htm".equals(inMap.getString("TPL_NM"))){
			mailContent[3] = "입금표 확인";			 		 // 버튼제목 = 입금표
		} else if("email_0017_01.htm".equals(inMap.getString("TPL_NM"))){
			mailContent[3] = "채권잔액확인서 확인";			 // 버튼제목 = 채권잔액확인서
		} else{
			mailContent[3] = taxType;						 // 버튼제목 = 기타
		}
		mailContent[4] = StringUtil.null2void(inMap.getString("BTN_NM"));		     // 버튼이미지링크

		// mailTemplet 에 따라 마리아DB에 Insert 쳐준다. => 새로운 컬럼에 ISSU_ID랑 SEQ_NO를 넣어준다.
		ResultMap paramMap = new ResultMap();
		paramMap.setFirst();
		paramMap.putMap("ISSU_ID"	, StringUtil.null2void(inMap.getString("ISSU_ID")));
		paramMap.putMap("SEQ_NO"	, StringUtil.null2void(inMap.getString("SEQ_NO")));
		paramMap.putMap("SND_ADDR"	, StringUtil.null2void(inMap.getString("SND_ADDR")));
		paramMap.putMap("RES_ADDR"	, StringUtil.null2void(inMap.getString("RES_ADDR")));
		paramMap.putMap("RES_USERNM", StringUtil.null2void(inMap.getString("USER_NM")));
		paramMap.putMap("INS_TIME"  , StringUtil.null2void(inMap.getString("INS_TIME")));
		paramMap.putMap("INS_DATE"  , StringUtil.null2void(inMap.getString("INS_DATE")));
		paramMap.putMap("USE_MON"   , StringUtil.null2void(inMap.getString("USE_MON")));
		paramMap.putMap("USE_DATETIME", StringUtil.null2void(inMap.getString("USE_DATETIME")));
		paramMap.setLast();	
		
		// 템플릿에 해당하는 automailID를 구한다.
		String mailTempletID = getMailTempletId(StringUtil.null2void(inMap.getString("TPL_NM")));

		// 썬더메일 Maria DB에 Insert 
		boolean boolCd =  ds.insertMailToThunder(mailTempletID, mailContent, paramMap, sqlSession);
		
		if(!boolCd){
			dropLog.write_file("썬더메일 등록 오류 - sendMail : ISSU_ID[" + StringUtil.null2void(inMap.getString("ISSU_ID")) + "]");
		}
	}

	// 거래 명세표 메일 전송 
	public ResultMap sndStsMail(ResultMap mailMap, SqlSession sqlSession){

		ResultMap resMap = new ResultMap();
		ResultMap outMap = new ResultMap();

		try {
			DeamonSql ds = new DeamonSql();
			outMap = ds.sts_mstr(mailMap); // SELR_CORP_NM 구한다.

			// 메일전송값 셋팅
			ResultMap inMap = new ResultMap();
			inMap.setFirst();
			inMap.putMap("ISSU_ID"	, mailMap.getString("ISSU_ID"));
			inMap.putMap("USER_ID"	, mailMap.getString("USER_ID"));			
			inMap.putMap("SND_ADDR", mailMap.getString("SND_ADDR"));
			inMap.putMap("RES_ADDR"	, mailMap.getString("RES_ADDR"));
			inMap.putMap("SEQ_NO"	, mailMap.getString("SEQ_NO"));
			inMap.putMap("USE_MON"	, mailMap.getString("USE_MON"));
			inMap.putMap("USE_DATETIME"	, mailMap.getString("USE_DATETIME"));
			inMap.putMap("INS_DATE"	, mailMap.getString("INS_DATE"));
			inMap.putMap("INS_TIME"	, mailMap.getString("INS_TIME"));
			inMap.setLast();
			
			if("2".equals(mailMap.getString("SND_GB")) ){  		//거래명세표일때
				String mailTempletID = getMailTempletId(StringUtil.null2void(mailMap.getString("TPL_NM")));
				
				String[] mailContent = new String[5];
				mailContent[0] = StringUtil.null2void(mailMap.getString("TITL"));			 // 메일제목
				mailContent[1] = StringUtil.null2void(mailMap.getString("CONTS")); 			 // 메일내용
				mailContent[2] = StringUtil.null2void(mailMap.getString("LINK_URL")); 		 // 링크URL					 
				mailContent[3] = "전자거래명세서";											 // 버튼제목 전자발주
				mailContent[4] = StringUtil.null2void(mailMap.getString("BTN_NM"));		     // 버튼이미지명
				
				//썬더메일 Maria DB에 Insert 한다.
				boolean boolCd =  ds.insertMailToThunder(mailTempletID, mailContent, inMap, sqlSession);
				
				if(!boolCd){
					DropLog dropLog = new DropLog();
					dropLog.write_file("썬더메일 등록 오류 - sendMail : ISSU_ID[" + StringUtil.null2void(inMap.getString("ISSU_ID")) + "]");
				}
			}

		} catch(Exception e){			
			DropLog dropLog = new DropLog();
			dropLog.write_file("이메일 발송 오류 [sndStsMail(ResultMap mailMap) Method] - sndMail : issu_id[" + StringUtil.null2void(mailMap.getString("ISSU_ID")) + "]" + e.toString());
		}
		return resMap;
	}
	
	// 메일 템플릿으로 썬더메일 ID를 구한다.
	public String getMailTempletId(String mailTemplet){
		DropLog dropLog = new DropLog();
		String templetName   = null;
		String mailTempletID = null;
		
		try{
			InputSource   is = new InputSource(new FileReader("/taxbill_module/ThunderMail/config/templetMngt.xml"));
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			
			// xpath 생성
			XPath  xpath = XPathFactory.newInstance().newXPath();
			String expression = "//*/templet";
			NodeList  cols = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);

			for( int idx=0; idx<cols.getLength(); idx++ ){
				templetName = cols.item(idx).getAttributes().item(0).getTextContent();
				
				if(templetName.equals(mailTemplet)){
					XPathExpression xPathExpression = xpath.compile("//ThunderMailTemplet//templets//templet[@name='" +templetName + "']//automailID");
					mailTempletID = xPathExpression.evaluate(document,XPathConstants.STRING).toString();
					break;
				}
			}
			
			if(mailTempletID == null){
				dropLog.write_file("메일컨텐트파일이 존재 하지 않습니다. ["+ mailTemplet + "]");
			}
		}catch(Exception e){
			dropLog.write_file("템플릿 파싱 오류 [sndStsMail(String mailTemplet) Method] " + e.toString());
		}

		return mailTempletID;
	}
}