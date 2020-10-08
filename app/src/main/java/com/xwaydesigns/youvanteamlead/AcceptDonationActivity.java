package com.xwaydesigns.youvanteamlead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xwaydesigns.youvanteamlead.Adapters.AcceptDonationAdapter;
import com.xwaydesigns.youvanteamlead.Adapters.AllDonorsAdapter;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.ExtraClasses.SessionManager;
import com.xwaydesigns.youvanteamlead.Model.AcceptDonation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class AcceptDonationActivity extends AppCompatActivity
{
    private RecyclerView list;
    private LinearLayoutManager linear_Layout;
    private String student_name;
    private String student_image;
    private String mobile;
    private String student_class ;
    private String item_quantity;
    private List<AcceptDonation> data;
    private HashMap<String, String> user_data;
    private String teamlead_student_id;
    private String accept_donations_server_url = Constants.BASE_URL+"youvan/fetch_accept_donation.php";
    private String type = "Team Lead";
    private String school_id;
    private String item_status = "Pending";
    private String donor_student_id;
    private ProgressDialog dialog;
    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_donation);
        school_id =  getIntent().getStringExtra("school_id");
       // Toast.makeText(AcceptDonationActivity.this,"school_id:"+ school_id,Toast.LENGTH_SHORT).show();

        dialog = new ProgressDialog(AcceptDonationActivity.this);
        dialog.setTitle("Loading, Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        manager = new SessionManager(AcceptDonationActivity.this);
        user_data = manager.getUserData();
        teamlead_student_id = user_data.get("student_id");
       // Toast.makeText(AcceptDonationActivity.this,"student_id:"+ student_id,Toast.LENGTH_SHORT).show();
        list = findViewById(R.id.accept_list);
        linear_Layout = new LinearLayoutManager(this);
        list.setHasFixedSize(true);
        list.setLayoutManager(linear_Layout);
        data = new ArrayList<>();
        FetchFromDatabase();
    }

    private void FetchFromDatabase()
    {
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
            Toast.makeText(AcceptDonationActivity.this,"No internet Connectivity", Toast.LENGTH_SHORT).show();
        }
        //-------------------------------------------------------------------------------------------------\\
        StringRequest request = new StringRequest(Request.Method.POST,accept_donations_server_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                       // Toast.makeText(AcceptDonationActivity.this,"Response:"+response,Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray result = new JSONArray(response);
                            JSONObject object = null ;

                            for(int i = 0; i< result.length();i++)
                            {
                                object = result.getJSONObject(i);
                                donor_student_id = object.getString("student_id");
                                item_quantity = object.getString("item_quantity");
                                student_name = object.getString("student_name");
                                student_image = object.getString("student_image");
                                student_class = object.getString("student_class");
                                mobile = object.getString("mobile");
                                AcceptDonation donation = new AcceptDonation( donor_student_id,student_name,student_image,student_class,mobile,item_quantity);
                                data.add(donation);
                            }
                            if(!donor_student_id.equals("null") && !item_quantity.equals("null") && !student_name.equals("null")
                                    && !student_class.equals("null") && !mobile.equals("null"))
                            {
                                AcceptDonationAdapter adapter = new AcceptDonationAdapter(AcceptDonationActivity.this,data,donor_student_id);
                                list.setAdapter(adapter);
                                dialog.dismiss();
                            }
                            else
                            {
                                dialog.dismiss();
                                Intent intent = new Intent(AcceptDonationActivity.this, EmptyDonationActivity.class);
                                intent.putExtra("heading","Accept Donations");
                                startActivity(intent);
                                finish();
                            }

                        }
                        catch (JSONException e)
                        {
                            dialog.dismiss();
                            e.printStackTrace();
                            Log.e("fetch JSONException ", String.valueOf(e));
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
                        Log.e("fetch Volley error", String.valueOf(error));
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
                params.put("fetch","Accept");
                params.put("school_id",school_id);
                params.put("item_status",item_status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AcceptDonationActivity.this);
        requestQueue.add(request);
        //-------------------------------------------------------------------------------------------------\\

    }

    public void displayExceptionMessage1(String msg) {
        Toast.makeText(AcceptDonationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
    }
    public void displayExceptionMessage2(String msg) {
        Toast.makeText(AcceptDonationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
    }

}