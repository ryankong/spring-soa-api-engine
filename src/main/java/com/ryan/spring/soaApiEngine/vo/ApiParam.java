package com.ryan.spring.soaApiEngine.vo;

import java.io.Serializable;


public class ApiParam implements Serializable{


	private static final long serialVersionUID = -4856124769574151038L;
	private String name="" ;
	private String className="" ;
	private String description="" ;
	private Boolean isJson=false ;
	private Boolean isValueObject=false ;
	private Boolean isCollection=false ;
	private String CollectionClass="" ;
	private Boolean required=false;
	private Boolean isParameterizedType = false;
	private String parametersClass="";
	private String parameterizedClass="";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsJson() {
		return isJson;
	}
	public void setIsJson(Boolean isJson) {
		this.isJson = isJson;
	}
	public Boolean getIsValueObject() {
		return isValueObject;
	}
	public void setIsValueObject(Boolean isValueObject) {
		this.isValueObject = isValueObject;
	}
	public Boolean getIsCollection() {
		return isCollection;
	}
	public void setIsCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}
	public String getCollectionClass() {
		return CollectionClass;
	}
	public void setCollectionClass(String collectionClass) {
		CollectionClass = collectionClass;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public Boolean getIsParameterizedType() {
		return isParameterizedType;
	}
	public void setIsParameterizedType(Boolean isParameterizedType) {
		this.isParameterizedType = isParameterizedType;
	}
	public String getParametersClass() {
		return parametersClass;
	}
	public void setParametersClass(String parametersClass) {
		this.parametersClass = parametersClass;
	}
	public String getParameterizedClass() {
		return parameterizedClass;
	}
	public void setParameterizedClass(String parameterizedClass) {
		this.parameterizedClass = parameterizedClass;
	}


	

}