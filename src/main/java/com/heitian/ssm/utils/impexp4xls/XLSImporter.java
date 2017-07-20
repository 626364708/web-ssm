package com.heitian.ssm.utils.impexp4xls;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface XLSImporter {
	/**
	 * 从配置文件中加载配置
	 * 
	 * @param configfile
	 */
	@SuppressWarnings("rawtypes")
	public void loadConfig(String configName, Map extraMap);

	/**
	 * 设置配置内容
	 * 
	 * @param configMap
	 */
	public void setConfig(Map<String, Object> configMap);

	/*
	 * 获取XLS数据
	 */
	public List<?> getXLSData(MultipartFile file);

	/**
	 * 把数据集保存到数据库中
	 */
	public void save2DB(List<?> record);

	/**
	 * 根据配置文件导入数据
	 * 
	 * @param configfile
	 * @param record
	 * @param filePath
	 */
	@SuppressWarnings("rawtypes")
	public void importXLS(String configfile, List<?> record, MultipartFile file, Map extraMap);
}
