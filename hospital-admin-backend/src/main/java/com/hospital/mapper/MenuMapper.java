package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    @Select("SELECT m.* FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId} AND m.deleted = 0 " +
            "ORDER BY m.sort ASC")
    List<Menu> selectMenusByRoleId(@Param("roleId") Long roleId);

    @Select("SELECT m.* FROM sys_menu m WHERE m.parent_id IS NULL AND m.deleted = 0 ORDER BY m.sort ASC")
    List<Menu> selectParentMenus();
}
