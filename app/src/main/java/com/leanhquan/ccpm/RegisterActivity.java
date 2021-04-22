package com.leanhquan.ccpm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leanhquan.ccpm.Model.User;

public class RegisterActivity extends AppCompatActivity {
    private Button btnSignup;
    private EditText edtEmail,
            edtUsername,
            edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();
                String email = edtEmail.getText().toString();

                if (!user.isEmpty() && !pass.isEmpty() && validateEmail()) {
                    Register(user, pass, email);
                } else {
                    Toast.makeText(RegisterActivity.this, "input wrong type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Register(final String user, final String pass, final String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userDb = database.getReference("user");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please waiting ...");
        progressDialog.show();

        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user).exists()){
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "User name have already register", Toast.LENGTH_SHORT).show();
                }else{
                    User userRegis = new User(user,pass,email);
                    userDb.child(user).setValue(userRegis);
                    Toast.makeText(RegisterActivity.this, "Sign up successfuly", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean validateEmail() {
        String valueEmai = edtEmail.getText().toString();
        if (valueEmai.isEmpty()) {
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(valueEmai).matches()) {
            return false;
        } else {
            return true;
        }
    }

    private void init() {
        btnSignup = findViewById(R.id.btnRegister);
        edtEmail = findViewById(R.id.edtEmailRegister);
        edtUsername = findViewById(R.id.edtUsernameRegister);
        edtPassword = findViewById(R.id.edtPasswordRegister);
    }
}