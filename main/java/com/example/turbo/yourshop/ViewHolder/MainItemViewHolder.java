package com.example.turbo.yourshop.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.turbo.yourshop.Interface.ItemClickListener;
import com.example.turbo.yourshop.R;

/**
 * Created by Vineet Choudhary on 4/4/2018.
 */

public class MainItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtItemName;
    public ImageView imageViewItem;

    private ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MainItemViewHolder(View itemView) {
        super(itemView);

        txtItemName = itemView.findViewById(R.id.mainItem_name);
        imageViewItem = itemView.findViewById(R.id.mainItem_image);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
