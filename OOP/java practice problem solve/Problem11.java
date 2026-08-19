class BasePerson {
    String name;
    
    public void displayName() {
        System.out.println("Name: " + name);
    }
}

class CollegeStudent extends BasePerson {
    int id;
}

public class Problem11 {
    public static void main(String[] args) {
        CollegeStudent student = new CollegeStudent();
        student.name = "Eve";
        student.id = 2024;
        
        student.displayName();
        System.out.println("ID: " + student.id);
    }
}