package com.example.projectbit.Wasmachine;


import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectbit.R;
import com.example.projectbit.addWasmachine;

import java.util.List;

public class WasmachineAdapter extends RecyclerView.Adapter<WasmachineAdapter.ViewHolder> {

    private Context context;
    private List<Wasmachine> list;

    public WasmachineAdapter(Context context, List<Wasmachine> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wasmachine_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.edtWasmachineNaam.setText(list.get(position).getName());
        holder.edtType.setText(list.get(position).getType());
        holder.edtMaxGewicht.setText("" + list.get(position).getMaxCapacity() + " KG");

        holder.saveToUser.setOnClickListener(view -> {
            Intent intent = new Intent(context, addWasmachine.class);
            intent.putExtra("detail", list.get(position));
            intent.putExtra("ID", list.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView edtWasmachineNaam, edtType, edtMaxGewicht;
        ImageButton saveToUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            edtWasmachineNaam = itemView.findViewById(R.id.txtWasmachineName);
            edtType = itemView.findViewById(R.id.txtType);
            edtMaxGewicht = itemView.findViewById(R.id.txtMaxCapacity);
            saveToUser = itemView.findViewById(R.id.btnSaveToUser);
        }
    }
}
