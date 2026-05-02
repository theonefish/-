package com.hospital.controller;

import com.hospital.entity.Suggest;
import com.hospital.service.SuggestService;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/suggest")
public class SuggestController {

    @Autowired
    private SuggestService suggestService;

    @PostMapping
    public ResultVo<?> add(@RequestBody Suggest suggest) {
        suggestService.save(suggest);
        return ResultVo.success();
    }

    @GetMapping("/{id}")
    public ResultVo<Suggest> detail(@PathVariable Integer id) {
        return ResultVo.success(suggestService.getById(id));
    }
}
