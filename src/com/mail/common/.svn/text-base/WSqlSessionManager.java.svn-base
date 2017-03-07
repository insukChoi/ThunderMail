package com.mail.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class WSqlSessionManager {
	private static Reader reader = null;
	private static SqlSessionFactory oracle_Factory;
	private static SqlSessionFactory maria_Factory;			

	
	public static SqlSessionFactory getOracleSqlSession () throws FileNotFoundException {
		if(oracle_Factory == null){
			reader = new BufferedReader(new FileReader(new File("/taxbill_module/ThunderMail/config/myBatisConfig.xml")));
			oracle_Factory = new SqlSessionFactoryBuilder().build(reader, "taxbillConn");	
		}
		return oracle_Factory;
	}
	
	public static SqlSessionFactory getMariaSqlSession () throws FileNotFoundException {
		if(maria_Factory == null){
			reader = new BufferedReader(new FileReader(new File("/taxbill_module/ThunderMail/config/myBatisConfig.xml")));
			maria_Factory = new SqlSessionFactoryBuilder().build(reader, "thunderMailConn");	
		}
		return maria_Factory;
	}
	

}
