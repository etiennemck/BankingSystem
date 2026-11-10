package com.bank.transfer;

import com.bank.account.domain.Account;
import com.bank.account.domain.AccountStatus;
import com.bank.account.domain.AccountType;
import com.bank.account.repo.AccountRepository;
import com.bank.common.event.EventPublisher;
import com.bank.common.exception.NotFoundException;
import com.bank.ledger.domain.Transaction;
import com.bank.ledger.domain.TransactionType;
import com.bank.ledger.service.LedgerService;
import com.bank.ledger.service.PostingLine;
import com.bank.transfer.service.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceCompatibilityTest {

    @Mock private AccountRepository accounts;
    @Mock private LedgerService ledger;
    @Mock private EventPublisher events;

    @Test
    void legacyTransferKeepsCompletedResponsePostingContractAndIdempotencyKey() {
        UUID userId = UUID.randomUUID();
        Account source = account("100000000001", userId, "200.00");
        Account destination = account("100000000002", UUID.randomUUID(), "0.00");
        BigDecimal amount = new BigDecimal("75.00");
        String key = "legacy-transfer-intent";
        Instant createdAt = Instant.parse("2026-11-09T10:00:00Z");
        Transaction posted = new Transaction("TXN-LEGACY", TransactionType.TRANSFER, "Transfer");
        posted.setCreatedAt(createdAt);
        when(accounts.findByIdAndOwnerUserId(source.getId(), userId)).thenReturn(Optional.of(source));
        when(accounts.findByAccountNumber(destination.getAccountNumber()))
                .thenReturn(Optional.of(destination));
        when(ledger.post(eq(TransactionType.TRANSFER), anyString(), eq(key), anyList()))
                .thenAnswer(invocation -> {
                    source.setBalance(new BigDecimal("125.00"));
                    destination.setBalance(new BigDecimal("75.00"));
                    return posted;
                });

        var response = new TransferService(accounts, ledger, events).transfer(userId, source.getId(),
                destination.getAccountNumber(), amount, "Demo transfer", key);

        assertThat(response.reference()).isEqualTo("TXN-LEGACY");
        assertThat(response.sourceAccountId()).isEqualTo(source.getId());
        assertThat(response.destinationAccountNumber()).isEqualTo(destination.getAccountNumber());
        assertThat(response.amount()).isEqualByComparingTo("75.00");
        assertThat(response.sourceBalanceAfter()).isEqualByComparingTo("125.00");
        assertThat(response.status()).isEqualTo("COMPLETED");
        assertThat(response.createdAt()).isEqualTo(createdAt);
        verify(ledger).post(eq(TransactionType.TRANSFER), anyString(), eq(key), eq(List.of(
                PostingLine.debit(source.getId(), amount),
                PostingLine.credit(destination.getId(), amount))));
    }

    @Test
    void legacyOwnershipDenialStillPreventsPosting() {
        UUID userId = UUID.randomUUID();
        UUID sourceId = UUID.randomUUID();
        when(accounts.findByIdAndOwnerUserId(sourceId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> new TransferService(accounts, ledger, events)
                .transfer(userId, sourceId, "100000000002", new BigDecimal("75.00"), null, null))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Source account not found");

        verifyNoInteractions(ledger, events);
        verify(accounts, never()).findByAccountNumber(anyString());
    }

    private Account account(String number, UUID ownerId, String balance) {
        Account account = new Account(number, ownerId, AccountType.CHECKING, "USD");
        account.setId(UUID.randomUUID());
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(new BigDecimal(balance));
        return account;
    }
}
