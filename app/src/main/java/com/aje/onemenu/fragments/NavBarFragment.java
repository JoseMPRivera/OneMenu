package com.aje.onemenu.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aje.onemenu.Profile;
import com.aje.onemenu.R;
import com.aje.onemenu.activities.FoodPreference;
import com.aje.onemenu.activities.ProfileActivity;
import com.aje.onemenu.activities.RestaurantsList;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavBarFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NavBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment NavBarFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static NavBarFragment newInstance(String param1) {
//        NavBarFragment fragment = new NavBarFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_bar_fragment, container, false);
//        ImageButton btn = (ImageButton) view.findViewById(R.id.menu_profile);
//        btn.setPressed(false);
//        btn.setOnClickListener(this);
//        Button btn2 = (Button) view.findViewById(R.id.menu_search);
//        btn2.setOnClickListener(this);
//        Button btn3 = (Button) view.findViewById(R.id.menu_settings);
//        btn3.setOnClickListener(this);
//        TabItem ti1 = (TabItem) view.findViewById(R.id.menu_profile);
//        if(this.hashCode() != ProfileActivity.class.hashCode()){
//            //Toast.makeText(this.getContext(), "profile is true", Toast.LENGTH_SHORT).show();
//            ti1.setSelected(true);
//        }
//        TabItem ti2 = (TabItem) view.findViewById(R.id.menu_search);
//
//        if(this.hashCode() == RestaurantsList.class.hashCode()){
//           // Toast.makeText(this.getContext(), "search is true", Toast.LENGTH_SHORT).show();
//
//            ti1.setSelected(true);
//        }
//        TabItem ti3 = (TabItem) view.findViewById(R.id.menu_settings);
//        if(this.hashCode() == FoodPreference.class.hashCode()){
//           // Toast.makeText(this.getContext(), "settings is true", Toast.LENGTH_SHORT).show();
//            ti1.setSelected(true);
//        }
        TabLayout tl =view.findViewById(R.id.navBar);
        tl.addOnTabSelectedListener(this);
//        TabItem ti1 = (TabItem) view.findViewById(R.id.menu_search);

//        ti1.setOnTouchListener((View.OnTouchListener) this);



        return view;
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.menu_profile:
//                Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.menu_settings:
//                Intent intent2 = new Intent(getActivity(), FoodPreference.class);
//                startActivity(intent2);
//                break;
//            case R.id.menu_search:
//                Intent intent3 = new Intent(getActivity(), RestaurantsList.class);
//                startActivity(intent3);
//                break;
//
//        }
//
//    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(getActivity(), FoodPreference.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivityIfNeeded(intent2, 0);
//                startActivity(intent2,0);
                startActivity(intent2);
                break;
            case 2:
                Intent intent3 = new Intent(getActivity(), RestaurantsList.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent3);

                break;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}

