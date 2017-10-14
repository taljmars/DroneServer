package com.dbanalyzer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;


@ComponentScan("com.dbanalyzer")
@ComponentScan("com.db.persistence.exceptionAdviser")
@Configuration
public class AppConfig
{

	public static ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

	private static final String IP = "127.0.0.1";
	//private static String IP = "178.62.1.156";
	
	private static <T> T LoadServices(Class<T> clz) {
		try {
			System.err.println("Got " + clz.getSimpleName());
			//URL url = new URL("http://localhost:9999/ws/" + clz.getSimpleName() + "?wsdl");
			URL url = new URL("http://" + IP + ":1234/ws/" + clz.getSimpleName() + "?wsdl");
			QName qName = new QName("http://internal.ws.persistence.dronedb.com/", clz.getSimpleName() + "ImplService");
			Service service = Service.create(url, qName);
			return service.getPort(clz);
		} 
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
//	@Bean
//	public ObjectCrudRestSvcRemote objectCrudSvcRemote() {
//		return LoadServices(ObjectCrudRestSvcRemote.class);
//	}
//
//	@Bean
//	public QueryRestSvcRemote querySvcRemote() {
//		return LoadServices(QueryRestSvcRemote.class);
//	}
//
//	@Bean
//	public SessionsRestSvcRemote sessionsSvcRemote() {
//		return LoadServices(SessionsRestSvcRemote.class);
//	}

}
