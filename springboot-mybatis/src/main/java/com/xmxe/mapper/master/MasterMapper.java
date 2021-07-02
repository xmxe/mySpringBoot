package com.xmxe.mapper.master;

import com.xmxe.entity.User;
import org.apache.ibatis.annotations.Param;

/*@Mapper*/
public interface MasterMapper {

	/*@Select("select * from xxcl_user where id = #{user_id}")*/
	User getUserById(@Param("user_id") Integer id);

}
