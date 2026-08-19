class StudentInfo {
    public String name;
    private int id;

}

public class Problem5 {
    public static void main(String[] args) {
        StudentInfo student = new StudentInfo();
        
        student.name = "Bob"; // This works because 'name' is public
        System.out.println("Name: " + student.name);
        
        // student.id = 123; 
        // ^ Error: id has private access in StudentInfo. 
        // The private member cannot be accessed directly outside its class.
    }
}