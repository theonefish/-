package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.News;
import com.hospital.service.NewsService;
import com.hospital.vo.PageVo;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/list")
    public ResultVo<PageVo<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(News::getTitle, keyword);
        }
        wrapper.orderByDesc(News::getCreateTime);
        Page<News> result = newsService.page(new Page<>(page, pageSize), wrapper);

        List<Map<String, Object>> records = result.getRecords().stream().map(n -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", n.getId());
            m.put("title", n.getTitle());
            m.put("textDesc", n.getTextDesc());
            m.put("textContent", n.getTextContent());
            m.put("createTime", n.getCreateTime());
            m.put("image", n.getImage());
            m.put("toIndex", n.getToIndex());
            // 兼容字段
            m.put("content", n.getTextContent());
            m.put("cover", n.getImage());
            m.put("recommended", "1".equals(n.getToIndex()));
            m.put("status", 1);
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(PageVo.of(records, result.getTotal(), page, pageSize));
    }

    @PostMapping
    public ResultVo<?> add(@RequestBody News news) {
        newsService.save(news);
        return ResultVo.success();
    }

    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Integer id, @RequestBody News news) {
        news.setId(id);
        newsService.updateById(news);
        return ResultVo.success();
    }

    @DeleteMapping("/{id}")
    public ResultVo<?> delete(@PathVariable Integer id) {
        newsService.removeById(id);
        return ResultVo.success();
    }
}
