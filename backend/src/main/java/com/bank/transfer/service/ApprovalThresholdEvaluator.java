package com.bank.transfer.service;

import com.bank.transfer.domain.AccountApprovalRule;
import com.bank.transfer.domain.ApprovalDecision;

import java.math.BigDecimal;
import java.util.Optional;

/** Pure decision component; it neither posts transfers nor enforces an approval workflow. */
public final class ApprovalThresholdEvaluator {

    public ApprovalDecision evaluate(BigDecimal amount, Optional<AccountApprovalRule> rule) {
        if (!isValidMoney(amount)) {
            throw new IllegalArgumentException(
                    "Amount must be positive with at most 15 integer and 2 fractional digits");
        }
        if (rule == null) {
            throw new IllegalArgumentException(
                    "Rule selection is required; use an empty selection when no rule applies");
        }
        if (rule.isEmpty()) {
            return ApprovalDecision.NOT_REQUIRED;
        }

        BigDecimal threshold = rule.get().threshold();
        if (!isValidMoney(threshold)) {
            return ApprovalDecision.INVALID_CONFIGURATION;
        }
        return amount.compareTo(threshold) >= 0
                ? ApprovalDecision.REQUIRED : ApprovalDecision.NOT_REQUIRED;
    }

    private boolean isValidMoney(BigDecimal value) {
        return value != null
                && value.signum() > 0
                && value.scale() <= 2
                && (long) value.precision() - value.scale() <= 15;
    }
}
