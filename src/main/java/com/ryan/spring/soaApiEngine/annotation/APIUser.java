package com.ryan.spring.soaApiEngine.annotation;


/**
 * API使用方，如果方法未写明调用方, 则很有可能被清理掉，请再APIUser中注册
 * 
 * @author 吴春海 Molo.Wu
 * @since 2014-11-16
 */
public enum APIUser {
	MVO_TRADE("mvo", "trade"),
	PORTAL_TRADE("portal", "trade"),
	OTHER("other", "other")
	;
	
	private String system;
	private String module;
	
	/**
	 * @param system 使用系统，使用功能模块
	 * @param module
	 */
	private APIUser(String system, String module) {
//		GMSOAExceptionAssert.isNotBlank(system, GMSOAExceptionCode.PARAM_REQUIRED_EXCEPTION, "system is requires");
//		GMSOAExceptionAssert.isNotBlank(module, GMSOAExceptionCode.PARAM_REQUIRED_EXCEPTION, "module is requires");
		
		this.system = system;
		this.module = module;
	}
	
	/**
	 * 输出使用系统 和  使用模块，例如: "trade module in mvo system"
	 */
	@Override
	public String toString() {
		return String.format("%s module in %s system", module, system);
	}

	public String getSystem() {
		return system;
	}

	public String getModule() {
		return module;
	}
}
