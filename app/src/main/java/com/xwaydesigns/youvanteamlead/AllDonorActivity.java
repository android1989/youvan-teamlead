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
import com.xwaydesigns.youvanteamlead.Adapters.ReceivedItemAdapter;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.ExtraClasses.SessionManager;
import com.xwaydesigns.youvanteamlead.Model.AcceptDonation;
import com.xwaydesigns.youvanteamlead.Model.AllDonor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class AllDonorActivity extends AppCompatActivity {

    private RecyclerView list;
    private LinearLayoutManager linear_Layout;
    String student_id;
    String student_name ;
    String student_image ;
    String student_class;
    private List<AllDonor> data;
    private String fetch_all_donors_server_url = Constants.BASE_URL+"youvan/fetch_all_donors.php";
    private ProgressDialog dialog;
    private SessionManager manager;
    private String school_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_donor);

        list = findViewById(R.id.donor_list);
        linear_Layout = new LinearLayoutManager(this);
        list.setHasFixedSize(true);
        list.setLayoutManager(linear_Layout);
        data = new ArrayList<>();
        school_id =  getIntent().getStringExtra("school_id");
        dialog = new ProgressDialog(AllDonorActivity.this);
        dialog.setTitle("Loading, Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        manager = new SessionManager(AllDonorActivity.this);

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
            Toast.makeText(AllDonorActivity.this,"No internet Connectivity", Toast.LENGTH_SHORT).show();
        }

        //-----------------------------------------------------------------------------------\\
        StringRequest request = new StringRequest(Request.Method.POST,fetch_all_donors_server_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                      //  Toast.makeText(AllDonorActivity.this,"Response:"+response,Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray result = new JSONArray(response);
                            JSONObject object = null ;

                            for(int i = 0; i< result.length();i++)
                            {
                                object = result.getJSONObject(i);
                                student_id = object.getString("student_id");
                                student_name = object.getString("student_name");
                                student_image = object.getString("student_image");
                                student_class = object.getString("student_class");

                                AllDonor donation = new AllDonor(student_id,student_name,student_image,student_class);
                                data.add(donation);
                            }
                            if(!student_id.equals("null") && !student_name.equals("null") && !student_class.equals("null"))
                            {
                                AllDonorsAdapter adapter = new AllDonorsAdapter(AllDonorActivity.this,data);
                                list.setAdapter(adapter);
                                dialog.dismiss();
                            }
                            else
                            {
                                dialog.dismiss();
                                Intent intent = new Intent(AllDonorActivity.this, EmptyDonationActivity.class);
                                intent.putExtra("heading","All Donors");
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
                params.put("fetch","All donors");
                params.put("school_id",school_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AllDonorActivity.this);
        requestQueue.add(request);
        //-----------------------------------------------------------------------------------\\
    }

    public void displayExceptionMessage1(String msg) {
        Toast.makeText(AllDonorActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
    }
    public void displayExceptionMessage2(String msg) {
        Toast.makeText(AllDonorActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
    }
}