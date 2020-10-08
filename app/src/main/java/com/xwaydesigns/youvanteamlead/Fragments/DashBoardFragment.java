package com.xwaydesigns.youvanteamlead.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.xwaydesigns.youvanteamlead.AcceptDonationActivity;
import com.xwaydesigns.youvanteamlead.AllDonorActivity;
import com.xwaydesigns.youvanteamlead.RejectedItemsActivity;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.ExtraClasses.SessionManager;
import com.xwaydesigns.youvanteamlead.MainActivity;
import com.xwaydesigns.youvanteamlead.PendingItemsActivity;
import com.xwaydesigns.youvanteamlead.R;
import com.xwaydesigns.youvanteamlead.ReceivedItemsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.net.sip.SipErrorCode.TIME_OUT;

public class DashBoardFragment extends Fragment
{
    private static final int CAPTURE_IMAGE_FROM_CAMERA = 1 ;
    private static final int CHOOSE_IMAGE_FROM_GALLERY = 2;
    private TextView dashboard_username;
    private TextView dashboard_school_name;
    private CircleImageView dashboard_profile_image;
    private GridLayout main_grid;
    private CardView cardView1, cardView2, cardView3, cardView4;
    private TextView donor_count,received_items,pending_items,donors_reward_point;
    private Button accept_donation_btn;
    private HashMap<String, String> user_data;
    private ProgressDialog dialog;
    private String student_id;
    private String fetch_main_students_server_url = Constants.BASE_URL+"youvan/fetch_main_teamlead.php";
    private String type="Team Lead";
    private String received_student_name;
    private String received_student_image;
    private String received_school_name;
    private String student_name;
    private String image_url;
    private Button add_image_btn;
    private Uri image_uri;
    private String Encoded_images;
    private String upload_main_student_image_server_url = Constants.BASE_URL+"youvan/upload_student_image.php";
    Context applicationContext = MainActivity.getContextOfApplication();
    private String received_school_id;
    private SessionManager manager;
    private String school_id;

    public DashBoardFragment()
    {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dash_board, container, false);
        dashboard_username = view.findViewById(R.id.dashboard_username);
        dashboard_school_name = view.findViewById(R.id.dashboard_school_name);
        dashboard_profile_image = view.findViewById(R.id.dashboard_profile_image);
        add_image_btn = view.findViewById(R.id.dashboard_add_profile_image_btn);
        donor_count = view.findViewById(R.id.dashboard_total_donor);
        received_items = view.findViewById(R.id.dashboard_received_items);
        pending_items = view.findViewById(R.id.dashboard_pending_items);
        donors_reward_point = view.findViewById(R.id.dashboard_donor_reward_point);
        accept_donation_btn = view.findViewById(R.id.accept_donation_btn);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading, Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        manager = new SessionManager(getActivity());
        user_data = manager.getUserData();
        student_id = user_data.get("student_id");
        school_id = user_data.get("school_id");
      //  Toast.makeText(getContext(),"School_Id:"+school_id,Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getContext(),"No internet Connectivity", Toast.LENGTH_SHORT).show();
        }
        //--------------------------------------------\\

        //-------------------------------------------------------------------------------------------\\
        StringRequest request = new StringRequest(Request.Method.POST,fetch_main_students_server_url,
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
                                received_school_name  = object.getString("school_name");
                                student_name = received_student_name;
                            }

                            //saving the teamlead school id in local shared preference

                            if(received_student_image.equals("null"))
                            {
                                dashboard_username.setText(received_student_name);
                                dashboard_profile_image.setImageResource(R.drawable.default_profile);
                                dashboard_school_name.setText(received_school_name);
                                dialog.dismiss();
                            }
                            else
                            {
                                dashboard_username.setText(received_student_name);
                                dashboard_school_name.setText(received_school_name);
                                image_url = Constants.BASE_URL+"youvan/images/"+student_id+"/StudentsImage/"+received_student_image;
                                Picasso.get().load(image_url).resize(120,120).centerCrop().placeholder(R.drawable.default_profile)
                                        .error(R.drawable.default_profile).into(dashboard_profile_image);
                                dialog.dismiss();

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
                params.put("fetch","student");
                params.put("student_id",student_id);
                params.put("type",type);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

        //-------------------------------------------------------------------------------------------\\

        main_grid = view.findViewById(R.id.home_grid);
        cardView1 = (CardView) main_grid.getChildAt(0);
        cardView1.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardview1_color));
        cardView2 = (CardView) main_grid.getChildAt(1);
        cardView2.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardview2_color));
        cardView3 = (CardView) main_grid.getChildAt(2);
        cardView3.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardview3_color));
        cardView4 = (CardView) main_grid.getChildAt(3);
        cardView4.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardview4_color));

        cardView1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(getActivity(), AllDonorActivity.class);
           intent.putExtra("school_id",received_school_id);
            startActivity(intent);
        }
    });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), ReceivedItemsActivity.class);
                intent.putExtra("school_id",received_school_id);
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PendingItemsActivity.class);
                intent.putExtra("school_id",received_school_id);
                startActivity(intent);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RejectedItemsActivity.class);
                intent.putExtra("school_id",received_school_id);
                startActivity(intent);
            }
        });
        accept_donation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                    Intent intent = new Intent(getActivity(), AcceptDonationActivity.class);
                    intent.putExtra("school_id",received_school_id);
                    startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------\\

        add_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                /////////////////////////////////////////////////////////////////////////////////////////////
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item)
                    {
                        if (options[item].equals("Take Photo"))
                        {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,CAPTURE_IMAGE_FROM_CAMERA);
                        }
                        else if (options[item].equals("Choose from Gallery"))
                        {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,"select image"),CHOOSE_IMAGE_FROM_GALLERY);
                        }
                        else if (options[item].equals("Cancel"))
                        {
                            dialog.dismiss();
                            Toast.makeText(getContext(),"item image loading cancelled",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
        /////////////////////////////////////////////////////////////////////////////////////////////
            }
        });

        return view;
    }


    ///////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
       // super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            bitmap = getResizedBitmap(bitmap,400);
            dashboard_profile_image.setImageBitmap(bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Encoded_images = Base64.encodeToString(byteArray, Base64.DEFAULT);
            SaveToDatabase();
        }

        if(requestCode == CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK)
        {
            image_uri = data.getData();
            Toast.makeText(getActivity(),"gallery Image Uri:" + image_uri ,Toast.LENGTH_SHORT).show();
            try
            {
                //applicationContext.getContentResolver();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(),image_uri);
                bitmap = getResizedBitmap(bitmap, 400);
                dashboard_profile_image.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Encoded_images = Base64.encodeToString(byteArray, Base64.DEFAULT);
                SaveToDatabase();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    ////////////////////////////////////////////////

    private void SaveToDatabase()
    {
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
            Toast.makeText(getContext(),"No internet Connectivity", Toast.LENGTH_SHORT).show();
        }
        //--------------------------------------------\\
        StringRequest request = new StringRequest(Request.Method.POST, upload_main_student_image_server_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // Toast.makeText(MainActivity.this,"Response:"+ response,Toast.LENGTH_SHORT).show();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String data = obj.getString("response");
                            if(data.equals("success"))
                            {
                                Toast.makeText(getContext(),"Profile Image Uploaded Successfully to Database" + data,Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(),"Error:" + data,Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Log.e("Upload JSONException ", String.valueOf(e));
                            e.printStackTrace();
                            displayExceptionMessage3(e.getMessage());
                        }
                    }//onResponse End
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("Upload Volley error", String.valueOf(error));
                        error.printStackTrace();
                        displayExceptionMessage4(error.getMessage());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("insert","photo");
                params.put("student_id",student_id);
                params.put("student_name",student_name);
                params.put("student_image",Encoded_images);
                params.put("doc_type","StudentsImage");
                params.put("type",type);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
        //----------------------------------------------------------------------------------------------------------------------//

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1)
        {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        }
        else
        {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }//getResizedBitmap() method end



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void displayExceptionMessage1(String msg) {
        Toast.makeText(getContext(), "Server Error ", Toast.LENGTH_SHORT).show();
    }
    public void displayExceptionMessage2(String msg) {
        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
    }

    public void displayExceptionMessage3(String msg)
    {
        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
    }

    private void displayExceptionMessage4(String message)
    {
        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
    }
}