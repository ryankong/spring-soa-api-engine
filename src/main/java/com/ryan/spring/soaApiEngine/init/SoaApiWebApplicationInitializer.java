package com.ryan.spring.soaApiEngine.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;

import com.ryan.spring.soaApiEngine.servlet.SoaApiServlet;
/**
 * web初始时，添加apiEngine的servlet及mapping
 * @author Ryan.Kong(孔政)
 *
 */
public class SoaApiWebApplicationInitializer implements WebApplicationInitializer {

	public void onStartup(ServletContext servletContext)
			throws ServletException {
		ServletRegistration.Dynamic registration= servletContext.addServlet("##soaApi", new SoaApiServlet());
		registration.addMapping("/apiEngine/*");
	}
	
	

}