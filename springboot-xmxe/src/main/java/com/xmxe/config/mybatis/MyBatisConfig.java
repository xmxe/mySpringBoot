package com.xmxe.config.mybatis;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement // 加上这个注解，使得支持事务
public class MyBatisConfig {

	@Bean(name = "db1")
	@ConfigurationProperties(prefix = "spring.datasource.druid.db1")
	@Primary
	public DataSource dataSource1() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean(name = "db1sqlSessionFactory")
	@Primary
	public SqlSessionFactory db1SqlSessionFactory(@Qualifier("db1") DataSource dataSource) {
		return setSessionFactoryBeanByDataSource(dataSource);
	}
	
	@Bean
	@Primary
	public SqlSessionTemplate db1SqlSessionTemplate(
			@Qualifier("db1sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	@Primary
	public DataSourceTransactionManager db1TransactionManager(
			@Qualifier("db1") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	
	@Bean(name = "db2")
	@ConfigurationProperties(prefix = "spring.datasource.druid.db2")
	public DataSource dataSource2() {
		return DruidDataSourceBuilder.create().build();
	}

	
	@Bean(name = "db2sqlSessionFactory")
	public SqlSessionFactory db2SqlSessionFactory(@Qualifier("db2") DataSource dataSource) {
		return setSessionFactoryBeanByDataSource(dataSource);
	}

	@Bean
	public SqlSessionTemplate db2SqlSessionTemplate(
			@Qualifier("db2sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	@Bean
	public DataSourceTransactionManager db2TransactionManager(
			@Qualifier("db2") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}


	@Bean
	public PageHelper pageHelper() {
		// 分页插件
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		// 默认值为false,当该参数设置为true时,使用RowBounds分页时,会将offset参数当成pageNum使用,可以用页码和页面大小两个参数进行分页。
		properties.setProperty("offsetAsPageNum", "true");
		// 默认值为false,当该参数设置为true时,使用RowBounds分页会进行count查询。
		properties.setProperty("rowBoundsWithCount", "true");
		// true 启用合理化时，如果pageNum<1会查询第一页,如果pageNum>pages会查询最后一页 false 禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据
		properties.setProperty("reasonable", "true");
		// 支持通过Mapper接口参数来传递分页参数
		properties.setProperty("supportMethodsArguments", "true");
		// 总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page
		properties.setProperty("returnPageInfo", "check");
		// 增加了一个`params`参数来配置参数映射，用于从Map或ServletRequest中取值,可以配置pageNum,pageSize,count,pageSizeZero,reasonable,orderBy,不配置映射的用默认值
		properties.setProperty("params", "count=countSql");
		// 数据库方言 4.0.0之后不需要设置此属性
		properties.setProperty("dialect", "mysql");
		pageHelper.setProperties(properties);
		return pageHelper;
	}

	private SqlSessionFactory setSessionFactoryBeanByDataSource(DataSource dataSource){
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			bean.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/*Mapper.xml"));
			// 加载全局的配置文件
			bean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis/mybatis-config.xml"));
			return bean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}