import model.Person;
import model.Transaction;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import repositories.TransactionRepository;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class CSVReportServiceTest {

    private final PersonsService personService = Mockito.mock(PersonsService.class);
    private final TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);

    CSVReportService csvReportService = new CSVReportService(personService, transactionRepository);

    TransactionService transactionService = new TransactionService(personService, transactionRepository);

    @Test
    public void getAverageConsumptionPerRoleDuringTheLastMonthTest(){

        Person john = new Person();
        List<String> rolesOfJohn = Arrays.asList("student","athlete");
        john.setRoles(rolesOfJohn.stream().collect(Collectors.toSet()));
        // john.setRoles(Set.of("student", "gamer", "athlete"));
        john.setEmailAddress("john@test.com");

        Person jane = new Person();
        List<String> rolesOfJane = Arrays.asList("gamer", "athlete");
        jane.setRoles(rolesOfJane.stream().collect(Collectors.toSet()));
        //   jane.setRoles(Set.of("employee", "athlete"));
        jane.setEmailAddress("jane@test.com");

        Person bob = new Person();
        List<String> rolesOfBob = Arrays.asList("gamer");
        bob.setRoles(rolesOfBob.stream().collect(Collectors.toSet()));
        //   jane.setRoles(Set.of("employee", "athlete"));
        bob.setEmailAddress("bob@test.com");

        List<Person> persons = new ArrayList<>();
        persons.add(bob);
        persons.add(jane);
        persons.add(john);

        // and
        Mockito.stub(personService.getPersonByEmailAddress(Matchers.eq("john@test.com"))).toReturn(Optional.of(john));
        Mockito.stub(personService.getPersonByEmailAddress(Matchers.eq("jane@test.com"))).toReturn(Optional.of(jane));
        Mockito.stub(personService.getPersonByEmailAddress(Matchers.eq("bob@test.com"))).toReturn(Optional.of(bob));

        // and
        List<Transaction> mockedTransactions = new ArrayList<>();

        mockedTransactions.add(createTransaction(10, "john@test.com", LocalDateTime.now().minusDays(5)));
        mockedTransactions.add(createTransaction(30, "jane@test.com",  LocalDateTime.now().minusDays(35)));
        mockedTransactions.add(createTransaction(20, "jane@test.com",  LocalDateTime.now().minusDays(10)));
        mockedTransactions.add(createTransaction(20, "bob@test.com",  LocalDateTime.now().minusDays(20)));
        Mockito.stub(transactionRepository.getTransactions())
                .toReturn(
                        mockedTransactions
                );

         String result = csvReportService.getAverageConsumptionPerRoleDuringTheLastMonth();

        Map<String, Double> consumptionPerRole = new HashMap<>();
        List<String> csvRoles = Arrays.asList(result.split("\n")[0].split(","));
        List<String> csvValues = Arrays.asList(result.split("\n")[1].split(","));
        for(int i = 0;i < csvRoles.size(); i++){
            consumptionPerRole.put(csvRoles.get(i), Double.parseDouble(csvValues.get(i)));
        }
        assertEquals(3, csvRoles.size());
        assertTrue(consumptionPerRole.get("student") == 10.0);
        assertTrue(consumptionPerRole.get("gamer") == 20.0);
        assertTrue(consumptionPerRole.get("athlete") == 15.0);
    }

    private Transaction createTransaction(int amount, String emailAddress, LocalDateTime date) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setEmailAddress(emailAddress);
        transaction.setDate(date);
        return transaction;
    }
}
