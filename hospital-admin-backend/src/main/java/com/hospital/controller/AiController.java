package com.hospital.controller;

import com.hospital.vo.ResultVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    @PostMapping("/analyze")
    public ResultVo<Map<String, Object>> analyze(@RequestBody Map<String, Object> params) {
        // 模拟AI分析结果
        Map<String, Object> result = new HashMap<>();
        result.put("analysis", "根据报告内容分析，各项指标基本正常。建议继续保持良好的生活习惯，定期复查。");
        result.put("suggestions", Arrays.asList("保持规律作息", "适量运动", "定期体检"));
        result.put("riskLevel", "low");
        return ResultVo.success(result);
    }
}
