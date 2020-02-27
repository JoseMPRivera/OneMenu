package com.aje.onemenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodPreference extends AppCompatActivity {

    private ArrayList<String> meats = new ArrayList<>();
    private ArrayList<String> veggie = new ArrayList<>();
    private ArrayList<String> misc = new ArrayList<>();
    private String meat;
    private ArrayList<String> my_meats = new ArrayList<>();
    //create checkboxes from list of meats
    //on update, add checked boxes to my_meats array
    private boolean meatEdit = true;
    private boolean meatPopulated = false;
    private boolean veggieEdit = true;
    private boolean veggiePopulated = false;
    private boolean miscEdit = true;
    private boolean miscPopulated = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addMeat();
        addVeggie();
        addMisc();
        LinearLayout preferenceScreen = new LinearLayout(this);
        preferenceScreen.setOrientation(LinearLayout.VERTICAL);
        LinearLayout showMeat = showPreference("meat", meats, meatEdit, meatPopulated);
        LinearLayout showVeggie = showPreference("vegetables", veggie, veggieEdit, veggiePopulated);
        LinearLayout showMisc = showPreference("misc", misc, miscEdit, miscPopulated);
        preferenceScreen.addView(showMeat);
        preferenceScreen.addView(showVeggie);
        preferenceScreen.addView((showMisc));


        this.setContentView(preferenceScreen);

    }
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
    private void ingredientsView(){

    }
    private LinearLayout showPreference(String name, final ArrayList<String> ingredients, boolean e, boolean p){
        final int[] edit = {1};
        final int[] populated = {0};
        final LinearLayout rl = new LinearLayout(this);
        rl.setOrientation(LinearLayout.VERTICAL);

        TextView tv1 = new TextView(this);
        tv1.setText(name);
        rl.addView(tv1);

        final Button b = new Button(this);
        b.setText(R.string.edit);
        b.setLayoutParams(new LinearLayout.LayoutParams(300, 150));
        rl.addView(b);

        final LinearLayout scrollLayout = new LinearLayout(this);
        //scrollLayout.setLayoutParams(new LinearLayout.LayoutParams(1300, 500));
        scrollLayout.setVisibility(View.GONE);

        rl.addView(scrollLayout);


        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(1300,500));
        sv.setScrollbarFadingEnabled(false);
        scrollLayout.addView(sv);

        final LinearLayout meatScroller = new LinearLayout(this);
        meatScroller.setOrientation(LinearLayout.HORIZONTAL);
        sv.addView(meatScroller);

        final LinearLayout col1 = new LinearLayout(this);
        col1.setOrientation(LinearLayout.VERTICAL);
        meatScroller.addView(col1);

        final LinearLayout col2 = new LinearLayout(this);
        col2.setOrientation(LinearLayout.VERTICAL);
        meatScroller.addView(col2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit[0] == 1) {
                    b.setText(R.string.submit);
                    scrollLayout.setVisibility(View.VISIBLE);
                    if(populated[0] == 0){
                        int altCol = 0;
                        for (int i = 0; i < ingredients.size(); i++) {
                            CheckBox cb = new CheckBox(getApplicationContext());
                            cb.setText(ingredients.get(i));
                            if(altCol == 0){
                                col1.addView(cb);
                                altCol = 1;
                            }
                            else {
                                col2.addView(cb);
                                altCol = 0;
                            }
                            populated[0] = 1;
                        }
                    }
                    edit[0] = 0;
                }
                else{
                    b.setText(R.string.edit);
                    scrollLayout.setVisibility(View.GONE);
                    edit[0] = 1;
                }

            }
        });
    return rl;

    }
}