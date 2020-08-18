package com.xmxe.config.main;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动时加载
 */
@Component
@Order(2)//@Order 注解中，数字越小，优先级越大，默认情况下，优先级的值为 Integer.MAX_VALUE，表示优先级最低。
public class MyApplicationRunner implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<String> nonOptionArgs = args.getNonOptionArgs();
        System.out.println("MyApplicationRunner1>>>>>>>>>>>>>>>>"+nonOptionArgs);
        Set<String> optionNames = args.getOptionNames();
        for (String key : optionNames) {
            System.out.println("MyApplicationRunner2>>>>>>>>>>>>>"+key + ":" + args.getOptionValues(key));
        }
        String[] sourceArgs = args.getSourceArgs();
        System.out.println("MyApplicationRunner3>>>>>>>>>>>>"+Arrays.toString(sourceArgs));
		
	}
}
