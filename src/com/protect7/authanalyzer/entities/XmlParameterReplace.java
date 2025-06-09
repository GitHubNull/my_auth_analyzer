package com.protect7.authanalyzer.entities;

/**
 * XML参数替换规则实体类
 * 
 * 支持标准XPath语法，例如：
 * - /root/user/name - 简单元素访问
 * - /root/user[@id='123'] - 带属性条件的元素访问  
 * - //user - 递归搜索所有user元素
 * - /root/users/user[1] - 数组索引访问（从1开始）
 * - /root/user/@id - 访问属性值
 * - /root/user[position()=1]/name - 使用XPath函数
 * 
 * 更多语法请参考：https://www.w3.org/TR/xpath/
 */
public class XmlParameterReplace {
	
	/**
	 * XPath表达式，用于定位要修改的XML元素或属性
	 * 必须符合标准XPath语法规范
	 */
	private String xpath;
	
	/**
	 * 替换值，将原值替换为此值
	 * 支持字符串、数字、布尔值等类型
	 */
	private String replaceValue;
	
	/**
	 * 是否移除参数
	 * 如果为true，则会删除匹配的XML元素或属性
	 */
	private boolean remove;
	
	public XmlParameterReplace() {
		this.xpath = "";
		this.replaceValue = "";
		this.remove = false;
	}
	
	public XmlParameterReplace(String xpath, String replaceValue, boolean remove) {
		this.xpath = xpath;
		this.replaceValue = replaceValue;
		this.remove = remove;
	}
	
	public String getXpath() {
		return xpath;
	}
	
	public void setXpath(String xpath) {
		this.xpath = xpath;
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
		return "XmlParameterReplace{" +
				"xpath='" + xpath + '\'' +
				", replaceValue='" + replaceValue + '\'' +
				", remove=" + remove +
				'}';
	}
} 