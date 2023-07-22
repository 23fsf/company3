package com.ahmed22.company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ThirdActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Listitems> listitems;
    private RecyclerView.Adapter adapter;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        //
        db=FirebaseFirestore.getInstance();
        //
        recyclerView=findViewById(R.id.recyclerviewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listitems=new ArrayList<>();
        //
        db.collection("Company")
                .document("cmp")
                .collection("emp")
                .get()
                .addOnCompleteListener(task -> {
                    QuerySnapshot querySnapshot=task.getResult();
                    for(DocumentSnapshot documentSnapshot:querySnapshot.getDocuments()){
                        listitems.add(new Listitems(documentSnapshot.getString("firstName")
                                ,documentSnapshot.getString("age")
                                ,documentSnapshot.getString("imageUri"))
                        );
                    }
                    setData(listitems);
        });
        //
//        for (int i = 0; i < 10; i++) {
//            Listitems listitem=new Listitems("ahmed"+(1+i),"22");
//            listitems.add(listitem);
//        }
    }
    private void setData(List<Listitems> listitems) {
        adapter=new MyAdapter(this, this.listitems);
        recyclerView.setAdapter(adapter);
    }

}