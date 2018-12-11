package com.zhtangsh.SpringDataMultiTenantMySQL.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/10
 */
@Component
@ApplicationPath("api/v1")
public class JerseyConfig extends ResourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(JerseyConfig.class);

    @Autowired
    private ApplicationContext context;
    @PostConstruct
    public void setup() {
        scan(Path.class);
        scan(Provider.class);
    }

    private void scan(Class<? extends Annotation> annotationType) {
        context.getBeansWithAnnotation(annotationType).forEach((name, bean) -> {
            logger.info("Register JAX-RS component -> {}", bean.getClass().getName());
            register(bean);
        });
    }
}
