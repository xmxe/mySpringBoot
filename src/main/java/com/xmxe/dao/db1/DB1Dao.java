package com.xmxe.dao.db1;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xmxe.entity.Book;
import com.xmxe.entity.Dept;
import com.xmxe.entity.User;

/*@Mapper*/
public interface DB1Dao {

	/*@Select("select * from xxcl_user where id = #{user_id}")*/
	User getUserById(@Param("user_id") Integer id);
	
	int queryUserCount(@Param(value = "tj") String tj);
	
	List<Book> querySome(@Param(value="tj")String tj, @Param(value="start") int start,@Param(value="end") int end);
	
	List<Dept> findDept(); 
	
	List<Map<String,Object>> getDensity(Map<String,Object> map);
}
