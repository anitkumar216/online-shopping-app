package com.example.turbo.yourshop.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.turbo.yourshop.Interface.ItemClickListener;
import com.example.turbo.yourshop.R;

/**
 * Created by Vineet Choudhary on 4/8/2018.
 */

public class ItemListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView itemName;
    public ImageView imageItem;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ItemListViewHolder(View itemView) {
        super(itemView);

        itemName = itemView.findViewById(R.id.listItem_name);
        imageItem = itemView.findViewById(R.id.listItem_image);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
