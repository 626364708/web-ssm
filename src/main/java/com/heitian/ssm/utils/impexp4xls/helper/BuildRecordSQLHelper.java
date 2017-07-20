package com.heitian.ssm.utils.impexp4xls.helper;

import com.heitian.ssm.utils.BusinessConstants;
import com.hui.platform.system.idgenarater.IdGenaraterUtil;
import com.hui.platform.system.utility.StringUtil;

import java.util.List;
import java.util.Map;

public class BuildRecordSQLHelper {
	public static String buildRecordSQL(String tableName, List<String> fields) {
		String sql = "select ";
		for (int i = 0; i < fields.size(); i++) {
			sql += " " + fields.get(i) + ",";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1);
		}
		sql += " from " + tableName + " where 1=2";
		return sql;
	}

	@SuppressWarnings("rawtypes")
	public static String buildInsertSQL(String tableName, List fieldNameList, Map row) {
		String sql;
		sql = "insert into " + tableName + "(";
		for (Object o : fieldNameList) {
			sql += o + ",";
		}
		sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
		sql += ")";
		sql += "values(";

		for (Object o : fieldNameList) {
			String value = (String) row.get(o);
			if (value != null) {
				sql += value + ",";
			} else {
				sql += null + ",";
			}
		}
		sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
		sql += ")";
		return sql;
	}

	@SuppressWarnings("rawtypes")
	public static String buildInsertSQL(String tableName, String idField, List fieldNameList, Map row) {
		if (StringUtil.isNull(idField)) {
			return buildInsertSQL(tableName, fieldNameList, row);
		}
		String sql;
		sql = "insert into " + tableName + "(";
		for (Object o : fieldNameList) {
			sql += o + ",";
		}
		sql += "dbid";
		sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
		sql += ")";
		sql += "values(";

		for (Object o : fieldNameList) {
			String value = (String) row.get(o);
			if (value != null) {
				if (value.startsWith("to_date")) {
					sql += value + ",";
				} else {
					sql += "'" + value + "',";
				}
			} else {
				sql += null + ",";
			}
		}
		// 获取dbid
		int dbid = IdGenaraterUtil.nextIntValue(BusinessConstants.TABLEPK, idField);
		sql += dbid;
		sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
		sql += ")";
		return sql;
	}

	@SuppressWarnings("rawtypes")
	public static String buildInsertSQL(Map<String, String> customMap, List fieldNameList, Map row) {
		String tableName = customMap.get("tableName");
		String id = customMap.get("idfield");
		String code = customMap.get("codefield");
		String state = customMap.get("state");
		String sql;
		sql = "insert into " + tableName + "(";
		for (Object o : fieldNameList) {
			sql += o + ",";
		}
		if (StringUtil.isNotNull(code)) {
			sql += "code,";
		}
		if (StringUtil.isNotNull(id)) {
			sql += "dbid,";
		}
		if(StringUtil.isNotNull(state)){
			sql += "state,";
		}
		sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
		sql += ")";
		sql += "values(";

		for (Object o : fieldNameList) {
			String value = (String) row.get(o);
			if (value != null) {
				if (value.startsWith("to_date")) {
					sql += value + ",";
				} else {
					sql += "'" + value.replace("'", "''") + "',";
				}
			} else {
				sql += null + ",";
			}
		} 
		// 获取dbid
		if (StringUtil.isNotNull(code)) {
			code = IdGenaraterUtil.nextValue(code, "");
			sql += "'" + code + "',";
		}
		if (StringUtil.isNotNull(id)) {
			int dbid = IdGenaraterUtil.nextIntValue(BusinessConstants.TABLEPK, id);
			sql += dbid + ",";
		}
		if(StringUtil.isNotNull(state)){
			sql += state + ",";
		}
		
		sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
		sql += ")";
		return sql;
	}
	
	/**
	 * @param customMap
	 * @param fieldNameList
	 * @param row  
	 * @param entryMap 非模板配置项
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String buildInsertSQL(Map<String, String> customMap, List fieldNameList, Map row,Map<String, Object> entryMap) {

		String tableName = customMap.get("tableName");
		String id = customMap.get("idfield");
		String code = customMap.get("codefield");
		String state = customMap.get("state");
		String sql;
		sql = "insert into " + tableName + "(";
		for (Object o : fieldNameList) {
			sql += o + ",";
		}
		if (StringUtil.isNotNull(code)) {
			sql += "code,";
		}
		if (StringUtil.isNotNull(id)) {
			sql += "dbid,";
		}
		if(StringUtil.isNotNull(state)){
			sql += "state,";
		}
		if(null != entryMap){
			for (String s : entryMap.keySet()) {
				sql += s + ",";
			}
		}
		sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
		sql += ")";
		sql += "values(";

		for (Object o : fieldNameList) {
			String value = (String) row.get(o);
			if (value != null) {
				if (value.startsWith("to_date")) {
					sql += value + ",";
				} else {
					sql += "'" + value.replace("'", "''") + "',";
				}
			} else {
				sql += null + ",";
			}
		} 
		// 获取dbid
		if (StringUtil.isNotNull(code)) {
			code = IdGenaraterUtil.nextValue(code, "");
			sql += "'" + code + "',";
		}
		if (StringUtil.isNotNull(id)) {
			int dbid = IdGenaraterUtil.nextIntValue(BusinessConstants.TABLEPK, id);
			sql += dbid + ",";
		}
		if(StringUtil.isNotNull(state)){
			sql += state + ",";
		}
		if(null != entryMap)
		{
			for (String s: entryMap.keySet()) {
				Object value = entryMap.get(s);
				if (value != null) {
					if (value.toString().startsWith("to_date")) {
						sql += value + ",";
					} else {
						sql += "'" + value.toString().replace("'", "''") + "',";
					}
				} else {
					sql += null + ",";
				}
			}
		}
		sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
		sql += ")";
		return sql;
	}
}
