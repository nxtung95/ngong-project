//package vn.ngong.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import vn.ngong.cache.LocalCacheConfig;
//
//@Slf4j
//@Component
//public class InitBeanApp implements InitializingBean {
//	@Autowired
//	private LocalCacheConfig localCacheConfig;
//
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		localCacheConfig.loadCityDistrictWardList();
//	}
//
//}
