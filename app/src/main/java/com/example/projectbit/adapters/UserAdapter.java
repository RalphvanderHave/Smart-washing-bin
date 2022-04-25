package com.example.projectbit.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectbit.Models.User;
import com.example.projectbit.ProfileActivity;
import com.example.projectbit.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> list;

    public UserAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.edtNaam.setText(list.get(position).getNaam());
        holder.edtAdres.setText(list.get(position).getAdres());
        holder.edtPhone.setText(list.get(position).getPhoneNumber());
        holder.edtEmail.setText(list.get(position).getEmail());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("User", list.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText edtNaam, edtEmail, edtPhone, edtAdres;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            edtNaam = itemView.findViewById(R.id.edtNaam);
            edtEmail = itemView.findViewById(R.id.edtEmailLogin);
            edtPhone = itemView.findViewById(R.id.edtPhoneNumber);
            edtAdres = itemView.findViewById(R.id.edtAdres);
        }
    }
}
