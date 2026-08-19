class Shape {
    public void draw() {
        System.out.println("Drawing a shape");
    }
}

class Circle extends Shape {
    @Override
    public void draw() {
        System.out.println("Drawing a circle");
    }
}

class RectangleShape extends Shape {
    @Override
    public void draw() {
        System.out.println("Drawing a rectangle");
    }
}

public class Problem15 {
    public static void main(String[] args) {
        Circle circle = new Circle();
        RectangleShape rectangle = new RectangleShape();
        
        circle.draw();
        rectangle.draw();
    }
}