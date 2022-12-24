package com.icezhg.athena.controller.system;

import com.icezhg.athena.annotation.Operation;
import com.icezhg.athena.enums.OperationType;
import com.icezhg.athena.service.system.ConfigService;
import com.icezhg.athena.vo.ConfigInfo;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.query.ConfigQuery;
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
 * 参数配置
 */
@RestController
@RequestMapping("/system/config")
public class ConfigController {
    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping
    @Operation(title = "system config properties addition", type = OperationType.INSERT)
    public ConfigInfo add(@Validated @RequestBody ConfigInfo configInfo) {
        if (!configService.checkUnique(configInfo)) {
            throw new ErrorCodeException("", "config key is already exists");
        }
        return configService.save(configInfo);
    }

    @PutMapping
    @Operation(title = "system config properties modification", type = OperationType.UPDATE)
    public ConfigInfo edit(@Validated @RequestBody ConfigInfo configInfo) {
        if (!configService.checkUnique(configInfo)) {
            throw new ErrorCodeException("", "config key is already exists");
        }
        return configService.update(configInfo);
    }

    @DeleteMapping
    @Operation(title = "system config properties deletion", type = OperationType.DELETE)
    public int delete(@RequestBody List<Integer> dictTypeIds) {
        return configService.deleteByIds(dictTypeIds);
    }

    @GetMapping("/list")
    public PageResult list(ConfigQuery query) {
        return new PageResult(configService.count(query), configService.find(query));
    }

    @GetMapping("/{id}")
    public ConfigInfo get(@PathVariable Integer id) {
        return configService.findById(id);
    }
}
