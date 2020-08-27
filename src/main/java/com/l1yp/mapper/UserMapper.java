package com.l1yp.mapper;

import com.l1yp.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author Lyp
 * @Date 2020-08-27
 * @Email l1yp@qq.com
 */
@Mapper
public interface UserMapper {

    List<UserInfo> selectAll();

}
