package com.ciit.freelanceplus.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.ciit.freelanceplus.Model.UserModel;
import com.ciit.freelanceplus.Model.Utils;
import com.ciit.freelanceplus.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {

    LinearLayout registerLayout;
    TextView loginBtn;
    EditText email, password;
    String emailStr, passwordStr, userRole;
    RadioButton radioButton;
    RadioGroup radioGroup;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        registerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkFields())
                {
                    int id = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(id);
                    userRole = radioButton.getText().toString();

                    if(userRole.equals("Seller"))
                    {
                        sellerLogin();

                    }else if(userRole.equals("Buyer"))
                    {
                        buyerLogin();
                    }

                }

            }
        });
    }

    private void init()
    {
        registerLayout = findViewById(R.id.register_layout);
        loginBtn = findViewById(R.id.login_btn);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        radioGroup = findViewById(R.id.login_radioGroup);

    }

    private void sellerLogin()
    {
        String URL = getResources().getString(R.string.API_URL)+"/freelancePlus_API/public/loginSeller";
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("User Authentication ...");
        dialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("user-jsn", response);
                        if(response.startsWith("["))
                        {
                            try {
                                JSONArray array = new JSONArray(response);
                                JSONObject object = array.getJSONObject(0);
                                UserModel model = new UserModel();
                                model.id = object.getInt("id");
                                model.name = object.getString("name");
                                model.email = object.getString("email");
                                model.account_balance = object.getString("account_balance");
                                model.user_id = object.getString("user_id");
                                model.verified = object.getInt("varified");
                                model.user_role = "Seller";

                                if(model.verified == 1){

                                    Utils.currentUser = model;

                                    setSharedPreferences(model);
                                    Toast.makeText(LoginActivity.this, model.toString() , Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(i);
                                    finish();

                                    showSnackBar("User Successfully Login");

                                }else {

                                    Toast.makeText(LoginActivity.this, "You are un-verified seller, please go to web", Toast.LENGTH_SHORT).show();
                                }


                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        dialog.dismiss();
;
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

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(postRequest);
    }

    private void buyerLogin()
    {
        String URL = getResources().getString(R.string.API_URL)+"/freelancePlus_API/public/loginBuyer";
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("User Authentication ...");
        dialog.setCancelable(false);
        dialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        dialog.dismiss();


                        if(response.startsWith("["))
                        {
                            try {
                                JSONArray array = new JSONArray(response);
                                JSONObject object = array.getJSONObject(0);
                                UserModel model = new UserModel();
                                model.id = object.getInt("id");
                                model.name = object.getString("name");
                                model.email = object.getString("email");
                                model.account_balance = object.getString("account_balance");
                                model.user_id = object.getString("user_id");
                                model.user_role = "Buyer";

                                Utils.currentUser = model;

                                setSharedPreferences(model);

                                Log.d("USER-JSON", model.toString());

                                    Intent i = new Intent(LoginActivity.this, BuyerDashboard.class);
                                    startActivity(i);
                                    finish();
                                    showSnackBar("User Successfully Login");

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

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

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(postRequest);
    }

    private boolean checkFields()
    {
        emailStr = email.getText().toString();
        passwordStr = password.getText().toString();

        if(emailStr.isEmpty() && passwordStr.isEmpty())
        {
          showSnackBar("Enter Email & Password");
          return false;
        }else if(emailStr.isEmpty() || passwordStr.isEmpty())
        {
            if(emailStr.isEmpty())
            {
                showSnackBar("Enter Email");
            }

            if(passwordStr.isEmpty()){
                showSnackBar("Enter Password");
            }

            return false;
        }else if(radioGroup.getCheckedRadioButtonId() == -1){

            showSnackBar("Select Seller or Buyer");
            return false;

        }else {

            return true;
        }

    }

    private void showSnackBar(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setSharedPreferences(UserModel model)
    {
        sharedPreferences =  getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("id", model.id);
        editor.putString("name", model.name);
        editor.putString("email", model.email);
        editor.putString("account_balance", model.account_balance);
        editor.putString("user_id", model.user_id);
        editor.putString("user_role", model.user_role);
        editor.apply();
    }

    private UserModel getSharedPreferences()
    {
        UserModel model = new UserModel();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        model.id = sharedPreferences.getInt("id", 0);
        model.email = sharedPreferences.getString("email","");
        model.name = sharedPreferences.getString("name","");
        model.user_id = sharedPreferences.getString("user_id","");
        model.account_balance = sharedPreferences.getString("account_balance","");
        model.user_role = sharedPreferences.getString("user_role","");
        return model;
    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id",0);

        if(id != 0)
        {
            Utils.currentUser = getSharedPreferences();
            String userRole = Utils.currentUser.user_role.toLowerCase();
            if(userRole.equals("buyer"))
            {
                Intent i = new Intent(LoginActivity.this, BuyerDashboard.class);
                startActivity(i);
                finish();
            }

            if(userRole.equals("seller"))
            {
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }

        }

    }
}
