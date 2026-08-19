class Vehicle {
    public void start() {
        System.out.println("Vehicle engine started.");
    }
}

class Car extends Vehicle {
    public void drive() {
        System.out.println("Car is driving.");
    }
}

public class Problem10 {
    public static void main(String[] args) {
        Car myCar = new Car();
        myCar.start();
        myCar.drive();
    }
}