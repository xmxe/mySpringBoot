package com.xmxe.config.mybatis;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.github.pagehelper.PageHelper;

@Configuration//类似xml中的<beans>
@EnableTransactionManagement//加上这个注解，使得支持事务
public class MyBatisConfig implements TransactionManagementConfigurer {
	
  @Autowired
  private DataSource dataSource;

  @Bean//@Bean类似于xml中的<bean>
  @Override
  public PlatformTransactionManager annotationDrivenTransactionManager() {
       return new DataSourceTransactionManager(dataSource);
  }

  @Bean(name = "sqlSessionFactory")
  public SqlSessionFactory sqlSessionFactoryBean() {
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

  @Bean
  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
      return new SqlSessionTemplate(sqlSessionFactory);
  }
  @Bean
  public PageHelper pageHelper(){
      //分页插件
      PageHelper pageHelper = new PageHelper();
      Properties properties = new Properties();
      properties.setProperty("offsetAsPageNum", "true");//默认值为false,当该参数设置为true时,使用RowBounds分页时,会将offset参数当成pageNum使用,可以用页码和页面大小两个参数进行分页。
      properties.setProperty("rowBoundsWithCount", "true");//默认值为false,当该参数设置为true时,使用RowBounds分页会进行count查询。
      properties.setProperty("reasonable", "true");// true 启用合理化时，如果pageNum<1会查询第一页,如果pageNum>pages会查询最后一页 false 禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据
      properties.setProperty("supportMethodsArguments", "true");//支持通过Mapper接口参数来传递分页参数
      properties.setProperty("returnPageInfo", "check");//总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page
      properties.setProperty("params", "count=countSql");//增加了一个`params`参数来配置参数映射，用于从Map或ServletRequest中取值 ,可以配置pageNum,pageSize,count,pageSizeZero,reasonable,orderBy,不配置映射的用默认值 
      properties.setProperty("dialect","mysql");//数据库方言 4.0.0之后不需要设置此属性
      pageHelper.setProperties(properties);
      return pageHelper;
  }
}