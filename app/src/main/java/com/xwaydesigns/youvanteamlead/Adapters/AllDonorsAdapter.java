package com.xwaydesigns.youvanteamlead.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xwaydesigns.youvanteamlead.DonatedItemActivity;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.Model.AcceptDonation;
import com.xwaydesigns.youvanteamlead.Model.AllDonor;
import com.xwaydesigns.youvanteamlead.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllDonorsAdapter extends RecyclerView.Adapter<AllDonorsAdapter.AllDonorsViewHolder>
{
    Context ctx;
    List<AllDonor> data;
    ArrayList<String> list = new ArrayList<>();
    String student_id;
    private String image_url;

    public AllDonorsAdapter(Context ctx,List<AllDonor> data)
    {
        this.ctx =ctx;
        this.data =data;
    }

    @NonNull
    @Override
    public AllDonorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row_all_donors, parent, false);
        return new AllDonorsViewHolder(view, ctx);
    }

    @Override
    public void onBindViewHolder(@NonNull AllDonorsViewHolder holder, int position)
    {
        final AllDonor obj = data.get(position);
        holder.name.setText(obj.getStudent_name());
        holder.standard.setText(obj.getStudent_class());

        image_url = Constants.BASE_URL+"youvan/images/"+obj.getStudent_id()+"/StudentsImage/"+obj.getStudent_image();
        Picasso.get().load(image_url).resize(70,70).centerCrop().placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile).into(holder.image);
        list.add(position,obj.getStudent_id());
        holder.holder_list = (ArrayList<String>) list.clone();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AllDonorsViewHolder extends RecyclerView.ViewHolder
    {
        View mview;
        CircleImageView image;
        TextView name;
        TextView standard;
        ArrayList<String> holder_list = new ArrayList<>();
        Context ctx;

        public AllDonorsViewHolder(@NonNull View itemView, final Context ctx)
        {
            super(itemView);
            mview = itemView;
            this.ctx = ctx;
            image = mview.findViewById(R.id.single_row_all_donor_image);
            name = mview.findViewById(R.id.single_row_all_donor_name);
            standard = mview.findViewById(R.id.single_row_item_quantity);

            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(ctx, DonatedItemActivity.class);
                    intent.putExtra("student_id",holder_list.get(getAdapterPosition()));
                    ctx.startActivity(intent);
                }
            });
        }

    }

}
