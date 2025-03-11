package com.egorkivilev.util.paystub;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Paystub {
    DecimalFormat numberFormat = new DecimalFormat("#.00");
    private float hours;
    private float payRate;
    private float grossPay;

    private float eiRate;
    private float cppRate;

    private float netPay;

    public Paystub(float hours) {
        this.hours = hours;
        payRate = Util.payRate;
        eiRate = Util.EI_RATE;
        cppRate = Util.CPP_RATE;

        grossPay = hours * payRate;
        netPay = grossPay - (grossPay * (eiRate / 100)) - (grossPay * (cppRate / 100));
    }

    public Paystub(float hours, float payRate, float grossPay, float eiRate, float cppRate, float netPay) {
        this.hours = hours;
        this.payRate = payRate;
        this.grossPay = grossPay;
        this.eiRate = eiRate;
        this.cppRate = cppRate;
        this.netPay = netPay;
    }

    public static Paystub create(ArrayList<String> args) {
        try {
            float hours = Float.parseFloat(args.get(0));
            float payRate = Float.parseFloat(args.get(1));
            float grossPay = Float.parseFloat(args.get(2));
            float eiRate = Float.parseFloat(args.get(3));
            float cppRate = Float.parseFloat(args.get(4));
            float netPay = Float.parseFloat(args.get(5));

            return new Paystub(hours, payRate, grossPay, eiRate, cppRate, netPay);
        }
        catch (Exception e) {
            System.out.println(Util.colourText("ERROR: " + e.getMessage() +"; Paystub is voided and empty.", "COLOUR_RED"));
        }

        return new Paystub(0);
    }

    public String toString() {
        System.out.println("===================================");
        System.out.println("* PAYCHECK");
        System.out.println("> Hours: " + hours);
        System.out.println();
        System.out.println("> Pay Rate: $" + numberFormat.format(payRate) + "/hr");
        System.out.println("> Gross Pay: $" + numberFormat.format(grossPay));
        System.out.println();
        System.out.println("> EI: $" + numberFormat.format(grossPay * (eiRate / 100)) + " (" + eiRate + "%)");
        System.out.println("> CPP: $" + numberFormat.format(grossPay * (cppRate / 100)) + " (" + cppRate + "%)");
        System.out.println("[CPP Rate varies based on factors and may not be accurate]");

        System.out.println();
        System.out.println("> Net Pay: $" + numberFormat.format(netPay));
        System.out.println("===================================");

        return null;
    }

    public String toCSVString() {
        return hours + "," + payRate + "," + grossPay + "," + eiRate + "," + cppRate + "," + netPay; // Modify based on your attributes
    }

    public String netPayToString() {
        return numberFormat.format(netPay);
    }
}
