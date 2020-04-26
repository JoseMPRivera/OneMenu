package com.aje.onemenu.classes;

import java.util.ArrayList;

public class Ingredients {

    private ArrayList<String> meats;
    private ArrayList<String> veggie;
    private ArrayList<String> misc;

    private void addMeat(){
        meats.add("pork");
        meats.add("chicken");
        meats.add("beef");
        meats.add("fish");
        meats.add("crab");
        meats.add("tofu");
        meats.add("shrimp");
        meats.add("duck");
        meats.add("lamb");
        meats.add("goat");
        meats.add("lobster");
        meats.add("bison");
        meats.add("frog");
        meats.add("turkey");
        meats.add("eggs");
        meats.add("deer");

    }
    private void addVeggie(){
        veggie.add("lettuce");
        veggie.add("onion");
        veggie.add("potato");
        veggie.add("tomato");

    }
    private void addMisc(){
        misc.add("ketchup");
        misc.add("mustard");
        misc.add("bun");
        misc.add("mayonnaise");

    }

    private static Ingredients instance = null;

    protected Ingredients() {
        // Exists only to defeat instantiation.

        meats = new ArrayList<>();
        misc = new ArrayList<>();
        veggie = new ArrayList<>();

        addMeat();
        addMisc();
        addVeggie();
    }
    public static Ingredients getInstance() {

        if(instance == null) {
            instance = new Ingredients();
        }
        return instance;
    }

    public ArrayList<String> getMeats() {
        return meats;
    }

    public ArrayList<String> getVeggie() {
        return veggie;
    }

    public ArrayList<String> getMisc() {
        return misc;
    }
}
