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
import com.xwaydesigns.youvanteamlead.Adapters.AllDonorsAdapter;
import com.xwaydesigns.youvanteamlead.Adapters.ReceivedItemAdapter;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.ExtraClasses.SessionManager;
import com.xwaydesigns.youvanteamlead.Model.AllDonor;
import com.xwaydesigns.youvanteamlead.Model.ReceivedItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class ReceivedItemsActivity extends AppCompatActivity {

    private RecyclerView list;
    private LinearLayoutManager linear_Layout;
    String donation_id;
    String item_id ;
    String student_id;
    String item_quantity ;
    String item_image ;
    String item_name ;
    private List<ReceivedItems> data;
    private ProgressDialog dialog;
    private String fetch_received_items_server_url = Constants.BASE_URL+"youvan/fetch_received_items.php";
    private SessionManager manager;
    private HashMap<String, String> user_data;
    private String school_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_items);

        list = findViewById(R.id.received_item_list);
        linear_Layout = new LinearLayoutManager(this);
        list.setHasFixedSize(true);
        list.setLayoutManager(linear_Layout);
        data = new ArrayList<>();

        school_id =  getIntent().getStringExtra("school_id");

        dialog = new ProgressDialog(ReceivedItemsActivity.this);
        dialog.setTitle("Loading, Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        manager = new SessionManager(ReceivedItemsActivity.this);
        user_data = manager.getUserData();
       // student_id = user_data.get("student_id");
       // school_id = user_data.get("school_id");

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
            Toast.makeText(ReceivedItemsActivity.this,"No internet Connectivity", Toast.LENGTH_SHORT).show();
        }
        //-----------------------------------------------------------------------------------\\
        StringRequest request = new StringRequest(Request.Method.POST,fetch_received_items_server_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                       // Toast.makeText(ReceivedItemsActivity.this,"Response:"+response,Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray result = new JSONArray(response);
                            JSONObject object = null ;

                            for(int i = 0; i< result.length();i++)
                            {
                                object = result.getJSONObject(i);
                                donation_id = object.getString("donation_id");
                                item_id = object.getString("item_id");
                                student_id = object.getString("student_id");
                                item_quantity = object.getString("item_quantity");
                                item_image = object.getString("item_image");
                                item_name = object.getString("item_name");

                                ReceivedItems donation = new ReceivedItems(donation_id,item_id,student_id,item_quantity,item_image,item_name);
                                data.add(donation);
                            }
                            if(!donation_id.equals("null") && !item_id.equals("null") && !item_name.equals("null") && !item_quantity.equals("null") )
                            {
                                ReceivedItemAdapter adapter = new ReceivedItemAdapter(ReceivedItemsActivity.this,data);
                                list.setAdapter(adapter);
                                dialog.dismiss();
                            }
                            else
                            {
                                dialog.dismiss();
                                Intent intent = new Intent(ReceivedItemsActivity.this,EmptyDonationActivity.class);
                                intent.putExtra("heading","Received Items");
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
                params.put("fetch","received item");
                params.put("school_id",school_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ReceivedItemsActivity.this);
        requestQueue.add(request);
        //-----------------------------------------------------------------------------------\\
    }

    public void displayExceptionMessage1(String msg) {
        Toast.makeText(ReceivedItemsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
    }
    public void displayExceptionMessage2(String msg) {
        Toast.makeText(ReceivedItemsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
    }

}