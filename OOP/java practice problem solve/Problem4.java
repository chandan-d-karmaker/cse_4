class Rectangle {
    double length;
    double width;

    public double area() {
        return length * width;
    }
}

public class Problem4 {
    public static void main(String[] args) {
        Rectangle rect = new Rectangle();
        rect.length = 5.0;
        rect.width = 4.0;
        
        System.out.println("The area of the rectangle is: " + rect.area());
    }
}