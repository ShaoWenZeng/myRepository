package com.cn.mySpringBootCustomer.controler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.collections.SynchronizedStack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.cn.mySpringBootCustomer.request.TestRequest;

@RestController
@PropertySource(value = {"classpath:application.properties"},encoding="utf-8")  
public class RequestFromHttp {
	
	@Value("${spring.dubbo.application.name}")  
	private String applicationName;
	
	@Value("${spring.dubbo.registry.address}") 
	private String address;
	
	private  Class<?> class1;
	
  private	GenericService genericService;
	//restful分 get post  put  delete
	//如果是post调用
	@RequestMapping(value="/getInfo/{version}/{interfacename}/{methodname}", method = RequestMethod.POST)
	@ResponseBody
	public Object getNameByPost(
			@PathVariable(value="version",required=false) String version,
			@PathVariable("interfacename") String interfacename,
			@PathVariable("methodname") String methodname,
			@RequestBody Map param
			){
		    Object result=null;  
	        try {
	        	if(genericService==null)
				 genericService = getClient(version, interfacename);
		//		result = genericService.$invoke(methodname, getMethodParamType(interfacename,methodname), new Object[] { param });
				result = genericService.$invoke(methodname, null, new Object[] { param });
				return  result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return  null;
		
	}

	public GenericService getClient(String version,String interfacename) throws Exception{
		 try {
			ApplicationConfig application = new ApplicationConfig();
			application.setName(applicationName);
			RegistryConfig registry = new RegistryConfig();
			registry.setAddress(address);
			ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
			reference.setApplication(application);
			reference.setRegistry(registry);
			reference.setInterface(interfacename);
			if(StringUtils.isNotEmpty(version)&&!" ".equals(version))
			reference.setVersion(version);
			reference.setGeneric(true); // 声明为泛化接口  
			ReferenceConfigCache cache = ReferenceConfigCache.getCache();
			GenericService genericService = cache.get(reference);
			return genericService;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}  
	  
	}
	
	 public static  String[] getMethodParamType(String interfaceName, String methodName) {
	        try {
	            //创建类
	            Class<?> class1=null;
				try {
					class1 = Class.forName(interfaceName);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("发生是你");
				}
	            //获取所有的公共的方法
	            Method[] methods = class1.getMethods();
	            for (Method method : methods) {
	                if (method.getName().equals(methodName)) {
	                    Class[] paramClassList = method.getParameterTypes();
	                    String[] paramTypeList = new String[paramClassList.length];
	                    int i = 0;
	                    for (Class className : paramClassList) {
	                        paramTypeList[i] = className.getTypeName();
	                        i++;
	                    }
	                    return paramTypeList;
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("反射获取参数类型出出");
	        }
	        return null;

	    
	}
	 
	 public  void g(String... d ){
		 for (String string : d) {
			
		}
	 }
}
