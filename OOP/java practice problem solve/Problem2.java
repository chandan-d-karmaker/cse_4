class Book {
    String title;
    double price;
}

public class Problem2 {
    public static void main(String[] args) {
        Book book1 = new Book();
        book1.title = "The Great Gatsby";
        book1.price = 15.99;

        Book book2 = new Book();
        book2.title = "1984";
        book2.price = 12.50;

        System.out.println("Book 1: " + book1.title + " ($" + book1.price + ")");
        System.out.println("Book 2: " + book2.title + " ($" + book2.price + ")");
    }
}