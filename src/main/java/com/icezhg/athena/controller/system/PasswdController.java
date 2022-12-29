package com.icezhg.athena.controller.system;

import com.icezhg.athena.annotation.Operation;
import com.icezhg.athena.enums.OperationType;
import com.icezhg.athena.service.system.PasswdService;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.PasswdInfo;
import com.icezhg.athena.vo.query.NameQuery;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhongjibing on 2022/12/30.
 */
@RestController
@RequestMapping("/system/passwd")
public class PasswdController {

    private final PasswdService passwdService;

    public PasswdController(PasswdService passwdService) {
        this.passwdService = passwdService;
    }

    @PostMapping
    @Operation(title = "passwd addition", type = OperationType.INSERT, saveParameter = false, saveResult = false)
    public PasswdInfo add(@Validated @RequestBody PasswdInfo passwd) {
        return passwdService.save(passwd);
    }

    @PutMapping
    @Operation(title = "passwd modification", type = OperationType.UPDATE, saveParameter = false, saveResult = false)
    public PasswdInfo edit(@Validated @RequestBody PasswdInfo passwd) {
        return passwdService.update(passwd);
    }

    @PostMapping("/{id}")
    @Operation(title = "passwd detail", type = OperationType.QUERY, saveResult = false)
    public PasswdInfo get(@PathVariable String id, @RequestParam String secretKey) {
        return passwdService.findPasswd(id, secretKey);
    }


    @GetMapping("/list")
    @Operation(title = "passwd list", type = OperationType.LIST, saveResult = false)
    public PageResult list(NameQuery query) {
        return new PageResult(passwdService.count(query), passwdService.find(query));
    }

    @GetMapping("/generate")
    public Object generate(@Validated @Min(1) int size) {
        return passwdService.generate(size);
    }
}
