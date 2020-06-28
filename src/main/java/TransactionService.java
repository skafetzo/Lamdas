import model.Person;
import model.Transaction;
import repositories.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionService {
    private final PersonsService personsService;
    private final TransactionRepository transactionRepository;

    public TransactionService(PersonsService personsService, TransactionRepository transactionRepository) {
        this.personsService = personsService;
        this.transactionRepository = transactionRepository;
    }

    List<String> getPersonRolesOfAllTransactions() {

        List <Transaction> transactions = transactionRepository.getTransactions();

        return    transactions.stream().map(transaction -> transaction.getEmailAddress())
                .map(email -> personsService.getPersonByEmailAddress(email))
                .filter (Optional::isPresent)
                .map(Optional::get)
                .map(Person::getRoles)
                .flatMap(roles -> roles.stream())
                .distinct()
                .collect(Collectors.toList());

    }
}
