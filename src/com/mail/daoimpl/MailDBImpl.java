package com.mail.daoimpl;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.mail.dao.MailDBDao;

public class MailDBImpl implements MailDBDao{
	
	public int sqlUpdate(SqlSession session, String targetId, HashMap map) {
		int resultCd = session.update(targetId, map);
		
		return resultCd;
	}

	public int sqlInsert(SqlSession session, String targetId, HashMap map) {
		int resultCd = session.insert(targetId, map);
		
		return resultCd;
	}

	public int sqlDelete(SqlSession session, String targetId, HashMap map) {
		int resultCd = session.delete(targetId, map);
		
		return resultCd;
	}

	public List sqlSelect(SqlSession session, String targetId, HashMap map) {
		List<HashMap<String, String>> OutList =  session.selectList(targetId, map);
	
		return OutList;
	}

	public int sqlSelectCnt(SqlSession session, String targetId, HashMap map) {
		int resultCd = session.selectOne(targetId, map);
		
		return resultCd;
	}

	public void sqlSessionCommit(SqlSession session) {
		session.commit(true);
	}

	public void sqlSessionRollback(SqlSession session) {
		session.rollback(true);
	}

}
