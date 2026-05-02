package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    @Select("SELECT m.* FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.menu_id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId} ORDER BY m.order_num ASC")
    List<SysMenu> selectMenusByRoleId(@Param("roleId") Integer roleId);

    @Select("SELECT * FROM sys_menu WHERE parent_id IS NULL ORDER BY order_num ASC")
    List<SysMenu> selectParentMenus();
}
