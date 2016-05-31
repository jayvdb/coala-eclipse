package com.coala.core.tests;

import java.util.Random;

public class TestUtils {

    public static String generateRandomStr(int length) {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars
                    .charAt(random.nextInt(candidateChars.length())));
        }

        return sb.toString();
    }
}
