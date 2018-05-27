package com.example.turbo.yourshop;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.turbo.yourshop.Common.Common;
import com.example.turbo.yourshop.Database.Database;
import com.example.turbo.yourshop.Model.ItemMenu;
import com.example.turbo.yourshop.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItemDetail extends AppCompatActivity {

    TextView item_name,item_price,item_description;
    ImageView item_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String ItemId = "";

    FirebaseDatabase database;
    DatabaseReference items;

    ItemMenu currentItemMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        database = FirebaseDatabase.getInstance();
        items = database.getReference("Item");

        numberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ItemDetail.this,"Check error",Toast.LENGTH_SHORT).show();
                new Database(getBaseContext()).addToCart(new Order(
                        ItemId,
                        currentItemMenu.getName(),
                        numberButton.getNumber(),
                        currentItemMenu.getPrice(),
                        currentItemMenu.getDiscount()
                ));
                Toast.makeText(ItemDetail.this,"Added to Cart",Toast.LENGTH_SHORT).show();
            }
        });

        item_description = findViewById(R.id.item_description);
        item_name = findViewById(R.id.item_name);
        item_price = findViewById(R.id.item_price);
        item_image = findViewById(R.id.img_item);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if (getIntent() != null){
            ItemId = getIntent().getStringExtra("ItemId");
        }
        if (!ItemId.isEmpty()){
            if (Common.isConnectedToInternet(getBaseContext())) {
                getDetailItem(ItemId);
            }
            else {
                Toast.makeText(ItemDetail.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getDetailItem(String itemId) {

        items.child(ItemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentItemMenu = dataSnapshot.getValue(ItemMenu.class);
                Picasso.with(getBaseContext()).load(currentItemMenu.getImage()).into(item_image);
                collapsingToolbarLayout.setTitle(currentItemMenu.getName());
                item_price.setText(currentItemMenu.getPrice());
                item_name.setText(currentItemMenu.getName());
                item_description.setText(currentItemMenu.getDescription());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
