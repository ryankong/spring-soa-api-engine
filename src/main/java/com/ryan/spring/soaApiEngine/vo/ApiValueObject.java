package com.ryan.spring.soaApiEngine.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ApiValueObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2627064423644962721L;
	/**
	 * 
	 */
	private String description ;
	private String className ;
	private List<ApiField> apiFields=new ArrayList<ApiField>();


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<ApiField> getApiFields() {
		return apiFields;
	}
	public void setApiFields(List<ApiField> apiFields) {
		this.apiFields = apiFields;
	}


}