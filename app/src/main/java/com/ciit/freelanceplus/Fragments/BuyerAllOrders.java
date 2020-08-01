package com.ciit.freelanceplus.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ciit.freelanceplus.Activities.BuyerOrderDetails;
import com.ciit.freelanceplus.Activities.SellerOrderDetails;
import com.ciit.freelanceplus.Adapters.Order_Adapter;
import com.ciit.freelanceplus.Model.OrderModel;
import com.ciit.freelanceplus.Model.Utils;
import com.ciit.freelanceplus.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerAllOrders extends Fragment {

    private ArrayList<OrderModel> orderModels;
    TextView textView;

    public BuyerAllOrders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_buyer_all_orders, container, false);
        final RecyclerView recyclerView = root.findViewById(R.id.buyer_all_orders_list);
        textView = root.findViewById(R.id.buyer_all_orders_txt);
        textView.setVisibility(View.GONE);

        Context context;
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading data...");
        dialog.setCancelable(false);
        dialog.show();

        String URL = getResources().getString(R.string.API_URL)+"/freelancePlus_API/public/getAllBuyerOrders/"+ Utils.currentUser.id;
        StringRequest request =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    orderModels = new ArrayList<>();

                    JSONArray array = new JSONArray(response);
                    if(array.length() > 0 )
                    {
                        textView.setVisibility(View.GONE);

                        for(int i=0 ; i<array.length(); i++)
                        {
                            JSONObject object = array.getJSONObject(i);
                            String status = object.getString("status");

                            if(status.toLowerCase().equals("requested"))
                            {
                                OrderModel model = getOrderWithOutSeller(object);

                                if(Integer.parseInt(model.buyer_id) == Utils.currentUser.id)
                                {
                                    orderModels.add(model);
                                }
                            }else {

                                OrderModel model = getOrderWithSeller(object);

                                if(Integer.parseInt(model.buyer_id) == Utils.currentUser.id)
                                {
                                    orderModels.add(model);
                                }
                            }

                        }

                        dialog.dismiss();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(new Order_Adapter(getActivity(), orderModels, new Order_Adapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(OrderModel model) {

                                Utils.selectOrder = model;
                                Toast.makeText(getActivity(), Utils.selectOrder.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("ORD-MDL", model.toString());
                                Intent intent = new Intent(getActivity(), BuyerOrderDetails.class);
                                startActivity(intent);

                            }
                        }));


                    }else {

                        dialog.dismiss();
                        textView.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);



        return root;
    }

    private OrderModel getOrderWithOutSeller(JSONObject object)
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
            model.buyer_id = object.getString("buyer_id");
            model.status = object.getString("status");
            model.buyer_name = object.getString("buyer_email");
            model.buyer_emmail = object.getString("buyer_email");

        }catch (Exception e)
        {
            e.printStackTrace();
        }

      return model;

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
