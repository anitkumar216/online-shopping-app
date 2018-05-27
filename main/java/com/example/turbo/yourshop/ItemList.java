package com.example.turbo.yourshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.turbo.yourshop.Common.Common;
import com.example.turbo.yourshop.Interface.ItemClickListener;
import com.example.turbo.yourshop.Model.ItemMenu;
import com.example.turbo.yourshop.ViewHolder.ItemListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceItem;

    String menuId="";

    FirebaseRecyclerAdapter<ItemMenu,ItemListViewHolder> adapter;

    FirebaseRecyclerAdapter<ItemMenu,ItemListViewHolder> searchAdapter;

    List<String> suggestList = new ArrayList<>();

    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceItem = firebaseDatabase.getReference("Item");

        recyclerView = findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null)
            menuId = getIntent().getStringExtra("MyMenuId");
        if (!menuId.isEmpty() && menuId != null){
            if (Common.isConnectedToInternet(getBaseContext())) {
                loadItemList(menuId);
            }
            else {
                Toast.makeText(ItemList.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        materialSearchBar = findViewById(R.id.searchBar);
        //materialSearchBar.setHint("Search Item...");
        //materialSearchBar.setSpeechMode(false);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> suggest = new ArrayList<String>();
                for (String search:suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<ItemMenu, ItemListViewHolder>(
                ItemMenu.class,
                R.layout.item_list_view,
                ItemListViewHolder.class,
                databaseReferenceItem.orderByChild("Name").equalTo(text.toString())


        ) {
            @Override
            protected void populateViewHolder(ItemListViewHolder viewHolder, ItemMenu model, int position) {

                viewHolder.itemName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageItem);

                final ItemMenu local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intentItem = new Intent(ItemList.this,ItemDetail.class);
                        intentItem.putExtra("ItemId",adapter.getRef(position).getKey());
                        startActivity(intentItem);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        databaseReferenceItem.orderByChild("MenuId").equalTo(menuId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                            ItemMenu itemMenu = postSnapshot.getValue(ItemMenu.class);
                            suggestList.add(itemMenu.getName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadItemList(String menuId) {
        adapter = new FirebaseRecyclerAdapter<ItemMenu, ItemListViewHolder>(ItemMenu.class,
                R.layout.item_list_view,
                ItemListViewHolder.class,
                databaseReferenceItem.orderByChild("MenuId").equalTo(menuId)) {
            @Override
            protected void populateViewHolder(ItemListViewHolder viewHolder, ItemMenu model, int position) {
                viewHolder.itemName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageItem);

                final ItemMenu local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intentItem = new Intent(ItemList.this,ItemDetail.class);
                        intentItem.putExtra("ItemId",adapter.getRef(position).getKey());
                        startActivity(intentItem);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

    }
}
