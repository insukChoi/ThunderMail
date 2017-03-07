package com.mail.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

public interface MailDBDao {
	
	public int sqlUpdate(SqlSession session, String targetId, HashMap map);
	public int sqlInsert(SqlSession session, String targetId, HashMap map);
	public int sqlDelete(SqlSession session, String targetId, HashMap map);
	public List sqlSelect(SqlSession session, String targetId, HashMap map);
	public int sqlSelectCnt(SqlSession session, String targetId, HashMap map);
	public void sqlSessionCommit(SqlSession session);
	public void sqlSessionRollback(SqlSession session);
}
