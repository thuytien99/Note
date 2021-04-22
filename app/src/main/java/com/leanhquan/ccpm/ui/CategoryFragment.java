package com.leanhquan.notemanagementsystem.UI;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.leanhquan.notemanagementsystem.Common.Common;
import com.leanhquan.notemanagementsystem.Model.Category;
import com.leanhquan.notemanagementsystem.R;
import com.leanhquan.notemanagementsystem.ViewHolder.CategoryViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.Date;


public class CategoryFragment extends Fragment {

    private CounterFab              fabAddCategory;
    private MaterialEditText        edtNameNewCategory;
    private Button                  btnAdd, btnCancel;
    private FirebaseDatabase        database;
    private DatabaseReference       categories;
    private Category                createCategory;
    private RecyclerView            recycleCategory;

    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapterCategorylist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        database = FirebaseDatabase.getInstance();
        categories = database.getReference().child("categories");

        recycleCategory = view.findViewById(R.id.recycler_category);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recycleCategory.setLayoutManager(manager);

        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddnewCategory();
            }
        });

        showListCategory();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapterCategorylist != null) { adapterCategorylist.startListening();}
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapterCategorylist != null) {adapterCategorylist.stopListening();}
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapterCategorylist != null) { adapterCategorylist.startListening();}
    }

    private void showListCategory() {
        Query query = FirebaseDatabase.getInstance().getReference().child("categories");
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class)
                .build();
        adapterCategorylist = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_category, parent, false);
                return new CategoryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Category model) {
                holder.txtNameCategory.setText((CharSequence) model.getName());
                holder.txtDateCreateCategory.setText(model.getDate());
            }

        };
        recycleCategory.setAdapter(adapterCategorylist);
        adapterCategorylist.notifyDataSetChanged();
    }

    private void showDialogAddnewCategory() {
        final AlertDialog optionDialog = new AlertDialog.Builder(getActivity()).create();
        optionDialog.setTitle("Add new category");
        optionDialog.setMessage("Category form");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View addMenuLayout = inflater.inflate(R.layout.layout_create_new_category,null, false);
        edtNameNewCategory = addMenuLayout.findViewById(R.id.edtNamenewCategory);
        btnAdd = addMenuLayout.findViewById(R.id.btnAddnewCategory);
        btnCancel = addMenuLayout.findViewById(R.id.btnCancelAddCategory);

        optionDialog.setView(addMenuLayout);
        optionDialog.setIcon(R.drawable.ic_note);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Waiting....");
                progressDialog.show();

                final String newCategory = edtNameNewCategory.getText().toString();
                final String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date()).trim();

                if(!newCategory.isEmpty()){
                    createCategory = new Category(newCategory, currentDateTimeString);
                    categories.push().setValue(createCategory);
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
            showDialogUpdateCategory(adapterCategorylist.getRef(item.getOrder()).getKey(),adapterCategorylist.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            showDialogDeleteCategory(adapterCategorylist.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void showDialogDeleteCategory(String key) {
        categories.child(key).removeValue();
        Toast.makeText(getActivity(), "Item Deleted", Toast.LENGTH_SHORT).show();
    }

    private void showDialogUpdateCategory(final String key, final Category item) {
        final AlertDialog optionDialog = new AlertDialog.Builder(getActivity()).create();
        optionDialog.setTitle("edit category");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View addMenuLayout = inflater.inflate(R.layout.layout_create_new_category,null, false);
        edtNameNewCategory = addMenuLayout.findViewById(R.id.edtNamenewCategory);
        btnAdd = addMenuLayout.findViewById(R.id.btnAddnewCategory);
        btnCancel = addMenuLayout.findViewById(R.id.btnCancelAddCategory);

        optionDialog.setView(addMenuLayout);
        optionDialog.setIcon(R.drawable.ic_note);

        edtNameNewCategory.setText((CharSequence) item.getName());


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Waiting....");
                progressDialog.show();

                final String newCategoryUpdate = edtNameNewCategory.getText().toString().trim();

                if(!newCategoryUpdate.isEmpty()){
                    item.setName(newCategoryUpdate);
                    categories.child(key).setValue(item);
                    progressDialog.dismiss();
                    optionDialog.dismiss();
                    Toast.makeText(getActivity(), "Update "+newCategoryUpdate+" successfuly", Toast.LENGTH_SHORT).show();
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


    private void init(View view) {
        fabAddCategory = view.findViewById(R.id.fabCategory);
    }
}
