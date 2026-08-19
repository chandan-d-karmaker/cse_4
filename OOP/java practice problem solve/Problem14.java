class SoundAnimal {
    public void sound() {
        System.out.println("Some generic animal sound");
    }
}

class SoundDog extends SoundAnimal {
    @Override
    public void sound() {
        System.out.println("Dog barks");
    }
}

public class Problem14 {
    public static void main(String[] args) {
        SoundDog dog = new SoundDog();
        dog.sound();
    }
}