package com.egorkivilev.util.paystub;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    static Scanner sc;
    static UserStubs user;
    static DecimalFormat numberFormat = new DecimalFormat("#.00");

    static void showMenu() {
        System.out.println("===========================");
        System.out.println("| PAYSTUB CALCULATOR v1.0 |");
        System.out.println("===========================");
        System.out.println("1. Add paystub");
        System.out.println("2. Review paystub");
        System.out.println("3. Delete paystub");
        System.out.println("4. Show paystubs");
        System.out.println("5. Personal Settings");
        System.out.println("6. Save & Exit");
    }

    static void addPaystub() throws PayException {
        String input;
        float hours;
        System.out.print("Enter amount of hours worked (in decimal): ");
        input = sc.nextLine();
        hours = Util.parseFloat(input);
        if(hours < 3) throw new PayException("The amount of hours must be equal or greater than 3");
        user.addPaystub(hours);
    }

    static void reviewPaystub() throws PayException {
        String choice;
        int index;
        System.out.print("Enter the paystub index: ");
        choice = sc.nextLine();
        index = Util.parseInt(choice);
        user.printPaystub(index);
        System.out.print(Util.colourText("Enter any value to continue...", "COLOUR_YELLOW"));
        sc.nextLine();
    }

    static void deletePaystub() throws PayException {
        String choice;
        int index;
        System.out.print("Enter the paystub index: ");
        choice = sc.nextLine();
        index = Util.parseInt(choice);
        user.deletePaystub(index);
        System.out.println(Util.colourText("Paystub with index " + index + " deleted successfully.", "COLOUR_GREEN"));
        System.out.print(Util.colourText("Enter any value to continue...", "COLOUR_YELLOW"));
        sc.nextLine();
    }

    static void showPaystubs() {
        user.printPaystub();
        System.out.print(Util.colourText("Enter any value to continue...", "COLOUR_YELLOW"));
        sc.nextLine();
    }

    static void personalSettings() throws PayException {
        System.out.println(Util.colourText("\n> Personal Settings:", "COLOUR_YELLOW"));
        System.out.println(Util.colourText("1. Change PayRate ($" + numberFormat.format(Util.payRate) + "/hr)", "COLOUR_YELLOW"));
        System.out.println(Util.colourText("2. Exit", "COLOUR_YELLOW"));
        String choice;
        int index;
        System.out.print("Enter your choice: ");
        choice = sc.nextLine();
        index = Util.parseInt(choice);
        switch(index) {
            case 1 -> {
                String newRate;
                System.out.print("Enter new pay rate: ");
                newRate = sc.nextLine();
                float rate = Util.parseFloat(newRate);
                Util.changePayRate(rate);
                System.out.println(Util.colourText("Pay rate changed to $" + numberFormat.format(rate) + "/hr", "COLOUR_GREEN"));
                System.out.print(Util.colourText("Enter any value to continue...", "COLOUR_YELLOW"));
                sc.nextLine();
            }
            case 2 -> {
                System.out.println("Left Settings.");
            }
            default -> throw new PayException("Incorrect choice.");
        }
    }

    static void exit() throws PayException {
        System.out.print(Util.colourText("Saving Paystubs... ", "COLOUR_YELLOW"));
        Util.writePaystubsToCSV(user.getPaystubs());
        System.out.println(Util.colourText("Done.", "COLOUR_GREEN"));

        System.out.print(Util.colourText("Saving Settings... ", "COLOUR_YELLOW"));
        Util.writeSettingsToCSV();
        System.out.println(Util.colourText("Done.", "COLOUR_GREEN"));

        System.out.println(Util.colourText("Successfully saved!", "COLOUR_GREEN"));
        System.out.println("Goodbye!");
        System.exit(0);
    }

    public static void main(String[] args) throws PayException {
        sc = new Scanner(System.in);
        System.out.print(Util.colourText("Loading paystubs... ", "COLOUR_YELLOW"));
        user = Util.loadPaystubs();
        System.out.println(Util.colourText("Done.", "COLOUR_GREEN"));

        System.out.print(Util.colourText("Loading settings... ", "COLOUR_YELLOW"));
        Util.loadSettings();
        System.out.println(Util.colourText("Done.", "COLOUR_GREEN"));

        System.out.println(Util.colourText("Successfully loaded!", "COLOUR_GREEN"));

        if(Util.payRate == 0.0) {
            System.out.println(Util.colourText("The current Pay Rate is set to $0/hr. Make sure to put the correct value in Personal Settings.", "COLOUR_YELLOW"));
        }

        while(true) {
            showMenu();
            try {
                String input;
                System.out.print("Enter your choice: ");
                input = sc.nextLine();

                int choice = Util.parseInt(input);

                switch(choice) {
                    case 1 -> addPaystub();
                    case 2 -> reviewPaystub();
                    case 3 -> deletePaystub();
                    case 4 -> showPaystubs();
                    case 5 -> personalSettings();
                    case 6 -> exit();
                }
            }
            catch(PayException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
