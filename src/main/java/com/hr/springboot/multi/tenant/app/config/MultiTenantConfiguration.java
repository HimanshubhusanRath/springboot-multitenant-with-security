package com.hr.springboot.multi.tenant.app.config;

import com.hr.springboot.multi.tenant.app.datasource.MultiTenantDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class MultiTenantConfiguration {

    @Value("${default.tenant}")
    private String defaultTenant;

    @Bean
    public DataSource datasource()
    {
        final Map<Object, Object> resolvedDatasources = new HashMap<>();
        try
        {
            final File[] files = ResourceUtils.getFile("classpath:tenants").listFiles();

            for(File propFile : files)
            {
                final Properties tenantProps = new Properties();
                final DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();


                tenantProps.load(new FileInputStream(propFile));
                final String tenantId = tenantProps.getProperty("name");
                dataSourceBuilder.url(tenantProps.getProperty("datasource.url"));
                dataSourceBuilder.driverClassName(tenantProps.getProperty("datasource.driver-class-name"));
                dataSourceBuilder.username(tenantProps.getProperty("datasource.username"));
                dataSourceBuilder.password(tenantProps.getProperty("datasource.password"));
                resolvedDatasources.put(tenantId, dataSourceBuilder.build());
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception while preparing the datasource from the properties");
        }

        final AbstractRoutingDataSource datasource = new MultiTenantDataSource();
        datasource.setDefaultTargetDataSource(resolvedDatasources.get(defaultTenant));
        datasource.setTargetDataSources(resolvedDatasources);
        datasource.afterPropertiesSet();
        return datasource;
     }
}
