package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.SysDepartment;
import com.hospital.entity.SysUser;
import com.hospital.service.SysDepartmentService;
import com.hospital.service.SysUserService;
import com.hospital.utils.Md5Util;
import com.hospital.vo.PageVo;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysDepartmentService departmentService;

    /**
     * 模拟医生数据 - 当数据库中没有医生数据时使用
     * 后续数据库导入真实数据后可移除
     */
    private List<Map<String, Object>> getMockDoctors() {
        List<Map<String, Object>> list = new ArrayList<>();
        String[][] doctors = {
            {"1", "王明", "主任医师", "1", "内科", "慢性咳嗽、哮喘、胃炎、消化性溃疡", "50", "从事内科临床工作20余年，擅长呼吸系统、消化系统疾病的诊断与治疗", "内科门诊3楼A区"},
            {"2", "李丽", "副主任医师", "2", "外科", "胆囊切除、阑尾切除、疝气修补、甲状腺手术", "40", "外科临床工作15年，专注于微创手术技术", "外科门诊2楼B区"},
            {"3", "张伟", "主任医师", "3", "儿科", "小儿肺炎、哮喘、过敏性疾病、生长发育迟缓", "45", "儿科主任，从事儿科临床工作25年", "儿科门诊1楼C区"},
            {"4", "刘华", "副主任医师", "4", "妇产科", "高危妊娠、产前诊断、子宫肌瘤、卵巢囊肿", "40", "妇产科资深专家，擅长高危妊娠管理", "妇产科门诊3楼D区"},
            {"5", "陈刚", "主治医师", "5", "骨科", "骨折、关节置换、运动损伤、腰椎间盘突出", "35", "骨科骨干医生，擅长运动损伤、关节疾病", "骨科门诊2楼E区"},
            {"6", "赵燕", "副主任医师", "6", "眼科", "近视矫正、白内障、青光眼、眼底病变", "40", "眼科专家，专注近视防控、白内障手术", "眼科门诊4楼F区"},
            {"7", "孙涛", "主任医师", "7", "口腔科", "种植牙、牙齿矫正、牙周病、根管治疗", "50", "口腔科主任，从事口腔临床工作22年", "口腔科门诊2楼G区"},
            {"8", "周芳", "主治医师", "8", "皮肤科", "湿疹、痤疮、银屑病、激光美容、注射美容", "35", "皮肤科医师，擅长各类皮肤病诊治及医学美容", "皮肤科门诊3楼H区"}
        };
        for (String[] d : doctors) {
            Map<String, Object> m = new HashMap<>();
            m.put("userId", Integer.parseInt(d[0]));
            m.put("username", "doctor" + d[0]);
            m.put("nickName", d[1]);
            m.put("jobTitle", d[2]);
            m.put("deptId", Integer.parseInt(d[3]));
            m.put("deptName", d[4]);
            m.put("image", "/static/doctor" + d[0] + ".jpg");
            m.put("goodAt", d[5]);
            m.put("price", Integer.parseInt(d[6]));
            m.put("introduction", d[7]);
            m.put("visitAddress", d[8]);
            m.put("isEnabled", 1);
            m.put("toHome", "1");
            // 兼容字段
            m.put("id", Integer.parseInt(d[0]));
            m.put("name", d[1]);
            m.put("title", d[2]);
            m.put("avatar", "/static/doctor" + d[0] + ".jpg");
            m.put("specialty", d[5]);
            m.put("status", 1);
            list.add(m);
        }
        return list;
    }

    @GetMapping("/list")
    public ResultVo<PageVo<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer deptId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String toHome) {

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getIsAdmin, "0");
        wrapper.orderByDesc(SysUser::getCreateTime);

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(SysUser::getNickName, keyword).or().like(SysUser::getJobTitle, keyword));
        }
        if (deptId != null) {
            wrapper.eq(SysUser::getDeptId, deptId);
        }
        if (status != null) {
            wrapper.eq(SysUser::getIsEnabled, status);
        }
        if (toHome != null && !toHome.isEmpty()) {
            wrapper.eq(SysUser::getToHome, toHome);
        }

        Page<SysUser> result = sysUserService.page(new Page<>(page, pageSize), wrapper);

        List<Map<String, Object>> records;
        // 如果数据库中没有医生数据，返回模拟数据
        if (result.getTotal() == 0) {
            records = getMockDoctors();
            // 根据参数过滤模拟数据
            if (deptId != null) {
                records = records.stream()
                        .filter(r -> deptId.equals(r.get("deptId")))
                        .collect(Collectors.toList());
            }
            if (keyword != null && !keyword.isEmpty()) {
                String kw = keyword.toLowerCase();
                records = records.stream()
                        .filter(r -> {
                            String name = (String) r.get("nickName");
                            String title = (String) r.get("jobTitle");
                            String dept = (String) r.get("deptName");
                            return (name != null && name.contains(kw))
                                    || (title != null && title.contains(kw))
                                    || (dept != null && dept.contains(kw));
                        })
                        .collect(Collectors.toList());
            }
            // 分页处理
            int start = (int) ((page - 1) * pageSize);
            int end = (int) Math.min(start + pageSize, records.size());
            List<Map<String, Object>> pageRecords = start < records.size()
                    ? records.subList(start, end)
                    : new ArrayList<>();
            return ResultVo.success(PageVo.of(pageRecords, (long) records.size(), page, pageSize));
        }

        // 批量查科室名称
        List<Integer> deptIds = result.getRecords().stream()
                .map(SysUser::getDeptId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, String> deptMap = deptIds.isEmpty() ? new HashMap<>()
                : departmentService.listByIds(deptIds).stream()
                .collect(Collectors.toMap(SysDepartment::getDeptId, SysDepartment::getDeptName, (a, b) -> a));

        records = result.getRecords().stream().map(d -> {
            Map<String, Object> m = new HashMap<>();
            m.put("userId", d.getUserId());
            m.put("username", d.getUsername());
            m.put("nickName", d.getNickName());
            m.put("jobTitle", d.getJobTitle());
            m.put("deptId", d.getDeptId());
            m.put("image", d.getImage());
            m.put("goodAt", d.getGoodAt());
            m.put("introduction", d.getIntroduction());
            m.put("isEnabled", d.getIsEnabled());
            m.put("toHome", d.getToHome());
            m.put("price", d.getPrice());
            m.put("visitAddress", d.getVisitAddress());
            // 兼容字段
            m.put("id", d.getUserId());
            m.put("name", d.getNickName() != null ? d.getNickName() : d.getUsername());
            m.put("title", d.getJobTitle());
            m.put("deptName", deptMap.getOrDefault(d.getDeptId(), ""));
            m.put("avatar", d.getImage());
            m.put("specialty", d.getGoodAt());
            m.put("status", d.getIsEnabled() != null ? d.getIsEnabled() : 1);
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(PageVo.of(records, result.getTotal(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ResultVo<Map<String, Object>> detail(@PathVariable Integer id) {
        SysUser d = sysUserService.getById(id);
        if (d == null) {
            return ResultVo.error("医生不存在");
        }
        SysDepartment dept = departmentService.getById(d.getDeptId());
        Map<String, Object> m = new HashMap<>();
        m.put("userId", d.getUserId());
        m.put("username", d.getUsername());
        m.put("nickName", d.getNickName());
        m.put("jobTitle", d.getJobTitle());
        m.put("deptId", d.getDeptId());
        m.put("image", d.getImage());
        m.put("goodAt", d.getGoodAt());
        m.put("introduction", d.getIntroduction());
        m.put("isEnabled", d.getIsEnabled());
        m.put("toHome", d.getToHome());
        m.put("price", d.getPrice());
        m.put("visitAddress", d.getVisitAddress());
        m.put("id", d.getUserId());
        m.put("name", d.getNickName() != null ? d.getNickName() : d.getUsername());
        m.put("title", d.getJobTitle());
        m.put("deptName", dept != null ? dept.getDeptName() : "");
        m.put("avatar", d.getImage());
        m.put("specialty", d.getGoodAt());
        m.put("status", d.getIsEnabled() != null ? d.getIsEnabled() : 1);
        return ResultVo.success(m);
    }

    @PostMapping
    public ResultVo<?> add(@RequestBody SysUser user) {
        user.setPassword(Md5Util.encrypt("123456"));
        user.setIsAdmin("0");
        user.setIsEnabled(1);
        sysUserService.save(user);
        return ResultVo.success();
    }

    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Integer id, @RequestBody SysUser user) {
        user.setUserId(id);
        sysUserService.updateById(user);
        return ResultVo.success();
    }

    @DeleteMapping("/{id}")
    public ResultVo<?> delete(@PathVariable Integer id) {
        sysUserService.removeById(id);
        return ResultVo.success();
    }

    @PostMapping("/{id}/reset-password")
    public ResultVo<Map<String, String>> resetPassword(@PathVariable Integer id) {
        boolean success = sysUserService.resetPassword(id);
        if (!success) {
            return ResultVo.error("重置失败");
        }
        Map<String, String> data = new HashMap<>();
        data.put("newPassword", "666666");
        return ResultVo.success(data);
    }
}
