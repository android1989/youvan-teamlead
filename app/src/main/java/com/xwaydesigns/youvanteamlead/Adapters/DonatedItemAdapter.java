package com.xwaydesigns.youvanteamlead.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.xwaydesigns.youvanteamlead.DonatedItemActivity;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.MainActivity;
import com.xwaydesigns.youvanteamlead.Model.AcceptDonation;
import com.xwaydesigns.youvanteamlead.Model.DonatedItems;
import com.xwaydesigns.youvanteamlead.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonatedItemAdapter extends RecyclerView.Adapter<DonatedItemAdapter.DonatedItemViewHolder>
{
    Context ctx;
    List<DonatedItems> data;
    String student_id;
    private String image_url;
    ArrayList<String> name_list = new ArrayList<>();
    ArrayList<String> donation_id_list = new ArrayList<>();
    ArrayList<String> item_id_list = new ArrayList<>();
    ArrayList<String> item_quantity_list = new ArrayList<>();
    String[] item_quantity_array;

    public DonatedItemAdapter(Context ctx, List<DonatedItems> data,String student_id, String[] item_quantity_array)
    {
        this.ctx =ctx;
        this.data = data;
        this.student_id = student_id;
        this.item_quantity_array = item_quantity_array;
    }

    @NonNull
    @Override
    public DonatedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row_donated_item, parent, false);
        return new DonatedItemViewHolder(view,ctx,item_quantity_array);
    }

    @Override
    public void onBindViewHolder(@NonNull final DonatedItemViewHolder holder, int position)
    {
        DonatedItems obj = data.get(position);
        holder.holder_data = data;
        holder.name.setText(obj.getItem_name());
        holder.quantity.setText(obj.getItem_quantity());
        image_url = Constants.BASE_URL+"youvan/images/"+student_id+"/Items Image/"+obj.getDonation_id()+"/"+obj.getItem_image();
        Picasso.get().load(image_url).resize(70,70).centerCrop().placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile).into(holder.image);

        donation_id_list.add(position,obj.getDonation_id());
        holder.holder_donation_id_list = (ArrayList<String>) donation_id_list.clone();

        name_list.add(position,obj.getItem_name());
        holder.holder_name_list = (ArrayList<String>) name_list.clone();

        item_id_list.add(position,obj.getItem_id());
        holder.holder_item_id_list = (ArrayList<String>) item_id_list.clone();

        item_quantity_list.add(position,obj.getItem_quantity());
        holder.holder_item_quantity_list = (ArrayList<String>) item_quantity_list.clone();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public  class DonatedItemViewHolder extends RecyclerView.ViewHolder
    {
        View mview;
        CircleImageView image;
        TextView name;
        TextView quantity;
        Button accept_btn;
        Button reject_btn;
        Context ctx;
        private String add_donated_items_server_url = Constants.BASE_URL+"youvan/add_donated_items.php";
        private String reject_donated_items_server_url = Constants.BASE_URL+"youvan/reject_donated_items.php";
        private String donation_id;
        private String item_name;
        private String item_id;
        private String actual_item_quantity;
        private String received_item_quantity;
        ArrayList<String> holder_donation_id_list = new ArrayList<>();
        ArrayList<String> holder_name_list = new ArrayList<>();
        ArrayList<String> holder_item_id_list = new ArrayList<>();
        ArrayList<String> holder_item_quantity_list = new ArrayList<>();
        ArrayList<String> holder_item_quantity_list_array = new ArrayList<>();
        String[] holder_item_quantity_array;
        List<DonatedItems> holder_data = new ArrayList<DonatedItems>();

        public DonatedItemViewHolder(@NonNull View itemView, final Context ctx, final String[] holder_item_quantity_array)
        {
            super(itemView);
            mview = itemView;
            this.ctx = ctx;
            this.holder_item_quantity_array = holder_item_quantity_array;
            image = mview.findViewById(R.id.single_row_donated_item_image);
            name = mview.findViewById(R.id.single_row_donated_item_name);
            quantity = mview.findViewById(R.id.single_row_donated_item_quantity);
            accept_btn = mview.findViewById(R.id.single_row_donated_item_accept_btn);
            reject_btn = mview.findViewById(R.id.single_row_donated_item_reject_btn);
            holder_item_quantity_list_array.addAll(Arrays.asList(holder_item_quantity_array));
//-----------------------------------------------------------------------------------------------------\\
            accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                   // Toast.makeText(ctx, "The position is: "+ getAdapterPosition() + "Clicked", Toast.LENGTH_SHORT).show();
                    donation_id = holder_donation_id_list.get(getAdapterPosition());
                    item_name = holder_name_list.get(getAdapterPosition());
                    item_id = holder_item_id_list.get(getAdapterPosition());
                    actual_item_quantity = holder_item_quantity_list.get(getAdapterPosition());
                    LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    //  LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view1 = inflater.inflate(R.layout.accept_dialog_layout,null,false);
                    final TextView row_item_name = view1.findViewById(R.id.accept_dialog_item_name);
                    final Spinner quantity = view1.findViewById(R.id.accept_dialog_item_quantity);
                    row_item_name.setText(item_name);

                    //spinner code
                   // ArrayList<String> sublist = new ArrayList<>();
                    int item_counter = Integer.parseInt(actual_item_quantity);
                    item_counter = item_counter + 1;
                    List<String> sublist =  holder_item_quantity_list_array.subList(0, item_counter);
                    quantity.setAdapter(new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_dropdown_item,sublist));

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
                    alertDialog.setTitle("Accepting Donation");
                    alertDialog.setView(view1);
                    alertDialog.setMessage("Please Select the Item Quantity ");
                    alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                             received_item_quantity =  quantity.getSelectedItem().toString();
      //----------------------------------------------------------------------------------------------------------------\\
                            StringRequest request = new StringRequest(Request.Method.POST,add_donated_items_server_url,
                                    new Response.Listener<String>()
                                    {
                                        @Override
                                        public void onResponse(String response)
                                        {
                                            JSONObject obj = null;
                                            try {
                                                obj = new JSONObject(response);
                                                String rec_data = obj.getString("response");
                                                if(rec_data.equals("success"))
                                                {
                                                    Toast.makeText(ctx,"Congratulations, Donor Item Accepted and Details uploaded to Server",Toast.LENGTH_LONG).show();
                                                    data.remove(getAdapterPosition());
                                                    mview.setVisibility(View.GONE);
                                                    notifyDataSetChanged();
                                                    Intent intent = new Intent(ctx, MainActivity.class);
                                                    ctx.startActivity(intent);

                                                }
                                                else
                                                {
                                                    Toast.makeText(ctx,"Sorry, Donor Item Accepted and Details Couldn't Uploaded to Server",Toast.LENGTH_LONG).show();
                                                }

                                            }
                                            catch (JSONException e)
                                            {
                                                Log.e("fetch JSONException ", String.valueOf(e));
                                                e.printStackTrace();
                                                displayExceptionMessage(e.getMessage());
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
                                            displayExceptionMessage(error.getMessage());
                                        }
                                    }
                            )
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError
                                {
                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put("insert","donated");
                                    params.put("donation_id",donation_id);
                                    params.put("item_id",item_id);
                                    params.put("actual_item_quantity",actual_item_quantity);
                                    params.put("received_item_quantity",received_item_quantity);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(ctx);
                            requestQueue.add(request);
      //-----------------------------------------------------------------------------------------------------------------\\
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // DO SOMETHING HERE
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                    //////////////////////
                }
            });
  //-------------------------------------------------------------------------------------------------------\\

            reject_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    donation_id = holder_donation_id_list.get(getAdapterPosition());
                    item_name = holder_name_list.get(getAdapterPosition());
                    item_id = holder_item_id_list.get(getAdapterPosition());
                   // Toast.makeText(ctx, "This Item Is Rejected"+ getAdapterPosition() + "Rejected", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
                    alertDialog.setTitle("Rejected Donation");
                   // alertDialog.setView(view1);
                    alertDialog.setMessage("Are you sure, Do You Want to Reject item?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //----------------------------------------------------------------------------------------------------------------\\
                            StringRequest request = new StringRequest(Request.Method.POST,reject_donated_items_server_url,
                                    new Response.Listener<String>()
                                    {
                                        @Override
                                        public void onResponse(String response)
                                        {
                                            JSONObject obj = null;
                                            try {
                                                obj = new JSONObject(response);
                                                String rec_data = obj.getString("response");
                                                if(rec_data.equals("success"))
                                                {
                                                    Toast.makeText(ctx,"Donor Item Rejected SuccesFully",Toast.LENGTH_LONG).show();
                                                    data.remove(getAdapterPosition());
                                                    mview.setVisibility(View.GONE);
                                                    notifyDataSetChanged();
                                                    //call main intent
                                                    Intent intent = new Intent(ctx, MainActivity.class);
                                                    ctx.startActivity(intent);

                                                }
                                                else
                                                {
                                                    Toast.makeText(ctx,"Sorry, Donor Item Could not Rejected by the Server",Toast.LENGTH_LONG).show();
                                                }

                                            }
                                            catch (JSONException e)
                                            {
                                                Log.e("fetch JSONException ", String.valueOf(e));
                                                e.printStackTrace();
                                                displayExceptionMessage(e.getMessage());
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
                                            displayExceptionMessage(error.getMessage());
                                        }
                                    }
                            )
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError
                                {
                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put("reject","donations");
                                    params.put("donation_id",donation_id);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(ctx);
                            requestQueue.add(request);
                            //-----------------------------------------------------------------------------------------------------------------\\
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // DO SOMETHING HERE
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                    //////////////////////
                }
            });

        }



    }

    public void displayExceptionMessage(String msg) {
        Toast.makeText(ctx, "Server Error", Toast.LENGTH_SHORT).show();
    }


}
