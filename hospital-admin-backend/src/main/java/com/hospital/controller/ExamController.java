package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.Proberep;
import com.hospital.service.ProberepService;
import com.hospital.vo.PageVo;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ProberepService proberepService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/list")
    public ResultVo<PageVo<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String status) {

        LambdaQueryWrapper<Proberep> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Proberep::getCreateTime);

        if (status != null && !status.isEmpty()) {
            wrapper.eq(Proberep::getHasProberep, status);
        }

        Page<Proberep> result = proberepService.page(new Page<>(page, pageSize), wrapper);
        List<Map<String, Object>> records = result.getRecords().stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("proberepId", p.getProberepId());
            m.put("makeId", p.getMakeId());
            m.put("userId", p.getUserId());
            m.put("hasProberep", p.getHasProberep());
            m.put("proAdvice", p.getProAdvice());
            m.put("proTime", p.getProTime());
            m.put("createTime", p.getCreateTime());
            // 兼容字段
            m.put("id", p.getProberepId());
            m.put("patientName", "");
            m.put("doctorName", "");
            m.put("examType", "检查");
            m.put("result", p.getProAdvice());
            m.put("reportUrl", "");
            m.put("status", "1".equals(p.getHasProberep()) ? 1 : 0);
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(PageVo.of(records, result.getTotal(), page, pageSize));
    }

    @GetMapping("/user/{userId}")
    public ResultVo<List<Map<String, Object>>> listByUser(@PathVariable Integer userId) {
        List<Proberep> list = proberepService.lambdaQuery()
                .eq(Proberep::getUserId, userId)
                .orderByDesc(Proberep::getCreateTime)
                .list();

        List<Map<String, Object>> result = list.stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("proberepId", p.getProberepId());
            m.put("probename", "检查");
            m.put("probepart", "");
            m.put("probeprice", 0);
            m.put("probereport", p.getProAdvice());
            m.put("probetime", p.getProTime() != null ? p.getProTime().toString() : p.getCreateTime());
            m.put("createTime", p.getCreateTime());
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(result);
    }

    @PostMapping
    public ResultVo<?> add(@RequestBody Proberep proberep) {
        proberepService.save(proberep);
        return ResultVo.success();
    }

    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Integer id, @RequestBody Proberep proberep) {
        proberep.setProberepId(id);
        proberepService.updateById(proberep);
        return ResultVo.success();
    }

    @PostMapping("/{id}/report")
    public ResultVo<?> uploadReport(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultVo.error("文件不能为空");
        }

        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null) originalName = "unknown.jpg";
            String suffix = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf(".")) : ".jpg";
            String fileName = UUID.randomUUID() + suffix;
            File dest = new File(uploadPath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);

            Proberep proberep = proberepService.getById(id);
            if (proberep == null) {
                return ResultVo.error("记录不存在");
            }
            proberep.setHasProberep("1");
            proberep.setProAdvice("报告已上传: " + fileName);
            proberep.setProTime(LocalDateTime.now());
            proberepService.updateById(proberep);

            return ResultVo.success();
        } catch (IOException e) {
            return ResultVo.error("上传失败");
        }
    }
}
