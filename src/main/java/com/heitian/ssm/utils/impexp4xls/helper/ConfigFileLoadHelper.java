package com.heitian.ssm.utils.impexp4xls.helper;

import com.hui.platform.kernel.helper.HuiCommonUtilCache;
import com.hui.platform.system.exception.HuiRuntimeException;
import com.hui.platform.system.utility.StringUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigFileLoadHelper {

	@SuppressWarnings("rawtypes")
	public static void loadCofingFile(String configPath, Map<String, Object> configure, Map extraCfg) {
		SAXBuilder builder = new SAXBuilder(false);
		Document document = null;
		try {
			InputStream in = new FileInputStream(new File(configPath));
			document = builder.build(in);
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new HuiRuntimeException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new HuiRuntimeException(e.getMessage());
		}
		Element root = document.getRootElement();
		// 获取模式
		String style = root.getAttributeValue("style");
		// 获取开始行
		String begin = root.getAttributeValue("begin");
		// 获取结束行
		String end = root.getAttributeValue("end");
		// 获取忽略行
		String ignores = root.getAttributeValue("ignores");
		String sheetName = root.getAttributeValue("sheetname");
		// 获取模板
		String modal = root.getAttributeValue("modal");

		configure.put("style", style);
		configure.put("begin", begin);
		configure.put("end", end);
		configure.put("ignores", ignores);
		configure.put("sheetname", sheetName);
		configure.put("modal", modal);

		// 获取实体配置
		getEntityProp(root, configure, extraCfg);
		// 获取字段配置
		getFieldProps(root, configure, extraCfg);
		// 获取自定义配置
		getCustomProps(root, configure, extraCfg);
	}

	@SuppressWarnings("rawtypes")
	private static void getCustomProps(Element root, Map<String, Object> configure, Map extraCfg) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<?> list4Custom = root.getChildren("custom");
		for (Iterator<?> it3 = list4Custom.iterator(); it3.hasNext();) {
			HashMap<String, String> custom = new HashMap<String, String>();
			Element e3 = (Element) it3.next();
			String rowIndex = e3.getAttributeValue("rowIndex");
			String columnIndex = e3.getAttributeValue("columnIndex");
			String fieldName = e3.getAttributeValue("fieldName");
			String labelName = e3.getAttributeValue("labelName");

			custom.put("rowIndex", rowIndex);
			custom.put("columnIndex", columnIndex);
			custom.put("fieldName", fieldName);
			custom.put("labelName", labelName);

			list.add(custom);
		}
		configure.put("custom", list);
	}

	@SuppressWarnings("rawtypes")
	public static void getFieldProps(Element root, Map<String, Object> configure, Map extraCfg) {
		Map<String, Object> fieldMapping = new LinkedHashMap<String, Object>();
		// 所有字段
		List<String> fieldNameList = new ArrayList<String>();

		// 所有字段配置内容
		Map<String, Object> fn = null;
		List<?> list4Field = root.getChildren("fieldMapping");
		for (Iterator<?> it = list4Field.iterator(); it.hasNext();) {
			Element e = (Element) it.next();
			fn = new HashMap<String, Object>();
			String columnIndex = e.getAttributeValue("columnIndex");
			if (e.getAttribute("labelName") != null) {
				String labelName = e.getAttributeValue("labelName");
				fn.put("labelName", labelName);
			}
			if (e.getAttribute("prefix") != null) {
				String prefix = e.getAttributeValue("prefix");
				fn.put("prefix", prefix);
			}
			if (e.getAttributeValue("suffix") != null) {
				String suffix = e.getAttributeValue("suffix");
				fn.put("suffix", suffix);
			}
			if (e.getAttributeValue("dataType") != null) {
				String dataType = e.getAttributeValue("dataType");
				fn.put("dataType", dataType);
			}
			if (e.getAttributeValue("formatter") != null) {
				String formtter = e.getAttributeValue("formatter");
				fn.put("formatter", formtter);
			}
			String fieldName = e.getAttributeValue("fieldName");
			fn.put("fieldName", fieldName);

			if (StringUtil.isNotNull(fieldName)) {
				fieldNameList.add(fieldName);
			}

			// 获取字段映射
			getFieldVMapping(fn, e);

			if (extraCfg == null || extraCfg.isEmpty()) {
				fieldMapping.put(columnIndex, fn);
			} else {
				String fields = (String) extraCfg.get("fields");
				filterLabel(fieldMapping, fn, columnIndex, fields, configure);
			}
		}
		configure.put("fieldMapping", fieldMapping);
		configure.put("fieldNameList", fieldNameList);
	}

	// 前台下载时勾选字段过滤
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void filterLabel(Map<String, Object> fieldMapping, Map fn, String index, String labels,
			Map configure) {
		String newIndex = (String) configure.get("newIndex");
		if (newIndex == null) {
			newIndex = "0";

		}
		if (StringUtil.isNotNull(labels)) {
			String[] l = labels.split(",");
			for (int i = 0; i < l.length; i++) {
				String labelName = l[i];
				String fnLabelName = (String) fn.get("fieldName");
				if (labelName.equalsIgnoreCase(fnLabelName)) {
					fieldMapping.put(newIndex, fn);
					configure.put("newIndex", (Integer.parseInt(newIndex) + 1) + "");
				}
			}
		} else {
			fieldMapping.put(index, fn);
		}
	}

	public static void getFieldVMapping(Map<String, Object> fn, Element e) {
		List<?> valueMapping = e.getChildren("valueMapping");
		if (valueMapping != null && !valueMapping.isEmpty()) {
			for (Iterator<?> it2 = valueMapping.iterator(); it2.hasNext();) {
				Element e2 = (Element) it2.next();
				List<?> list3 = e2.getChildren("item");
				Map<String, String> item = new HashMap<String, String>();
				for (Iterator<?> it4 = list3.iterator(); it4.hasNext();) {
					Element e4 = (Element) it4.next();
					String key = e4.getAttributeValue("key");
					String value = e4.getAttributeValue("value");
					item.put(key, value);
				}
				if (!list3.isEmpty()) {
					fn.put("valueMapping", item);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void getEntityProp(Element root, Map<String, Object> configure, Map extraCfg) {
		HashMap<String, String> customProperty = new HashMap<String, String>();
		List<?> list4CustomProp = root.getChildren("customProperty");
		for (Iterator<?> it3 = list4CustomProp.iterator(); it3.hasNext();) {
			Element e3 = (Element) it3.next();
			String tableName = e3.getAttributeValue("tableName");
			String downloadName = e3.getAttributeValue("downloadName");
			String importMode = e3.getAttributeValue("importMode");
			String keyfield = e3.getAttributeValue("keyfield");
			String idfield = e3.getAttributeValue("idfield");
			String codefield = e3.getAttributeValue("codefield");
			String state = e3.getAttributeValue("state");
			
			customProperty.put("tableName", tableName);
			customProperty.put("idfield", idfield);
			customProperty.put("codefield", codefield);
			customProperty.put("importMode", importMode);
			customProperty.put("keyfield", keyfield);
			customProperty.put("downloadName", downloadName);
			customProperty.put("state",state);
		}
		configure.put("customProperty", customProperty);
	}

	public static String getConfigPath(String configName) {
//		System.out.println("开始获取配置文件\t" + configName);
		if (StringUtil.isNull(configName)) {
			throw new HuiRuntimeException("Config file is not exist!");
		}
		if (!configName.endsWith(".xml")) {
			// 检查是否存在后缀
			if (configName.indexOf("[.]") == -1) {
				configName += ".xml";
			} else {
				throw new HuiRuntimeException("Config file is not xml file!");
			}
		}
		// 获取配置文件路径
		String configPath = HuiCommonUtilCache.getCommonUtil(HuiCommonUtilCache.KEY_WebRealPath)+File.separator+
		"WEB-INF/classes/sysconfig/impexp/" + configName;

		 
//		System.out.println("路径" + HuiCommonUtilCache.getCommonUtil(HuiCommonUtilCache.KEY_WebRealPath) +"\t" + configPath);
		return configPath.replace("/",File.separator);
	}
}
