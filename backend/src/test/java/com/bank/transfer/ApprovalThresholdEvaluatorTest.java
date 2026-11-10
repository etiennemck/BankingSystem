package com.bank.transfer;

import com.bank.transfer.domain.AccountApprovalRule;
import com.bank.transfer.domain.ApprovalDecision;
import com.bank.transfer.service.ApprovalThresholdEvaluator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Optional;

import static com.bank.transfer.domain.ApprovalDecision.INVALID_CONFIGURATION;
import static com.bank.transfer.domain.ApprovalDecision.NOT_REQUIRED;
import static com.bank.transfer.domain.ApprovalDecision.REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ApprovalThresholdEvaluatorTest {

    private final ApprovalThresholdEvaluator evaluator = new ApprovalThresholdEvaluator();

    @ParameterizedTest
    @ValueSource(strings = {"0.01", "99.99", "100.00", "1000000.00", "999999999999999.99"})
    void noApplicableRuleNeverInventsAThreshold(String amount) {
        assertThat(evaluator.evaluate(new BigDecimal(amount), Optional.empty()))
                .isEqualTo(NOT_REQUIRED);
    }

    @ParameterizedTest
    @CsvSource({
            "99.99, 100.00, NOT_REQUIRED",
            "100.00, 100.00, REQUIRED",
            "100.01, 100.00, REQUIRED",
            "100, 100.00, REQUIRED",
            "100.00, 100, REQUIRED",
            "1E+2, 100.00, REQUIRED",
            "100.00, 1E+2, REQUIRED",
            "0.01, 0.01, REQUIRED",
            "999999999999999.99, 999999999999999.99, REQUIRED",
            "999999999999999.98, 999999999999999.99, NOT_REQUIRED",
            "200000.00, 200000.00, REQUIRED",
            "1E+14, 100000000000000.00, REQUIRED"
    })
    void evaluatesExactNumericBoundary(String amount, String threshold, ApprovalDecision expected) {
        var rule = new AccountApprovalRule(new BigDecimal(threshold));

        assertThat(evaluator.evaluate(new BigDecimal(amount), Optional.of(rule)))
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "0", "-1.00", "0.001", "100.000", "1000000000000000.00", "1E+15", "1E+2147483647"
    })
    void invalidThresholdNeverMeansNoRule(String threshold) {
        var rule = new AccountApprovalRule(threshold == null ? null : new BigDecimal(threshold));

        assertThat(evaluator.evaluate(new BigDecimal("100.00"), Optional.of(rule)))
                .isEqualTo(INVALID_CONFIGURATION);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "0", "-0.01", "0.001", "100.000", "1000000000000000.00", "1E+15", "1E+2147483647"
    })
    void rejectsInvalidAmountEvenWithoutARule(String amount) {
        BigDecimal value = amount == null ? null : new BigDecimal(amount);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> evaluator.evaluate(value, Optional.empty()));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> evaluator.evaluate(value,
                        Optional.of(new AccountApprovalRule(new BigDecimal("100.00")))));
    }

    @Test
    void missingRuleSelectionIsNotEvidenceOfNoRule() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> evaluator.evaluate(new BigDecimal("100.00"), null));
    }

    @Test
    void repeatedAndInterleavedSnapshotsDoNotChangeInputsOrLeakDecisions() {
        BigDecimal amount = new BigDecimal("50.00");
        BigDecimal threshold = new BigDecimal("100.00");
        var firstAccount = new AccountApprovalRule(threshold);
        var secondAccount = new AccountApprovalRule(new BigDecimal("10.00"));

        for (int i = 0; i < 3; i++) {
            assertThat(evaluator.evaluate(amount, Optional.of(firstAccount))).isEqualTo(NOT_REQUIRED);
            assertThat(evaluator.evaluate(amount, Optional.of(secondAccount))).isEqualTo(REQUIRED);
            assertThat(evaluator.evaluate(amount, Optional.empty())).isEqualTo(NOT_REQUIRED);
        }
        assertThat(firstAccount.threshold()).isSameAs(threshold).isEqualByComparingTo("100.00");
        assertThat(amount).isEqualByComparingTo("50.00");
    }
}
