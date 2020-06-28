package repositories;

import model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository {
    List<Transaction> getTransactions();
    List<Transaction> getTransactions(LocalDateTime from);
}
