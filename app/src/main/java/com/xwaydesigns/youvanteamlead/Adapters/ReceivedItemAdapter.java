package com.xwaydesigns.youvanteamlead.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.Model.DonatedItems;
import com.xwaydesigns.youvanteamlead.Model.ReceivedItems;
import com.xwaydesigns.youvanteamlead.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReceivedItemAdapter extends RecyclerView.Adapter<ReceivedItemAdapter.ReceivedItemViewHolder>
{
    Context ctx;
    List<ReceivedItems> data;
    private String image_url;

    public ReceivedItemAdapter(Context ctx, List<ReceivedItems> data)
    {
        this.ctx =ctx;
        this.data = data;
    }
    @NonNull
    @Override
    public ReceivedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row_received_item, parent, false);
        return new ReceivedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedItemViewHolder holder, int position)
    {
        ReceivedItems obj = data.get(position);
        holder.name.setText(obj.getItem_name());
        holder.quantity.setText(obj.getItem_quantity());
        image_url = Constants.BASE_URL+"youvan/images/"+obj.getStudent_id()+"/Items Image/"+obj.getDonation_id()+"/"+obj.getItem_image();
        Picasso.get().load(image_url).resize(70,70).centerCrop().placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ReceivedItemViewHolder extends RecyclerView.ViewHolder
    {
        View mview;
        CircleImageView image;
        TextView name;
        TextView quantity;

        public ReceivedItemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mview = itemView;
            image = mview.findViewById(R.id.single_row_received_item_image);
            name = mview.findViewById(R.id.single_row_received_item_name);
            quantity = mview.findViewById(R.id.single_row_received_item_quantity);
        }
    }

}
