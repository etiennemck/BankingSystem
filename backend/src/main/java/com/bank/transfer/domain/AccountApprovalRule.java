package com.bank.transfer.domain;

import java.math.BigDecimal;

/** Applicable-rule snapshot; a null threshold represents invalid configuration. */
public record AccountApprovalRule(BigDecimal threshold) {
}
