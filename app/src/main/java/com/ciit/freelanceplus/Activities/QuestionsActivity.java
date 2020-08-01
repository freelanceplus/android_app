package com.ciit.freelanceplus.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ciit.freelanceplus.Model.FilePath;
import com.ciit.freelanceplus.Model.SkillModel;
import com.ciit.freelanceplus.Model.Utils;
import com.ciit.freelanceplus.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class QuestionsActivity extends AppCompatActivity {

    ArrayList<SkillModel> skills;
    ArrayList<String> names;
    ArrayList<String> filesArray = new ArrayList<>();
    ArrayList<String> filesNameArray = new ArrayList<>();
    ArrayList<String> imagesArray = new ArrayList<>();
    ArrayList<String> imagesNameArray = new ArrayList<>();
    public static  EditText projectType;
    private String selectedFilePath;
    EditText title, description, deadline, budget;
    TextView fileNames, imageNames, uploadbtn, imagebtn, nextBtn;
    String projectTypeStr, titleStr, desStr, deadLineStr, budgetStr, fileStr, imageStr;
    StorageReference storageRef;
    FirebaseStorage storage;
    StorageReference fileReference, imageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        init();

        if(!checkPermissionForReadExtertalStorage())
        {
            try {
                requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        projectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(QuestionsActivity.this, SearchSkillActivity.class);
                startActivity(i);
            }
        });

        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               showDatePickerDialog();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkFields())
                {

                    final ProgressDialog dialog = new ProgressDialog(QuestionsActivity.this);
                    dialog.setMessage("Creating new order ...");
                    dialog.show();
                    dialog.setCancelable(false);

                    String url = getResources().getString(R.string.API_URL)+"/freelancePlus_API/public/createOrder";

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

                    if(imagesArray.size()>0)
                    {
                        imageStr = imagesArray.get(0);
                        for(int i=1; i< imagesArray.size() ;i++)
                        {
                            imageStr = imageStr +", "+imagesArray.get(i);
                        }

                    }else {
                        imageStr = "";
                    }
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    dialog.dismiss();
                                    Log.d("Response", response);
                                    showSnackBar(response);

                                    projectType.setText("");
                                    Utils.currentSkill = null;
                                    title.setText("");
                                    description.setText("");
                                    budget.setText("");
                                    deadline.setText("");

                                    Intent i = new Intent(QuestionsActivity.this, BuyerDashboard.class);
                                    startActivity(i);
                                    finishAffinity();

                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    dialog.dismiss();
                                    showSnackBar(error.getMessage());
                                    Log.d("Error.Response", error.getMessage());
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams()
                        {

                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("title", titleStr);
                            params.put("description", desStr);
                            params.put("project_type", projectTypeStr);
                            params.put("images", imageStr);
                            params.put("deadline", deadLineStr);
                            params.put("budget", budgetStr);
                            params.put("files", fileStr);
                            params.put("buyer_id", String.valueOf(Utils.currentUser.id));
                            params.put("status", "requested");

                            Log.d("ORDER-JSON", params.toString());

                            return params;
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(QuestionsActivity.this);
                    queue.add(postRequest);
                }
            }
        });

        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                galleryIntent();
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
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

    private void init(){

        projectType = findViewById(R.id.questions_proType_edt);
        title = findViewById(R.id.questions_title_edt);
        description = findViewById(R.id.questions_description_edt);
        deadline = findViewById(R.id.questions_deadline_edt);
        budget = findViewById(R.id.questions_budget_edt);
        fileNames = findViewById(R.id.questions_filesNames);
        imageNames = findViewById(R.id.questions_imageNames);
        uploadbtn = findViewById(R.id.uploadbtn);
        imagebtn = findViewById(R.id.imagebtn);
        nextBtn = findViewById(R.id.questions_next);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    private void showDatePickerDialog()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(QuestionsActivity.this  , R.style.DatePickerTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        month = month+1;
                        deadLineStr = (year+ "-"+ ((month < 10 ? "0" : "") + month)+ "-" + (day < 10 ? "0" : "") + day) ;
                        deadline.setText(deadLineStr);

                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private boolean checkFields()
    {
        titleStr = title.getText().toString();
        desStr = description.getText().toString();
        deadLineStr = deadline.getText().toString();

        if(Utils.currentSkill != null)
        {
            projectTypeStr = String.valueOf(Utils.currentSkill.id);
        }else {


            projectTypeStr = "";
        }

        budgetStr = budget.getText().toString();

        if(titleStr.isEmpty() && desStr.isEmpty() && deadLineStr.isEmpty() && projectTypeStr.isEmpty() && budgetStr.isEmpty())
        {
            showSnackBar("Please Enter Required Field");
            return false;
        }else if(titleStr.isEmpty() || desStr.isEmpty() || deadLineStr.isEmpty() || projectTypeStr.isEmpty() || budgetStr.isEmpty())
        {
            if(titleStr.isEmpty())
            {
                showSnackBar("Enter project title");
            }
            if(projectTypeStr.isEmpty())
            {
                showSnackBar("Enter project type");
            }
            if(desStr.isEmpty())
            {
                showSnackBar("Enter project description");
            }
            if(budgetStr.isEmpty())
            {
                showSnackBar("Enter project budget");
            }
            if(deadLineStr.isEmpty())
            {
                showSnackBar("Enter project deadline");
            }

            return false;
        }else {

            return true;
        }

    }

    private void showSnackBar(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

                final ProgressDialog dialog = new ProgressDialog(QuestionsActivity.this);
                dialog.setMessage("Uploading ...");
                dialog.setCancelable(false);
                dialog.show();

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(QuestionsActivity.this, selectedFileUri);

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
                                Toast.makeText(QuestionsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                String x = filesNameArray.get(0);
                                for(int i=1; i< filesNameArray.size() ;i++)
                                {
                                    x = x +", "+filesNameArray.get(i);
                                }
                                fileNames.setText(x);

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

            if(requestCode == 100)
            {
                if(data == null)
                {
                    return;
                }

                try {

                    final ProgressDialog dialog = new ProgressDialog(QuestionsActivity.this);
                    dialog.setMessage("Uploading ...");
                    dialog.setCancelable(false);
                    dialog.show();

                Uri selectedImage = data.getData();
                selectedFilePath = FilePath.getPath(QuestionsActivity.this, selectedImage);

                    Log.i("UPLOAD-STR", "Selected File Path:" + selectedFilePath);

                    if (selectedFilePath != null && !selectedFilePath.equals("")) {

                        Uri file = Uri.fromFile(new File(selectedFilePath));
                        imagesNameArray.add(file.getLastPathSegment());

                        imageReference = storageRef.child("images/" + file.getLastPathSegment());
                        UploadTask uploadTask = imageReference.putFile(file);

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return imageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();

                                    dialog.dismiss();
                                    imagesArray.add(downloadUri.toString());
                                    Toast.makeText(QuestionsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                    String x = imagesNameArray.get(0);
                                    for(int i=1; i< imagesNameArray.size() ;i++)
                                    {
                                        x = x +", "+imagesNameArray.get(i);
                                    }
                                    imageNames.setText(x);


                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void galleryIntent()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 100);
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }



    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) QuestionsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    22);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
