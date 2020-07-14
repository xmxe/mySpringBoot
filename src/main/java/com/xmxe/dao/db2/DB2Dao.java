package com.xmxe.dao.db2;


import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/*@Mapper*/
public interface DB2Dao {

	@Select("select * from tbluser where oid = 1")
	Map<String,Object> getUserById(@Param("user_id") Integer id);
	
}
