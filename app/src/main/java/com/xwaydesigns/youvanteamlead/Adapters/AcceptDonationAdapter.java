package com.xwaydesigns.youvanteamlead.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xwaydesigns.youvanteamlead.AcceptDonationActivity;
import com.xwaydesigns.youvanteamlead.DonatedItemActivity;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.Model.AcceptDonation;
import com.xwaydesigns.youvanteamlead.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptDonationAdapter extends RecyclerView.Adapter<AcceptDonationAdapter.AcceptDonationViewHolder>
{
    Context ctx;
    List<AcceptDonation> data;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> mobile_list = new ArrayList<>();
    String student_id;
    private String image_url;



    public AcceptDonationAdapter(Context ctx,List<AcceptDonation> data,String student_id )
    {
        this.ctx =ctx;
        this.data =data;
        this.student_id =student_id;

    }

    @NonNull
    @Override
    public AcceptDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row_accept_donation, parent, false);
        return new AcceptDonationViewHolder(view,ctx);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptDonationViewHolder holder, int position)
    {
        final AcceptDonation obj = data.get(position);
        holder.name.setText(obj.getStudent_name());
        holder.standard.setText(obj.getStudent_class());
       // holder.mobile.setText(obj.getMobile());
        holder.quantity.setText(obj.getItem_quantity());
        image_url = Constants.BASE_URL+"youvan/images/"+student_id+"/StudentsImage/"+obj.getStudent_image();
        Picasso.get().load(image_url).resize(70,70).centerCrop().placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile).into(holder.image);

        list.add(position,obj.getStudent_id());
        holder.holder_list = (ArrayList<String>) list.clone();

        mobile_list.add(position,obj.getMobile());
        holder.holder_mobile_list = (ArrayList<String>) mobile_list.clone();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AcceptDonationViewHolder extends RecyclerView.ViewHolder
    {
        View mview;
        Context ctx;
        CircleImageView image;
        TextView name;
        TextView standard;
        TextView mobile;
        TextView quantity;
        ArrayList<String> holder_list = new ArrayList<>();
        ArrayList<String> holder_mobile_list = new ArrayList<>();

        public AcceptDonationViewHolder(@NonNull View itemView, final Context ctx)
        {
            super(itemView);
            mview = itemView;
            this.ctx = ctx;
            name = mview.findViewById(R.id.single_row_accept_donation_name);
            image = mview.findViewById(R.id.single_row_accept_donation_image);
            standard = mview.findViewById(R.id.single_row_accept_donation_standard);
            mobile = mview.findViewById(R.id.accept_mobile);
            quantity = mview.findViewById(R.id.accept_item_quantity);

            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(ctx, DonatedItemActivity.class);
                    intent.putExtra("student_id",holder_list.get(getAdapterPosition()));
                    ctx.startActivity(intent);
                }
            });

            mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(ctx,"mobile:"+holder_mobile_list.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+ holder_mobile_list.get(getAdapterPosition())));
                    ctx.startActivity(intent);
                }
            });
        }

    }

}
