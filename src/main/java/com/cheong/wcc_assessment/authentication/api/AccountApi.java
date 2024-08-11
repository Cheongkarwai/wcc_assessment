package com.cheong.wcc_assessment.authentication.api;

import com.cheong.wcc_assessment.authentication.dto.AccountDTO;
import com.cheong.wcc_assessment.authentication.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountApi {

    private final UserService userService;

    public AccountApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody AccountDTO accountDTO) {
        userService.createUser(accountDTO);
    }
}
