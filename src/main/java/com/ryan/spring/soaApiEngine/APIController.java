package com.ryan.spring.soaApiEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ryan.spring.soaApiEngine.vo.ApiMethod;
import com.ryan.spring.soaApiEngine.vo.ApiValueObject;
/**
 * soa-api控制器层，现在只是静态类做跳板用，以后优化
 * @author Ryan.Kong(孔政)
 *
 */
public class APIController {

	private static APIScanEngine apiScanEngine;

	private static Map<String, Object> categoryApiMap = new HashMap<String, Object>();

	public static Map<String, Object> openAPI() {
		if (categoryApiMap.isEmpty()) {
			Map<String, List<ApiMethod>> api = apiScanEngine.getAllAPIMethod();
			categoryApiMap.put("api", api);
			categoryApiMap.put("applicationName", apiScanEngine.getApplicationName());
			categoryApiMap.put("env", apiScanEngine.getEnv());
		}

		return categoryApiMap;
	}

	public static ApiValueObject getValueObjectDesc(String className,String parameters)
			throws ClassNotFoundException {
		ApiValueObject apiValueObject = apiScanEngine
				.getValueObjectDesc(className,parameters);
		return apiValueObject;
	}

	public static void setApiScanEngine(APIScanEngine apiScanEngine) {
		APIController.apiScanEngine = apiScanEngine;
	}

}
