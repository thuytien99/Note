package com.leanhquan.ccpm.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leanhquan.notemanagementsystem.Common.Common;
import com.leanhquan.notemanagementsystem.R;

public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public TextView txtNameNote,
                    txtCategoryNote,
                    txtPiorityNote,
                    txtStatusNote,
                    txtPlanDateNote,
                    txtDateCreatedNote;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        txtNameNote = itemView.findViewById(R.id.txtNameNote);
        txtCategoryNote = itemView.findViewById(R.id.txtCategoryNote);
        txtPiorityNote = itemView.findViewById(R.id.txtPiorityNote);
        txtStatusNote = itemView.findViewById(R.id.txtStatusNote);
        txtPlanDateNote = itemView.findViewById(R.id.txtPlanDateNote);
        txtDateCreatedNote = itemView.findViewById(R.id.txtDateCreateNote);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}
