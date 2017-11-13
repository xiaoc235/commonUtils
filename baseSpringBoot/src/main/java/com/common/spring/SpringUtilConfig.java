package com.common.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@ComponentScan(basePackages={"com.common.dao","com.common.redis","com.common.spring"})
@Configuration
public class SpringUtilConfig {

}
