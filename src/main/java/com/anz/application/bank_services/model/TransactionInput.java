package com.anz.application.bank_services.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionInput {
    String txnId;
    private Long accountNumber;
    private double amount;
}
