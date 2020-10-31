package com.sthwin.webflux.config;

import org.springframework.context.annotation.Configuration;

/**
 * Created by sthwin on 2020/06/06 3:50 오후
 */

@Configuration
//@MapperScan(value = {"com.sthwin.webflux.mapper"})
//@EnableTransactionManagement
public class MyBatisConfig {

//    @Bean
//    public DataSource dataSource() {
//        return DataSourceBuilder.create()
//                .url("jdbc:postgresql://127.0.0.1:5432/testdb")
//                .driverClassName("org.postgresql.Driver")
//                .username("testuser")
//                .password("testpwd")
//                .build();
//    }
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource);
//
//        Resource[] res = new PathMatchingResourcePatternResolver()
//                .getResources("classpath:mapper/*Mapper.xml");
//
//        sessionFactory.setMapperLocations(res);
//
//        return sessionFactory.getObject();
//    }
//
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//    @Bean
//    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
}
