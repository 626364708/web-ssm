package com.heitian.ssm.utils;

/**
 * 业务相关的常量定义
 * @author Administrator
 *
 */
public class BusinessConstants {
	
	/** 应用相关的常量  **/
	public static final String WEBAPP_PATH="basic";
	public static final String ACTION_METHOD="method";
	public static final String DATEFORMAT="yyyy-MM-dd HH:mm:ss";
	public static final String DATEFORMAT2DATE="yyyy-MM-dd";
	public static final String PROMPT_SYSERROR="系统处理异常，请联系系统管理员";
	public static final int DefaultResultLength = 10;
	
	/**
	 * 用户登录
	 */
	public static final  String USERSTATUS_NOTEXIST_MSG = "用户不存在";
	public static final  String USERSTATUS_PWDERROR_MSG = "用户密码不正确";
	public static final  String USERSTATUS_LOCKED_MSG = "用户被锁定";
	public static final  String ENCRYPT_SALT = "javacspring@gmail.com";
	
	/**
	 * 异常阀值相关  
	 */
	public static final  String EXEC_HOLD = "exec_hold";
	public static final  String REPORTHOLD = "reporthold";
	public static final  String EXTENSIONHOLD = "ExtensionHold";
	public static final  String EXEC_KIND = "exec_kind";
	
	/**
	 * 流程状态
	 * */
	public static final String REQ_STATUS_FINISHED="1";//运行完的
	public static final String REQ_STATUS_WAITED="3";//处理中
	public static final String REQ_STATUS_REFUSED="2";//拒绝,回退,中止
	public static final String REQ_STATUS_SUSPENDED="4";//暂停
	
	/**
	 * 业务字典常量
	 */
	public static final String SYS_STATUS_LOCKED="0";//未启用
	public static final String SYS_STATUS_UNLOCKED="1";//启用
	
	
	
	public static final String TABLEPK="TABLEPK";
	
	/**
	 * ajax 返回标识
	 */
	public static final String AJAXTRUE="success";
	public static final String AJAXFAILURE="failure";
	
	
	
	public static final String LOG_FLAG_1="01";
	public static final String LOG_FLAG_2="02";
	public static final String LOG_FLAG_3="03";
}
