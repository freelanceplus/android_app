package com.ciit.freelanceplus.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciit.freelanceplus.Model.OrderModel;
import com.ciit.freelanceplus.R;

import java.util.ArrayList;
import java.util.List;

public class Order_Adapter_Listview extends BaseAdapter {

    Context mContext;
    private List<OrderModel> items;
    LayoutInflater layoutInflater;

    public Order_Adapter_Listview(Context context, ArrayList<OrderModel> items)
    {
         mContext = context;
        this.items = items;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View root;
        if(view == null)
        {
            root = layoutInflater.inflate(R.layout.order_model, null);
        }else {
            root = view;
        }

        TextView name = root.findViewById(R.id.order_model_buyerName);
        TextView deadline = root.findViewById(R.id.order_model_deadline);
        TextView title = root.findViewById(R.id.order_model_title);
        TextView decs = root.findViewById(R.id.order_model_Des);
        TextView status = root.findViewById(R.id.order_model_status);
        TextView budget = root.findViewById(R.id.order_model_price);
        OrderModel model = items.get(i);

        name.setText(model.buyer_name);
        deadline.setText(model.deadline);
        title.setText("Title: "+model.title);
        decs.setText("Description: "+model.description);
        status.setText(model.status);
        budget.setText("Budget: PKR "+model.budget);

        return root;
    }
}
