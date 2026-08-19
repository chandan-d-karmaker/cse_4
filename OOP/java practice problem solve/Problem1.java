class Student {
    String name;
    int id;
}

public class Problem1 {
    public static void main(String[] args) {
        Student student = new Student();
        student.name = "Alice";
        student.id = 101;
        
        System.out.println("Student ID: " + student.id);
        System.out.println("Student Name: " + student.name);
    }
}