package com.xwaydesigns.youvanteamlead;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.ExtraClasses.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class LoginActivity extends AppCompatActivity {

    private EditText e_email,e_password;
    private ImageView show_pass_btn;
    private Button login_submit;
    private String email,password;
    private String student_id;
    private String student_name;
    private HashMap<String,String> user_data;
    private String saved_email;
    private String saved_password;
    private String server_url = Constants.BASE_URL+"youvan/login.php";
    private String type="Team Lead";
    private ProgressDialog dialog;
    private SessionManager manager;
    private String school_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e_email = findViewById(R.id.log_email);
        e_password = findViewById(R.id.log_password);
        show_pass_btn =findViewById(R.id.show_pass_btn);
        login_submit =findViewById(R.id.log_submit);

        dialog = new ProgressDialog(LoginActivity.this);
        manager = new SessionManager(LoginActivity.this);


        login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final SessionManager manager = new SessionManager(LoginActivity.this);
                email = e_email.getText().toString().trim();
                password = e_password.getText().toString().trim();
                dialog.setTitle("Loading, Please wait...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
 //------------------------------------------------------------------------------------------------------------------//

                if(!email.equals("") || !password.equals(""))
                {
                    user_data = manager.getUserData();
                    saved_email = user_data.get("user_email");
                    saved_password = user_data.get("user_password");

                        //-------------------------------------\\
                        if(manager.connectivity()){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Intent intent = new Intent(DonateNowActivity.this, Dashboard.class);
                                    //  startActivity(intent);
                                }
                            }, TIME_OUT);
                        }else if(!manager.connectivity()){
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this,"No internet Connectivity", Toast.LENGTH_SHORT).show();
                        }
                        //--------------------------------------------\\
                                     //first time login\\
                        //---------------------------------------------------------//
                        StringRequest request = new StringRequest(Request.Method.POST, server_url,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        try {
                                            JSONArray result = new JSONArray(response);
                                            for(int i = 0; i< result.length() ; i++)
                                            {
                                                JSONObject object = result.getJSONObject(i);
                                                student_id = object.getString("student_id");
                                                student_name = object.getString("student_name");
                                                school_id = object.getString("school_id");
                                            }
                                            if(!student_id.equals("null") && !student_name.equals("null"))
                                            {
                                                //  Toast.makeText(LoginActivity.this,"student_id:" + student_id + "student_name:" + student_name ,Toast.LENGTH_SHORT).show();
                                                e_email.setText("");
                                                e_password.setText("");

                                                Toast.makeText(LoginActivity.this, "Login SuccessFully..", Toast.LENGTH_SHORT).show();
                                                manager.LoginSuccessful(email, password, student_id, school_id);
                                                dialog.dismiss();
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                            {
                                                dialog.dismiss();
                                                Toast.makeText(LoginActivity.this, "Invalid UserName or Password..", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        catch (JSONException e)
                                        {
                                            dialog.dismiss();
                                            Log.e("JSONException ", String.valueOf(e));
                                            e.printStackTrace();
                                            displayExceptionMessage1(e.getMessage());
                                        }
                                    }//onResponse End
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error)
                                    {
                                        dialog.dismiss();
                                        Log.e("Volley error", String.valueOf(error));
                                        error.printStackTrace();
                                        displayExceptionMessage2(error.getMessage());
                                    }
                                }
                        )
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError
                            {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("login","user");
                                params.put("email",email);
                                params.put("password",password);
                                params.put("type",type);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                        requestQueue.add(request);
                        //---------------------------------------------//
                    }

                else
                {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Enter All the Fields..", Toast.LENGTH_SHORT).show();
                }
//------------------------------------------------------------------------------------------------------------------//
            }
        });
    }
    //

    public void ShowHidePass(View view)
    {
        if(view.getId()==R.id.show_pass_btn){

            if(e_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                show_pass_btn.setImageResource(R.drawable.eye_button);

                //Show Password
                e_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                show_pass_btn.setImageResource(R.drawable.close_eye_button);

                //Hide Password
                e_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }

    }

    public void ForgotPassword(View view)
    {

    }

    public void displayExceptionMessage1(String msg) {
        Toast.makeText(LoginActivity.this,"Server Error", Toast.LENGTH_SHORT).show();
    }
    public void displayExceptionMessage2(String msg) {
        Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
    }
}