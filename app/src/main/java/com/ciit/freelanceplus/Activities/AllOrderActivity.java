package com.ciit.freelanceplus.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ciit.freelanceplus.Adapters.Order_Adapter_Listview;
import com.ciit.freelanceplus.Model.OrderModel;
import com.ciit.freelanceplus.Model.Utils;
import com.ciit.freelanceplus.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class AllOrderActivity extends AppCompatActivity {

    private ArrayList<OrderModel> orderModels, filterModels;
    ListView listView;
    ArrayList<String> status;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order);

        ImageView back = findViewById(R.id.all_order_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        tabLayout = findViewById(R.id.tabLayout);


        listView = findViewById(R.id.orders_listview);

        getSellerOrders();


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

                            Order_Adapter_Listview adapter = new Order_Adapter_Listview(AllOrderActivity.this, filterModels);
                            listView.setAdapter(adapter);
                            listView.setDivider(getResources().getDrawable(R.drawable.transperent_color));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    Utils.selectOrder = filterModels.get(i);
                                    Intent in = new Intent(AllOrderActivity.this, SellerOrderDetails.class);
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

                    Order_Adapter_Listview adapter = new Order_Adapter_Listview(AllOrderActivity.this, orderModels);
                    listView.setAdapter(adapter);
                    listView.setDivider(getResources().getDrawable(R.drawable.transperent_color));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Utils.selectOrder = orderModels.get(i);
                            Intent in = new Intent(AllOrderActivity.this, SellerOrderDetails.class);
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

        RequestQueue queue = Volley.newRequestQueue(AllOrderActivity.this);
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
