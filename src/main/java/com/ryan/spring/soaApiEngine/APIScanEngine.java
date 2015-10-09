package com.ryan.spring.soaApiEngine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ryan.spring.soaApiEngine.annotation.APIField;
import com.ryan.spring.soaApiEngine.annotation.APIMethod;
import com.ryan.spring.soaApiEngine.annotation.APIParam;
import com.ryan.spring.soaApiEngine.annotation.APIUser;
import com.ryan.spring.soaApiEngine.annotation.APIValueObject;
import com.ryan.spring.soaApiEngine.annotation.SoaService;
import com.ryan.spring.soaApiEngine.vo.ApiField;
import com.ryan.spring.soaApiEngine.vo.ApiMethod;
import com.ryan.spring.soaApiEngine.vo.ApiParam;
import com.ryan.spring.soaApiEngine.vo.ApiReturn;
import com.ryan.spring.soaApiEngine.vo.ApiValueObject;
/**
 * soa-api扫描引擎，用于根据代码里的配置，生成接口说明数据
 * @author Ryan.Kong(孔政)
 *
 */
public class APIScanEngine implements ApplicationContextAware{
	
	private String applicationName;
	
	private String env;
	
	private String suffix = "";//dispatcher的统一后缀
	
	private ApplicationContext applicationContext;
	
	private void getCategoryApiMethod(Map<String, List<ApiMethod>> categoryApiMap, Method method, String beanName) {
		APIMethod api = method.getAnnotation(APIMethod.class);
		RequestMapping reqMapping = method.getAnnotation(RequestMapping.class);
//		TODO 获取实际的mapping路径，而不是直接根据方法名，生成路径
//		reqMapping.path();
		String url = beanName + "/" + method.getName() + suffix;
		String category = api.category().toString();
		if (null == categoryApiMap.get(category)) {
			categoryApiMap.put(category, new ArrayList<ApiMethod>());
		}
		
		ApiMethod apiVO = new ApiMethod();
		apiVO.setName("[" + api.version() + "]" + api.name());
		apiVO.setCategory(category);
		apiVO.setUrl(new String[] { url });
		apiVO.setDescription(api.desc());
		List<String> apiUsers = new ArrayList<String>();
		for(APIUser apiUser : api.apiUsers()) {
			apiUsers.add(apiUser.toString());
		}
		apiVO.setApiUsers(apiUsers);
		apiVO.setHttp(method.getAnnotation(RequestMapping.class) != null);
		
		
		apiVO.setAuthors(api.authors());
		
		ApiReturn apiReturn = getApiReturn(method);
		apiVO.setApiReturn(apiReturn);
		
		List<ApiParam> paramVOList = getApiParamList(method);
		apiVO.setParam(paramVOList);
		
		categoryApiMap.get(category).add(apiVO);
	}
	
	private List<ApiParam> getApiParamList(Method method) {
		//Class<?> returnType = method.getReturnType();
		Class<?>[] paramterTypes = method .getParameterTypes();
		Annotation[][] paramAnnotations = method .getParameterAnnotations();
		ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		String[] paramNames = parameterNameDiscoverer .getParameterNames(method);
		List<ApiParam> paramVOList = new ArrayList<ApiParam>();
		for (int i = 0; i < paramterTypes.length; i++) {
			ApiParam apiParamVO = new ApiParam();
			apiParamVO.setClassName(paramterTypes[i] .getName());
			apiParamVO.setName(paramNames[i]);
			if (paramterTypes[i].isArray()) {
				apiParamVO.setIsCollection(true);
				apiParamVO.setCollectionClass(paramterTypes[i].getName());
				Class<?> elementType = paramterTypes[i].getComponentType();
				apiParamVO.setClassName(elementType.getName());
				boolean isValueObject = elementType.getAnnotation(APIValueObject.class) == null ? false : true;
				apiParamVO.setIsValueObject(isValueObject);
			} else if (Collection.class.isAssignableFrom(paramterTypes[i])) {
				apiParamVO.setIsCollection(true);
				apiParamVO.setCollectionClass(paramterTypes[i].getName());
				Type elementType = method.getGenericParameterTypes()[i];
				if (elementType != null && elementType instanceof ParameterizedType) {
					ParameterizedType pType = (ParameterizedType) elementType;
					Type acctualType=pType.getActualTypeArguments()[0];
					if (acctualType !=null && acctualType instanceof Class) {
						Class<?> genericClazz = (Class<?>) pType.getActualTypeArguments()[0];
						apiParamVO.setClassName(genericClazz.getName());
						boolean isValueObject = genericClazz.getAnnotation(APIValueObject.class) == null ? false : true;
						apiParamVO.setIsValueObject(isValueObject);	
					}else{
						apiParamVO.setClassName(acctualType.toString());
						apiParamVO.setIsValueObject(false);	
					}
				}
			} else if(paramterTypes[i].getAnnotation(APIValueObject.class)!=null){
				apiParamVO.setIsValueObject(true);
				apiParamVO.setIsCollection(false);
			} else {
				apiParamVO.setIsValueObject(false);
				apiParamVO.setIsCollection(false);
			}

			for (int j = 0; j < paramAnnotations[i].length; j++) {
				if (0 != paramAnnotations[i].length
						&& null != paramAnnotations[i][j]) {
					if (paramAnnotations[i][j] .annotationType().equals( APIParam.class)) {
						APIParam openAPIParam = (APIParam) paramAnnotations[i][j];
						apiParamVO.setDescription(openAPIParam .value());
					}
					if (paramAnnotations[i][j] .annotationType().equals( RequestBody.class)) {
						apiParamVO.setIsJson(true);
					}
				}
			}
			paramVOList.add(apiParamVO);
		}
		return paramVOList;
	}
	
	private ApiReturn getApiReturn(Method method) {
		Class<?> returnClass = method.getReturnType();
		Type returnType = method.getGenericReturnType();
		ApiReturn apiReturn = new ApiReturn();
		if (returnClass.isArray()) {
			apiReturn.setIsCollection(true);
			apiReturn.setCollectionClass(returnClass.getName());
			Class<?> elementType = returnClass.getComponentType();
			apiReturn.setClassName(elementType.getName());
			boolean isValueObject = elementType.getAnnotation(APIValueObject.class) == null ? false : true;
			apiReturn.setIsValueObject(isValueObject);
		} else if (Collection.class.isAssignableFrom(returnClass)) {
			apiReturn.setIsCollection(true);
			apiReturn.setCollectionClass(returnClass.getName());
			Type elementType = method.getGenericReturnType();
			if (elementType != null && elementType instanceof ParameterizedType) {
				ParameterizedType pType = (ParameterizedType) elementType;
				Type acctualType=pType.getActualTypeArguments()[0];
				if (acctualType !=null && acctualType instanceof Class) {
					Class<?> genericClazz = (Class<?>) pType.getActualTypeArguments()[0];
					apiReturn.setClassName(genericClazz.getName());
					boolean isValueObject = genericClazz.getAnnotation(APIValueObject.class) == null ? false : true;
					apiReturn.setIsValueObject(isValueObject);	
				}else{
					apiReturn.setClassName(acctualType.toString());
					apiReturn.setIsValueObject(false);	
				}

			}
		} else if (returnType instanceof ParameterizedType){
			boolean isValueObject = returnClass.getAnnotation(APIValueObject.class) == null ? false : true;
			Type[] typeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
			apiReturn.setIsParameterizedType(true);
			apiReturn.setParameterizedClass(returnClass.getName());
			
			String parametesClassStr = "";
			for(Type ty:typeArguments){
				parametesClassStr += parametesClassStr + ty.getTypeName();
			}
			apiReturn.setParametersClass(parametesClassStr);
			apiReturn.setIsValueObject(isValueObject);	
			apiReturn.setIsCollection(false);
			apiReturn.setClassName(returnType.toString());
		}else if (returnClass.getAnnotation(APIValueObject.class) != null) {
			apiReturn.setIsValueObject(true);
			apiReturn.setIsCollection(false);
			apiReturn.setClassName(returnClass.getName());
		} else {
			apiReturn.setIsValueObject(false);
			apiReturn.setIsCollection(false);
			apiReturn.setClassName(returnClass.getName());
		}
		return apiReturn;
	}
	
	public Map<String, List<ApiMethod>> getAllAPIMethod() {
		final Map<String, List<ApiMethod>> categoryApiMap = new HashMap<String, List<ApiMethod>>();
		Map<String, Object> map = applicationContext.getBeansWithAnnotation(SoaService.class);
		for (final String beanName : map.keySet()) {
			Object controller = map.get(beanName);
			ReflectionUtils.doWithMethods(controller.getClass(), new ReflectionUtils.MethodCallback() {
				@Override
				public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
					 getCategoryApiMethod(categoryApiMap, method, beanName);
				}
			},new ReflectionUtils.MethodFilter() {
				@Override
				public boolean matches(Method method) {
					if (AnnotationUtils.getAnnotation(method, APIMethod.class) != null) {
						return true;
					}
					return false;
				}
			});
		}
		return categoryApiMap;
	}

	public List<ApiParam> getMethodParam(String beanName,
			final String methodName) {
		Object bean = applicationContext.getBean(beanName);
		if (null == bean) {
			throw new NoSuchBeanDefinitionException(beanName);
		}
		final List<ApiParam> paramVOList = new ArrayList<ApiParam>();
		final List<Method> foundMethodList = new ArrayList<Method>();
		ReflectionUtils.doWithMethods(bean.getClass(),
				new ReflectionUtils.MethodCallback() {
					public void doWith(Method method)
							throws IllegalArgumentException {

						foundMethodList.add(method);
						ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
						String[] paramNames = parameterNameDiscoverer
								.getParameterNames(method);
						Class<?>[] paramterTypes = method.getParameterTypes();
						Class<?>[] params = method.getParameterTypes();
						Annotation[][] paramAnnotations = method
								.getParameterAnnotations();
						for (int i = 0; i < params.length; i++) {
							ApiParam apiParamVO = new ApiParam();
							apiParamVO.setClassName(paramterTypes[i].getName());
							apiParamVO.setName(paramNames[i]);

							for (int j = 0; j < paramAnnotations[i].length; j++) {
								if (0 != paramAnnotations[i].length
										&& null != paramAnnotations[i][j]) {
									if (paramAnnotations[i][j].annotationType()
											.equals(APIParam.class)) {
										APIParam openAPIParam = (APIParam) paramAnnotations[i][j];
										apiParamVO.setDescription(openAPIParam
												.value());
									}
									if (paramAnnotations[i][j].annotationType()
											.equals(RequestBody.class)) {
										apiParamVO.setIsJson(true);
									}

								}
							}
							paramVOList.add(apiParamVO);
						}

					}
				}, new ReflectionUtils.MethodFilter() {

					@Override
					public boolean matches(Method method) {

						if (ClassUtils.isCglibProxyClassName(method
								.getDeclaringClass().getName())) {
							return false;

						}
						if (Proxy.isProxyClass(method.getDeclaringClass()
								.getClass())) {
							return false;

						}
						if (method.getName().equalsIgnoreCase(methodName)) {
							return true;

						}

						return false;
					}

				});

		if (foundMethodList.isEmpty()) {
			throw new NoSuchMethodError(beanName.getClass().toString()
					+ methodName);
		}

		return paramVOList;
	}
	
	/**
	 * 
	 * @param className
	 * @param parameters 泛型的参数，英文逗号分隔
	 * @return
	 * @throws ClassNotFoundException
	 */
	public   ApiValueObject getValueObjectDesc(String className,String parameters) throws ClassNotFoundException {
		Class<?> objectValueClazz = Class.forName(className);
		ArrayList<Class> parameterClassArr = new ArrayList<Class>();
		ResolvableType resolvableType;
		if(StringUtils.isNotBlank(parameters)){
			for(String parameterClassName:parameters.split(",")){
				parameterClassArr.add(Class.forName(parameterClassName));
			}
			resolvableType = ResolvableType.forClassWithGenerics(objectValueClazz, (Class[])parameterClassArr.toArray(new Class[parameterClassArr.size()]));
		}else{
			resolvableType = ResolvableType.forClass(objectValueClazz);
		}
		
		APIValueObject valueObjectAnnotation = objectValueClazz.getAnnotation(APIValueObject.class);
		ApiValueObject apiValueObject = new ApiValueObject();
		apiValueObject.setClassName(className);
		apiValueObject.setDescription(valueObjectAnnotation.value());
		List<ApiField> apiFieldVOList = apiValueObject.getApiFields();
		Field[] fields = objectValueClazz.getDeclaredFields();
		//递归父类，直到父类不带@APIValueObject
		Class<?> parenClazz = objectValueClazz.getSuperclass();
		while(parenClazz.getAnnotation(APIValueObject.class)!=null){
			fields = (Field[]) ArrayUtils.addAll(fields, parenClazz.getDeclaredFields());
			parenClazz = parenClazz.getSuperclass();
		}
		for (Field field : fields) {
			APIField fieldDescription = field.getAnnotation(APIField.class);
			if (null != fieldDescription) {
				ApiField apiFieldVO = new ApiField();
				apiFieldVO.setName(field.getName());
				apiFieldVO.setDescription(fieldDescription.value());
				
				if (field.getType().isArray()) {
					apiFieldVO.setIsCollection(true);
					apiFieldVO.setCollectionClass(field.getType().getName());
//				    Class<?> elementType = field.getType().getComponentType();
					Class<?> elementType = ResolvableType.forField(field, resolvableType).getComponentType().resolve();
				    apiFieldVO.setClassName(elementType.getName());
					boolean isValueObject=elementType.getAnnotation(APIValueObject.class)==null?false:true;
					apiFieldVO.setIsValueObject(isValueObject);
					
				}else if (Collection.class.isAssignableFrom(field.getType())) {
					apiFieldVO.setIsCollection(true);
					apiFieldVO.setCollectionClass(field.getType().getName());
					Type elementType=field.getGenericType();
					
					if (elementType!=null) {
						 if(elementType instanceof ParameterizedType)  
				            {   
									 ParameterizedType pType = (ParameterizedType) elementType;
//									 Class<?> genericClazz = (Class<?>) pType.getActualTypeArguments()[0];
									 Class<?> genericClazz =ResolvableType.forField(field, resolvableType).getGeneric(0).resolve();
									 apiFieldVO.setClassName(genericClazz.getName());
				                   boolean isValueObject=genericClazz.getAnnotation(APIValueObject.class)==null?false:true;
				                   apiFieldVO.setIsValueObject(isValueObject);

				                 
				             }   
					}
				
					
				}else if(field.getType().getAnnotation(APIValueObject.class)!=null){
					apiFieldVO.setIsValueObject(true);
					apiFieldVO.setIsCollection(false);
//					apiFieldVO.setClassName(field.getType().getName());
					apiFieldVO.setClassName(ResolvableType.forField(field, resolvableType).resolve().getName());
				}else {
					apiFieldVO.setIsValueObject(false);
					apiFieldVO.setIsCollection(false);
//					apiFieldVO.setClassName(field.getType().getName());
					apiFieldVO.setClassName(ResolvableType.forField(field, resolvableType).resolve().getName());
				
				}
				apiFieldVOList.add(apiFieldVO);
			}
		}
		return   apiValueObject;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		APIController.setApiScanEngine(this);
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	
	
}