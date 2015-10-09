package com.ryan.spring.soaApiEngine.config.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class SoaApiEngineNamespaceHandler extends NamespaceHandlerSupport  {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		registerBeanDefinitionParser("application",new SoaApiEngineBeanDefinitionParser());
	}

}
