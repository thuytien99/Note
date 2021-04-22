package com.leanhquan.ccpm.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leanhquan.notemanagementsystem.Common.Common;
import com.leanhquan.notemanagementsystem.R;

public class StatusViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public TextView txtNameStatus, txtDateCreateStatus;

    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);
        txtNameStatus = itemView.findViewById(R.id.txtNameStatus);
        txtDateCreateStatus = itemView.findViewById(R.id.txtDateCreateStatus);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}
