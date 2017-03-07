package com.mail.main;

import com.mail.thread.TaxbillToThunderMailThread;
import com.mail.thread.ThunderMailToTaxbillThread;

public class Main {
	public static void main(String[] args){
		/*
		 *  TaxbillToThunderMail 쓰레드 역할
		 *  1. [Taxbill Side] etaxbill_snd_mail 테이블에서 SND_YN 값이 N 인것을 조회
		 *  2. [Taxbill Side] etaxbill_snd_mail 테이블에서 SND_YN 을 P로 Update
		 *  3. [ThunderMail Side] tm6_automail_sendqueue 테이블에 Insert
		 */
		Thread taxbillToThunderMail_thread = new Thread(new TaxbillToThunderMailThread());
		
		/*
		 *  ThunderMailToTaxbill 쓰레드 역할
		 *  1. [Taxbill Side] etaxbill_snd_mail 테이블에서 SND_YN 값이 P 인것을 조회
		 *  2. [ThunderMail Side] tm6_part_automail_sendresult 테이블에서 smtpCode , smtpMsg 조회
		 *  3. [Taxbill Side] 
		 *    1) smtpCode 코드가 정상이면  , etaxbill_snd_mail 의 SND_YN = Y , etaxbill_bizmail 의 SND_STAT = 01 (정상전송) Update
		 *    2) smtpCode 코드가 비정상이면, etaxbill_snd_mail 의 SND_YN = Y , etaxbill_bizmail 의 SND_STAT = 02 (전송실패) Update
		 */
		Thread thunderMailToTaxbill_thread = new Thread(new ThunderMailToTaxbillThread());
		 
		try {
			taxbillToThunderMail_thread.start();
			//thunderMailToTaxbill_thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
