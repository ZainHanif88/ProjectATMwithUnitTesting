package caw.atm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BankomatTest {

    IBank bankMock;
    Bankomat bankomat;
    User currentUser;
    private Account account;

    public BankomatTest() {
    }

    @BeforeEach
    public void setup(){
        bankomat = new Bankomat();
    }

    @Test
   public void CheckAccounts_UserExistButNotAccountNumber() {
        List<User> bankUsers = new LinkedList<>();
        List<Account> user1Accounts = new ArrayList<>();
        user1Accounts.add(new Account("1", 0));

        User user1 = new User("A", "A123", user1Accounts);
        bankUsers.add(user1);

        assertEquals( -9999, bankomat.CheckAccountBalance(user1, "15") );

    }

    @Test
    public void CheckAccounts_Comparing2UsersAndAccount() {


        List<User> bankUsers = new LinkedList<>();
        List<Account> user1Accounts = new ArrayList<>();
        List<Account> user2Accounts = new ArrayList<>();

        user1Accounts.add(new Account("1", 100));
        user2Accounts.add(new Account("2", 200));

        User user1 = new User("Ali", "A", user1Accounts);
        User user2 = new User("mrAli", "b", user2Accounts);
        bankUsers.add(user1);
        bankUsers.add(user2);


        int test1 = bankomat.CheckAccountBalance(user1, "1");
        int test2 = bankomat.CheckAccountBalance(user2, "2");
        assertNotEquals(test1 , test2 );
    }

    @Test
    public void CheckAccounts_UserDoesNtExist_ButAccountIs() {

        List<Account> user1Accounts = new ArrayList<>();
        user1Accounts.add(new Account("1", 0));

        int test1 =  bankomat.CheckAccountBalance(new User("", "", user1Accounts),"0" );
        assertEquals(-9999, test1);
    }



    @Test
    public void IsDepositeWorking() {

        account = new Account("1",0);
        bankomat.DepositMoney(account, 100);
        //see if depost working
        assertEquals(100, account.getAccountBalance());
    }


    @Test
     public void depositingAccountThatDoesntExist() {
        account = new Account("1",0);
        bankomat.DepositMoney(new Account("2", 0), 0);

        assertEquals(0, account.getAccountBalance());
    }


    @Test
    public void DepositeWithdraw() {

        account = new Account("1",100);

        bankomat.DepositMoney(account, 100);
        bankomat.WithdrawMoney(account, 100);


        assertEquals( 100, account.getAccountBalance());
    }

    @Test
    public void WithdrawMoney() {
        account = new Account("1",200);
        bankomat.WithdrawMoney(account, 190);

        assertEquals(10, account.getAccountBalance());
    }

    @Test
   public void withdrawMoreThenDeposit() {
        account = new Account("1",100);

        bankomat.DepositMoney(account, 100);
        bankomat.WithdrawMoney(account, 10000);

        assertEquals(200, account.getAccountBalance());

    }

    @Test
    void withdrawWhenBlance_0() {
        account = new Account("1",0);
        bankomat.WithdrawMoney(account, 10000);

        assertEquals(0, account.getAccountBalance());
    }


    @Test
    public void Login() {
        bankMock = mock(IBank.class);
        bankomat = new Bankomat(bankMock);

        List<User> bankUsers = new LinkedList<>();
        List<Account> user1Accounts = new ArrayList<>();
        user1Accounts.add(new Account("123456", 11));
        user1Accounts.add(new Account("987654", 12));
        User user1 = new User("Zainn", "Password", user1Accounts);
        bankUsers.add(user1);
        List<Account> user2Accounts = new ArrayList<>();
        user2Accounts.add(new Account("45753", 101));
        user2Accounts.add(new Account("455543", 102));
        User user2 = new User("Zain", "Password", user2Accounts);
        bankUsers.add(user2);

        when(bankMock.getUsers()).thenReturn(bankUsers);

        User result = bankomat.logIn("Zain", "Password");

        assertAll(() -> assertEquals(result.getName(), user2.getName()),
                () -> assertEquals(result.getPassword(), user2.getPassword()),
                () -> assertNotSame(result.getName(),user1.getName()),
                () -> assertEquals(result.getAccounts().get(0).getAccountNumber(), "" + 45753),
                () -> assertEquals(result.getAccounts().get(0).getAccountBalance(), 101),
                () -> assertEquals(result.getAccounts().get(1).getAccountNumber(), "" + 455543),
                () -> assertEquals(result.getAccounts().get(1).getAccountBalance(), 102));
    }
}
