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
import com.xwaydesigns.youvanteamlead.Adapters.DonatedItemAdapter;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.ExtraClasses.SessionManager;
import com.xwaydesigns.youvanteamlead.Model.AcceptDonation;
import com.xwaydesigns.youvanteamlead.Model.DonatedItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class DonatedItemActivity extends AppCompatActivity {

    private RecyclerView list;
    private LinearLayoutManager linear_Layout;
    String donation_id;
    String item_name ;
    String item_quantity ;
    String item_image;
    private List<DonatedItems> data;
    private HashMap<String, String> user_data;
    private String accept_donations_server_url = Constants.BASE_URL+"youvan/fetch_donated_items.php";
    private String item_status = "Pending";
    private String teamlead_student_id;
    private String donor_student_id;
    private String item_id;
    String[] item_quantity_array;
    private ProgressDialog dialog;
    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donated_item);

        list = findViewById(R.id.donated_list);
        linear_Layout = new LinearLayoutManager(this);
        list.setHasFixedSize(true);
        list.setLayoutManager(linear_Layout);
        item_quantity_array = getResources().getStringArray(R.array.item_quantity);
        donor_student_id = getIntent().getStringExtra("student_id");

        dialog = new ProgressDialog(DonatedItemActivity.this);
        dialog.setTitle("Loading, Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


         manager = new SessionManager(DonatedItemActivity.this);
        user_data = manager.getUserData();
        teamlead_student_id = user_data.get("student_id");
        data = new ArrayList<>();

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
            Toast.makeText(DonatedItemActivity.this,"No internet Connectivity", Toast.LENGTH_SHORT).show();
        }

        //-------------------------------------------------------------------------------------------------\\
        StringRequest request = new StringRequest(Request.Method.POST,accept_donations_server_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONArray result = new JSONArray(response);
                            JSONObject object = null ;

                            for(int i = 0; i< result.length() ; i++)
                            {
                                object = result.getJSONObject(i);
                                donation_id = object.getString("donation_id");
                                item_id = object.getString("item_id");
                                item_quantity = object.getString("item_quantity");
                                item_image = object.getString("item_image");
                                item_name = object.getString("item_name");

                                DonatedItems donation = new DonatedItems(donation_id,item_id,item_quantity,item_image,item_name);
                                data.add(donation);
                            }

                            if(!donation_id.equals("null") && !item_id.equals("null") && !item_quantity.equals("null")
                                    && !item_name.equals("null") )
                            {
                                DonatedItemAdapter adapter = new DonatedItemAdapter(DonatedItemActivity.this,data,donor_student_id,item_quantity_array);
                                list.setAdapter(adapter);
                                dialog.dismiss();
                            }
                            else
                            {
                                dialog.dismiss();
                                Intent intent = new Intent(DonatedItemActivity.this, EmptyDonationActivity.class);
                                intent.putExtra("heading","Donated Items");
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
                params.put("fetch","donated");
                params.put("student_id",donor_student_id);
                params.put("item_status",item_status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DonatedItemActivity.this);
        requestQueue.add(request);
        //-------------------------------------------------------------------------------------------------\\

    }

    public void displayExceptionMessage1(String msg) {
        Toast.makeText(DonatedItemActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
    }
    public void displayExceptionMessage2(String msg) {
        Toast.makeText(DonatedItemActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
    }


}