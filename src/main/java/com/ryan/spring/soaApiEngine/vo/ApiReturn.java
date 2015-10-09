package com.ryan.spring.soaApiEngine.vo;

import java.io.Serializable;

public class ApiReturn implements Serializable{


	private static final long serialVersionUID = -4856124769574151038L;
	private Boolean isCollection=false ;
	private Boolean isParameterizedType = false;
	private String parametersClass="";
	private String parameterizedClass="";
	private String CollectionClass="" ;
	private String className="" ;
	private Boolean isValueObject=false ;
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Boolean getIsValueObject() {
		return isValueObject;
	}
	public void setIsValueObject(Boolean isValueObject) {
		this.isValueObject = isValueObject;
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
