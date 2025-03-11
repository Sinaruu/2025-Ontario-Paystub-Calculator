package com.egorkivilev.util.paystub;

import java.util.ArrayList;

public class UserStubs {
    private ArrayList<Paystub> paystubs;

    public UserStubs() {
        paystubs = new ArrayList<>();
    }

    public UserStubs(ArrayList<Paystub> paystubs) {
        this.paystubs = paystubs;
    }

    public void addPaystub(float hours) {
        paystubs.add(new Paystub(hours));
    }

    public void printPaystub() {
        System.out.println(" === PAYSTUBS ===");
        if(paystubs.isEmpty()) System.out.println(Util.colourText("No paystubs are saved", "COLOUR_WHITE"));
        else {
            for(int i = 0; i < paystubs.size(); i++) {
                System.out.println(Util.colourText("> Paystub #" + i + " [$" + paystubs.get(i).netPayToString() + "]", "COLOUR_CYAN"));
            }
        }
        System.out.println(" === PAYSTUBS ===");
    }

    public void printPaystub(int index) throws PayException {
        try {
            paystubs.get(index).toString();
        }
        catch(IndexOutOfBoundsException e) {
            throw new PayException("Index out of bounds");
        }
    }

    public void deletePaystub(int index) throws PayException {
        try {
            paystubs.remove(index);
        }
        catch(IndexOutOfBoundsException e) {
            throw new PayException("Index out of bounds");
        }
    }

    public ArrayList<Paystub> getPaystubs() {
        return paystubs;
    }
}
