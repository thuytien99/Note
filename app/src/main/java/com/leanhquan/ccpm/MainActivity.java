package com.leanhquan.ccpm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leanhquan.ccpm.Common.Common;
import com.leanhquan.ccpm.Model.User;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private TextView btnSignUp;
    private Button btnLogin;
    private CheckBox cbRemember;
    private FirebaseDatabase database;
    private DatabaseReference userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        Paper.init(this);

        database = FirebaseDatabase.getInstance();
        userDB = database.getReference("user");

        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PASS_KEY);
        if(user!=null && pwd != null)
        {
            if(!user.isEmpty() && !pwd.isEmpty())
                loginRemember(user,pwd);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();
                if (!user.isEmpty() && !pass.isEmpty()) {
                    login(user, pass);
                } else {
                    Toast.makeText(MainActivity.this, "Username or password does not empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterInten = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(RegisterInten);
            }
        });


    }

    private void loginRemember(final String user, final String pwd) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please waiting...");
        dialog.show();

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(user).exists()) {
                    dialog.dismiss();
                    User userLogin = snapshot.child(user).getValue(User.class);

                    if (userLogin.getPassword().equals(pwd)) {
                        if (cbRemember.isChecked()) {
                            Paper.book().write(Common.USER_KEY, edtUsername.getText().toString());
                            Paper.book().write(Common.PASS_KEY, edtPassword.getText().toString());
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void login(final String user, final String pass) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please waiting...");
        dialog.show();

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(user).exists()){
                    dialog.dismiss();
                    User userLogin = snapshot.child(user).getValue(User.class);

                    if (userLogin.getPassword().equals(pass)){
                        if(cbRemember.isChecked())
                        {
                            Paper.book().write(Common.USER_KEY,edtUsername.getText().toString());
                            Paper.book().write(Common.PASS_KEY,edtPassword.getText().toString());
                        }

                        Common.currentUser = userLogin;
                        finish();
                    }else {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "User does not exits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    private void init() {
        edtUsername = findViewById(R.id.edtUsernameLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        cbRemember = findViewById(R.id.cbRememberLogin);
        btnSignUp = findViewById(R.id.txtSignUp);
        btnLogin = findViewById(R.id.btnLogin);
    }

}