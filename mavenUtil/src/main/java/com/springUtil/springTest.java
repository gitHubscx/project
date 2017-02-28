package com.springUtil;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

public class springTest {
	
	@Test
	public void test () {
		ApplicationContext context = SpringBeansUtil.getContext();
		// 直接通过spring获取上下文得到结果
		String result = context.getBean(springTestService.class).test();
	}
}
