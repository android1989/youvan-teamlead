package com.xwaydesigns.youvanteamlead.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.Model.PendingItems;
import com.xwaydesigns.youvanteamlead.Model.ReceivedItems;
import com.xwaydesigns.youvanteamlead.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingItemAdapter extends RecyclerView.Adapter<PendingItemAdapter.PendingItemViewHolder>
{
    Context ctx;
    List<PendingItems> data;
    ArrayList<String> list = new ArrayList<>();
    private String image_url;

    public PendingItemAdapter(Context ctx,List<PendingItems> data)
    {
        this.ctx =ctx;
        this.data = data;
    }

    @NonNull
    @Override
    public PendingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row_pending_item, parent, false);
        return new PendingItemViewHolder(view,ctx);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingItemViewHolder holder, int position)
    {
        PendingItems obj = data.get(position);
        holder.item_name.setText(obj.getItem_name());
        holder.item_quantity.setText(obj.getItem_quantity());
        holder.student_name.setText(obj.getStudent_name());
      //  holder.mobile.setText(obj.getMobile());
        image_url = Constants.BASE_URL+"youvan/images/"+obj.getStudent_id()+"/Items Image/"+obj.getDonation_id()+"/"+obj.getItem_image();
        Picasso.get().load(image_url).resize(70,70).centerCrop().placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile).into(holder.image);
        list.add(position,obj.getMobile());
        holder.holder_list = (ArrayList<String>) list.clone();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class PendingItemViewHolder extends RecyclerView.ViewHolder
    {
        View mview;
        CircleImageView image;
        TextView item_name;
        TextView item_quantity;
        TextView student_name;
        TextView mobile;
        Context ctx;
        ArrayList<String> holder_list = new ArrayList<>();

        public PendingItemViewHolder(@NonNull View itemView, final Context ctx)
        {
            super(itemView);
            mview = itemView;
            this.ctx = ctx;
            image = mview.findViewById(R.id.single_row_pending_item_image);
            item_name = mview.findViewById(R.id.single_row_pending_item_name);
            item_quantity = mview.findViewById(R.id.single_row_pending_item_quantity);
            student_name = mview.findViewById(R.id.single_row_pending_item_student_name);
            mobile = mview.findViewById(R.id.pending_mobile);

            mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+ holder_list.get(getAdapterPosition())));
                    ctx.startActivity(intent);
                }
            });
        }

    }

}
