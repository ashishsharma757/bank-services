package com.anz.application.bank_services.controller;

import com.anz.application.bank_services.model.Account;
import com.anz.application.bank_services.model.TransactionInput;
import com.anz.application.bank_services.repo.BankRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BankController {

    @Autowired
    private BankRepository bankRepository;

    @PostMapping("/create-account")
    public ResponseEntity<?> checkBalance(@RequestBody Account account) {
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            validate(account);
            addFreeGift(account);
            Account savedAccount = bankRepository.save(account);
            return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts()
    {
        try{
            List<Account> accountList = bankRepository.findAll();
            return new ResponseEntity<>(accountList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionInput transactionInput)
    {
        try {
            Long inputAccountNumber = transactionInput.getAccountNumber();
            double inputAmount = transactionInput.getAmount();
            validateAmount(inputAmount);
            Account foundAccount = bankRepository.findByAccountNumber(inputAccountNumber);

            if (foundAccount != null) {
                double pastBalance = foundAccount.getCurrentBalance();
                foundAccount.setCurrentBalance(pastBalance + inputAmount);
                bankRepository.save(foundAccount);
                return new ResponseEntity<>(foundAccount, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Account doesn't exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<?> withdrawal(@RequestBody TransactionInput transactionInput)
    {
        try {
            Long inputAccountNumber = transactionInput.getAccountNumber();
            double inputAmount = transactionInput.getAmount();
            Account foundAccount = bankRepository.findByAccountNumber(inputAccountNumber);
            if (foundAccount != null) {
                double pastAmount = foundAccount.getCurrentBalance();
                if (pastAmount < inputAmount) {
                    throw new Exception("Insufficient amount");
                }
                foundAccount.setCurrentBalance(pastAmount - inputAmount);
                bankRepository.save(foundAccount);
                return new ResponseEntity<>(foundAccount,HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Account doesn't exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/account/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id)
    {
        try {
            Account foundAccount = bankRepository.findByAccountNumber(id);
            if (foundAccount != null) {
                return new ResponseEntity<>(foundAccount, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Account doesn't exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id)
    {
        try {
            Account foundAccount = bankRepository.findByAccountNumber(id);
            if (foundAccount != null) {
                 bankRepository.deleteById(id);
                 return new ResponseEntity<>("Account deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Account doesn't exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addFreeGift(Account account) {
        account.setCurrentBalance(1000);
    }


    private void validate(Account account) throws Exception {
        String firstName = account.getFirstName();
        String phoneNumber = account.getPhoneNumber();
        if (firstName == null || firstName.isEmpty()) {
            throw new Exception("First name is required");
        }

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new Exception("phone number is required");
        } else if (phoneNumber.length() != 10) {
            throw new Exception("phone number is invalid");
        }
    }

    private void validateAmount(double inputAmount) throws Exception {
        if(inputAmount < 0) {
            throw new Exception("Deposit amount should be greater than 0");
        }
    }

}
