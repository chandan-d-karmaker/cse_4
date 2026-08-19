class BankAccount {
    private double balance;

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public double getBalance() {
        return balance;
    }
}

public class Problem8 {
    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        account.deposit(500.75);
        System.out.println("Current Balance: $" + account.getBalance());
    }
}