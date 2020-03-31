package com.github.stefanvozd.cqrs.reactiveaxon.api;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class AccountDebitedEvt implements TransactionEvt {

    UUID accountId;
    BigDecimal amount;
    String description;
    BigDecimal newBalance;

}
