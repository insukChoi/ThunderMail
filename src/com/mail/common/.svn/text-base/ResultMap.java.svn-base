package com.mail.common;

/**
 * @(#) ResultMap.java
 * @author  최인석, cis172510@bizplay.co.kr.
 */

import java.util.*;

public class ResultMap {
	private List _list = null;
	private Map  _map  = null;
	private int  _cnt  = -1;

	/**
     * ResultMap 인스턴스 생성.
     *
     * @param     autoCmt     boolean형태의 DB AutoCommit 유무 정의.
     */
	public ResultMap() {
		_list = new ArrayList();
	}


	/**
     * 해당 Map을 초기화 한다.
     *
     */
	public void setFirst() {
		_map = new HashMap();
	}
	
	/**
     * 해당 Map을 반복부 리스트에 담는다.
     *
     * @param     strYYYYMMDD1     String 형으로 'YYYYMMDD' 식으로 시작날짜를 입력받는다.
     * @param     strYYYYMMDD2    String 형으로 'YYYYMMDD' 식으로 종료날짜를 입력받는다.
     * @return     int 형으로 두 날 사이의 일자를 반환한다.
     */
	public void setLast() {
		_list.add(_map);
	}

	/**
     * 해당 Map에 String 값을 담는다.
     *
     * @param     strYYYYMMDD1     String 형으로 'YYYYMMDD' 식으로 시작날짜를 입력받는다.
     * @param     strYYYYMMDD2    String 형으로 'YYYYMMDD' 식으로 종료날짜를 입력받는다.
     * @return     int 형으로 두 날 사이의 일자를 반환한다.
     */
	public void putMap(String _key, String _value) {
		_map.put(_key, _value);
	}

	/**
     * 해당 Map에 String 값을 담는다.
     *
     * @param     strYYYYMMDD1     String 형으로 'YYYYMMDD' 식으로 시작날짜를 입력받는다.
     * @param     strYYYYMMDD2    String 형으로 'YYYYMMDD' 식으로 종료날짜를 입력받는다.
     * @return     int 형으로 두 날 사이의 일자를 반환한다.
     */
	public void putMap(String _key, Object _value) {
		_map.put(_key, _value);
	}
	/**
     * 선택되어져 있는 map 을 반환한다
     */
	public Map getHashMap(){
		return _map;
	}
	/**
     * ArrayList 를 반환한다
     */
	public List getList(){
		return _list;
	}
	/**
     * ArrayList 를 셋팅한다.
     */
	public List setList(List list){
		return _list = list;
	}
	/**
     * 반복부의 다음 List에 있는 Map을 가져온다.
     *
     * @param     strYYYYMMDD1     String 형으로 'YYYYMMDD' 식으로 시작날짜를 입력받는다.
     * @param     strYYYYMMDD2    String 형으로 'YYYYMMDD' 식으로 종료날짜를 입력받는다.
     * @return     int 형으로 두 날 사이의 일자를 반환한다.
     */
	public boolean next() {
		if ( _cnt + 1 >= _list.size() ) return false;
		_cnt = _cnt + 1;
		_map = (HashMap)_list.get(_cnt);
		return true;
	}
	
	/**
     * 반복부의 다음 List에 있는 Map을 가져온다.
     *
     * @param     strYYYYMMDD1     String 형으로 'YYYYMMDD' 식으로 시작날짜를 입력받는다.
     * @param     strYYYYMMDD2    String 형으로 'YYYYMMDD' 식으로 종료날짜를 입력받는다.
     * @return     int 형으로 두 날 사이의 일자를 반환한다.
     */
	public boolean prev() {
		if ( _cnt - 1 < 0 ) return false;
		_cnt = _cnt - 1;
		_map = (HashMap)_list.get(_cnt);
		return true;
	}	

	public int size() {
		if ( _list != null ) return _list.size();
		else return 0;
	}

	/**
     * 해당 List의 첫번째로 리턴한다.
     *
     * @param     strYYYYMMDD1     String 형으로 'YYYYMMDD' 식으로 시작날짜를 입력받는다.
     * @param     strYYYYMMDD2    String 형으로 'YYYYMMDD' 식으로 종료날짜를 입력받는다.
     * @return     int 형으로 두 날 사이의 일자를 반환한다.
     */
	public void first() {
		_cnt = 0;
		if (_list.size() > 0) _map = (HashMap)_list.get(_cnt);
	}

	/** 
     * 해당 List의 첫번째 이전으로 리턴한다.
     *
     * @param     strYYYYMMDD1     String 형으로 'YYYYMMDD' 식으로 시작날짜를 입력받는다.
     * @param     strYYYYMMDD2    String 형으로 'YYYYMMDD' 식으로 종료날짜를 입력받는다.
     * @return     int 형으로 두 날 사이의 일자를 반환한다.
     */
	public void beforeFirst() {
		_cnt = -1;
		_map = null;
	}
	/**
     * 해당 key의 String 값을 리턴한다.
     *
     * @param     strYYYYMMDD1     String 형으로 'YYYYMMDD' 식으로 시작날짜를 입력받는다.
     * @param     strYYYYMMDD2    String 형으로 'YYYYMMDD' 식으로 종료날짜를 입력받는다.
     * @return     int 형으로 두 날 사이의 일자를 반환한다.
     */
	public String getString(String _key) {
		if ( null == _map ) return null;
		return StringUtil.null2void((String)_map.get(_key));
	}

	/**
     * 해당 key의 Object 값을 리턴한다.
     *
     * @param     strYYYYMMDD1     String 형으로 'YYYYMMDD' 식으로 시작날짜를 입력받는다.
     * @param     strYYYYMMDD2    String 형으로 'YYYYMMDD' 식으로 종료날짜를 입력받는다.
     * @return     int 형으로 두 날 사이의 일자를 반환한다.
     */
	public Object getMap(String _key) {
		if ( null == _map ) return null;
		return (Object)_map.get(_key);
	}

	public Set keySet() {
		HashMap keymap = (HashMap)_list.get(0);
		if ( null == keymap ) return null;
		return (Set)keymap.keySet();
	}
	
	public Set keySet(int index) {
		HashMap keymap = (HashMap)_list.get(index);
		if ( null == keymap ) return null;
		return (Set)keymap.keySet();
	}
	
	public void close() {	
		try {
			
			if (_list != null) {				
				_list.clear();
				_list = null;
			}
		} catch(Exception e) {}
		
	}
}