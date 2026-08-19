class Person {
    protected String name;
    
    public void displayName() {
        System.out.println("Name: " + name);
    }
}

class Student extends Person {
    public void setupStudent() {
        name = "Charlie"; // Accessing the protected variable from the superclass
        displayName();
    }
}

public class Problem6 {
    public static void main(String[] args) {
        Student s = new Student();
        s.setupStudent();
    }
}