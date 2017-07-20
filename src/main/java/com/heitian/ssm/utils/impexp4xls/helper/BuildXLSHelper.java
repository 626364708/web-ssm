package com.heitian.ssm.utils.impexp4xls.helper;

import com.heitian.ssm.utils.BusinessConstants;
import com.hui.platform.kernel.helper.HuiCommonUtilCache;
import com.hui.platform.system.utility.DatetimeUtil;
import com.hui.platform.system.utility.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BuildXLSHelper {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Workbook buildXLS(Map<String, Object> configure, List<?> record, Map Custom) {

//		System.out.println("下载数据");

		Workbook wb = null;
		String modal = (String) configure.get("modal");
		if (StringUtil.isNotNull(modal)) {
			try {
				String mbPath = HuiCommonUtilCache.getCommonUtil(HuiCommonUtilCache.KEY_WebRealPath)+"WEB-INF/classes/"
						+ "sysconfig/" + modal;
				System.out.println("模板路径:"+mbPath);
				wb = new HSSFWorkbook(new FileInputStream(mbPath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		} else {
			wb = new HSSFWorkbook();
		}
		String sheetName = "Export Data";
		if (StringUtil.isNotNull((String) configure.get("sheetname"))) {
			sheetName = (String) configure.get("sheetname");
		}

		// 定义单元格样式
		CellStyle style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("微软雅黑");
		// font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		// font.setStrikeout(true);
		style.setFont(font);

		Sheet sheet = null;//
		if (StringUtil.isNotNull(modal)) {
			sheet = wb.getSheetAt(0);
		} else {
			sheet = wb.createSheet(sheetName);
		}

		String begin = (String) configure.get("begin");
		if (StringUtil.isNull(begin)) {
			begin = "1";
		}

		//
		// 写表头
		// 表头为第一行
		Row row = null;// sheet.createRow((short) 0); // 定义一行
		Map<String, Object> fieldMapping = (Map) configure.get("fieldMapping");
		Iterator<String> keyit = fieldMapping.keySet().iterator();
		Cell cell = null;

		if (StringUtil.isNull(modal)) {
			// sheet.setColumnWidth((short)1,(short)5000);// 单位
			// sheet.setColumnWidth((short)3,(short)5000);// 单位
			// sheet.setColumnWidth((short)6,(short)10000);// 单位
			row = sheet.createRow((short) 0);
			while (keyit.hasNext()) {
				String key = keyit.next();
				Map columInfo = (Map) fieldMapping.get(key);
				String labelName = (String) columInfo.get("labelName");
				if (labelName == null) {
					continue;
				}
				cell = row.createCell(Integer.parseInt(key));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(labelName);
				cell.setCellStyle(style);
			}
		}

		//判断特殊处理
		if(null!=Custom.get("isCreateColum"))
		{
			String sumValue = "";//积分总值
			if(null!=Custom.get("sumValue")){
				sumValue = Custom.get("sumValue").toString();
			}
			//System.out.println("整改表格");
			//样式1
			CellStyle thstyle = wb.createCellStyle();
			thstyle.setBorderBottom((short)1);
			thstyle.setBorderTop((short)1);
			thstyle.setBorderLeft((short)1);
			thstyle.setBorderRight((short)1);
			Font thfont = wb.createFont();
			thfont.setFontHeightInPoints((short) 11);
			//thfont.setFontName("微软雅黑");
			thfont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			// font.setStrikeout(true);
			thstyle.setFont(thfont);
			//设置上下左右居中
			thstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			thstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			//样式2
			CellStyle thstyle2 = wb.createCellStyle();
			thstyle2.setBorderBottom((short)1);
			thstyle2.setBorderTop((short)1);
			thstyle2.setBorderLeft((short)1);
			thstyle2.setBorderRight((short)1);
			Font thfont2 = wb.createFont();
			thfont2.setFontHeightInPoints((short) 11);
			//thfont.setFontName("微软雅黑");
			thfont2.setBoldweight(Font.BOLDWEIGHT_BOLD);
			// font.setStrikeout(true);
			thstyle2.setFont(thfont2);
			//设置上下左右居中
			thstyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			thstyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);



			//Map<String, Object> m = (Map)record.get(0);
			//Object [] cols = m.keySet().toArray();
			/*for (String key : m.keySet()) {
				m.get("sumvalue");
			}*/
			//合并第一行
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
			//第2行
			row = sheet.getRow(1);
			cell = row.createCell(0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("部门积分合计:" +sumValue);
			cell.setCellStyle(thstyle);

//			cell = row.createCell(1);
//			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell.setCellValue(sumValue);
//			cell.setCellStyle(thstyle2);
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,1));

			//第3行
			row = sheet.getRow(2);
			cell = row.createCell(0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("部门明细如下:");
			cell.setCellStyle(thstyle2);
			sheet.addMergedRegion(new CellRangeAddress(2,2,0,1));


			row = sheet.getRow(3);
			while (keyit.hasNext()) {
				String key = keyit.next();
				Map columInfo = (Map) fieldMapping.get(key);
				String labelName = (String) columInfo.get("labelName");
				if (labelName == null) {
					continue;
				}
				cell = row.createCell(Integer.parseInt(key));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(labelName);
				cell.setCellStyle(thstyle);
			}

			/*//遍历列
			for(int j = 0;j<cols.length;j++)
			{
				String colname = cols[j].toString().split("_")[0];
				//第4行
				row = sheet.getRow(3);
				cell = row.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(colname);
				cell.setCellStyle(thstyle);
			}*/

			//重构数据列
			/*fieldMapping = new HashMap<String, Object>();
			for(int k =0;k<cols.length;k++)
			{
				Map colMap = new HashMap();
				colMap.put("fieldName", cols[k].toString());
				colMap.put("labelName", cols[k].toString());
				fieldMapping.put(k+"",colMap);
			}*/

		}





		for (int i = 0; i < record.size(); i++) {
			/*if(null != Custom.get("Xml") && i == 0){
				continue;
			}*/
			Map rowData = (Map) record.get(i);
			if (StringUtil.isNotNull(modal)) {
				row = sheet.getRow((i + Integer.parseInt(begin)));
				if (row == null) {
					row = sheet.createRow((short) (i + Integer.parseInt(begin))); // 定义一行
				}
			} else {
				row = sheet.createRow((short) (i + Integer.parseInt(begin))); // 定义一行
			}
			keyit = fieldMapping.keySet().iterator();
			while (keyit.hasNext()) {
				String key = keyit.next();
				Map columInfo = (Map) fieldMapping.get(key);
				String fieldName = (String) columInfo.get("fieldName");
				String labelName = (String) columInfo.get("labelName");
				if (labelName == null) {
					continue;
				} else if ("".equals(fieldName)) {
					fieldName = "xls_id";
				}
				Object value = rowData.get(fieldName);
				if ("xls_id".equals(fieldName)) {
					value = i + 1;
				}

				if (value == null) {
					value = "";
				}
				value = Filed2Cell(columInfo, value);

				if (StringUtil.isNotNull(modal)) {
					cell = row.getCell(Integer.parseInt(key));
					if (cell == null) {
						cell = row.createCell(Integer.parseInt(key));
					}
				} else {
					cell = row.createCell(Integer.parseInt(key));
				}
				
				
				
				// cell.setCellStyle(style);
				// System.out.println(value+ "     "
				// +value.getClass().getSimpleName());
				if (value instanceof BigDecimal || value instanceof Integer || value instanceof Double) {
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					if (value instanceof Integer) {
						cell.setCellValue((Integer) value);
					} else if (value instanceof Double) {
						cell.setCellValue((Double) value);
					} else {
						cell.setCellValue(((BigDecimal) value).doubleValue());
					}

				} else {
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(value.toString());
				}
				if (columInfo.get("dataType") != null) {
					String dataType = (String) columInfo.get("dataType");
					if("color".equals(dataType))
					{
						System.out.println("yanss"); 
						CellStyle s =  wb.createCellStyle();
						
						short c = getColor(Integer.parseInt(value.toString()));
						
						s.setFillForegroundColor(c);
						s.setFillBackgroundColor(c);
						s.setFillPattern(CellStyle.SOLID_FOREGROUND);
						cell.setCellStyle(s);
						cell.setCellValue("");
					}
				}
			}
		}
		/*
		List<Map<String, String>> customList = (List<Map<String, String>>) configure.get("custom");
		for (Map<String, String> map : customList) {
			String rowIndex = map.get("rowIndex");
			String columnIndex = map.get("columnIndex");
			String fieldName = map.get("fieldName");
			String LabelName = map.get("labelName");
			if (null != record && !record.isEmpty()) {
				Map rowData = (Map) record.get(0);
				String data = (String) rowData.get(fieldName);
				LabelName += "(" + data + ")";
				Cell c = sheet.getRow(Integer.parseInt(rowIndex)).getCell(Integer.parseInt(columnIndex));
				if (c == null) {
					c = sheet.getRow(Integer.parseInt(rowIndex)).createCell(Integer.parseInt(columnIndex));
				}
				c.setCellValue(LabelName);
			}
		}
		*/
		if (null != Custom) {
			// 获取要隐藏的列
			List<String> hideList = (List) Custom.get("hidden");
			if(hideList!=null)
			{
				for (String label : hideList) {
					keyit = fieldMapping.keySet().iterator();
					while (keyit.hasNext()) {
						String key = keyit.next();
						Map columInfo = (Map) fieldMapping.get(key);
						String labelName = (String) columInfo.get("labelName");
						if (label.equals(labelName)) {
							sheet.setColumnHidden((short) Integer.parseInt(key), true);
						}
					}
				}
			}
			//标题
			String titleConfig = (String) Custom.get("title");
			if(StringUtil.isNotNull(titleConfig))
			{
				String title = titleConfig.split(",")[0];
				String titleRow = titleConfig.split(",")[1];
				String titleColum = titleConfig.split(",")[2];
				sheet.getRow(Integer.parseInt(titleRow)).getCell(Integer.parseInt(titleColum))
						.setCellValue(title);
			}
			//副标题
			String sonTitleConfig = (String) Custom.get("son-title");
			if(StringUtil.isNotNull(sonTitleConfig))
			{
				String title = sonTitleConfig.split(",")[0];
				String titleRow = sonTitleConfig.split(",")[1];
				String titleColum = sonTitleConfig.split(",")[2];
				sheet.getRow(Integer.parseInt(titleRow)).getCell(Integer.parseInt(titleColum))
						.setCellValue(title);
			}
			
			//替换
			Map replaceConfig = (Map) Custom.get("replace");
			if(null!=replaceConfig)
			{
				Iterator it = replaceConfig.keySet().iterator();
				while(it.hasNext())
				{
					String t = (String)it.next();
					if(StringUtil.isNotNull(t))
					{
						String z1 = t.split(",")[0];
						String z2 = t.split(",")[1];
						String nr = (String)replaceConfig.get(t);
						sheet.getRow(Integer.parseInt(z1)).getCell(Integer.parseInt(z2))
						.setCellValue(nr);
					}
				}
			}
			
		}
		return wb;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Workbook buildXLS(Map<String, Object> configure, List<?> record) {
		Workbook wb = null;
		String modal = (String) configure.get("modal");
		if (StringUtil.isNotNull(modal)) {
			try {
				String mbPath = Thread.currentThread().getContextClassLoader().getResource("").getFile()
						+ "sysconfig/" + modal;
				wb = new HSSFWorkbook(new FileInputStream(mbPath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		} else {
			wb = new HSSFWorkbook();
		}
		String sheetName = "Export Data";
		if (StringUtil.isNotNull((String) configure.get("sheetname"))) {
			sheetName = (String) configure.get("sheetname");
		}

		// 定义单元格样式
		CellStyle style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("微软雅黑");
		// font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		// font.setStrikeout(true);
		style.setFont(font);

		Sheet sheet = null;//
		if (StringUtil.isNotNull(modal)) {
			sheet = wb.getSheetAt(0);
		} else {
			sheet = wb.createSheet(sheetName);
		}

		String begin = (String) configure.get("begin");
		if (StringUtil.isNull(begin)) {
			begin = "1";
		}

		//
		// 写表头
		// 表头为第一行
		Row row = null;// sheet.createRow((short) 0); // 定义一行
		Map<String, Object> fieldMapping = (Map) configure.get("fieldMapping");
		Iterator<String> keyit = fieldMapping.keySet().iterator();
		Cell cell = null;

		if (StringUtil.isNull(modal)) {
			// sheet.setColumnWidth((short)1,(short)5000);// 单位
			// sheet.setColumnWidth((short)3,(short)5000);// 单位
			// sheet.setColumnWidth((short)6,(short)10000);// 单位
			row = sheet.createRow((short) 0);
			while (keyit.hasNext()) {
				String key = keyit.next();
				Map columInfo = (Map) fieldMapping.get(key);
				String labelName = (String) columInfo.get("labelName");
				if (labelName == null) {
					continue;
				}
				cell = row.createCell(Integer.parseInt(key));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(labelName);
				cell.setCellStyle(style);
			}
		}
		for (int i = 0; i < record.size(); i++) {
			Map rowData = (Map) record.get(i);
			if (StringUtil.isNotNull(modal)) {
				row = sheet.getRow((i + Integer.parseInt(begin)));
				if (row == null) {
					row = sheet.createRow((short) (i + Integer.parseInt(begin))); // 定义一行
				}
			} else {
				row = sheet.createRow((short) (i + Integer.parseInt(begin))); // 定义一行
			}
			keyit = fieldMapping.keySet().iterator();
			while (keyit.hasNext()) {
				String key = keyit.next();
				Map columInfo = (Map) fieldMapping.get(key);
				String fieldName = (String) columInfo.get("fieldName");
				String labelName = (String) columInfo.get("labelName");
				if (labelName == null) {
					continue;
				} else if ("".equals(fieldName)) {
					fieldName = "xls_id";
				}
				Object value = rowData.get(fieldName);
				if ("xls_id".equals(fieldName)) {
					value = i + 1;
				}

				if (value == null) {
					value = "";
				}
				value = Filed2Cell(columInfo, value);

				if (StringUtil.isNotNull(modal)) {
					cell = row.getCell(Integer.parseInt(key));
					if (cell == null) {
						cell = row.createCell(Integer.parseInt(key));
					}
				} else {
					cell = row.createCell(Integer.parseInt(key));
				}
				// cell.setCellStyle(style);
				// System.out.println(value+ "     "
				// +value.getClass().getSimpleName());
				if (value instanceof BigDecimal || value instanceof Integer || value instanceof Double) {
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					if (value instanceof Integer) {
						cell.setCellValue((Integer) value);
					} else if (value instanceof Double) {
						cell.setCellValue((Double) value);
					} else {
						cell.setCellValue(((BigDecimal) value).doubleValue());
					}

				} else {
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(value.toString());
				}

			}
		}

		// 设置隐藏列
		// sheet.setColumnHidden((short)3, false);
		// sheet.setColumnHidden((short)4, false);
		// 设置隐藏行 将第8行隐藏就是将他的高度设为0也等同为隐藏
		// HSSFRow row = sheet.getRow(8);
		// row.setZeroHeight(true);

		return wb;
	}

	@SuppressWarnings({ "rawtypes" })
	private static Object Filed2Cell(Map map, Object value) {
		Object cellContent = value;
		if (map == null || map.get("labelName") == null || value == null) {
			return null;
		} else {
			if (map.get("prefix") != null) {
				String prefix = (String) map.get("prefix");
				cellContent = cellContent + prefix;
			}
			if (map.get("suffix") != null) {
				String suffix = (String) map.get("suffix");
				cellContent = cellContent + suffix;
			}
			// 格式化数据
			if (map.get("dataType") != null) {
				String dataType = (String) map.get("dataType");
				if ("int".equals(dataType)) {
					cellContent = "".equals(cellContent)?0:cellContent;
				} else if ("date".equals(dataType)) {
					if (cellContent instanceof Timestamp) {
						Timestamp temp = (Timestamp) cellContent;
						// String stringDate = temp.getYear() + "-" +
						// temp.getMonth() +"-" +temp.getDay();
						String formatter = map.get("formatter") == null ? BusinessConstants.DATEFORMAT2DATE
								: map.get("formatter").toString();
						cellContent = DatetimeUtil.formatDate(temp, formatter);
					}
					if (cellContent instanceof Date) {
						String formatter = map.get("formatter") == null ? BusinessConstants.DATEFORMAT2DATE
								: map.get("formatter").toString();
						cellContent = DatetimeUtil.formatDate((Date) cellContent, formatter);
					}
				}
			}
			cellContent = mappingField(map, cellContent);
		}
		return cellContent;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object mappingField(Map map, Object cellContent) {
		Map valueMapping = (Map) map.get("valueMapping");
		if (valueMapping != null) {
			// Map itemMap = (Map) valueMapping.get("item");
			Iterator<String> itemIter = valueMapping.keySet().iterator();
			while (itemIter.hasNext()) {
				String key = itemIter.next();
				String v = (String) valueMapping.get(key);
				if (cellContent.equals(v)) {
					cellContent = (String) key;
				}
			}
		}
		return cellContent;
	}
	
	private static short getColor(int lx)
	{
		short r = 0;
		switch (lx) {
		case 1:
			r = IndexedColors.GREEN.getIndex();
			break;
		case 2:
			r = IndexedColors.YELLOW.getIndex();
			break;
		case 3:
			r = IndexedColors.ORANGE.getIndex();
			break;
		case 4:
			r = IndexedColors.RED.getIndex();
			break;
		default:
			break;
		}
		return r;
	}
}
