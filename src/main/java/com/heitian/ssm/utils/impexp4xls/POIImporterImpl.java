package com.heitian.ssm.utils.impexp4xls;

import com.heitian.ssm.utils.impexp4xls.helper.BuildRecordSQLHelper;
import com.heitian.ssm.utils.impexp4xls.helper.ConfigFileLoadHelper;
import com.heitian.ssm.utils.impexp4xls.helper.GetXLSHelper;
import com.hui.platform.basic.logger.Logger;
import com.hui.platform.framework.dao.util.ExecUtil;
import com.hui.platform.kernel.service.HuiServiceManager;
import com.hui.platform.system.exception.HuiRuntimeException;
import com.hui.platform.system.utility.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public class POIImporterImpl implements XLSImporter {
	private static Logger logger = (Logger) HuiServiceManager.getInstance().getService(
			Logger.class.toString());
	static {
		logger.initlog(POIImporterImpl.class, Logger.INFO);
	}

	// 全局配置
	HashMap<String, Object> configure = new HashMap<String, Object>();

	@SuppressWarnings("rawtypes")
	public void loadConfig(String configName, Map extraMap) {
		String configPath = ConfigFileLoadHelper.getConfigPath(configName);
		ConfigFileLoadHelper.loadCofingFile(configPath, configure, extraMap);
	}

	public void setConfig(Map<String, Object> configMap) {
		this.configure = (HashMap<String, Object>) configMap;
	}

	@SuppressWarnings("rawtypes")
	public List<?> getXLSData(MultipartFile file) {
		List<?> returnList = new ArrayList();
		Iterator<Row> rowit = null;
		if (StringUtil.isNotNull(file.getOriginalFilename())) {
			String fileExtName = file.getOriginalFilename().split("[.]")[file.getOriginalFilename().split("[.]").length - 1];
			try {
				if ("xls".equalsIgnoreCase(fileExtName)) {
					// 2007之前格式
					HSSFWorkbook book = new HSSFWorkbook(file.getInputStream());
					HSSFSheet sheet = book.getSheetAt(0);
					rowit = sheet.iterator();
				} else if ("xlsx".equalsIgnoreCase(fileExtName)) {
					XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
					XSSFSheet sheet = book.getSheetAt(0);
					rowit = sheet.iterator();
				}
				if (rowit != null) {
					returnList = GetXLSHelper.transferXLSData(configure, rowit);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new HuiRuntimeException("--", "Data file is not exist!");
		}
		return returnList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void save2DB(List<?> list) {
		if (list != null && !list.isEmpty()) {
			String importMode = (String) ((Map) configure.get("customProperty")).get("importMode");
			List fieldNameList = (List) configure.get("fieldNameList");
			String errorIndex = "";
			for (int i = 0; i < list.size(); i++) {
				String sql = "";
				Map row = (Map) list.get(i);
				if ("insert".equals(importMode)) {
					sql = BuildRecordSQLHelper.buildInsertSQL((Map) configure.get("customProperty"),
							fieldNameList, row);
				}
				if (StringUtil.isNotNull(sql)) {
					ExecUtil eq = new ExecUtil();
					eq.setSQL(sql);
					try {
						eq.exec();
					} catch (Exception e) {
						e.printStackTrace();
						errorIndex += (i + 2) + ",";
					}
				}
			}
			System.out.println("数据出错行:" + errorIndex);
		}
	}
	
	public void save2DB(List<?> list,Map<String,Object> entryMap) {
		if (list != null && !list.isEmpty()) {
			String importMode = (String) ((Map) configure.get("customProperty")).get("importMode");
			List fieldNameList = (List) configure.get("fieldNameList");
			String errorIndex = "";
			for (int i = 0; i < list.size(); i++) {
				String sql = "";
				Map row = (Map) list.get(i);
				if ("insert".equals(importMode)) {
					sql = BuildRecordSQLHelper.buildInsertSQL((Map) configure.get("customProperty"),
							fieldNameList, row,entryMap);
				}
				if (StringUtil.isNotNull(sql)) {
					ExecUtil eq = new ExecUtil();
					eq.setSQL(sql);
					try {
						eq.exec();
					} catch (Exception e) {
						e.printStackTrace();
						errorIndex += (i + 2) + ",";
					}
				}
			}
			System.out.println("数据出错行:" + errorIndex);
		}
	}

	@SuppressWarnings("rawtypes")
	public void importXLS(String configfile, List<?> record, MultipartFile file, Map extraMap) {
		loadConfig(configfile, extraMap);
		if (record == null || record.isEmpty()) {
			record = getXLSData(file);
		}
		save2DB(record);
	}
	
	public static void main(String[] args) {
		String filepath = "F:/test/学习积分线下.xml";
		POIImporterImpl poiimport = new POIImporterImpl();
		poiimport.loadConfig(filepath, null);
		//List<?> list = poiimport.getXLSData("F:/test/hzh.xlsx");
	}

}
