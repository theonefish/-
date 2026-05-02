package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.Inspectrep;
import com.hospital.service.InspectrepService;
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
@RequestMapping("/lab")
public class LabController {

    @Autowired
    private InspectrepService inspectrepService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/list")
    public ResultVo<PageVo<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String status) {

        LambdaQueryWrapper<Inspectrep> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Inspectrep::getCreateTime);

        if (status != null && !status.isEmpty()) {
            wrapper.eq(Inspectrep::getHasInspectrep, status);
        }

        Page<Inspectrep> result = inspectrepService.page(new Page<>(page, pageSize), wrapper);
        List<Map<String, Object>> records = result.getRecords().stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("inspectrepId", p.getInspectrepId());
            m.put("makeId", p.getMakeId());
            m.put("userId", p.getUserId());
            m.put("hasInspectrep", p.getHasInspectrep());
            m.put("insAdvice", p.getInsAdvice());
            m.put("insTime", p.getInsTime());
            m.put("createTime", p.getCreateTime());
            // 兼容字段
            m.put("id", p.getInspectrepId());
            m.put("patientName", "");
            m.put("doctorName", "");
            m.put("labType", "检验");
            m.put("result", p.getInsAdvice());
            m.put("reportUrl", "");
            m.put("status", "1".equals(p.getHasInspectrep()) ? 1 : 0);
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(PageVo.of(records, result.getTotal(), page, pageSize));
    }

    @GetMapping("/user/{userId}")
    public ResultVo<List<Map<String, Object>>> listByUser(@PathVariable Integer userId) {
        List<Inspectrep> list = inspectrepService.lambdaQuery()
                .eq(Inspectrep::getUserId, userId)
                .orderByDesc(Inspectrep::getCreateTime)
                .list();

        List<Map<String, Object>> result = list.stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("inspectrepId", p.getInspectrepId());
            m.put("inspectname", "检验");
            m.put("inspectpart", "");
            m.put("inspectprice", 0);
            m.put("inspectreport", p.getInsAdvice());
            m.put("inspecttime", p.getInsTime() != null ? p.getInsTime().toString() : p.getCreateTime());
            m.put("createTime", p.getCreateTime());
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(result);
    }

    @PostMapping
    public ResultVo<?> add(@RequestBody Inspectrep inspectrep) {
        inspectrepService.save(inspectrep);
        return ResultVo.success();
    }

    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Integer id, @RequestBody Inspectrep inspectrep) {
        inspectrep.setInspectrepId(id);
        inspectrepService.updateById(inspectrep);
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

            Inspectrep inspectrep = inspectrepService.getById(id);
            if (inspectrep == null) {
                return ResultVo.error("记录不存在");
            }
            inspectrep.setHasInspectrep("1");
            inspectrep.setInsAdvice("报告已上传: " + fileName);
            inspectrep.setInsTime(LocalDateTime.now());
            inspectrepService.updateById(inspectrep);

            return ResultVo.success();
        } catch (IOException e) {
            return ResultVo.error("上传失败");
        }
    }
}
