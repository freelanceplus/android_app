package com.ciit.freelanceplus.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ciit.freelanceplus.Model.ChatModel;
import com.ciit.freelanceplus.R;

import java.util.List;

public class SellerChatAdapter extends RecyclerView.Adapter<SellerChatAdapter.MyViewHolder>{

    Context mContext;
    private List<ChatModel> items;

    public SellerChatAdapter(Context mContext,List<ChatModel> items){
        this.items = items;
        this.mContext=mContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent, container;
        TextView name, msg;


        public MyViewHolder(View view) {
            super(view);

            parent = view.findViewById(R.id.parent);
            container = view.findViewById(R.id.msg_container);
            name = (TextView) view.findViewById(R.id.bubble_sender_name);
            msg = view.findViewById(R.id.bubble_message);

        }

    }

    @Override
    public SellerChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_model, parent, false);

        return new SellerChatAdapter.MyViewHolder(itemView);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    @Override
    public void onBindViewHolder(final SellerChatAdapter.MyViewHolder holder, final int position) {


        ChatModel message =items.get(position);

        if (message.sender.toLowerCase().equals("seller")) {
            holder.name.setText("Me");
            holder.container.setBackgroundResource(R.drawable.outgoing_bubble_item);
            holder.parent.setGravity(Gravity.RIGHT);
        }

        else {
            holder.name.setText(message.buyer_name);
            holder.container.setBackgroundResource(R.drawable.incoming_bubble);
            holder.parent.setGravity(Gravity.LEFT);
        }


        holder.msg.setText(message.message);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}