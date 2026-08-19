class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}

public class Problem3 {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        int sum = calc.add(10, 25);
        System.out.println("The sum is: " + sum);
    }
}