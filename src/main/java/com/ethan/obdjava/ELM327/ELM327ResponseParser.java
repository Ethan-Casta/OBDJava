package com.ethan.obdjava.ELM327;

public class ELM327ResponseParser {
    public static Integer parseCommandData(ELM327Command command, String responseHex) {

        switch (command) {
            case SPEED -> {
                return speedParser(responseHex);
            }
            case RPM ->  {
                return rpmParser(responseHex);
            }
            case AIRINTAKE -> {
                return airInTakeParser(responseHex);
            }
            case ENGINETEMP -> {
                return engineTempParser(responseHex);
            }
            default -> {
                return 0;
            }
        }
    }

    private static Integer speedParser(String responseHex) {
        String[] bytes = responseHex.trim().split(" ");

        if (bytes.length >= 3) {
            return Integer.parseInt(bytes[2], 16);
        }
        return null;
    }

    private static Integer rpmParser(String responseHex) {
        String[] bytes = responseHex.trim().split(" ");

        if (bytes.length >= 4) {
            return (Integer.parseInt(bytes[2], 16) * 256 + Integer.parseInt(bytes[3], 16)) / 4;
        }
        return null;
    }

    private static Integer airInTakeParser(String responseHex) {
        String[] bytes = responseHex.trim().split(" ");

        if (bytes.length >= 4) {
            return Integer.parseInt(bytes[2], 16) * 256 + Integer.parseInt(bytes[3], 16);
        }
        return null;
    }
    private static Integer engineTempParser(String responseHex) {
        String[] bytes = responseHex.trim().split(" ");

        if (bytes.length >= 3) {
            return Integer.parseInt(bytes[2], 16) - 40;
        }
        return null;
    }
}
