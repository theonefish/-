package com.hospital.controller;

import com.hospital.entity.SysDepartment;
import com.hospital.entity.SysUser;
import com.hospital.service.SysDepartmentService;
import com.hospital.service.SysUserService;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公开医生接口 - 供小程序游客访问
 * 无需登录即可查看医生列表和详情
 */
@RestController
@RequestMapping("/doctor/public")
public class DoctorPublicController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysDepartmentService departmentService;

    /**
     * 模拟医生数据 - 当数据库中没有医生数据时使用
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
            // 兼容字段 - 给小程序用
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

    /**
     * 公开接口：获取医生详情 - 无需登录
     * @param id 医生ID
     */
    @GetMapping("/{id}")
    public ResultVo<Map<String, Object>> detail(@PathVariable Integer id) {
        SysUser d = sysUserService.getById(id);

        // 如果数据库中没有，尝试从模拟数据中查找
        if (d == null) {
            List<Map<String, Object>> mockList = getMockDoctors();
            for (Map<String, Object> m : mockList) {
                if (id.equals(m.get("userId")) || id.equals(m.get("id"))) {
                    return ResultVo.success(m);
                }
            }
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
        // 兼容字段
        m.put("id", d.getUserId());
        m.put("name", d.getNickName() != null ? d.getNickName() : d.getUsername());
        m.put("title", d.getJobTitle());
        m.put("deptName", dept != null ? dept.getDeptName() : "");
        m.put("avatar", d.getImage());
        m.put("specialty", d.getGoodAt());
        m.put("status", d.getIsEnabled() != null ? d.getIsEnabled() : 1);
        return ResultVo.success(m);
    }
}
