package com.ciit.freelanceplus.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ciit.freelanceplus.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ImageView back;
    EditText name, email, password;
    String nameStr, emailStr, passwordStr, userRole;
    RadioGroup radioGroup;
    RadioButton radioButton;
    CheckBox checkBox;
    TextView registerBtn;
    LinearLayout login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkFields())
                {
                    int id = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(id);
                    userRole = radioButton.getText().toString();

                    if(userRole.equals("Seller"))
                    {

                        sellerRegister();

                    }else if(userRole.equals("Buyer"))
                    {
                        buyerRegister();
                    }
                }
            }
        });
    }

    private void init()
    {
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        name = findViewById(R.id.register_name);
        radioGroup = findViewById(R.id.register_radioGroup);
        checkBox = findViewById(R.id.register_checkBox);
        back = findViewById(R.id.register_back);
        registerBtn = findViewById(R.id.register_btn);
        login = findViewById(R.id.register_login);
    }

    private void sellerRegister()
    {
        String URL = getResources().getString(R.string.API_URL)+"/freelancePlus_API/public/signupSeller";
        final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setMessage("User Authentication ...");
        dialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        dialog.dismiss();
                        showSnackBar(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        dialog.dismiss();
                        showSnackBar(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", emailStr);
                params.put("password", passwordStr);
                params.put("name", nameStr);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(postRequest);


    }

    private void buyerRegister()
    {
        String URL = getResources().getString(R.string.API_URL)+"/freelancePlus_API/public/signupBuyer";
        final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setMessage("User Authentication ...");
        dialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        dialog.dismiss();
                        showSnackBar(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        dialog.dismiss();
                        showSnackBar(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", emailStr);
                params.put("password", passwordStr);
                params.put("name", nameStr);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(postRequest);
    }
    private boolean checkFields()
    {
        emailStr = email.getText().toString();
        passwordStr = password.getText().toString();
        nameStr = name.getText().toString();

        if(emailStr.isEmpty() && passwordStr.isEmpty() && nameStr.isEmpty() && (radioGroup.getCheckedRadioButtonId() == -1) && !checkBox.isChecked())
        {
            showSnackBar("Please Enter Required Fields");
            return false;

        }else if(emailStr.isEmpty() || passwordStr.isEmpty() || nameStr.isEmpty() || (radioGroup.getCheckedRadioButtonId() == -1) || !checkBox.isChecked())
        {
            if(emailStr.isEmpty())
            {
                showSnackBar("Enter Email");
            }

            if(passwordStr.isEmpty()){
                showSnackBar("Enter Password");
            }

            if(nameStr.isEmpty())
            {
                showSnackBar("Enter Name");
            }

            if(radioGroup.getCheckedRadioButtonId() == -1)
            {
                showSnackBar("Select Seller or Buyer");
            }
            if(!checkBox.isChecked())
            {
                showSnackBar("Accept Terms & Condition");
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
}
