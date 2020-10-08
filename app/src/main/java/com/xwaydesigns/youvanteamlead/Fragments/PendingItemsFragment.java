package com.xwaydesigns.youvanteamlead.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xwaydesigns.youvanteamlead.Adapters.PendingItemAdapter;
import com.xwaydesigns.youvanteamlead.EmptyDonationActivity;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.ExtraClasses.SessionManager;
import com.xwaydesigns.youvanteamlead.Model.PendingItems;
import com.xwaydesigns.youvanteamlead.PendingItemsActivity;
import com.xwaydesigns.youvanteamlead.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class PendingItemsFragment extends Fragment
{
    private RecyclerView list;
    private List<PendingItems> data;
    private String donation_id;
    private String item_id;
    private String student_id;
    private String item_quantity;
    private String item_image;
    private String item_name;
    private String student_name;
    private String mobile;
    private ProgressDialog dialog;
    private String fetch_pending_items_server_url = Constants.BASE_URL+"youvan/fetch_pending_items.php";
    private SessionManager manager;
    private HashMap<String, String> user_data;
    private String school_id;

    public PendingItemsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_pending_items, container, false);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        list = (RecyclerView) view.findViewById(R.id.fragment_pending_item_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        data = new ArrayList<>();

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading, Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        manager = new SessionManager(getActivity());
        user_data = manager.getUserData();
       // student_id = user_data.get("student_id");
        school_id = user_data.get("school_id");
       // Toast.makeText(getContext(),"School_Id:"+school_id,Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getContext(),"No internet Connectivity", Toast.LENGTH_SHORT).show();
        }

    //----------------------------------------------------------------------------------------------\\

        StringRequest request = new StringRequest(Request.Method.POST,fetch_pending_items_server_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // Toast.makeText(PendingItemsActivity.this,"Response:"+response,Toast.LENGTH_SHORT).show();
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
                                student_name = object.getString("student_name");
                                mobile = object.getString("mobile");

                                PendingItems donation = new PendingItems(donation_id,item_id,student_id,item_quantity,item_image,item_name,student_name,mobile);
                                data.add(donation);
                            }

                            if(!donation_id.equals("null") && !item_id.equals("null") && !item_name.equals("null") && !item_quantity.equals("null") )
                            {
                                PendingItemAdapter adapter = new PendingItemAdapter(getActivity(),data);
                                list.setAdapter(adapter);
                                dialog.dismiss();
                            }
                            else
                            {
                                dialog.dismiss();
                                view.setBackgroundResource(R.drawable.empty_page);
                              // Intent intent = new Intent(getActivity(), EmptyDonationActivity.class);
                              // intent.putExtra("heading","Pending Items");
                             //  startActivity(intent);
                               // getActivity().finish();
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
                params.put("fetch","pending item");
                params.put("school_id",school_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    //-------------------------------------------------------------------------------------------------\\

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);
    }
    public void displayExceptionMessage1(String msg) {
        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
    }
    public void displayExceptionMessage2(String msg) {
        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
    }
}