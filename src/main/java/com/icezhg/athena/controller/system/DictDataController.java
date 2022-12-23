package com.icezhg.athena.controller.system;

import com.icezhg.athena.annotation.Operation;
import com.icezhg.athena.domain.DictData;
import com.icezhg.athena.enums.OperationType;
import com.icezhg.athena.service.system.DictDataService;
import com.icezhg.athena.service.system.DictTypeService;
import com.icezhg.athena.vo.DictDataInfo;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.query.DictQuery;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Operation(title = "dict data options", type = OperationType.QUERY)
    public List<DictData> dictType(@PathVariable String dictType) {
        return dictTypeService.findDictDataByType(dictType);
    }

    @PostMapping
    @Operation(title = "dict data addition", type = OperationType.INSERT)
    public DictDataInfo add(@Validated @RequestBody DictDataInfo typeInfo) {
        return dictDataService.save(typeInfo);
    }

    @PutMapping
    @Operation(title = "dict data modification", type = OperationType.UPDATE)
    public DictDataInfo edit(@Validated @RequestBody DictDataInfo typeInfo) {
        return dictDataService.update(typeInfo);
    }

    @DeleteMapping
    @Operation(title = "dict data deletion", type = OperationType.DELETE)
    public int delete(@RequestBody List<Integer> dictTypeIds) {
        return dictDataService.deleteByIds(dictTypeIds);
    }

    @GetMapping("/list")
    @Operation(title = "dict data list", type = OperationType.LIST)
    public PageResult list(DictQuery query) {
        return new PageResult(dictDataService.count(query), dictDataService.find(query));
    }

    @GetMapping("/{id}")
    @Operation(title = "dict data detail", type = OperationType.QUERY)
    public DictDataInfo get(@PathVariable Integer id) {
        return dictDataService.findById(id);
    }
}
