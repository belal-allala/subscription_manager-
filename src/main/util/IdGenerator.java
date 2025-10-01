package main.util;

import java.util.Random;

public class IdGenerator {
    public static String generateId() {
        Random random = new Random();
        int number = random.nextInt(999999); 
        return String.format("%06d", number); 
    }
}
