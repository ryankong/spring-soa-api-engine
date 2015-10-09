package com.ryan.spring.soaApiEngine.config.spring.schema;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.ryan.spring.soaApiEngine.APIScanEngine;

public class SoaApiEngineBeanDefinitionParser implements BeanDefinitionParser {

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
        String name = element.getAttribute("name");  
        String env = element.getAttribute("env");  
        String suffix = element.getAttribute("suffix") != null?element.getAttribute("suffix"):"";  
        RootBeanDefinition beanDefinition = new RootBeanDefinition();  
        beanDefinition.setBeanClass(APIScanEngine.class);  
        beanDefinition.getPropertyValues().addPropertyValue("applicationName", name);  
        beanDefinition.getPropertyValues().addPropertyValue("env", env); 
        beanDefinition.getPropertyValues().addPropertyValue("suffix", suffix); 
        parserContext.getRegistry().registerBeanDefinition("soaApiEngine", beanDefinition);  
  
        return beanDefinition;  
	}

}
