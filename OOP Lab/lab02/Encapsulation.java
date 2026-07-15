class Atm {

    // attributes
    private int balance = 1890;
    private final int card_number = 1;

    // methods
    void withdraw(int amount) {
        System.out.println("Withdraw amout: " + amount);
        if (amount < balance) {
            balance = balance - amount;
            System.out.println("Balance after withdraw: " + balance);

        } else {
            System.out.println("Invalid amount");
        }
    }

    public void display() {
        System.out.println("Balance: " + balance);
        System.out.println("Card Number: " + card_number);
    }

}

public class Encapsulation {

    public static void main(String[] args) {

        // Atm user1 = new Atm();
        // user1.balance = 1000;
        // user1.card_number = 3252523;
        // user1.display();
        Atm user2 = new Atm();

        // user2.balance = 5000;
        // user2.card_number = 24525;
        user2.display();
        user2.withdraw(1000);
    }
}
