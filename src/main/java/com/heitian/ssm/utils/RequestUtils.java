package com.heitian.ssm.utils;


import com.hui.platform.framework.dao.HuiPageCond;
import com.hui.platform.framework.domain.view.JsonResponse;
import com.hui.platform.system.Constants;
import com.hui.platform.system.utility.DatetimeUtil;
import com.hui.platform.system.utility.JsonUtil;
import com.hui.platform.system.utility.StringUtil;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;


import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Controll层公用方法
 * 
 * @author Administrator
 * 
 */
public class RequestUtils {

	public static String getOrderCond(HttpServletRequest request) {
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");
		String order = "";
		if (StringUtil.isNotNullAndBlank(sort)) {
			if (sort.endsWith("4")) {
				sort = sort.substring(0, sort.length() - 1);
			}
			if (StringUtil.isNotNullAndBlank(dir)) {
				order = sort + " " + dir;
			} else {
				order = sort;
			}
		}
		return order;
	}

	/**
	 * 设置分页信息
	 * 
	 * @param request
	 * @return
	 */
	public static HuiPageCond getHuiPage(HttpServletRequest request) {
		String isPaginator = request.getParameter("isPaginator") == null ? "N"
				: "Y";
		int startIndex = 0;
		int results = 0;
		boolean isCount = false;
		if ("Y".equals(isPaginator)) {
			startIndex = request.getParameter("page.begin") == null ? 0
					: Integer.parseInt(request.getParameter("page.begin"));
			results = request.getParameter("page.length") == null ? BusinessConstants.DefaultResultLength
					: Integer.parseInt(request.getParameter("page.length"));
			isCount = request.getParameter("page.isCount") == null ? false
					: StringUtil
							.toBoolean(request.getParameter("page.isCount"));
		} else {
			startIndex = request.getParameter("start") == null ? 0 : Integer
					.parseInt(request.getParameter("start"));

			String[] vString = request.getParameterValues("limit");
			if (null != vString) {
				results = Integer.parseInt(vString[vString.length - 1]);
			} else {
				results = BusinessConstants.DefaultResultLength;
			}

			// results = request.getParameter("limit") == null ?
			// BusinessConstants.DefaultResultLength
			// : Integer.parseInt(request.getParameter("limit"));
			isCount = true;
		}
		if (request.getParameter("NOPAGE") != null) {
			return null;
		}
		HuiPageCond pg = new HuiPageCond(startIndex, results, isCount);
		return pg;
	}

	/**
	 * 处理返回结果
	 * 
	 * @param request
	 * @return
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static ModelAndView getJsonLibView(HttpServletRequest request,
											  boolean flag, Object list, HuiPageCond pg, Map params) {
		String pageName = request == null ? null : request
				.getParameter("pageName");
		if (pageName == null) {
			pageName = Constants.RESPONSE_VIEW4JSONLIB;
		}
		ModelAndView modelAndView = new ModelAndView(pageName);
		modelAndView.addObject("page", pg);
		modelAndView.addObject("queryparam", params);
		// JSON data = JsonUtil.toJSON(list);
		JsonResponse res = new JsonResponse(flag);
		res.setData(list);
		// if (isJSON) {
		// JSON data = JsonUtil.toJSON(list);
		// System.out.println(data.toString());
		// res.setData(data);
		// } else {
		// res.setData(list);
		// }
		res.setExtension(pg);
		modelAndView.addObject(Constants.RESPONSE_DATANAME, res);
		if (flag) {
			modelAndView.addObject(BusinessConstants.AJAXTRUE,
					new Boolean(flag));
		} else {
			modelAndView.addObject(BusinessConstants.AJAXTRUE,
					new Boolean(flag));
		}
		return modelAndView;
	}

	/**
	 * 处理返回结果
	 * 
	 * @param request
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static ModelAndView getModelAndView(HttpServletRequest request,
											   boolean flag, Object list, HuiPageCond pg, Map params,
											   boolean isJSON) {
		String pageName = request == null ? null : request
				.getParameter("pageName");
		if (pageName == null) {
			pageName = Constants.RESPONSE_VIEW;
		}
		ModelAndView modelAndView = new ModelAndView(pageName);
		modelAndView.addObject("page", pg);
		modelAndView.addObject("queryparam", params);
		JsonResponse res = new JsonResponse(flag);
		if (list != null) {
			res.setData(list);
			if (isJSON) {
				JSON data = (JSON) JsonUtil.toJSON(list);
				// System.out.println(data.toString());
				res.setData(data);
			} else {
				res.setData(list);
			}
		} else {

		}
		res.setExtension(pg);
		modelAndView.addObject(Constants.RESPONSE_DATANAME, res);
		if (flag) {
			modelAndView.addObject(BusinessConstants.AJAXTRUE,
					new Boolean(flag));
		} else {
			modelAndView.addObject(BusinessConstants.AJAXFAILURE, new Boolean(
					flag));
		}
		return modelAndView;
	}

	/**
	 * 处理返回结果
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ModelAndView getModelAndView(HttpServletRequest request,
											   boolean flag, Object list, HuiPageCond pg, Map params) {
		return getModelAndView(request, flag, list, pg, params, false);
	}

	/**
	 * 将request中的对象set 到domain
	 * 
	 * @param request
	 * @param c
	 * @return
	 * @deprecated
	 */
	@Deprecated
	public static Object getEntity(HttpServletRequest request, Class<?> c) {
		Map<?, ?> mp = request.getParameterMap(); // 获取客户端传递过来的所有参数
		Object entityObj = null; // 定义一个Object类，最终将返回他
		try {
			entityObj = c.newInstance(); // 实例化entityObj，类型为参数2
			Method[] methods = c.getDeclaredMethods(); // 获取该类中定义的所有方法
			for (Method m : methods) { // 遍历这个方法数组
				if (m.getParameterTypes().length > 0
						&& m.getName().startsWith("set")) { // 找出里面所有的set方法，准备赋值

					String mapKey = m.getName().substring(3).toUpperCase(); // 获取map的key值，即request过来的参数名称
					Object mapValues = mp.get(mapKey) == null ? mp.get(mapKey
							.toLowerCase()) : mp.get(mapKey); // 获取参数的值

					if (mapValues != null) { // 如果参数不等于空
						String parameter = StringUtil
								.arrayToCommaDelimitedString((String[]) mapValues); // 取出参数
						// System.out.println("method :"+m
						// +"	parameter:"+parameter);
						setValue2Method(m, entityObj, parameter);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entityObj; // 返回对象
	}

	/**
	 * 将request中的对象set 到domain
	 * 
	 * @param request
	 * @param c
	 * @return
	 * @deprecated
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	public static Object getEntity(HttpServletRequest request, Class<?> c,
								   String prefix) {
		Map<?, ?> mp = request.getParameterMap(); // 获取客户端传递过来的所有参数
		Object entityObj = null; // 定义一个Object类，最终将返回他
		try {
			entityObj = c.newInstance(); // 实例化entityObj，类型为参数2
			Method[] methods = c.getDeclaredMethods(); // 获取该类中定义的所有方法
			Iterator it = mp.keySet().iterator();
			while (it.hasNext()) {
				String mapKey = it.next().toString();
				if (mapKey.startsWith(prefix)) {
					Object mapValues = mp.get(mapKey); // 获取参数的值
					if (mapValues == null) {
						continue;
					}
					String parameter = StringUtil
							.arrayToCommaDelimitedString((String[]) mapValues);// ((String[])mapValues)[0];
					// //取出参数
					for (Method m : methods) { // 遍历这个方法数组
						if (m.getParameterTypes().length > 0
								&& m.getName().startsWith("set")) { // 找出里面所有的set方法，准备赋值
							String methodName = m.getName().substring(3);// 获取类里方法名

							if (methodName.equalsIgnoreCase(mapKey
									.substring(prefix.length() + 1))) {

								setValue2Method(m, entityObj, parameter);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entityObj; // 返回对象
	}

	/**
	 * 将request中的对象set 到domain
	 * 
	 * @param request
	 * @param c
	 * @return
	 * @deprecated
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	public static Object getEntityIgnoreCase(HttpServletRequest request,
			Class<?> c) {
		Map<?, ?> mp = request.getParameterMap(); // 获取客户端传递过来的所有参数
		Object entityObj = null; // 定义一个Object类，最终将返回他
		try {
			entityObj = c.newInstance(); // 实例化entityObj，类型为参数2
			Method[] methods = c.getDeclaredMethods(); // 获取该类中定义的所有方法
			Iterator it = mp.keySet().iterator();
			while (it.hasNext()) {
				String mapKey = it.next().toString();
				Object mapValues = mp.get(mapKey); // 获取参数的值
				if (mapValues == null) {
					continue;
				}
				String parameter = StringUtil
						.arrayToCommaDelimitedString((String[]) mapValues);// ((String[])mapValues)[0];
				// //取出参数
				for (Method m : methods) { // 遍历这个方法数组
					if (m.getParameterTypes().length > 0
							&& m.getName().startsWith("set")) { // 找出里面所有的set方法，准备赋值
						String methodName = m.getName().substring(3);// 获取类里方法名

						if (methodName.equalsIgnoreCase(mapKey)) {

							setValue2Method(m, entityObj, parameter);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entityObj; // 返回对象
	}

	/**
	 * 将request中的对象set 到domain
	 * 
	 * @param request
	 * @param c
	 * @param jsonString
	 * @return
	 * @deprecated
	 */
	@Deprecated
	public static List<Object> getEntityList(HttpServletRequest request,
			Class<?> c, String jsonString) {
		List<Object> list = new ArrayList<Object>();
		jsonString = jsonString.replace(",\"undefined\":\"\"", "");
		JSONArray jsonarray = new JSONArray();
		jsonarray = JSONArray.fromObject(jsonString);
		for (int i = 0; i < jsonarray.size(); i++) {
			JSONObject js = jsonarray.getJSONObject(i);// 根据字符串转换对象
			Object bean = JSONObject.toBean(js, c); // 把值绑定成相应的值对象
			list.add(bean);
		}
		return list; // 返回对象
	}

	/**
	 * 
	 * @param m
	 * @param entityObj
	 * @param parameter
	 * @throws Exception
	 * @deprecated
	 */
	@Deprecated
	private static void setValue2Method(Method m, Object entityObj,
			String parameter) throws Exception {

		if (m.getParameterTypes()[0].getSimpleName().equals("int")
				|| m.getParameterTypes()[0].getSimpleName().equals("Integer")) { // 如果参数类型是int型的
			if (!parameter.equals("")) {
				m.invoke(entityObj, Integer.parseInt(parameter)); // 执行set方法，将参数设为Int型
			}
		} else if (m.getParameterTypes()[0].getSimpleName().equals("double")) {// 如果参数类型是Double型的
			if (!parameter.equals("")) {
				m.invoke(entityObj, Double.parseDouble(parameter));// 执行set方法
			}
		} else if (m.getParameterTypes()[0].getSimpleName().equals("String")) { // 如果参数类型是String型的
			if (parameter != null) {
				m.invoke(entityObj, parameter);// 执行set方法
			}
		} else if (m.getParameterTypes()[0].getSimpleName().equals("Date")) { // 如果参数类型是String型的
			if (parameter != null) {
				m.invoke(entityObj, DatetimeUtil.formatString(parameter,
						BusinessConstants.DATEFORMAT));// 执行set方法
			}
		}
	}

	/**
	 * 将request中的对象映射到map
	 * 
	 * @deprecated
	 */
	@Deprecated
	public static Map<String, String> buildParamsByNamesFromRequest(
			HttpServletRequest request, String names) {
		HashMap<String, String> result = new HashMap<String, String>();
		String[] namesArray = names.split(",");
		for (String name : namesArray) {
			String paramName = name;
			String paramValue = null;
			if (name.indexOf("|") > 0) {
				String[] nameValue = name.split("[|]");
				paramName = nameValue[0];
				paramValue = nameValue[1];
			}
			String nameObject = request.getParameter(paramName);
			if (paramName.startsWith("Q_")) {
				paramName = paramName.substring(2);
			}
			if (StringUtil.isNotNull(nameObject)) {
				result.put(paramName, nameObject.trim());
			} else {
				if (paramValue != null)
					result.put(paramName, paramValue.trim());
			}
		}
		return result;
	}

	/**
	 * 将request中的对象映射到map
	 * 
	 */
	public static Map<String, String> buildParamsByNamesFromRequest(
			HttpServletRequest request) {
		Map<?, ?> mp = request.getParameterMap(); // 获取客户端传递过来的所有参数
		@SuppressWarnings("unchecked")
		Set<String> keySet = (Set<String>) mp.keySet();
		HashMap<String, String> result = new HashMap<String, String>();
		for (String name : keySet) {
			String paramName = name;
			String paramValue = null;
			if (name.indexOf("|") > 0) {
				String[] nameValue = name.split("[|]");
				paramName = nameValue[0];
				paramValue = nameValue[1];
			}
			String nameObject = request.getParameter(paramName);
			if (paramName.startsWith("Q_")) {
				paramName = paramName.substring(2);
				if (StringUtil.isNotNull(nameObject)) {

					if (paramName.endsWith("_start")
							|| paramName.endsWith("_end")) {
						if (paramName.endsWith("_start")) {
							result.put(paramName + "_dbopt", ">=");
						} else {
							result.put(paramName + "_dbopt", "<=");
						}

						// paramName =
						// paramName.substring(0,paramName.length()-2);

						toDBMap4Oracle(result, paramName, nameObject);

					} else {
						if (paramName.endsWith("_dbopt")
								&& nameObject.equals("!")) {
							result.put(paramName, "!=");
						} else {
							result.put(paramName, nameObject.trim());
						}
					}

				} else {
					if (paramValue != null)
						result.put(paramName, paramValue.trim());
				}
			}

		}
		return result;
	}

	private static void toDBMap4Oracle(HashMap<String, String> result,
			String paramName, String nameObject) {
		if (nameObject.trim().indexOf(" ") > 0) {
			// result.put(paramName,
			// "to_date('"+nameObject.trim()+"','yyyy-mm-dd hh24:mi:ss')");

			result.put(paramName, nameObject.trim());

		} else {

			if (paramName.endsWith("_start")) {
				// result.put(paramName,
				// "to_date('"+nameObject.trim()+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
				result.put(paramName, nameObject.trim() + "000000");
			} else {
				// result.put(paramName,
				// "to_date('"+nameObject.trim()+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
				result.put(paramName, nameObject.trim() + "235959");
			}
		}
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public static String getUserAgent(HttpServletRequest request) {
		String ua = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(ua);  
	    Browser browser = userAgent.getBrowser();  
	    OperatingSystem os = userAgent.getOperatingSystem();
	    System.out.println(ua);


		return "";
	}

}
