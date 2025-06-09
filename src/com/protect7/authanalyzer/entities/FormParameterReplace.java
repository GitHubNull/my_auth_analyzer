package com.protect7.authanalyzer.entities;

/**
 * Form参数替换规则实体类
 * 
 * 支持application/x-www-form-urlencoded和multipart/form-data格式的参数替换
 * 可以替换或删除指定的form参数
 */
public class FormParameterReplace {
	
	/**
	 * 参数名称，要替换的form参数名
	 */
	private String parameterName;
	
	/**
	 * 替换值，将原值替换为此值
	 */
	private String replaceValue;
	
	/**
	 * 是否移除参数
	 * 如果为true，则会删除匹配的form参数
	 */
	private boolean remove;
	
	public FormParameterReplace() {
		this.parameterName = "";
		this.replaceValue = "";
		this.remove = false;
	}
	
	public FormParameterReplace(String parameterName, String replaceValue, boolean remove) {
		this.parameterName = parameterName;
		this.replaceValue = replaceValue;
		this.remove = remove;
	}
	
	public String getParameterName() {
		return parameterName;
	}
	
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
	public String getReplaceValue() {
		return replaceValue;
	}
	
	public void setReplaceValue(String replaceValue) {
		this.replaceValue = replaceValue;
	}
	
	public boolean isRemove() {
		return remove;
	}
	
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	
	@Override
	public String toString() {
		return "FormParameterReplace{" +
				"parameterName='" + parameterName + '\'' +
				", replaceValue='" + replaceValue + '\'' +
				", remove=" + remove +
				'}';
	}
} 