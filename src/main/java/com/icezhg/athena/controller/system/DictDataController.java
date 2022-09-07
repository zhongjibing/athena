package com.icezhg.athena.controller.system;

import com.icezhg.athena.domain.DictData;
import com.icezhg.athena.service.DictDataService;
import com.icezhg.athena.service.DictTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据字典信息
 */
@RestController
@RequestMapping("/system/dict/data")
public class DictDataController {

    private final DictTypeService dictTypeService;

    private final DictDataService dictDataService;

    public DictDataController(DictTypeService dictTypeService, DictDataService dictDataService) {
        this.dictTypeService = dictTypeService;
        this.dictDataService = dictDataService;
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public List<DictData> dictType(@PathVariable String dictType) {
        return dictTypeService.findDictDataByType(dictType);
    }
}
