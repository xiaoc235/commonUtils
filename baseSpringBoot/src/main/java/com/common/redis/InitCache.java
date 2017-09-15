package com.common.redis;


import com.common.base.CommConstants;
import com.common.base.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化缓存管理
 *
 * 先清除，再加载
 *
 */
@Order(value=9)
@Component
public final class InitCache implements CommandLineRunner {

	private static final Logger _logger = LoggerFactory.getLogger(InitCache.class);

	@Autowired
	private RedisClient redis;

	@Override
	public void run(String... strings) throws BusinessException,Exception {
		_logger.info("init common cache .....");
		try {

		}catch (Exception e) {
			final String message = CommConstants.SYSTEM_ERROR;
			_logger.error(message, e);
		}
	}








}
