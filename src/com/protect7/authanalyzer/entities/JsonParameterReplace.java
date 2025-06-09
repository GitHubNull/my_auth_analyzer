package com.protect7.authanalyzer.entities;

/**
 * JSON参数替换规则实体类
 * 
 * 支持标准JSON Path语法，例如：
 * - $.user.name - 简单属性访问
 * - $.store.book[0].title - 数组索引访问  
 * - $.store.book[*].author - 通配符访问
 * - $..price - 递归搜索
 * - $.store.book[?(@.price < 10)] - 条件过滤
 * - $.store.book[0:2] - 数组切片
 * 
 * 更多语法请参考：https://github.com/json-path/JsonPath
 */
public class JsonParameterReplace {
	
	/**
	 * JSON Path表达式，用于定位要修改的JSON元素
	 * 必须符合标准JSON Path语法规范
	 */
	private String jsonPath;
	
	/**
	 * 替换值，将原值替换为此值
	 * 支持字符串、数字、布尔值、null等类型
	 */
	private String replaceValue;
	
	/**
	 * 是否移除参数
	 * 如果为true，则会删除匹配的JSON元素
	 */
	private boolean remove;
	
	public JsonParameterReplace() {
		this.jsonPath = "";
		this.replaceValue = "";
		this.remove = false;
	}
	
	public JsonParameterReplace(String jsonPath, String replaceValue, boolean remove) {
		this.jsonPath = jsonPath;
		this.replaceValue = replaceValue;
		this.remove = remove;
	}
	
	public String getJsonPath() {
		return jsonPath;
	}
	
	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}
	
	/**
	 * @deprecated 使用getJsonPath()代替
	 */
	@Deprecated
	public String getParameterPath() {
		return jsonPath;
	}
	
	/**
	 * @deprecated 使用setJsonPath()代替  
	 */
	@Deprecated
	public void setParameterPath(String parameterPath) {
		this.jsonPath = parameterPath;
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
		return "JsonParameterReplace{" +
				"jsonPath='" + jsonPath + '\'' +
				", replaceValue='" + replaceValue + '\'' +
				", remove=" + remove +
				'}';
	}
} 