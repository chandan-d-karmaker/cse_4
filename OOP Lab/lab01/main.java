class Student {
    int id;
    String name;
}

public class main {
    public static void main(String[] args) {
        Student s1 = new Student();
        s1.id = 12511006;
        s1.name = "Chandan Karmaker";

        Student s2 = new Student();
        s2.id = 12511043;
        s2.name = "Emon Hossain";

        System.out.println("Name: "+ s1.name + " " + "ID: " + s1.id);
        System.out.println("Name: "+ s2.name + " " + "ID: " + s2.id);
    }
}