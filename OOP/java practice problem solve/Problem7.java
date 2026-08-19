class EncapsulatedStudent {
    private String name;

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }
}

public class Problem7 {
    public static void main(String[] args) {
        EncapsulatedStudent student = new EncapsulatedStudent();
        student.setName("David");
        System.out.println("Student Name: " + student.getName());
    }
}