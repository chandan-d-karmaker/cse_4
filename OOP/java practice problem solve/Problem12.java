class AdvancedCalculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int add(int a, int b, int c) {
        return a + b + c;
    }
}

public class Problem12 {
    public static void main(String[] args) {
        AdvancedCalculator calc = new AdvancedCalculator();
        System.out.println("Sum of two numbers: " + calc.add(5, 10));
        System.out.println("Sum of three numbers: " + calc.add(5, 10, 15));
    }
}