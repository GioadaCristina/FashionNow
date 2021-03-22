package com.example.proiect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private Button LoginButton;
    private EditText UsernameIntrodus, ParolaIntrodusa;
    private ProgressDialog baraDeIncarcare;

    private String numeDbParinte="Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton=(Button) findViewById(R.id.loginpagebtn);
        UsernameIntrodus=(EditText)findViewById(R.id.loginuser);
        ParolaIntrodusa=(EditText)findViewById(R.id.loginpassword);
        baraDeIncarcare=new ProgressDialog(this);
       // String username=UsernameIntrodus.getText().toString();
        //String parola=ParolaIntrodusa.getText().toString();

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }
    private void loginUser(){
        String username=UsernameIntrodus.getText().toString();
        String parola=ParolaIntrodusa.getText().toString();
        if (TextUtils.isEmpty(username)){
            Toast.makeText(this,"Va rog introduceti numele",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(parola)){
            Toast.makeText(this,"Va rog introduceti parola",Toast.LENGTH_SHORT).show();
        }
        else {
            baraDeIncarcare.setTitle("Logare");
            baraDeIncarcare.setMessage("Asteptati pana ce va verificam datele.");
            baraDeIncarcare.setCanceledOnTouchOutside(false);
            baraDeIncarcare.show();

           LogareReusita(username,parola);
        }
    }

    private void LogareReusita(String username, String password){
        final DatabaseReference Referinta;
        Referinta= FirebaseDatabase.getInstance().getReference();
        Referinta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(numeDbParinte).child(username).exists())
                {
                    Toast.makeText(LoginActivity.this, "Logare succesful", Toast.LENGTH_SHORT).show();
                    baraDeIncarcare.dismiss();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Contul cu numele de utilizator : "+username+" nu exista!", Toast.LENGTH_SHORT).show();
                    baraDeIncarcare.dismiss();
                    Toast.makeText(LoginActivity.this, "Puteti sa creati un cont apasand butonul de inregistrare.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}