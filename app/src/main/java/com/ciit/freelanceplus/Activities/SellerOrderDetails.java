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

public class SellerOrderDetails extends AppCompatActivity {

    ImageView img, back;
    TextView orderid, sellername, budget, title, dec, projecttype, status, remarks, deadline, sellerFile, buyerFile, statusbtn;
    LinearLayout sellerAttachments, buyerAttachments;
    RecyclerView recyclerView;
    EditText  editText;
    ImageButton send;
    FirebaseDatabase firebaseDatabase;
    OrderModel order;
    ArrayList<ChatModel> chatModels;
    DatabaseReference databaseReference;
    LinearLayout chatLayout, SellerLayout;
    Button startBtn, attachFilesBtn;
    private String selectedFilePath;
    ArrayList<String> filesArray = new ArrayList<>();
    ArrayList<String> filesNameArray = new ArrayList<>();
    StorageReference storageRef;
    FirebaseStorage storage;
    StorageReference fileReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_details);

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

           Glide.with(SellerOrderDetails.this).load("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSZxWOfkyLXn4j_EzhJRuOccwQcbHemOwftOxXOtoIPaP1b_U9b&usqp=CAU").into(img);

        }else {
            Glide.with(this).load(order.image).into(img);
        }

        if(order.status.toLowerCase().equals("completed"))
        {
            chatLayout.setVisibility(View.VISIBLE);
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

        }

         if(order.status.toLowerCase().equals("assigntoseller"))
        {
            chatLayout.setVisibility(View.GONE);
            statusbtn.setVisibility(View.VISIBLE);
            statusbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String Url = getResources().getString(R.string.API_URL)+"/freelancePlus_API/public/startOrder/"+order.id;
                    StringRequest request = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.equals("1"))
                            {
                                Toast.makeText(SellerOrderDetails.this, response, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SellerOrderDetails.this, HomeActivity.class);
                                startActivity(i);
                                finishAffinity();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(SellerOrderDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    RequestQueue queue = Volley.newRequestQueue(SellerOrderDetails.this);
                    queue.add(request);
                }
            });

            orderid.setText("Order # "+order.id);
            sellername.setText("Seller: "+order.seller_name);
            budget.setText("Budget: "+order.budget);
            title.setText("Project Title: "+order.title);
            dec.setText("Description: "+order.description);
            deadline.setText("Deadline: "+order.deadline);
            projecttype.setText("Project Type: "+order.projectType);
            status.setText("Status: "+order.status);
            remarks.setText("Remarks: "+order.remark);


        } else if(order.status.toLowerCase().equals("requested"))
        {
            SellerLayout.setVisibility(View.GONE);
            chatLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

            orderid.setText("Order # "+order.id);
            budget.setText("Budget: "+order.budget);
            title.setText("Project Title: "+order.title);
            dec.setText("Description: "+order.description);
            deadline.setText("Deadline: "+order.deadline);
            projecttype.setText("Project Type: "+order.projectType);
            status.setText("Status: "+order.status);
            remarks.setText("Remarks: "+order.remark);

        }else{

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

            chatLayout.setVisibility(View.VISIBLE);
            statusbtn.setVisibility(View.VISIBLE);
            statusbtn.setText("Submit Order");
            statusbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(filesArray.size() >0)
                    {
                        Toast.makeText(SellerOrderDetails.this, "Please attached files", Toast.LENGTH_SHORT).show();
                    }else {

                        final ProgressDialog dialog = new ProgressDialog(SellerOrderDetails.this);
                        dialog.setMessage("Creating new order ...");
                        dialog.show();
                        dialog.setCancelable(false);

                        String URL = getResources().getString(R.string.API_URL)+"/freelancePlus_API/public/submitOrder";
                        String fileStr;
                        if(filesArray.size() > 0)
                        {
                            fileStr = filesArray.get(0);
                            for(int i=1; i< filesArray.size() ;i++)
                            {
                                fileStr = fileStr +", "+filesArray.get(i);
                            }

                        }else {

                            fileStr = "";

                        }
                        final String finalFileStr = fileStr;
                        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                        dialog.dismiss();
                                        Log.d("Response", response);
                                        Toast.makeText(SellerOrderDetails.this, response, Toast.LENGTH_SHORT).show();

                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        dialog.dismiss();
                                        Toast.makeText(SellerOrderDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("Error.Response", error.getMessage());
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams()
                            {

                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("order_id", String.valueOf(order.id));
                                params.put("remarks", "Not yet Added");
                                params.put("files", finalFileStr);


                                Log.d("ORDER-JSON", params.toString());

                                return params;
                            }
                        };

                        RequestQueue queue = Volley.newRequestQueue(SellerOrderDetails.this);
                        queue.add(postRequest);

                    }

                }
            });


            attachFilesBtn.setVisibility(View.VISIBLE);
            attachFilesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    //sets the select file to all types of files
                    intent.setType("*/*");
                    //allows to select data and return it
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    //starts new activity to select file and return data
                    startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), 1);

                }
            });
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


          loadSellerChat();


    }

    private void loadSellerChat(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                chatModels = new ArrayList<>();

                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ChatModel model = snapshot.getValue(ChatModel.class);
                        String myId = String.valueOf(Utils.currentUser.id);

                        if (model.seller_id.equals(myId) && model.buyer_id.equals(order.buyer_id)) {

                            chatModels.add(model);
                        }

                    }




                    LinearLayoutManager layoutManager = new LinearLayoutManager(SellerOrderDetails.this,
                            LinearLayoutManager.VERTICAL,true);
                    recyclerView.setLayoutManager(layoutManager);
                    SellerChatAdapter adapter = new SellerChatAdapter(SellerOrderDetails.this, chatModels);
                    recyclerView.setAdapter(adapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(SellerOrderDetails.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadBuyerFiles()
    {
        final String[] buyerfiles = order.files.split(",");

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
                final int finalI = i;
                buyerFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = buyerfiles[finalI];
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

            }
    }

    private void loadSellerFiles()
    {
        final String[] sellerFiles = order.seller_files.split(",");

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
                final int finalI = i;
                sellerFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = sellerFiles[finalI];
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
            }
    }

    private void sendMsg(String msg)
    {
        ChatModel model = new ChatModel();
        model.message = msg;

            model.buyer_id = order.buyer_id;
            model.buyer_name = order.buyer_name;
            model.seller_id = String.valueOf(Utils.currentUser.id);
            model.seller_name = Utils.currentUser.name;
            model.order_id = String.valueOf(order.id);
            model.sender= Utils.currentUser.user_role.toLowerCase();
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
        img = findViewById(R.id.order_details_img);
        orderid = findViewById(R.id.order_details_id);
        sellername = findViewById(R.id.order_details_sellerName);
        budget = findViewById(R.id.order_details_budget);
        title = findViewById(R.id.order_details_title);
        dec = findViewById(R.id.order_details_des);
        projecttype = findViewById(R.id.order_details_projectType);
        status = findViewById(R.id.order_details_status);
        remarks = findViewById(R.id.order_details_remarks);
        sellerAttachments = findViewById(R.id.seller_attachments);
        buyerAttachments = findViewById(R.id.buyer_attachments);
        recyclerView = findViewById(R.id.order_details_Chat);
        editText = findViewById(R.id.order_details_edt);
        send = findViewById(R.id.order_details_send);
        deadline = findViewById(R.id.order_details_deadline);
        firebaseDatabase = FirebaseDatabase.getInstance();
        order = Utils.selectOrder;
        databaseReference = firebaseDatabase.getReference("chat");
        back = findViewById(R.id.order_details_back);
        chatLayout = findViewById(R.id.chat_layout);
        SellerLayout = findViewById(R.id.seller_details_layout);
        statusbtn = findViewById(R.id.order_details_startBTn);
        attachFilesBtn = findViewById(R.id.order_details_addfiles);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (data == null) {
                    //no data present
                    return;
                }

                final ProgressDialog dialog = new ProgressDialog(SellerOrderDetails.this);
                dialog.setMessage("Uploading ...");
                dialog.setCancelable(false);
                dialog.show();

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(SellerOrderDetails.this, selectedFileUri);

                Log.i("UPLOAD-STR", "Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {

                    Uri file = Uri.fromFile(new File(selectedFilePath));
                    filesNameArray.add(file.getLastPathSegment());

                    fileReference = storageRef.child("files/"+file.getLastPathSegment());
                    UploadTask uploadTask = fileReference.putFile(file);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                dialog.dismiss();
                                filesArray.add(downloadUri.toString());
                                Toast.makeText(SellerOrderDetails.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                for(int i=0; i< filesNameArray.size() ;i++)
                                {
                                    sellerFile = new TextView(SellerOrderDetails.this);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(0,0,0,10);
                                    sellerFile.setLayoutParams(params);
                                    sellerFile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clip, 0, 0, 0);
                                    sellerFile.setText(filesNameArray.get(i));
                                    sellerFile.setBackground(getResources().getDrawable(R.drawable.textview_background));
                                    sellerFile.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                    sellerFile.setPadding(16,16,16,16);
                                    sellerAttachments.addView(sellerFile);
                                }


                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });


                } else {

                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
