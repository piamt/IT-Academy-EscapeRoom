package utils;

import classes.enums.Level;
import classes.enums.Material;
import classes.enums.Theme;
import exceptions.IncorrectInputException;
import exceptions.StringException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Entry {
    public static Integer readInt(String message) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        boolean isValidInput = false;
        int validInput = 0;
        while (!isValidInput) {
            try {
                validInput = scanner.nextInt();
                isValidInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Format error. " + message);
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        return validInput;
    }

    private static Integer checkValidIntegerFromList(Integer input, List<Integer> validResults) throws IncorrectInputException {
        if (validResults.contains(input)) {
            return input;
        } else {
            throw new IncorrectInputException("Introduced value does not exist. ");
        }
    }

    public static Integer readInt(String message, List<Integer> validResults) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        boolean isValidInput = false;
        int validInput = 0;
        while (!isValidInput) {
            try {
                validInput = checkValidIntegerFromList(scanner.nextInt(), validResults);
                isValidInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Format error. " + message);
            } catch (IncorrectInputException e) {
                System.out.println(e.getMessage() + message);
            }
            finally {
                scanner.nextLine();
            }
        }
        return validInput;
    }

    public static String readString(String message) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        boolean isValidInput = false;
        String validInput = null;
        while (!isValidInput) {
            try {
                validInput = stringReadLineAndCheck(scanner);
                isValidInput = true;
            } catch (StringException e) {
                System.out.println("Format error. " + e.getMessage());
            }
        }
        scanner.reset();
        return validInput;
    }

    public static Double readDouble(String message) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        boolean isValidInput = false;
        double validInput = 0;
        while (!isValidInput) {
            try {
                validInput = scanner.nextDouble();
                isValidInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Format error. " + message);
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        return validInput;
    }

    public static Level readLevel(String message) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        boolean isValidInput = false;
        Level validInput = null;
        while (!isValidInput) {
            try {
                validInput = checkValidLevel(stringReadLineAndCheck(scanner));
                isValidInput = true;
            } catch (StringException | IncorrectInputException e) {
                System.out.println("Format error. " + e.getMessage());
            }
        }
        scanner.reset();
        return validInput;
    }

    private static Level checkValidLevel(String input) throws IncorrectInputException {
        try {
            return Level.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IncorrectInputException("Introduced level doesn't match with the options. ");
        }
    }

    public static Material readMaterial(String message) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        boolean isValidInput = false;
        Material validInput = null;
        while (!isValidInput) {
            try {
                validInput = checkValidMaterial(stringReadLineAndCheck(scanner));
                isValidInput = true;
            } catch (StringException | IncorrectInputException e) {
                System.out.println("Format error. " + e.getMessage());
            }
        }
        scanner.reset();
        return validInput;
    }

    private static Material checkValidMaterial(String input) throws IncorrectInputException {
        try {
            return Material.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IncorrectInputException("Introduced material doesn't match with the options. ");
        }
    }

    public static Theme readTheme(String message) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        boolean isValidInput = false;
        Theme validInput = null;
        while (!isValidInput) {
            try {
                validInput = checkValidTheme(stringReadLineAndCheck(scanner));
                isValidInput = true;
            } catch (StringException | IncorrectInputException e) {
                System.out.println("Format error. " + e.getMessage());
            }
        }
        scanner.reset();
        return validInput;
    }

    private static Theme checkValidTheme(String input) throws IncorrectInputException {
        try {
            return Theme.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IncorrectInputException("Introduced theme doesn't match with the options. ");
        }
    }

    private static String stringReadLineAndCheck(Scanner scanner) throws StringException {
        String line = scanner.nextLine();
        if (line.isEmpty()) throw new StringException("Input string is empty.");
        else return line;
    }

    public static Boolean readBoolean(String message) {
        Scanner scanner = new Scanner(System.in);
        String result;
        do{
            System.out.println(message);
            result = scanner.nextLine().toUpperCase();
        }while (!result.matches("[YN]") );
        return result.equals("Y");
    }

    public static LocalDateTime readLocalDateTime(String message) {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime dateTime;

        while (true) {
            System.out.print(message);
            String dateStr = scanner.nextLine();

            try {
                dateTime = LocalDateTime.parse(dateStr + "T00:00:00");
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please type date with format 'yyyy-MM-dd'.");
            }
        }
        return dateTime;
    }
}
