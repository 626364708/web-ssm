package com.heitian.ssm.utils.impexp4xls;

import com.heitian.ssm.utils.impexp4xls.helper.BuildRecordSQLHelper;
import com.heitian.ssm.utils.impexp4xls.helper.BuildXLSHelper;
import com.heitian.ssm.utils.impexp4xls.helper.ConfigFileLoadHelper;
import com.hui.platform.basic.logger.Logger;
import com.hui.platform.framework.dao.util.QueryUtil;
import com.hui.platform.kernel.service.HuiServiceManager;
import com.hui.platform.system.exception.HuiRuntimeException;
import com.hui.platform.system.utility.StringUtil;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POIExporterImpl implements XLSExporter {
	private static Logger logger = (Logger) HuiServiceManager.getInstance().getService(
			Logger.class.toString());
	static {
		logger.initlog(POIExporterImpl.class, Logger.INFO);
	}

	// 全局配置
	HashMap<String, Object> configure = new HashMap<String, Object>();
	Workbook hssf;

	@SuppressWarnings("rawtypes")
	public void loadConfig(String configName, Map extraCfg) {
		String configPath = ConfigFileLoadHelper.getConfigPath(configName);
		ConfigFileLoadHelper.loadCofingFile(configPath, configure, extraCfg);
	}

	public void setConfig(Map<String, Object> configMap) {
		this.configure = (HashMap<String, Object>) configMap;
	}

	public void write(OutputStream out) {
		try {
			hssf.write(out);

		} catch (IOException e) {
			e.printStackTrace();
			throw new HuiRuntimeException("--", e);
		}
	}

	@SuppressWarnings("unchecked")
	public void buildXLS() {
		Map<String, String> entityProp = (Map<String, String>) configure.get("customProperty");
		String tableName = entityProp.get("tableName");
		List<String> fields = (List<String>) configure.get("fieldNameList");
		String sql = BuildRecordSQLHelper.buildRecordSQL(tableName, fields);
		bluidXLS(sql);
	}

	public void bluidXLS(String sql) {
		QueryUtil qu = new QueryUtil();
		qu.setWholeSQL(sql);
		List<?> record = qu.list();
		buildXLS(record);
	}

	public void buildXLS(List<?> record) {
		hssf = BuildXLSHelper.buildXLS(configure, record);
	}

	@SuppressWarnings("rawtypes")
	public void buildXLS(List<?> record, Map customMap) {
		hssf = BuildXLSHelper.buildXLS(configure, record, customMap);
	}

	public void exportXLS(OutputStream out, String configfile, List<?> record, String sql) {
		exportXLS(out, configfile, record, sql, null);
	}

	@SuppressWarnings("rawtypes")
	public void exportXLS(OutputStream out, String configfile, List<?> record, String sql, Map extraCfg) {
		loadConfig(configfile, extraCfg);
		if (record == null || record.isEmpty()) {
			if (StringUtil.isNotNull(sql)) {
				bluidXLS(sql);
			} else {
				buildXLS();
			}
		} else {
			buildXLS(record);
		}
		write(out);
	}

	@SuppressWarnings("rawtypes")
	public void exportXLS(OutputStream out, String configfile, List<?> record, Map customMap) {
		loadConfig(configfile, null);
		buildXLS(record, customMap);
		write(out);
	}

}
