package com.leanhquan.ccpm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout        drawer;
    private NavigationView      navigationView;
    private TextView txtFullname, txtNameToolbar;
    private Toolbar             toolbar;
    private PieChart            pieChart;
    private FirebaseDatabase database;
    private DatabaseReference reference;


    private ArrayList<PieEntry> works;
    private  PieDataSet pieDataSet;
    private ArrayList<ChartValue> chartValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        database = FirebaseDatabase.getInstance();

        pieChart = findViewById(R.id.pieChart);
        works = new ArrayList<>();
        chartValues = new ArrayList<>();

        reference = database.getReference("status");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot UserSnapshot : snapshot.getChildren())
                {
                    Status status= UserSnapshot.getValue(Status.class);
                    chartValues.add(new ChartValue(status.getName(),0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference = database.getReference("notes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot UserSnapshot : snapshot.getChildren())
                {
                    Note note= UserSnapshot.getValue(Note.class);

                    for(ChartValue chartStatus : chartValues)
                    {
                        if(note.getStatus().equals(chartStatus.name))
                        {
                            chartStatus.value += 1;
                        }
                    }
                }
                for(ChartValue chartStatus : chartValues)
                {
                    if(chartStatus.value>0)
                    {
                        works.add(new PieEntry(chartStatus.value,chartStatus.name));
                    }
                }
                pieChart.notifyDataSetChanged();
                pieChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pieDataSet = new PieDataSet(works,"works");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextSize(15f);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Schedule");
        pieChart.animate();





        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer,toolbar,
                R.string.navigation_drawer_opne,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        View HeaderView = navigationView.getHeaderView(0);
        txtFullname = HeaderView.findViewById(R.id.txtFullName);
        txtFullname.setText(Common.currentUser.getUsername());



    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void showSignOutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Log out");
        alertDialog.setIcon(getDrawable(R.drawable.ic_log_out));
        alertDialog.setMessage("You wanna logout?");
        LayoutInflater inflater = LayoutInflater.from(this);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Paper.book().destroy();
                Intent signIn = new Intent(HomeActivity.this, MainActivity.class);
                signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signIn);
            }
        });
        alertDialog.show();
    }

    private void init() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        txtNameToolbar = findViewById(R.id.nameToolbar);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment fragmentSelected = null;

        switch (id){
            case R.id.page_home:
                Intent intentHome = new Intent(this, HomeActivity.class);
                startActivity(intentHome);
            case R.id.page_category:
                fragmentSelected = new CategoryFragment();
                txtNameToolbar.setText("Category Management");
                Toast.makeText(this, "Go to category", Toast.LENGTH_SHORT).show();
                break;
            case R.id.page_piority:
                fragmentSelected = new PiorityFragment();
                txtNameToolbar.setText("Piority Management");
                Toast.makeText(this, "Go to piority", Toast.LENGTH_SHORT).show();
                break;
            case R.id.page_status:
                fragmentSelected = new StatusFragment();
                txtNameToolbar.setText("Status Management");
                Toast.makeText(this, "Go to status", Toast.LENGTH_SHORT).show();
                break;
            case R.id.page_note:
                fragmentSelected = new NoteFragment();
                txtNameToolbar.setText("Notes Management");
                Toast.makeText(this, "Go to note", Toast.LENGTH_SHORT).show();
                break;
            case R.id.page_edit_profile:
                fragmentSelected = new ProfileFragment();
                txtNameToolbar.setText("Profile");
                Toast.makeText(this, "Go to edti profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.page_change_password:
                fragmentSelected = new ChangePasswordFragment();
                txtNameToolbar.setText("Change password");
                Toast.makeText(this, "Go to change password", Toast.LENGTH_SHORT).show();
                break;
            case R.id.page_logout:
                showSignOutDialog();
                break;
        }

        if (fragmentSelected != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_home, fragmentSelected);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}