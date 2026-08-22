class Student {
    String name;
    int id;
}

public class Problem1 {
    public static void main(String[] args) {
        Student s1 = new Student();
        s1.name = "Maishya";
        s1.id = 101;
        
        System.out.println("Student ID: " + s1.id);
        System.out.println("Student Name: " + s1.name);
    }
}