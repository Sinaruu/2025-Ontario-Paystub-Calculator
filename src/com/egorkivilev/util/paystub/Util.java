package com.egorkivilev.util.paystub;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Util {
    public static final String COLOUR_RESET = "\u001B[0m";
    public static final String COLOUR_RED = "\u001B[31m";
    public static final String COLOUR_GREEN = "\u001B[32m";
    public static final String COLOUR_YELLOW = "\u001B[33m";
    public static final String COLOUR_BLUE = "\u001B[34m";
    public static final String COLOUR_MAGENTA = "\u001B[35m";
    public static final String COLOUR_CYAN = "\u001B[36m";
    public static final String COLOUR_WHITE = "\u001B[37m";

    public static float getTaxRate(float grossPay) {
        if(grossPay >= 235676) return 53.53f;
        else if(grossPay >= 220001) return 49.85f;
        else if(grossPay >= 165431) return 48.29f;
        else if(grossPay >= 150001) return 44.97f;
        else if(grossPay >= 106718) return 43.41f;
        else if(grossPay >= 102140) return 37.91f;
        else if(grossPay >= 98464) return 33.89f;
        else if(grossPay >= 86697) return 31.48f;
        else if(grossPay >= 53360) return 29.65f;
        else if(grossPay >= 49232) return 24.15f;
        else if(grossPay >= 15001) return 20.05f;
        else {return 0.0f;}
    }

    public static int parseInt(String input) throws PayException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new PayException("Invalid entry: Enter an integer between 1 and 6");
        }
    }

    public static float parseFloat(String input) throws PayException {
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException e) {
            throw new PayException("Invalid entry: Enter an integer between 1 and 6");
        }
    }

    public static String colourText(String line, String colour) {
        switch(colour) {
            case "COLOUR_RED" -> {return COLOUR_RED + line + COLOUR_RESET;}
            case "COLOUR_BLUE" -> {return COLOUR_BLUE + line + COLOUR_RESET;}
            case "COLOUR_CYAN" -> {return COLOUR_CYAN + line + COLOUR_RESET;}
            case "COLOUR_GREEN" -> {return COLOUR_GREEN + line + COLOUR_RESET;}
            case "COLOUR_MAGENTA" -> {return COLOUR_MAGENTA + line + COLOUR_RESET;}
            case "COLOUR_WHITE" -> {return COLOUR_WHITE + line + COLOUR_RESET;}
            case "COLOUR_YELLOW" -> {return COLOUR_YELLOW + line + COLOUR_RESET;}
            default -> {return line;}
        }
    }

    public static void changePayRate(float payRate) {
        Util.payRate = payRate;
    }

    static ArrayList<String> parseLine(String line) {
        ArrayList<String> fields = new ArrayList<String>();
        int index = 0;
        String field = "";
        boolean inQuotes = false;
        char ch;
        for (int i = 0; i < line.length(); i++) {
            ch = line.charAt(i);
            if (ch == '"') {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                fields.add(index++, field.strip());
                field = "";
            } else {
                field = field + ch;
            }
        }
        fields.add(index++, field.strip());
        return fields;
    }

    static int getNCols(String line) {
        //System.out.println(line);
        String[] split = line.split(",");
        return split.length;
    }

    static UserStubs loadPaystubs() throws PayException {
        ArrayList<Paystub> paystubs = new ArrayList<Paystub>();
        ArrayList<String> fields = new ArrayList<String>();
        String line;
        int nlines = 0;
        try {
            File file = new File("UserStubs.csv");
            Scanner input = new Scanner(file);

            while (input.hasNextLine()) {
                line = input.nextLine();
                //System.out.println(line);
                fields = Util.parseLine(line);
                //if (nlines==0) Util.getNCols(line);
                if (nlines!=0) {
                    paystubs.add(Paystub.create(fields));
                }
                nlines++;
            }
            input.close();

            if(paystubs.isEmpty()) {
                return new UserStubs();
            }

            return new UserStubs(paystubs);
        } catch (FileNotFoundException e) {
            return new UserStubs();
        } catch (InputMismatchException e) {
            throw new PayException("Incorrect input.");
        }
    }

    static void loadSettings() {
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<String> fields = new ArrayList<String>();

        String line;
        int nlines = 0;
        try {
            File file = new File("UserSettings.csv");
            if(file.exists()) {
                Scanner input = new Scanner(file);

                while (input.hasNextLine()) {
                    line = input.nextLine();
                    //System.out.println(line);
                    fields = Util.parseLine(line);
                    //if (nlines==0) Util.getNCols(line);
                    if (nlines!=0) {
                        values = fields;
                    }
                    nlines++;
                }
                input.close();

                if(!values.isEmpty()) {
                    Util.changePayRate(Float.parseFloat(values.getFirst()));
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void writePaystubsToCSV(List<Paystub> paystubs) throws PayException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("UserStubs.csv"))) {
            writer.write("hours,payRate,grossPay,eiRate,cppRate,netPay"); // Header (if needed)
            writer.newLine();

            for (Paystub p : paystubs) {
                writer.write(p.toCSVString()); // Convert each Paystub to CSV format
                writer.newLine();
            }
        } catch (IOException e) {
            throw new PayException("Error writing to file: " + e.getMessage());
        }
    }

    public static void writeSettingsToCSV() throws PayException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("UserSettings.csv"))) {
            writer.write("payRate"); // Header (if needed)
            writer.newLine();

            writer.write(String.valueOf(Util.payRate));
        } catch (IOException e) {
            throw new PayException("Error writing to file: " + e.getMessage());
        }
    }

    public static final float EI_RATE = 1.64f;
    public static final float CPP_RATE = 5.95f;
    public static float payRate = 0.0f;
}
