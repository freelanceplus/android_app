package com.ciit.freelanceplus.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ciit.freelanceplus.Adapters.ChatAdapter;
import com.ciit.freelanceplus.Adapters.SellerChatAdapter;
import com.ciit.freelanceplus.Model.ChatModel;
import com.ciit.freelanceplus.Model.FilePath;
import com.ciit.freelanceplus.Model.OrderModel;
import com.ciit.freelanceplus.Model.Utils;
import com.ciit.freelanceplus.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuyerOrderDetails extends AppCompatActivity {

    ImageView img, back;
    TextView orderid, sellername, budget, title, dec, projecttype, status, remarks, deadline, sellerFile, buyerFile, statusbtn;
    LinearLayout sellerAttachments, buyerAttachments;
    RecyclerView recyclerView;
    EditText editText;
    ImageButton send;
    FirebaseDatabase firebaseDatabase;
    OrderModel order;
    ArrayList<ChatModel> chatModels;
    DatabaseReference databaseReference;
    LinearLayout chatLayout, SellerLayout;
    ArrayList<String> filesArray = new ArrayList<>();
    ArrayList<String> filesNameArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_details);

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        if(order.image.contains(","))
        {

            String[] images = order.image.split(",");
            Glide.with(this).load(images[0]).into(img);

        }if (order.image == null ){

            Glide.with(BuyerOrderDetails.this).load("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSZxWOfkyLXn4j_EzhJRuOccwQcbHemOwftOxXOtoIPaP1b_U9b&usqp=CAU").into(img);

        }else {
            Glide.with(this).load(order.image).into(img);
        }

        if(order.status.toLowerCase().equals("completed"))
        {
            chatLayout.setVisibility(View.VISIBLE);
            SellerLayout.setVisibility(View.VISIBLE);

            orderid.setText("Order # "+order.id);
            sellername.setText("Seller: "+order.seller_name);
            budget.setText("Budget: "+order.budget);
            title.setText("Project Title: "+order.title);
            dec.setText("Description: "+order.description);
            deadline.setText("Deadline: "+order.deadline);
            projecttype.setText("Project Type: "+order.projectType);
            status.setText("Status: "+order.status);
            remarks.setText("Remarks: "+order.remark);

            loadSellerFiles();
            loadBuyerChat();

        }else if(order.status.toLowerCase().equals("assigntoseller"))
        {
            chatLayout.setVisibility(View.GONE);
            statusbtn.setVisibility(View.GONE);
            SellerLayout.setVisibility(View.VISIBLE);

            orderid.setText("Order # "+order.id);
            sellername.setText("Seller: "+order.seller_name);
            budget.setText("Budget: "+order.budget);
            title.setText("Project Title: "+order.title);
            dec.setText("Description: "+order.description);
            deadline.setText("Deadline: "+order.deadline);
            projecttype.setText("Project Type: "+order.projectType);
            status.setText("Status: "+order.status);
            remarks.setText("Remarks: "+order.remark);
            sellerAttachments.setVisibility(View.GONE);



        } else if(order.status.toLowerCase().equals("requested"))
        {
            SellerLayout.setVisibility(View.GONE);
            chatLayout.setVisibility(View.GONE);


            orderid.setText("Order # "+order.id);
            budget.setText("Budget: "+order.budget);
            title.setText("Project Title: "+order.title);
            dec.setText("Description: "+order.description);
            deadline.setText("Deadline: "+order.deadline);
            projecttype.setText("Project Type: "+order.projectType);
            status.setText("Status: "+order.status);
            remarks.setText("Remarks: "+order.remark);

        }else{

            chatLayout.setVisibility(View.VISIBLE);
            SellerLayout.setVisibility(View.VISIBLE);


            orderid.setText("Order # "+order.id);
            sellername.setText("Seller: "+order.seller_name);
            budget.setText("Budget: "+order.budget);
            title.setText("Project Title: "+order.title);
            dec.setText("Description: "+order.description);
            deadline.setText("Deadline: "+order.deadline);
            projecttype.setText("Project Type: "+order.projectType);
            status.setText("Status: "+order.status);
            remarks.setText("Remarks: "+order.remark);

            loadSellerFiles();
            loadBuyerChat();
        }

        loadBuyerFiles();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = editText.getText().toString();
                if(msg.isEmpty())
                {

                }else {

                    sendMsg(msg);
                }
            }
        });


    }

    private void loadBuyerChat(){

        Toast.makeText(this, Utils.currentUser.user_role, Toast.LENGTH_SHORT).show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                chatModels = new ArrayList<>();

                try {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatModel model = snapshot.getValue(ChatModel.class);

                        String myId = String.valueOf(Utils.currentUser.id);

                        if (model.buyer_id.equals(myId) && model.seller_id.equals(order.seller_id))  {

                            chatModels.add(model);

                        }

                    }

                    Toast.makeText(BuyerOrderDetails.this, chatModels.size()+" ", Toast.LENGTH_SHORT).show();

                    LinearLayoutManager layoutManager = new LinearLayoutManager(BuyerOrderDetails.this,
                            LinearLayoutManager.VERTICAL,true);
                    recyclerView.setLayoutManager(layoutManager);
                    ChatAdapter adapter = new ChatAdapter(BuyerOrderDetails.this, chatModels);
                    recyclerView.setAdapter(adapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


                Toast.makeText(BuyerOrderDetails.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadBuyerFiles()
    {
        String[] buyerfiles = order.files.split(",");

        if(buyerfiles.length>0)

            for(int i=0 ; i<buyerfiles.length ; i++)
            {
                buyerFile = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,10);
                buyerFile.setLayoutParams(params);
                buyerFile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clip, 0, 0, 0);
                buyerFile.setText("Attachment "+(i+1));
                buyerFile.setBackground(getResources().getDrawable(R.drawable.textview_background));
                buyerFile.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                buyerFile.setPadding(16,16,16,16);
                buyerAttachments.addView(buyerFile);
            }
    }

    private void loadSellerFiles()
    {
        String[] sellerFiles = order.seller_files.split(",");

        if(sellerFiles.length>0)

            for(int i=0 ; i<sellerFiles.length ; i++)
            {
                sellerFile = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,10);
                sellerFile.setLayoutParams(params);
                sellerFile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clip, 0, 0, 0);
                sellerFile.setText("Attachment "+(i+1));
                sellerFile.setBackground(getResources().getDrawable(R.drawable.textview_background));
                sellerFile.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                sellerFile.setPadding(16,16,16,16);
                sellerAttachments.addView(sellerFile);
            }
    }

    private void sendMsg(String msg)
    {
        ChatModel model = new ChatModel();
        model.message = msg;
        model.buyer_id = String.valueOf(Utils.currentUser.id);
        model.buyer_name = String.valueOf(Utils.currentUser.name);
        model.seller_id = order.seller_id;
        model.seller_name = order.seller_name;
        model.order_id = String.valueOf(order.id);
        model.sender= Utils.currentUser.user_role.toLowerCase();

        Log.d("Chat-MDL", model.toString());

        databaseReference.push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        editText.setText("");
                    }
                }
            });

    }

    private void init()
    {
        img = findViewById(R.id.buyer_order_details_img);
        orderid = findViewById(R.id.buyer_order_details_id);
        sellername = findViewById(R.id.buyer_order_details_sellerName);
        budget = findViewById(R.id.buyer_order_details_budget);
        title = findViewById(R.id.buyer_order_details_title);
        dec = findViewById(R.id.buyer_order_details_des);
        projecttype = findViewById(R.id.buyer_order_details_projectType);
        status = findViewById(R.id.buyer_order_details_status);
        remarks = findViewById(R.id.buyer_order_details_remarks);
        sellerAttachments = findViewById(R.id.buyer_seller_attachments);
        buyerAttachments = findViewById(R.id.buyer_buyer_attachments);
        recyclerView = findViewById(R.id.buyer_order_details_Chat);
        editText = findViewById(R.id.buyer_order_details_edt);
        send = findViewById(R.id.buyer_order_details_send);
        deadline = findViewById(R.id.buyer_order_details_deadline);
        firebaseDatabase = FirebaseDatabase.getInstance();
        order = Utils.selectOrder;
        databaseReference = firebaseDatabase.getReference("chat");
        back = findViewById(R.id.buyer_order_details_back);
        chatLayout = findViewById(R.id.buyer_chat_layout);
        SellerLayout = findViewById(R.id.buyer_sellerLayout);


    }


}
