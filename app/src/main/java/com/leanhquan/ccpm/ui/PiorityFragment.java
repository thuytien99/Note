package com.leanhquan.ccpm.ui;

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
import com.leanhquan.ccpm.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Date;


public class PiorityFragment extends Fragment {

    private CounterFab fabAddPiority;
    private MaterialEditText edtNameNewPiority;
    private Button btnAdd, btnCancel;
    private FirebaseDatabase database;
    private DatabaseReference piorities;
    private Piority                 newPiority;
    private RecyclerView recyclePiority;

    private FirebaseRecyclerAdapter<Piority, PiorityViewHolder> adapterPioritylist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_piority, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();
        piorities = database.getReference().child("piorities");

        recyclePiority = view.findViewById(R.id.recycler_piority);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclePiority.setLayoutManager(manager);

        fabAddPiority = view.findViewById(R.id.fabPiority);

        fabAddPiority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddnewPiority();
            }
        });

        showListPiority();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapterPioritylist != null) {adapterPioritylist.startListening();}
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapterPioritylist != null) {adapterPioritylist.startListening();}
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapterPioritylist != null) {adapterPioritylist.stopListening();}
    }

    private void showDialogAddnewPiority() {
        final AlertDialog optionDialog = new AlertDialog.Builder(getActivity()).create();
        optionDialog.setTitle("Add new piority");
        optionDialog.setMessage("Piority form");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View addPiority = inflater.inflate(R.layout.layout_create_new_piority,null, false);
        edtNameNewPiority = addPiority.findViewById(R.id.edtNamenewPiority);
        btnAdd = addPiority.findViewById(R.id.btnAddnewPiority);
        btnCancel = addPiority.findViewById(R.id.btnCancelAddPiority);

        optionDialog.setView(addPiority);
        optionDialog.setIcon(R.drawable.ic_priority);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Waiting....");
                progressDialog.show();

                final String newName = edtNameNewPiority.getText().toString().trim();
                final String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date()).trim();

                if(!newName.isEmpty()){
                    newPiority = new Piority(newName, currentDateTimeString);
                    piorities.push().setValue(newPiority);
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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)){
            showDialogUpdatePiority(adapterPioritylist.getRef(item.getOrder()).getKey(),adapterPioritylist.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            showDialogDeletePiority(adapterPioritylist.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void showDialogDeletePiority(String key) {
        piorities.child(key).removeValue();
        Toast.makeText(getActivity(), "Item Deleted", Toast.LENGTH_SHORT).show();
    }

    private void showDialogUpdatePiority(final String key, final Piority item) {
        final AlertDialog optionDialog = new AlertDialog.Builder(getActivity()).create();
        optionDialog.setTitle("edit piority");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View addMenuLayout = inflater.inflate(R.layout.layout_create_new_category,null, false);
        edtNameNewPiority = addMenuLayout.findViewById(R.id.edtNamenewCategory);
        btnAdd = addMenuLayout.findViewById(R.id.btnAddnewCategory);
        btnCancel = addMenuLayout.findViewById(R.id.btnCancelAddCategory);

        optionDialog.setView(addMenuLayout);
        optionDialog.setIcon(R.drawable.ic_note);

        edtNameNewPiority.setText(item.getName());


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Waiting....");
                progressDialog.show();

                final String newPiorityUpdate = edtNameNewPiority.getText().toString().trim();

                if(!newPiorityUpdate.isEmpty()){
                    item.setName(newPiorityUpdate);
                    piorities.child(key).setValue(item);
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

    private void showListPiority() {
        Query query = FirebaseDatabase.getInstance().getReference().child("piorities");
        FirebaseRecyclerOptions<Piority> options = new FirebaseRecyclerOptions.Builder<Piority>()
                .setQuery(query, Piority.class)
                .build();
        adapterPioritylist = new FirebaseRecyclerAdapter<Piority, PiorityViewHolder>(options) {
            @NonNull
            @Override
            public PiorityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_piority, parent, false);
                return new PiorityViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PiorityViewHolder holder, int position, @NonNull Piority model) {
                holder.txtNamePiority.setText(model.getName());
                holder.txtDateCreatePiority.setText(model.getDate());
            }
        };
        recyclePiority.setAdapter(adapterPioritylist);
    }
}