package com.ciit.freelanceplus.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ciit.freelanceplus.Activities.BuyerOrderDetails;
import com.ciit.freelanceplus.Activities.SellerOrderDetails;
import com.ciit.freelanceplus.Model.OrderModel;
import com.ciit.freelanceplus.Model.Utils;
import com.ciit.freelanceplus.R;

import java.util.List;

public class Order_Adapter extends RecyclerView.Adapter<Order_Adapter.MyViewHolder>{

    Context mContext;
    private List<OrderModel> items;

    private Order_Adapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(OrderModel model);
    }


    public Order_Adapter(Context mContext,List<OrderModel> items, Order_Adapter.OnItemClickListener listener){
        this.items = items;
        this.mContext=mContext;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, deadline, budget, status, des, title;
        LinearLayout parent;


        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.order_model_buyerName);
            deadline = (TextView) view.findViewById(R.id.order_model_deadline);
            budget = (TextView) view.findViewById(R.id.order_model_price);
            status = (TextView) view.findViewById(R.id.order_model_status);
            des = (TextView) view.findViewById(R.id.order_model_Des);
            title = view.findViewById(R.id.order_model_title);
            parent = view.findViewById(R.id.order_model_parent);

        }

        public void bind(final OrderModel model, final Order_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    listener.onItemClick(model);
                }
            });
        }


    }

    @Override
    public Order_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_model, parent, false);

        return new Order_Adapter.MyViewHolder(itemView);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    @Override
    public void onBindViewHolder(final Order_Adapter.MyViewHolder holder, final int position) {


        final OrderModel model = items.get(position);

        if (model.seller_name == null)
        {

            holder.name.setText("Seller: Not Assign Yet");
        }else {

            holder.name.setText("Seller: "+model.seller_name);
        }

        holder.deadline.setText("Deadline: "+model.deadline);
        holder.des.setText("Description: "+model.description);
        holder.budget.setText("Budget: PKR "+model.budget);
        holder.status.setText(model.status.toUpperCase());
        holder.title.setText("Title: "+model.title);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.selectOrder = model;
                Log.d("ORD-JSN", model.toString());
                Intent i = new Intent(mContext, BuyerOrderDetails.class);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
