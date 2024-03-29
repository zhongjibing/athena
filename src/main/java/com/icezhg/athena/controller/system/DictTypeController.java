package com.icezhg.athena.controller.system;

import com.icezhg.athena.annotation.Operation;
import com.icezhg.athena.enums.OperationType;
import com.icezhg.athena.service.system.DictTypeService;
import com.icezhg.athena.vo.query.DictQuery;
import com.icezhg.athena.vo.DictTypeInfo;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.commons.exception.ErrorCodeException;
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
@RequestMapping("/system/dict/type")
public class DictTypeController {
    private final DictTypeService dictTypeService;

    public DictTypeController(DictTypeService dictTypeService) {
        this.dictTypeService = dictTypeService;
    }

    @PostMapping
    @Operation(title = "dict types addition", type = OperationType.INSERT)
    public DictTypeInfo add(@Validated @RequestBody DictTypeInfo typeInfo) {
        if (!dictTypeService.checkUnique(typeInfo)) {
            throw new ErrorCodeException("", "dict type is already exists");
        }
        return dictTypeService.save(typeInfo);
    }

    @PutMapping
    @Operation(title = "dict types modification", type = OperationType.UPDATE)
    public DictTypeInfo edit(@Validated @RequestBody DictTypeInfo typeInfo) {
        if (!dictTypeService.checkUnique(typeInfo)) {
            throw new ErrorCodeException("", "dict type is already exists");
        }
        return dictTypeService.update(typeInfo);
    }

    @DeleteMapping
    @Operation(title = "dict types deletion", type = OperationType.DELETE)
    public int delete(@RequestBody List<Integer> dictTypeIds) {
        return dictTypeService.deleteByIds(dictTypeIds);
    }

    @GetMapping("/list")
    public PageResult list(DictQuery query) {
        return new PageResult(dictTypeService.count(query), dictTypeService.find(query));
    }

    @GetMapping("/options")
    public List<DictTypeInfo> listOptions() {
        return dictTypeService.listOptions();
    }

    @GetMapping("/{id}")
    public DictTypeInfo get(@PathVariable Integer id) {
        return dictTypeService.findById(id);
    }
}
