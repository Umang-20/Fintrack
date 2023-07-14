package com.backend.mock.controller;

import com.backend.dto.AccountDTO;
import com.backend.mock.dto.MockAccountDTO;
import com.backend.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Random;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/mock-account")
public class MockAccountController {
    private final AccountService accountService;
    private final Random random = new Random();

    @PostMapping("")
    public ResponseEntity getAccount(@RequestBody MockAccountDTO mockAccountDTO) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber(mockAccountDTO.getAccountNumber());
        accountDTO.setBankName(mockAccountDTO.getBankName());
        accountDTO.setBalance(random.nextDouble(5000));
        accountDTO.setAccountType(random.nextInt(2) == 1 ? "Credit" : "Debit");
        return ResponseEntity.status(200).body(accountDTO);
    }

    @GetMapping("/balance")
    public ResponseEntity getBalance() {
        ArrayList<AccountDTO> accountList = accountService.getAccounts();
        accountList.forEach(account -> {
            account.setBalance(account.getBalance() + random.nextDouble(-0.5, 0.5) * account.getBalance());
            accountService.updateAccount(account);
        });
        return ResponseEntity.status(200).body(null);
    }
}
