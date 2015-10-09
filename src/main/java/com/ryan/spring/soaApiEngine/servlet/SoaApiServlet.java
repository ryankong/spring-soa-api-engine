package com.ryan.spring.soaApiEngine.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.ryan.spring.soaApiEngine.APIController;
/**
 * apiEngine的servlet,只要提供getMethodList和getVODetail的方法入口
 * @author Ryan.Kong(孔政)
 *
 */
public class SoaApiServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		//获取访问的uri
		String requestURI = request.getRequestURI();
		Object rs = null;
		if(requestURI.endsWith("/apiEngine/getMethodList")){
			String categoryName = request.getParameter("categoryName");
			
			rs = APIController.openAPI();
			
		}else if(requestURI.endsWith("/apiEngine/getVODetail")){
			String voClassName = request.getParameter("voClassName");
			String parametersClassName = request.getParameter("parametersClassName");
			try {
				rs = APIController.getValueObjectDesc(voClassName,parametersClassName);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				rs = "error";
			}
		}else {
			rs = "not fund";
		}

		// 生成HTTP响应结果
		PrintWriter out;
		String title = "HelloServlet";
		// content type
		response.setContentType("application/json;charset=UTF-8");
		// write html page
		out = response.getWriter();
		
		out.print(JSON.toJSONString(rs));

		out.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		this.doGet(req, res);
	}
}
