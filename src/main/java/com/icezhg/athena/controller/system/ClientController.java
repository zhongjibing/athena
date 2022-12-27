package com.icezhg.athena.controller.system;

import com.icezhg.athena.annotation.Operation;
import com.icezhg.athena.enums.OperationType;
import com.icezhg.athena.service.system.ClientService;
import com.icezhg.athena.vo.ClientInfo;
import com.icezhg.athena.vo.ClientPasswd;
import com.icezhg.athena.vo.ClientStatus;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.query.ClientQuery;
import com.icezhg.commons.exception.ErrorCodeException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhongjibing on 2022/12/25.
 */
@RestController
@RequestMapping("/system/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @Operation(title = "clients addition", type = OperationType.INSERT, saveResult = false)
    public ClientInfo add(@Validated @RequestBody ClientInfo client) {
        if (!clientService.checkUnique(client)) {
            throw new ErrorCodeException("", "client name is already exists");
        }
        return clientService.save(client);
    }

    @PutMapping
    @Operation(title = "clients modification", type = OperationType.UPDATE, saveResult = false)
    public ClientInfo edit(@Validated @RequestBody ClientInfo client) {
        if (!clientService.checkUnique(client)) {
            throw new ErrorCodeException("", "client name is already exists");
        }
        return clientService.update(client);
    }

    @GetMapping("/{id}")
    @Operation(title = "clients detail", type = OperationType.QUERY, saveResult = false)
    public ClientInfo get(@PathVariable String id) {
        return clientService.findById(id);
    }


    @GetMapping("/list")
    @Operation(title = "clients list", type = OperationType.LIST, saveResult = false)
    public PageResult list(ClientQuery query) {
        return new PageResult(clientService.count(query), clientService.find(query));
    }

    @PutMapping("/changeStatus")
    @Operation(title = "client status change", type = OperationType.UPDATE)
    public int changeStatus(@Validated @RequestBody ClientStatus clientStatus) {
        return clientService.changeStatus(clientStatus);
    }

    @PutMapping("/resetPasswd")
    @Operation(title = "clients password reset", type = OperationType.UPDATE, saveParameter = false)
    public int resetPasswd(@Validated @RequestBody ClientPasswd clientPasswd) {
        return clientService.resetPasswd(clientPasswd);
    }
}
