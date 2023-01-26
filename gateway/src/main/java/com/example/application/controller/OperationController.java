package com.example.application.controller;

import com.example.application.config.Cache;
import com.example.application.model.Account;
import com.example.application.model.AccountOperation;
import com.example.application.model.UserInfo;
import com.example.application.service.OperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class OperationController {

    final OperationService service;

    public OperationController(OperationService service) {
        this.service = service;
    }

    @GetMapping("/account")
    public ResponseEntity<List<Account>> getAccounts(
            @RequestHeader("X-TOKEN") final String token,
            @RequestHeader("X-OPERATION-ID") final String operationId
    ) {
//        UserAccounts userAccounts = Cache.getAccounts(token);
//        Cache.fillBalances(userAccounts);
//        List<Account> rs = userAccounts.getAccounts();
        List<Account> rs = service.getAccounts(operationId, token);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping("/userInfo")
    public ResponseEntity<UserInfo> getUserInfo(
            @RequestHeader("X-TOKEN") final String token,
            @RequestHeader("X-OPERATION-ID") final String operationId
    ) {
        UserInfo rs = service.getUserInfo(operationId, token);
        //UserInfo rs = Cache.getUserInfo(token);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @PostMapping("/charge")
    public ResponseEntity<Void> charge(
            @RequestHeader("X-TOKEN") final String token,
            @RequestHeader("X-OPERATION-ID") final String operationId,
            @RequestBody AccountOperation operation
    ) {
        Cache.chargeBalance(operation.getAccount(), operation.getSum());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(
            @RequestHeader("X-TOKEN") final String token,
            @RequestHeader("X-OPERATION-ID") final String operationId,
            @RequestBody AccountOperation operation
    ) {
        Cache.withdrawBalance(operation.getAccount(), operation.getSum());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> error(Exception ex) {
        if (ex instanceof RuntimeException) {
            return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
