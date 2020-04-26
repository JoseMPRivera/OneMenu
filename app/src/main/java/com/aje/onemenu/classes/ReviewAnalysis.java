package com.aje.onemenu.classes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReviewAnalysis {

    private String idRestaurant;
    private String idMeal;
    private Float rating;
    private HashMap<String, Float> meats;
    private HashMap<String, Float> vegetables;
    private HashMap<String, Float> misc;
    private Date date;

    public ReviewAnalysis(String idRestaurant, String idMeal, Float rate, Date date){

        this.rating = rate;
        this.idRestaurant = idRestaurant;
        this.idMeal = idMeal;
        this.date = date;
        meats = new HashMap<>();
        vegetables = new HashMap<>();
        misc = new HashMap<>();

        updateIngredients();
    }

    private void updateIngredients(){

        for(String m: Ingredients.getInstance().getMeats()){
            meats.put(m, (float)0);
        }
        for(String v: Ingredients.getInstance().getVeggie()){
            vegetables.put(v, (float)0);
        }
        for(String m: Ingredients.getInstance().getMisc()){
            misc.put(m, (float)0);
        }
    }

    public void addIngredientMeat( String id, Float rate){

        meats.put("id", rate);
    }

    public void addIngredientVeggie( String id, Float rate){

        vegetables.put("id", rate);
    }

    public void addIngredientMisc( String id, Float rate){

        misc.put("id", rate);
    }

    public String getIdRestaurant() {
        return idRestaurant;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public Date getDate() {
        return date;
    }

    public Float getRating() {
        return rating;
    }
}
