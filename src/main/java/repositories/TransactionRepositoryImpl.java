package repositories;

import daos.TransactionDAO;
import daos.entities.TransactionEntity;
import model.Transaction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionDAO transactionDAO;

    public TransactionRepositoryImpl(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Override
    public List<Transaction> getTransactions() {

        List<TransactionEntity> entities = transactionDAO.getTransactions();

        List<Transaction> transactions =
                entities.stream()
                        .map(transactionEntity -> {
                            Transaction transaction = new Transaction();
                            transaction.setAmount(transactionEntity.getAmount());
                            transaction.setEmailAddress(transactionEntity.getEmail());

                            Instant timestamp = transactionEntity.getDate();
                            LocalDateTime date = LocalDateTime.ofInstant(timestamp, ZoneId.of("Europe/Athens"));
                            transaction.setDate(date);
                            return transaction;
                        }).collect(Collectors.toList());

        throw new UnsupportedOperationException();
    }

    @Override
    public List<Transaction> getTransactions(LocalDateTime from) {

        ZonedDateTime zonedDateTime = from.atZone(ZoneId.of("Europe/Athens"));
        Instant fromInstnat = zonedDateTime.toInstant();

        List<TransactionEntity> entities = transactionDAO.getTransactions();

        entities.stream()
                .filter(e -> {
                    return   e.getDate().isAfter(fromInstnat);
                })
                .map(transactionEntity -> {
                    Transaction transaction = new Transaction();
                    transaction.setAmount(transactionEntity.getAmount());
                    transaction.setEmailAddress(transactionEntity.getEmail());

                    Instant timestamp = transactionEntity.getDate();
                    LocalDateTime date = LocalDateTime.ofInstant(timestamp, ZoneId.of("Europe/Athens"));
                    transaction.setDate(date);
                    return transaction;
                }).collect(Collectors.toList());

        throw new UnsupportedOperationException();
    }
}
