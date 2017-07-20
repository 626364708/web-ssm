package com.heitian.ssm.utils.impexp4xls.helper;

import com.hui.platform.system.utility.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GetXLSHelper {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List transferXLSData(Map configure, Iterator<Row> rowit) throws FileNotFoundException,
			IOException {
		int beginrow = Integer.parseInt((String) configure.get("begin"));
		String end = configure.get("end") == null || ((String) configure.get("end")).indexOf(":") == -1 ? ":noEndSign"
				: (String) configure.get("end");
		String endKey = end.split(":")[0];
		String endValue = end.split(":")[1];
		// String ignoreRows = (String) configure.get("ignores");
		List returnList = new ArrayList();
		boolean isEnd = false;
		while (rowit.hasNext()) {
			Row row = rowit.next();
			String rowContent = "";
			Map rowdata = new HashMap();
			if (row != null) {
				int rowNum = row.getRowNum();
				if (rowNum < beginrow) {
					continue;
				}
				Iterator<Cell> it = row.iterator();
				while (it.hasNext()) {
					Object cellContent = "";
					Cell cell = it.next();
					if (cell == null) {
						continue;
					}
				
					int colNum = cell.getColumnIndex();
					/*if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						cellContent = cell.getNumericCellValue();
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							cellContent = cell.getDateCellValue();
						}
					} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						cellContent = cell.getStringCellValue();
					} else {
						cellContent = cell.getDateCellValue();
					}*/
					
					cellContent = parseExcel(cell);
					
					if (endKey.equals(colNum + "")) {
						if (cellContent.equals(endValue)) {
							isEnd = true;
							break;
						}
					}
					cellContent = cell2DB(configure, colNum, cellContent, rowdata);
					if (cellContent != null) {
						rowContent += cellContent;
					}
				}
			}

			// 读取到结束标记 不再读取
			if (isEnd) {
				break;
			}

			// 数据不全为空时装入list
			if (!rowContent.equals("")) {
				returnList.add(rowdata);
			}
		}

		return returnList;
	}
	
	public static Object parseExcel(Cell cell) {
		Object cellContent = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型
			if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
				Date date = cell.getDateCellValue();
				cellContent = date;
			} else if (cell.getCellStyle().getDataFormat() == 58) {
				// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
				double value = cell.getNumericCellValue();
				Date date = org.apache.poi.ss.usermodel.DateUtil
						.getJavaDate(value);
				cellContent = date;
			} else {
				double value = cell.getNumericCellValue();
				cellContent = value;
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:// String类型
			cellContent = cell.getRichStringCellValue().toString();
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			cellContent = "";
		default:
			cellContent = "";
			break;
		}
		return cellContent;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object cell2DB(Map configure, int colIndex, Object o, Map rowData) {
		Map map = (Map) ((Map) configure.get("fieldMapping")).get(colIndex + "");
		String cellContent = "";
		if (map == null || o == null) {
			return null;
		} else {

			// 处理各种类型转换
			cellContent = Object2Str(o);

			String fieldName = (String) map.get("fieldName");
			if (map.get("prefix") != null) {
				String prefix = (String) map.get("prefix");

				if (StringUtil.isNotNull(prefix)) {
					if (StringUtil.isNotNull(cellContent)) {
						if (cellContent.endsWith(prefix)) {
							cellContent = cellContent.substring(0, cellContent.length() - prefix.length());
						}
					}
				}
			}
			if (map.get("suffix") != null) {
				String suffix = (String) map.get("suffix");
				if (StringUtil.isNotNull(suffix)) {
					if (StringUtil.isNotNull((String) cellContent)) {
						String c = (String) cellContent;
						if (c.endsWith(suffix)) {
							cellContent = c.substring(0, c.length() - suffix.length());
						}
					}
				}
			}

			HashMap valueMapping = (HashMap) map.get("valueMapping");
			if (valueMapping != null) {
				Set<String> itemKey = valueMapping.keySet();
				Iterator<String> itemIter = itemKey.iterator();
				while (itemIter.hasNext()) {
					String key = itemIter.next();
					if (cellContent.equals(key)) {
						cellContent = (String) valueMapping.get(key);
					}
				}
			}

			if (map.get("dataType") != null) {
				String dataType = (String) map.get("dataType");
				if ("int".equals(dataType)) {

					try {
						Integer.parseInt(cellContent);
					} catch (Exception e) {
						e.printStackTrace();
						cellContent = Integer.MIN_VALUE + "";
					}

				} else if ("date".equals(dataType)) {
					if (StringUtil.isNotNull((String) cellContent)) {
						String famtter = (String) map.get("formatter");
						if (StringUtil.isNotNull(famtter)) {
							SimpleDateFormat sdf = new SimpleDateFormat(famtter);
							Date d = null;
							try {
								d = sdf.parse(cellContent);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							sdf = new SimpleDateFormat("yyyy-MM-dd");
							cellContent = sdf.format(d);
						}

						cellContent = "to_date('" + cellContent.toString() + "','yyyy-mm-dd')";
					}
					// if(cellContent instanceof Date)
					// {
					// Date date = ((Date) cellContent);
					// java.sql.Date d = new java.sql.Date(date.getTime());
					// cellContent = "to_date('" + d + "','yyyy-mm-dd')";
					// }else
					// {
					// if(StringUtil.isNotNull((String)cellContent))
					// {
					// cellContent = "to_date('" + cellContent.toString() +
					// "','yyyy-mm-dd')";
					// }
					// }
				} else {
					// cellContent = "'" + cellContent + "'";
				}
			}
			rowData.put(fieldName, cellContent.toString());
		}
		return cellContent;
	}

	private static String Object2Str(Object cellContent) {
		String s = "";
		if (cellContent instanceof Double) {
			s = String.format("%.2f", (Double) cellContent);
			// System.out.println(String.format("%.2f", (Double)cellContent));

			if (s.endsWith(".00")) {
				s = s.substring(0, s.length() - 3);
			}
		}

		if (cellContent instanceof Date) {
			Date date = ((Date) cellContent);
			java.sql.Date d = new java.sql.Date(date.getTime());
			s = d + "";
		}

		if (cellContent instanceof String) {
			s = (String) cellContent;
		}

		return s;
	}

}
