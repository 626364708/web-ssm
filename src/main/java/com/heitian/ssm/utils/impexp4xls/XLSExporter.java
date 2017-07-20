package com.heitian.ssm.utils.impexp4xls;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface XLSExporter {
	/**
	 * 从配置文件中加载配置
	 * 
	 * @param configfile
	 */
	@SuppressWarnings("rawtypes")
	public void loadConfig(String configfile, Map extraCfg);

	/**
	 * 设置配置内容
	 * 
	 * @param configMap
	 */
	public void setConfig(Map<String, Object> configMap);

	/*
	 * 根据配置获取数据
	 */
	public void buildXLS();

	/**
	 * 根据数据集生成XLS
	 */
	public void buildXLS(List<?> record);

	/**
	 * 根据SQL语句所获取的数据集生成XLS
	 * 
	 * @param sql
	 */
	public void bluidXLS(String sql);

	/**
	 * 输出流
	 * 
	 * @param out
	 */
	public void write(OutputStream out);

	/**
	 * 根据配置文件与数据集导出XLS到IO流中
	 * 
	 * @param out
	 * @param configfile
	 * @param record
	 * @param sql
	 */
	public void exportXLS(OutputStream out, String configfile, List<?> record, String sql);

	@SuppressWarnings("rawtypes")
	public void exportXLS(OutputStream out, String configfile, List<?> record, String sql, Map extra);

	@SuppressWarnings("rawtypes")
	public void exportXLS(OutputStream out, String configfile, List<?> record, Map hiddenMap);

}
