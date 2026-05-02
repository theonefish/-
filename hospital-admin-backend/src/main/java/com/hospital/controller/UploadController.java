package com.hospital.controller;

import com.hospital.vo.ResultVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping
    public ResultVo<Map<String, String>> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return ResultVo.error("文件不能为空");
        }

        try {
            String originalName = file.getOriginalFilename();
            String suffix = originalName.substring(originalName.lastIndexOf("."));
            String fileName = UUID.randomUUID() + suffix;
            File dest = new File(uploadPath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);

            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int port = request.getServerPort();
            String url = scheme + "://" + serverName + ":" + port + "/upload/" + fileName;

            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            data.put("name", fileName);
            return ResultVo.success(data);
        } catch (IOException e) {
            return ResultVo.error("上传失败: " + e.getMessage());
        }
    }
}
