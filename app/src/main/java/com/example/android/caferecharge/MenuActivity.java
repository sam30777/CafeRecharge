package com.example.android.caferecharge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cafedroid.android.caferecharge.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements MenuAdapter.ListItemInterface  {
    GridView foodGrid;
    TextView cartView;
    Button buyButton;
    Double totalBill;
    DatabaseReference mRef;
    Double cardValue;
    ArrayList<FoodItem> orderedItems;
    String cardNo;
    DatabaseReference databaseReference;
    DatabaseReference sectionNameRef;
    DatabaseReference menuItemRef;
    ArrayList<FoodItem> foodItems;
    boolean cardExists;
    private static LinearLayout linearLayout;
    private static TextView card_total;
    private MenuAdapter menuAdapter;
    public static Boolean firstRead=false;
    ChildEventListener childEventListener;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        linearLayout=findViewById(R.id.cart_summary);
        card_total=findViewById(R.id.cart_total);
        final String section_name = intent.getStringExtra(getString(R.string.section_name));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sectionNameRef=FirebaseDatabase.getInstance().getReference().child(getString(R.string.sections_card)).child(section_name);
        sectionNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(getString(R.string.menu_item))){
                    readData(section_name);
                    Toast.makeText(MenuActivity.this,"has child", Toast.LENGTH_SHORT).show();
                    sectionNameRef.removeEventListener(this);
                }else{
                    Toast.makeText(MenuActivity.this,"does'nt has child", Toast.LENGTH_SHORT).show();
                    writeData(section_name);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void writeData(String section) {
        menuItemRef=FirebaseDatabase.getInstance().getReference().child(getString(R.string.sections_card)).child(section).child(getString(R.string.menu_item));
         orderedItems=new ArrayList<>();
        if(section.equals(getString(R.string.non_veg))){
             getNonVegMenu(section);
        }else if(section.equals(getString(R.string.new_arrive))){
           getNewArrives(section);
        }else if(section.equals(getString(R.string.drinks))){
            getDrinksMenu(section);
        }else if(section.equals(getString(R.string.veg_food))){
            getVegMenu(section);
        }else if(section.equals(getString(R.string.breads))){
            getBreadsMenu(section);
        }else if(section.equals(getString(R.string.combos))){
            getComboMenu(section);
        }





        for (int i = 0; i < orderedItems.size(); i++) {
            FoodItem foodItem = orderedItems.get(i);
           menuItemRef.child(foodItem.getName()).setValue(foodItem);
        }

        }

    private void readData(String section){
        menuItemRef=FirebaseDatabase.getInstance().getReference().child(getString(R.string.sections_card)).child(section).child(getString(R.string.menu_item));
        orderedItems=new ArrayList<>();
        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RecyclerView recyclerView=findViewById(R.id.food_list);
                orderedItems.add(dataSnapshot.getValue(FoodItem.class));
                menuAdapter=new MenuAdapter(MenuActivity.this,orderedItems,MenuActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(menuAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    menuItemRef.addChildEventListener(childEventListener);

    }

    public static void AddTextView(final Context context){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference cart_tot=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(context.getString(R.string.student_Cart))
                .child(context.getString(R.string.cart_total));

        cart_tot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                linearLayout.setVisibility(View.VISIBLE);
                card_total.setText(dataSnapshot.getValue(String.class));
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context,CartActivity.class));
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void getComboMenu(String section){
        orderedItems.add(new FoodItem("Executive Thali","55","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/combos%2Fexecutivethali.png?alt=media&token=107e33b6-a581-4b8b-9ff0-1b744db12b92",false,section));
        orderedItems.add(new FoodItem("Chiken Combo","120","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/combos%2Fchikencombo.png?alt=media&token=d1128046-1908-419a-bcb0-d66ef2ff351e",false,section));
        orderedItems.add(new FoodItem("Naan Combo","50","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/combos%2Fnaancombo.png?alt=media&token=1d0f57b3-897d-45a5-bed9-d34bd0fe7243",false,section));
        orderedItems.add(new FoodItem("Paneer Combo","70","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/combos%2Fnaancombo.png?alt=media&token=1d0f57b3-897d-45a5-bed9-d34bd0fe7243",false,section));
        orderedItems.add(new FoodItem("Dal Makhani Combo","40","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/combos%2Fdaalmakhanicombo.png?alt=media&token=05bf9d55-3940-4434-8a7b-9a420d07a97f",false,section));
    }
    private void getBreadsMenu(String section){
        orderedItems.add(new FoodItem("Plain Naan","10","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Breads%2Fplainnaan.png?alt=media&token=1c5fba46-e3e3-4ff4-9e19-e5c30135afde",false,section));
        orderedItems.add(new FoodItem("Butter Naan","15","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Breads%2Fbutternaan.png?alt=media&token=7fb381a1-9b60-4b54-93d2-cb04c9ea2ccc",false,section));
        orderedItems.add(new FoodItem("Amritsari Naan","15","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Breads%2Famritsarinaan.png?alt=media&token=8c3c063b-9f0c-4dd6-811f-beb6fe6b4c50",false,section));
        orderedItems.add(new FoodItem("Lacha Paratha ","10","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Breads%2Flachaparatha.png?alt=media&token=8780a267-dce5-4f50-88a0-283341ac0995",false,section));
        orderedItems.add(new FoodItem("Roti ","05","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Breads%2Froti.png?alt=media&token=d018e387-84db-4a02-96ea-9f38134c90e0",false,section));
        orderedItems.add(new FoodItem("Aloo Paratha","30","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Breads%2Falooparatha.png?alt=media&token=4a057dbe-abe4-432d-9550-f8be6c661f7f",false,section));
        orderedItems.add(new FoodItem("Paneer Paratha","40","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Breads%2Fpaneerparatha.png?alt=media&token=75cba504-d713-4b6f-abf1-b01f4189c7fd",false,section));
        orderedItems.add(new FoodItem("Gobhi Paratha","30","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Breads%2Fgobhiparatha.png?alt=media&token=a41f1f9b-376a-4e93-89c0-913674ead1b9",false,section));

    }
    private void getVegMenu(String section){
        orderedItems.add(new FoodItem("Rice","10","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Veg%2Frice.jpg?alt=media&token=809687bf-922e-41d6-96ff-7de1cddfdba6",false,section));
        orderedItems.add(new FoodItem("Paneer Lababdar","50","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Veg%2Fpaneerlababdar.png?alt=media&token=7eab719e-a16c-4d44-ac97-57c8fc649435",false,section));
        orderedItems.add(new FoodItem("Mix Veg","25","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Veg%2Fmixveg.png?alt=media&token=be887cd3-f6fc-4286-9e9f-b03c6c78209d",false,section));
        orderedItems.add(new FoodItem("Chana Masala","25","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Veg%2Fchanamasala.png?alt=media&token=76a678bf-6a27-46a1-a1d9-4ec4657fb020",false,section));
        orderedItems.add(new FoodItem("Daal Makhani","25","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Veg%2Fdaalmakhani.png?alt=media&token=22aa6f71-ed79-4f56-824a-25d26ccc217e",false,section));
        orderedItems.add(new FoodItem("Pav Bhaji","40","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Veg%2Fpvbhaji.png?alt=media&token=06bd0eac-8478-4d08-8635-5fde6ffa2f1f",false,section));
        orderedItems.add(new FoodItem("Chole Bhature","50","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Veg%2Fcholebhature.png?alt=media&token=072bf2aa-4e8e-4666-9100-f6b73692979b",false,section));
        orderedItems.add(new FoodItem("Maggi","15","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Veg%2Fmggi.png?alt=media&token=a59a4dca-cdac-4b99-9e8a-8379198580fe",false,section));
        orderedItems.add(new FoodItem("Curd","10","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Veg%2Fcurd.png?alt=media&token=557f4937-3194-405e-ae9d-b15682b942d1",false,section));
        orderedItems.add(new FoodItem("Veg Khati Roll","30","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Veg%2Fvegkhati.png?alt=media&token=839e1998-ba80-41d0-92e3-33d15912a4de",false,section));
    }
    private void getDrinksMenu(String section){
        orderedItems.add(new FoodItem("Milk Packet","25","150","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks%2Fmilkpacket.png?alt=media&token=b9405dfc-37e0-455d-8d57-bbfc36244a99",false,section));
        orderedItems.add(new FoodItem("Soya Milk Packet","25","150","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks%2Fsoyamilkpacket.png?alt=media&token=236a29cb-83bc-46db-84da-e14cbf29a8a0",false,section));
        orderedItems.add(new FoodItem("Cold Drinks","35","150","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks%2Fcolddrinks.png?alt=media&token=a1d61e62-9882-4d6e-8aa9-b42deee1ac45",false,section));
        orderedItems.add(new FoodItem("Real Juice","20","150","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks%2Frealjuice.png?alt=media&token=31a84a77-4bcc-49a9-8b5b-c851bf09f26f",false,section));
        orderedItems.add(new FoodItem("Fruit Beer","40","150","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks%2Ffruitbeer.png?alt=media&token=566a7a90-e6df-439c-933e-473188046249",false,section));
        orderedItems.add(new FoodItem("Flavoured Milk","25","150","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks%2Fflavoured.png?alt=media&token=e22eb31d-959f-4e9a-bf1d-ec10a7efc786",false,section));
        orderedItems.add(new FoodItem("Mineral Water","20","150","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks%2FMINERALWATER.PNG?alt=media&token=73df9e42-a66d-4cc3-a4ca-ce2d386b995e",false,section));
        orderedItems.add(new FoodItem("Patiala Lassi Sweet","20","150","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks%2Fpatiallassi.png?alt=media&token=0c781fad-d637-407a-beab-bd904c5f8b96",false,section));
        orderedItems.add(new FoodItem("Patiala Lassi Flavoured","25","150","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks%2Fpatiallaflavoured.png?alt=media&token=768765d5-e2ee-4145-a284-f0b993f4e216",false,section));
        orderedItems.add(new FoodItem("Tea ","10","150","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks%2Ftea.png?alt=media&token=dc960d37-14e9-40bb-92e5-4584400553a5",false,section));
        orderedItems.add(new FoodItem("Coffe ","10","150","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks%2Fcoffe.png?alt=media&token=70c5d8b5-13f6-4f71-8878-aaa84361830c",false,section));
    }
    private void getNewArrives(String section){
        orderedItems.add(new FoodItem("Aloo Tikki","35","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/new%2Falootikki.png?alt=media&token=c44f28b5-b184-430b-b228-316869e77e09",false,section));
        orderedItems.add(new FoodItem("Chilly Chicken","150","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/new%2Fchikenchilly.jpeg?alt=media&token=acb80535-b442-48df-a465-5b6a1a207280",false,section));
        orderedItems.add(new FoodItem("BhelPuri","20","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/new%2Fbhelpuri.png?alt=media&token=d11fb8ef-304f-4b38-9131-3c55a615f5eb",false,section));
        orderedItems.add(new FoodItem("DahiBhale","35","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/new%2Fdahibhalle.png?alt=media&token=94cbd3c3-d580-451b-ae3d-7dc9857485ac",false,section));
        orderedItems.add(new FoodItem("Gool Gappe","30","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/new%2Fgoolgappe.png?alt=media&token=24ced596-34df-428c-9d94-5a5801bb6325",false,section));
        orderedItems.add(new FoodItem("Fresh Juice ","45","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/new%2FfreshJuice.png?alt=media&token=772011f8-723d-4e27-9779-893975b29f58",false,section));
        orderedItems.add(new FoodItem("RedPasta","50","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/new%2Fredpasta.png?alt=media&token=2bb1bb65-abb2-4d5f-90e2-c848b86697f4",false,section));
        orderedItems.add(new FoodItem("White Pasta","50","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/new%2Fwhitepasta.png?alt=media&token=ab596136-1c5a-4838-9095-c692c5606dd8",false,section));
    }
    private void getNonVegMenu(String section){
        orderedItems.add(new FoodItem("Egg Khati Roll","40","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Non%20veg%2Fomlets.png?alt=media&token=d0c64435-6429-41f6-b505-48790bd035df",false,section));
        orderedItems.add(new FoodItem("Chiken Khati Roll","55","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Non%20veg%2Feggrole.png?alt=media&token=28980027-46fd-4080-83e0-66511a156df1",false,section));
        orderedItems.add(new FoodItem("Omelettes","30","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Non%20veg%2Fomlets.png?alt=media&token=d0c64435-6429-41f6-b505-48790bd035df",false,section));
        orderedItems.add(new FoodItem("Boiled Egg","7.5","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Non%20veg%2Fboiledegg.png?alt=media&token=f3a90eff-e1bb-4366-aed5-38ef207d1e6c",false,section));
        orderedItems.add(new FoodItem("Butter Chiken Half","150","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Non%20veg%2Fbutterchiken.png?alt=media&token=0cb05c35-4277-46f2-82dc-46f22a520d66",false,section));
        orderedItems.add(new FoodItem("Chiken Seekh","40","100","https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Non%20veg%2Fchikenseekh.png?alt=media&token=e4d1c5b9-3a1c-4ca1-987c-76352fda0541",false,section));
    }

    @Override
    public void onItemClicked(int listitemindex) {
        FoodItem foodItem=orderedItems.get(listitemindex);
        Intent intent=new Intent(MenuActivity.this,ItemDetaills.class);
        intent.putExtra(getString(R.string.section_name),foodItem.getSection_name());
        intent.putExtra(getString(R.string.item_name),foodItem.getName());

        startActivity(intent);
    }
}

