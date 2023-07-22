package com.ahmed22.company;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private List<Listitems> listitems;

    public MyAdapter(Context context, List listitems){
        this.context=context;
        this.listitems=listitems;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        Listitems item=listitems.get(position);
        holder.name.setText(item.getName());
        holder.age.setText(item.getAge());
        Picasso.get().load(item.getImageUri()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView name,age;
        public ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name=itemView.findViewById(R.id.firstName);
            age=itemView.findViewById(R.id.age);
            image=itemView.findViewById(R.id.image);

        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            Listitems item=listitems.get(position);

            //Toast.makeText(context,item.getName(),Toast.LENGTH_LONG).show();

            Intent intent=new Intent(context,ForthActivity.class);
            intent.putExtra("name",item.getName());
            intent.putExtra("age",item.getAge());
            intent.putExtra("imageUri",item.getImageUri());
            context.startActivity(intent);
        }
    }
}
