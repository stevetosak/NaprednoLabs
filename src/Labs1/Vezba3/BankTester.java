package Labs1.Vezba3;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

class Account{
    private final String username;
    private final long id;
    private String balance;

    Account(String name,String balance){
        this.username = name;
        this.balance = balance;
        Random generator = new Random();
        id = (long) (generator.nextFloat() * 1000);
    }
    public String getBalance(){
        return balance;
    }
    public String getName(){
        return username;
    }
    public long getId(){
        return id;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("Name:%s\nBalance:%s\n",username,balance);
    }
}
class ConvertString{
    public static BigDecimal stringToDecimal(String s){
        if(s != null){
            StringBuilder newVal = new StringBuilder();
            newVal.append(s);
            newVal.deleteCharAt(s.length()-1);
            BigDecimal result = new BigDecimal(String.valueOf(newVal));
            result = result.setScale(2,RoundingMode.DOWN);

            return result;
        }
        return null;

    }
    public static String decimalToString(BigDecimal a){
        StringBuilder result = new StringBuilder();
        result.append(a);
        result.append('$');

        return result.toString();
    }

}
abstract class Transaction {
    protected final long fromId;
    protected final long toId;
    protected final String description;
    protected final String amount;

    public String getDescription() {
        return description;
    }

    public Transaction(){
        fromId = 0;
        toId = 0;
        description = "/";
        amount = "0.00$";
    };

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }

    abstract String getAmountWithProvision();
    public String getAmount(){
        return amount;
    }
    abstract String getProvisionOnly();
}
class FlatAmountProvisionTransaction extends Transaction{
    private final String flatProvision;

    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision = flatProvision;
    }

    @Override
    public String getAmountWithProvision(){
        BigDecimal fAmount = ConvertString.stringToDecimal(amount);
        fAmount = fAmount.add(ConvertString.stringToDecimal(flatProvision));
        return ConvertString.decimalToString(fAmount);
    }

    @Override
    String getProvisionOnly() {
        return flatProvision;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return Objects.equals(flatProvision, that.flatProvision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatProvision);
    }
}
class FlatPercentProvisionTransaction extends Transaction{
    private final int centsPerDollar;
    public FlatPercentProvisionTransaction(long fromId, long toId, String amount,int centsPerDollar) {
        super(fromId, toId, "FlatPercent", amount);
        this.centsPerDollar = centsPerDollar;
    }


    @Override
    String getProvisionOnly() {
//        BigDecimal amt = ConvertString.stringToDecimal(amount);
//        int result = amt.intValue();
//        result = (result * centsPerDollar)/100;
//        BigDecimal prov = new BigDecimal(result);
//        return ConvertString.decimalToString(prov);

        BigDecimal calc = ConvertString.stringToDecimal(amount);
        BigDecimal pct = new BigDecimal(centsPerDollar);
        calc = calc.multiply(pct);
        calc = calc.divide(BigDecimal.valueOf(100),2,RoundingMode.DOWN);

        return ConvertString.decimalToString(calc);


    }
    @Override
    public String getAmountWithProvision(){
        BigDecimal pAmount = ConvertString.stringToDecimal(amount);
        BigDecimal pct = ConvertString.stringToDecimal(getProvisionOnly());
        pAmount = pAmount.add(pct);         //pAmount + ((pAmount * centsPerDollar)/100);
        return ConvertString.decimalToString(pAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return centsPerDollar == that.centsPerDollar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(centsPerDollar);
    }
}
class Bank{
    private Account[] accounts;
    private String bankName;
    private String totalTransfers;
    private String totalProvison;

    private boolean checkAccounts(long targetId){
        for (int i = 0; i < accounts.length; i++) {
            if(accounts[i].getId() == targetId){
                return true;
            }
        }
        return false;
    }

    Bank(String bankName,Account[] accounts){
        this.bankName = bankName;
        this.accounts = accounts;
        totalTransfers = "0.00$";
        totalProvison = "0.00$";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Arrays.equals(accounts, bank.accounts) && Objects.equals(bankName, bank.bankName) && Objects.equals(totalTransfers, bank.totalTransfers) && Objects.equals(totalProvison, bank.totalProvison);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(bankName, totalTransfers, totalProvison);
        result = 31 * result + Arrays.hashCode(accounts);
        return result;
    }

    boolean makeTransaction(Transaction t){
        boolean state = false;
        if(checkAccounts(t.fromId) && checkAccounts(t.toId)){
            for (int i = 0; i < accounts.length; i++) {
                if(t.fromId == accounts[i].getId()){
                    if(ConvertString.stringToDecimal(accounts[i].getBalance()).compareTo(ConvertString.stringToDecimal(t.getAmountWithProvision())) >= 0 ){
                        state = true;
                        BigDecimal newBalance = ConvertString.stringToDecimal(accounts[i].getBalance());
                        newBalance = newBalance.subtract(ConvertString.stringToDecimal(t.getAmountWithProvision()));
                        accounts[i].setBalance(ConvertString.decimalToString(newBalance));
                    }
                }
            }
            if(state){
                BigDecimal newTotal = ConvertString.stringToDecimal(totalTransfers);
                newTotal = newTotal.add(ConvertString.stringToDecimal(t.amount));
                totalTransfers = ConvertString.decimalToString(newTotal);

                BigDecimal newProvision = ConvertString.stringToDecimal(totalProvison);
                newProvision = newProvision.add(ConvertString.stringToDecimal(t.getProvisionOnly()));
                totalProvison = ConvertString.decimalToString(newProvision);

                for (int i = 0; i < accounts.length; i++) {
                    if(t.toId == accounts[i].getId()){
                        BigDecimal newBalance = ConvertString.stringToDecimal(accounts[i].getBalance());
                        newBalance = newBalance.add(ConvertString.stringToDecimal(t.getAmount()));
                        accounts[i].setBalance(ConvertString.decimalToString(newBalance));
                    }
                }
                return true;
            }

        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Name: " + bankName + '\n' +'\n');

        for (int i = 0; i < accounts.length; i++) {
            result.append("Name: " + accounts[i].getName() + '\n' + "Balance: "+ accounts[i].getBalance() + '\n');
        }
        return result.toString();
    }

    public String totalTransfers(){
        return totalTransfers;
    }
    public String totalProvision(){
        return totalProvison;
    }


    public Account[] getAccounts() {
        return accounts;
    }
}
public class BankTester {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        //accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}
