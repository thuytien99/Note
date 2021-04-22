package com.leanhquan.ccpm.ui;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.leanhquan.ccpm.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoteFragment extends Fragment {
    private CounterFab fabAddNote;
    private MaterialEditText edtNameNewNote;
    private Spinner categorySpinner,
            pioritySpinner,
            statusSpinner,
            update;
    private Button btnAdd,
            btnok,
            btndis,
            btnCancel,
            btnPlanDate;
    private Note                createNote;

    private RecyclerView recycleNote;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<String> nameStatus;
    private ArrayAdapter<String> dataAdapter;

    private FirebaseRecyclerAdapter<Note, NoteViewHolder> adapterNoteList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();

        recycleNote = view.findViewById(R.id.recycler_note);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycleNote.setLayoutManager(layoutManager);

        fabAddNote = view.findViewById(R.id.fabNote);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddnewNote();
            }
        });
        nameStatus = new ArrayList<>();
        dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nameStatus);


        showListNote();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapterNoteList != null) {adapterNoteList.startListening();}
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapterNoteList != null) {adapterNoteList.startListening();}
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapterNoteList != null) {adapterNoteList.stopListening();}
    }

    private void showListNote() {
        Query query = FirebaseDatabase.getInstance().getReference().child("notes");
        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        adapterNoteList = new FirebaseRecyclerAdapter<Note, NoteViewHolder>(options) {

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_note, parent, false);
                return new NoteViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note model) {
                holder.txtNameNote.setText(model.getName());
                holder.txtCategoryNote.setText(model.getCategory());
                holder.txtPiorityNote.setText(model.getPriority());
                holder.txtStatusNote.setText(model.getStatus());
                holder.txtPlanDateNote.setText(model.getPlandate());
                holder.txtDateCreatedNote.setText(model.getCreateddate());
            }
        };
        recycleNote.setAdapter(adapterNoteList);
    }

    private void showDialogAddnewNote() {
        final AlertDialog optionDialog = new AlertDialog.Builder(getActivity()).create();
        optionDialog.setTitle("Add new note");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View addNote = inflater.inflate(R.layout.layout_create_new_note,null, false);
        edtNameNewNote = addNote.findViewById(R.id.edtNamenewNote);
        statusSpinner = addNote.findViewById(R.id.StatusSpinner);
        categorySpinner = addNote.findViewById(R.id.CategorySpinner);
        pioritySpinner = addNote.findViewById(R.id.PioritySpinner);
        btnPlanDate = addNote.findViewById(R.id.bntPlanDate);
        btnAdd = addNote.findViewById(R.id.btnAddnewNote);
        btnCancel = addNote.findViewById(R.id.btnCancelNote);

        optionDialog.setView(addNote);
        optionDialog.setIcon(R.drawable.ic_note);

        final Calendar c = Calendar.getInstance();
        final DatePickerDialog StartTime = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Toast.makeText(getContext(), ""+ newDate.getTime(), Toast.LENGTH_SHORT).show();
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));


        reference = database.getReference().child("categories");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> nameCate = new ArrayList<>();
                for (DataSnapshot cate : snapshot.getChildren()){
                    String category = cate.child("name").getValue(String.class);
                    nameCate.add(category);
                }
                final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nameCate);
                dataAdapter.notifyDataSetChanged();
                categorySpinner.setAdapter(dataAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = database.getReference().child("piorities");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> namePiority = new ArrayList<>();
                for (DataSnapshot pio : snapshot.getChildren()){
                    String category = pio.child("name").getValue(String.class);
                    namePiority.add(category);
                }
                final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, namePiority);
                dataAdapter.notifyDataSetChanged();
                pioritySpinner.setAdapter(dataAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = database.getReference().child("status");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> nameStatus = new ArrayList<>();
                for (DataSnapshot status : snapshot.getChildren()){
                    String category = status.child("name").getValue(String.class);
                    nameStatus.add(category);
                }
                final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nameStatus);
                dataAdapter.notifyDataSetChanged();
                statusSpinner.setAdapter(dataAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnPlanDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime.show();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Waiting....");
                progressDialog.show();

                reference = database.getReference().child("notes");

                final String name = edtNameNewNote.getText().toString().trim();
                final String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date()).trim();
                String noteCate = String.valueOf(categorySpinner.getSelectedItem());
                String notePiority = String.valueOf(pioritySpinner.getSelectedItem());
                String noteStatus = String.valueOf(statusSpinner.getSelectedItem());

                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String formatted = format1.format(c.getTime());
                System.out.println();


                if(!name.isEmpty()){
                    createNote = new Note(name, noteCate, notePiority, noteStatus, formatted, currentDateTimeString);
                    reference.push().setValue(createNote);
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
            showDialogUpdateNote(adapterNoteList.getRef(item.getOrder()).getKey(),adapterNoteList.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            showDialogDeleteNote(adapterNoteList.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void showDialogDeleteNote(String key) {
        reference = database.getReference().child("notes");
        reference.child(key).removeValue();
        Toast.makeText(getActivity(), "Item Deleted", Toast.LENGTH_SHORT).show();
    }

    private void showDialogUpdateNote(final String key, final Note item) {
        final AlertDialog optionDialog = new AlertDialog.Builder(getActivity()).create();
        optionDialog.setTitle("edit note");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View addNote = inflater.inflate(R.layout.layout_update_process_note,null, false);
        update = addNote.findViewById(R.id.updateProcess);
        btnok = addNote.findViewById(R.id.btnOkupdate);
        btndis = addNote.findViewById(R.id.btncancelUpdat);

        optionDialog.setView(addNote);
        optionDialog.setIcon(R.drawable.ic_status);

        reference = database.getReference().child("status");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot status : snapshot.getChildren()){
                    String category = status.child("name").getValue(String.class);
                    nameStatus.add(category);
                }
                dataAdapter.notifyDataSetChanged();
                update.setAdapter(dataAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Waiting....");
                progressDialog.show();

                DatabaseReference referencenotes;
                referencenotes = database.getReference().child("notes");
                String noteStatus = String.valueOf(update.getSelectedItem());


                item.setStatus(noteStatus);
                referencenotes.child(key).setValue(item);
                progressDialog.dismiss();
                optionDialog.dismiss();

            }
        });

        btndis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
            }
        });

        optionDialog.show();
    }
}