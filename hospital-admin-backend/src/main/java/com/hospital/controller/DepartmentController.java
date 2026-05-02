package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.SysDepartment;
import com.hospital.service.SysDepartmentService;
import com.hospital.vo.PageVo;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    /**
     * 科室描述映射表 - 数据库暂无 dept_desc 字段时的临时方案
     * 后续如需持久化，可执行：ALTER TABLE sys_department ADD COLUMN dept_desc VARCHAR(100);
     */
    private static final Map<String, String> DEPT_DESC_MAP = new HashMap<>();
    static {
        DEPT_DESC_MAP.put("内科", "呼吸系统、消化系统疾病诊治");
        DEPT_DESC_MAP.put("外科", "普外、微创手术、创伤修复");
        DEPT_DESC_MAP.put("儿科", "儿童常见病、生长发育评估");
        DEPT_DESC_MAP.put("妇产科", "孕产保健、妇科疾病诊疗");
        DEPT_DESC_MAP.put("骨科", "关节置换、脊柱矫正、运动损伤");
        DEPT_DESC_MAP.put("眼科", "近视矫正、白内障、眼底病");
        DEPT_DESC_MAP.put("口腔科", "牙齿矫正、种植修复、牙周治疗");
        DEPT_DESC_MAP.put("皮肤科", "皮肤病、美容激光、过敏检测");
    }

    @Autowired
    private SysDepartmentService departmentService;

    @GetMapping("/list")
    public ResultVo<PageVo<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String toHome,
            @RequestParam(required = false) Integer isIndex) {

        LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysDepartment::getOrderNum);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(SysDepartment::getDeptName, keyword);
        }
        // 兼容管理端的 toHome 参数和小程序的 isIndex 参数
        if (toHome != null && !toHome.isEmpty()) {
            wrapper.eq(SysDepartment::getToHome, toHome);
        } else if (isIndex != null) {
            wrapper.eq(SysDepartment::getToHome, isIndex == 1 ? "1" : "0");
        }

        Page<SysDepartment> result = departmentService.page(new Page<>(page, pageSize), wrapper);
        List<Map<String, Object>> records = result.getRecords().stream().map(d -> {
            Map<String, Object> m = new HashMap<>();
            // 从映射表获取科室描述（数据库暂无 dept_desc 字段）
            String deptDesc = DEPT_DESC_MAP.getOrDefault(d.getDeptName(), "专业诊疗");

            m.put("deptId", d.getDeptId());
            m.put("deptName", d.getDeptName());
            m.put("orderNum", d.getOrderNum());
            m.put("phone", d.getPhone());
            m.put("toHome", d.getToHome());
            m.put("deptDesc", deptDesc);
            // 管理端 / 小程序兼容字段
            m.put("id", d.getDeptId());
            m.put("name", d.getDeptName());
            m.put("description", deptDesc);
            m.put("recommended", "1".equals(d.getToHome()));
            m.put("status", 1);
            m.put("sort", d.getOrderNum() != null ? d.getOrderNum() : 0);
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(PageVo.of(records, result.getTotal(), page, pageSize));
    }

    @PostMapping
    public ResultVo<?> add(@RequestBody SysDepartment department) {
        departmentService.save(department);
        return ResultVo.success();
    }

    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Integer id, @RequestBody SysDepartment department) {
        department.setDeptId(id);
        departmentService.updateById(department);
        return ResultVo.success();
    }

    @DeleteMapping("/{id}")
    public ResultVo<?> delete(@PathVariable Integer id) {
        departmentService.removeById(id);
        return ResultVo.success();
    }
}
