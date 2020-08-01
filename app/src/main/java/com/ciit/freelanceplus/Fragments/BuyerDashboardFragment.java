package com.ciit.freelanceplus.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciit.freelanceplus.Activities.AllOrderActivity;
import com.ciit.freelanceplus.Activities.LoginActivity;
import com.ciit.freelanceplus.Activities.QuestionsActivity;
import com.ciit.freelanceplus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerDashboardFragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs" ;

    public BuyerDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_buyer_dashboard, container, false);

        LinearLayout back = root.findViewById(R.id.buyer_dashboard_logout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();

            }
        });

        TextView orderNow = root.findViewById(R.id.buyer_frag_ordernow);
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), QuestionsActivity.class);
                startActivity(i);
            }
        });

        return root;
    }
}
