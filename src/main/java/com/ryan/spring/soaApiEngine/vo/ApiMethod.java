package com.ryan.spring.soaApiEngine.vo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiMethod implements Serializable {
	private static final long serialVersionUID = 149056125227376502L;

	private String name;

	private String category;

	private String description;

	private String[] url;

	List<ApiParam> param = new ArrayList<ApiParam>();

	private ApiReturn apiReturn;
	
	private boolean http;
	
	private List<String> apiUsers;
	
	private String authors;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getUrl() {
		return url;
	}

	public void setUrl(String[] url) {
		this.url = url;
	}

	public List<ApiParam> getParam() {
		return param;
	}

	public void setParam(List<ApiParam> param) {
		this.param = param;
	}

	public ApiReturn getApiReturn() {
		return apiReturn;
	}

	public void setApiReturn(ApiReturn apiReturn) {
		this.apiReturn = apiReturn;
	}

	public boolean isHttp() {
		return http;
	}

	public void setHttp(boolean http) {
		this.http = http;
	}

	public List<String> getApiUsers() {
		return apiUsers;
	}

	public void setApiUsers(List<String> apiUsers) {
		this.apiUsers = apiUsers;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	@Override
	public String toString() {
		return "ApiMethod [name=" + name + ", category=" + category
				+ ", description=" + description + ", url="
				+ Arrays.toString(url) + ", param=" + param + ", apiReturn="
				+ apiReturn + ", http=" + http + ", apiUsers=" + apiUsers
				+ ", authors=" + authors + "]";
	}


}
