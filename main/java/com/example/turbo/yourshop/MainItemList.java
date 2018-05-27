package com.example.turbo.yourshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.turbo.yourshop.Common.Common;
import com.example.turbo.yourshop.Interface.ItemClickListener;
import com.example.turbo.yourshop.Model.MainItem;
import com.example.turbo.yourshop.ViewHolder.MainItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainItemList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference itemList;

    String categoryId="";

    FirebaseRecyclerAdapter<MainItem, MainItemViewHolder> adapterMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        itemList = firebaseDatabase.getReference("MainItem");
        recyclerView = findViewById(R.id.mainItem);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null) {
            if (Common.isConnectedToInternet(getBaseContext())) {
                loadListItem(categoryId);
            }
            else {
                Toast.makeText(MainItemList.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void loadListItem(String categoryId) {

        adapterMain = new FirebaseRecyclerAdapter<MainItem, MainItemViewHolder>(MainItem.class,
                R.layout.mainitem_view,
                MainItemViewHolder.class,
                itemList.orderByChild("MenuId").equalTo(categoryId)
                //itemList
        ) {
            @Override
            protected void populateViewHolder(MainItemViewHolder viewHolder, MainItem model, int position) {

                viewHolder.txtItemName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageViewItem);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intentItem = new Intent(MainItemList.this,ItemList.class);
                        intentItem.putExtra("MyMenuId",adapterMain.getRef(position).getKey());
                        startActivity(intentItem);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapterMain);

    }

}
