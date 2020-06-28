package daos;

import daos.entities.TransactionEntity;

import java.util.List;

public interface TransactionDAO {
    List<TransactionEntity> getTransactions();
}
