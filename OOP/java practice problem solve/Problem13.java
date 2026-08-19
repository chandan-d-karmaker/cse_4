class Display {
    public void show(int value) {
        System.out.println("Integer value: " + value);
    }

    public void show(double value) {
        System.out.println("Double value: " + value);
    }

    public void show(String value) {
        System.out.println("String value: " + value);
    }
}

public class Problem13 {
    public static void main(String[] args) {
        Display display = new Display();
        display.show(42);
        display.show(3.14159);
        display.show("Hello Java");
    }
}