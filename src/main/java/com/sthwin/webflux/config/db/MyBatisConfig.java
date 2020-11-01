package com.sthwin.webflux.config.db;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by sthwin on 2020/06/06 3:50 오후
 */

@Configuration
@MapperScan(value = {"com.sthwin.webflux.mapper"})
@EnableTransactionManagement
public class MyBatisConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://127.0.0.1:5432/testdb")
                .driverClassName("org.postgresql.Driver")
                .username("testuser")
                .password("testpwd")
                .build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        Resource[] res = new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/*Mapper.xml");

        sessionFactory.setMapperLocations(res);

        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    /**
     * SimpleBatchConfiguration.class 클래스에서 transactionManager 로 정의된 이름을 사용하고 있음.
     * 충돌나지 않도록 이름을 정해야함.
     *
     * @param dataSource
     * @return
     */
    @Bean
    public DataSourceTransactionManager mpisTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
