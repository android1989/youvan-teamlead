package com.xwaydesigns.youvanteamlead.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.ExtraClasses.SessionManager;
import com.xwaydesigns.youvanteamlead.R;
import com.xwaydesigns.youvanteamlead.UpdateProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.net.sip.SipErrorCode.TIME_OUT;


public class ProfileFragment extends Fragment
{
    private CircleImageView profile_image;
    private TextView teamlead_name;
    private TextView school_name;
    private TextView mobile;
    private TextView email;
    private Button update_profile_btn;
    private HashMap<String, String> user_data;
    private String student_id;
    private String fetch_profile_teamlead_server_url = Constants.BASE_URL+"youvan/fetch_profile_teamlead.php";
    private String type = "Team Lead";
    private String received_student_name;
    private String received_student_image;
    private String received_school_id;
    private String received_school_name;
    private String image_url;
    private String received_mobile;
    private String received_email;
    private ProgressDialog dialog;

    public ProfileFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        profile_image = view.findViewById(R.id.profile_image);
        teamlead_name = view.findViewById(R.id.fragment_profile_student_name);
        school_name = view.findViewById(R.id.fragment_profile_school_name);
        mobile = view.findViewById(R.id.fragment_profile_student_mobile);
        email = view.findViewById(R.id.fragment_profile_student_email);
        update_profile_btn = view.findViewById(R.id.update_profile_btn);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading, Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        SessionManager manager = new SessionManager(getActivity());
        user_data = manager.getUserData();
        student_id = user_data.get("student_id");
       // Toast.makeText(getContext(),"Student_Id:"+student_id,Toast.LENGTH_SHORT).show();
        //-------------------------------------------------------------\\
        update_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });
        //---------------------------------------------------------------\\
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
        //-------------------------------------------------------------------------------------------\\
        StringRequest request = new StringRequest(Request.Method.POST,fetch_profile_teamlead_server_url,
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
                                received_student_name = object.getString("student_name");
                                received_student_image = object.getString("student_image");
                                received_school_id  = object.getString("school_id");
                                received_mobile  = object.getString("mobile");
                                received_email  = object.getString("email");
                                received_school_name  = object.getString("school_name");

                            }
                            if(received_student_image.equals("null"))
                            {
                                teamlead_name.setText(received_student_name);
                                profile_image.setImageResource(R.drawable.default_profile_image);
                                school_name.setText(received_school_name);
                                mobile.setText(received_mobile);
                                email.setText(received_email);
                                dialog.dismiss();
                            }
                            else
                            {
                                teamlead_name.setText(received_student_name);
                                school_name.setText(received_school_name);
                                mobile.setText(received_mobile);
                                email.setText(received_email);
                                image_url = Constants.BASE_URL+"youvan/images/"+student_id+"/StudentsImage/"+received_student_image;
                                Picasso.get().load(image_url).resize(150,150).centerCrop().placeholder(R.drawable.default_profile_image)
                                        .error(R.drawable.default_profile_image).into(profile_image);
                                dialog.dismiss();

                            }
                        }
                        catch (JSONException e)
                        {
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
                params.put("fetch","teamlead");
                params.put("student_id",student_id);
                params.put("type",type);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
        //-------------------------------------------------------------------------------------------\\

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
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