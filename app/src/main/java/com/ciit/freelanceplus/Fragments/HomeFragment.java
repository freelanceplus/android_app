package com.ciit.freelanceplus.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ciit.freelanceplus.Activities.SellerOrderDetails;
import com.ciit.freelanceplus.Adapters.Order_Adapter_Listview;
import com.ciit.freelanceplus.Model.OrderModel;
import com.ciit.freelanceplus.Model.UserModel;
import com.ciit.freelanceplus.Model.Utils;
import com.ciit.freelanceplus.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    UserModel currentUser = Utils.currentUser;
    TabLayout tabLayout;
    ListView listView;
    private ArrayList<OrderModel> orderModels, filterModels;
    ArrayList<String> status;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root;
        root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView name, email, verified;
        name = root.findViewById(R.id.home_name);
        email = root.findViewById(R.id.home_email);
        verified = root.findViewById(R.id.home_verified);

        name.setText(currentUser.name);
        email.setText(currentUser.email);
        if(Utils.currentUser.verified == 1)
        {
            verified.setText("Verified Seller");
        }else {
            verified.setText("Not verified Seller");
        }
        tabLayout = root.findViewById(R.id.home_tabLayout);
        listView = root.findViewById(R.id.home_orders_listview);

        getSellerOrders();

        return root;
    }

    private void getSellerOrders()
    {
        String url = getResources().getString(R.string.API_URL)+"/freelancePlus_API/public/getAllSellerOrders/"+Utils.currentUser.id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                orderModels = new ArrayList<>();
                status = new ArrayList<>();

                Log.d("seller-orders",response);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i=0; i<array.length() ;i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        OrderModel model = getOrderWithSeller(object);
                        orderModels.add(model);
                        status.add(model.status);
                    }

                    HashSet<String> hashSet = new HashSet<String>();
                    hashSet.addAll(status);
                    status.clear();
                    status.addAll(hashSet);

                    for(String x: status)
                    {
                        tabLayout.addTab(tabLayout.newTab().setText(x));
                    }


                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {

                            String txt = tab.getText().toString();
                            filterModels = new ArrayList<>();
                            for(OrderModel x: orderModels)
                            {
                                if(x.status.toLowerCase().equals(txt.toLowerCase()))
                                {
                                    filterModels.add(x);
                                }
                            }

                            Order_Adapter_Listview adapter = new Order_Adapter_Listview(getActivity(), filterModels);
                            listView.setAdapter(adapter);
                            listView.setDivider(getResources().getDrawable(R.drawable.transperent_color));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    Utils.selectOrder = filterModels.get(i);
                                    Intent in = new Intent(getActivity(), SellerOrderDetails.class);
                                    startActivity(in);

                                }
                            });
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });

                    Order_Adapter_Listview adapter = new Order_Adapter_Listview(getActivity(), orderModels);
                    listView.setAdapter(adapter);
                    listView.setDivider(getResources().getDrawable(R.drawable.transperent_color));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Utils.selectOrder = orderModels.get(i);
                            Intent in = new Intent(getActivity(), SellerOrderDetails.class);
                            startActivity(in);
                        }
                    });


                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("seller-orders",error.getMessage());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private OrderModel getOrderWithSeller(JSONObject object)
    {

        OrderModel model = new OrderModel();
        try {

            model.id = object.getInt("id");
            model.title = object.getString("title");
            model.projectType = object.getString("project_type");
            model.description = object.getString("description");
            model.image = object.getString("image");
            model.deadline = object.getString("deadline");
            model.budget = object.getString("budget");
            model.files = object.getString("files");
            model.seller_files = object.getString("seller_files");
            model.remark = object.getString("remarks");
            model.buyer_id = object.getString("buyer_id");
            model.seller_id = object.getString("seller_id");
            model.status = object.getString("status");
            model.seller_name = object.getString("seller_name");
            model.seller_email = object.getString("seller_email");
            model.buyer_name = object.getString("buyer_email");
            model.buyer_emmail = object.getString("buyer_email");

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return model;
    }

}
