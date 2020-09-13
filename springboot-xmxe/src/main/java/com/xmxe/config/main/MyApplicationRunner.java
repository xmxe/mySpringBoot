package com.xmxe.config.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 启动时加载
 */
@Component
@Order(2)//@Order 注解中，数字越小，优先级越大，默认情况下，优先级的值为 Integer.MAX_VALUE，表示优先级最低。
public class MyApplicationRunner implements ApplicationRunner {
    Logger logger = LogManager.getLogger(MyApplicationRunner.class);
	@Override
	public void run(ApplicationArguments args) throws Exception {
	    List<String> nonOptionArgs = args.getNonOptionArgs();
		logger.info("ApplicationArguments.getNonOptionArgs====>{}",nonOptionArgs);
        Set<String> optionNames = args.getOptionNames();
        for (String key : optionNames) {
            logger.info("ApplicationArguments.getOptionNames====>key={},value={}",key, args.getOptionValues(key));
        }
        String[] sourceArgs = args.getSourceArgs();
        logger.info("ApplicationArguments.getSourceArgs====>{}",Arrays.toString(sourceArgs));
		
	}
}
