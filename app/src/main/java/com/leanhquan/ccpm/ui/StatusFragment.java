package com.leanhquan.notemanagementsystem.UI;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.leanhquan.notemanagementsystem.Common.Common;
import com.leanhquan.notemanagementsystem.Model.Piority;
import com.leanhquan.notemanagementsystem.Model.Status;
import com.leanhquan.notemanagementsystem.R;
import com.leanhquan.notemanagementsystem.ViewHolder.PiorityViewHolder;
import com.leanhquan.notemanagementsystem.ViewHolder.StatusViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Date;

public class StatusFragment extends Fragment {

    private CounterFab fabAddStatus;
    private MaterialEditText edtNameNewStatus;
    private Button btnAdd, btnCancel;
    private FirebaseDatabase database;
    private DatabaseReference status;
    private Status newStatus;
    private RecyclerView recycleStatus;

    private FirebaseRecyclerAdapter<Status, StatusViewHolder> adapterStatuslist;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();
        status = database.getReference().child("status");

        recycleStatus = view.findViewById(R.id.recycler_status);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recycleStatus.setLayoutManager(manager);

        fabAddStatus = view.findViewById(R.id.fabStatus);

        fabAddStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddnewStatus();
            }
        });

        showListStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapterStatuslist != null) {adapterStatuslist.startListening();}
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapterStatuslist != null) {adapterStatuslist.startListening();}
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapterStatuslist != null) {adapterStatuslist.stopListening();}
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)){
            showDialogUpdateStatus(adapterStatuslist.getRef(item.getOrder()).getKey(),adapterStatuslist.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            showDialogDeleteStatus(adapterStatuslist.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void showDialogDeleteStatus(String key) {
        status.child(key).removeValue();
    }

    private void showDialogUpdateStatus(final String key, final Status item) {
        final AlertDialog optionDialog = new AlertDialog.Builder(getActivity()).create();
        optionDialog.setTitle("edit status");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View addMenuLayout = inflater.inflate(R.layout.layout_create_new_status,null, false);
        edtNameNewStatus = addMenuLayout.findViewById(R.id.edtNamenewStatus);
        btnAdd = addMenuLayout.findViewById(R.id.btnAddnewStatus);
        btnCancel = addMenuLayout.findViewById(R.id.btnCancelAddStatus);

        optionDialog.setView(addMenuLayout);
        optionDialog.setIcon(R.drawable.ic_note);

        edtNameNewStatus.setText(item.getName());


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Waiting....");
                progressDialog.show();

                final String newPiorityUpdate = edtNameNewStatus.getText().toString().trim();

                if(!newPiorityUpdate.isEmpty()){
                    item.setName(newPiorityUpdate);
                    status.child(key).setValue(item);
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Update "+newPiorityUpdate+" successfuly", Toast.LENGTH_SHORT).show();
                    optionDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please fill full information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
            }
        });

        optionDialog.show();
    }

    private void showListStatus() {
        Query query = FirebaseDatabase.getInstance().getReference().child("status");
        FirebaseRecyclerOptions<Status> options = new FirebaseRecyclerOptions.Builder<Status>()
                .setQuery(query, Status.class)
                .build();
        adapterStatuslist = new FirebaseRecyclerAdapter<Status, StatusViewHolder>(options) {

            @NonNull
            @Override
            public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_status, parent, false);
                return new StatusViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull StatusViewHolder holder, int position, @NonNull Status model) {
                holder.txtNameStatus.setText(model.getName());
                holder.txtDateCreateStatus.setText(model.getDate());
            }
        };
        recycleStatus.setAdapter(adapterStatuslist);
    }

    private void showDialogAddnewStatus() {
        final AlertDialog optionDialog = new AlertDialog.Builder(getActivity()).create();
        optionDialog.setTitle("Add new status");
        optionDialog.setMessage("Status form");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View addStatus = inflater.inflate(R.layout.layout_create_new_status,null, false);
        edtNameNewStatus = addStatus.findViewById(R.id.edtNamenewStatus);
        btnAdd = addStatus.findViewById(R.id.btnAddnewStatus);
        btnCancel = addStatus.findViewById(R.id.btnCancelAddStatus);

        optionDialog.setView(addStatus);
        optionDialog.setIcon(R.drawable.ic_status);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Waiting....");
                progressDialog.show();

                final String newName = edtNameNewStatus.getText().toString().trim();
                final String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date()).trim();

                if(!newName.isEmpty()){
                    newStatus = new Status(newName, currentDateTimeString);
                    status.push().setValue(newStatus);
                    progressDialog.dismiss();
                    optionDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please fill full information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
            }
        });

        optionDialog.show();
    }
}
