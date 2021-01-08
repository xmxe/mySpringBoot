package com.xmxe.mapper.slave;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;


/*@Mapper*/
public interface SlaveMapper {

	@Select("select * from tbluser where oid = 1")
	Map<String,Object> getUserById(@Param("user_id") Integer id);
	
}
