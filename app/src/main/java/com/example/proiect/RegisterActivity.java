package com.example.proiect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreeazaContButon;
    private EditText UsernameIntrodus, NumeIntrodus , NrTelefonIntrodus , ParolaIntrodusa;
    private ProgressDialog baraDeIncarcare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);

        CreeazaContButon=(Button) findViewById(R.id.registerpagebtn);
        UsernameIntrodus=(EditText)findViewById(R.id.registeruser);
        NumeIntrodus=(EditText)findViewById(R.id.registernume);
        NrTelefonIntrodus=(EditText)findViewById(R.id.registernrtel);
        ParolaIntrodusa=(EditText)findViewById(R.id.registerparola);
        baraDeIncarcare=new ProgressDialog(this);

        CreeazaContButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(RegisterActivity.this,CategoryActivity.class);
               // startActivity(intent);
                CreeazaCont();
            }
        });

    }

    private void CreeazaCont() {
        //FirebaseApp.initializeApp(this);
        String username=UsernameIntrodus.getText().toString();
        String nume=NumeIntrodus.getText().toString();
        String nrtel=NrTelefonIntrodus.getText().toString();
        String parola=ParolaIntrodusa.getText().toString();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this,"Va rog introduceti numele",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(nume)){
            Toast.makeText(this,"Va rog introduceti numele",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(nrtel)){
            Toast.makeText(this,"Va rog introduceti numarul de telefon",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(parola)){
            Toast.makeText(this,"Va rog introduceti parola",Toast.LENGTH_SHORT).show();
        }
        else {
            baraDeIncarcare.setTitle("Creeaza cont");
            baraDeIncarcare.setMessage("Asteptati pana ce va verificam datele.");
            baraDeIncarcare.setCanceledOnTouchOutside(false);
            baraDeIncarcare.show();

            ValidateUsername(username,nume,nrtel,parola);
        }

    }

    private void ValidateUsername(String username, String nume, String nrtel, String parola) {
        final DatabaseReference Referinta;
        Referinta= FirebaseDatabase.getInstance().getReference();

        Referinta.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if (!(dataSnapshot.child("Utilizatori").child(username).exists()))
                {
                    HashMap<String,Object>userdataMap=new HashMap<>();
                    userdataMap.put("Nume Utilizator",username);
                    userdataMap.put("Nume",nume);
                    userdataMap.put("Numar Telefon",nrtel);
                    userdataMap.put("Parola",parola);

                    Referinta.child("Utilizatori").child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful()){

                                        Toast.makeText(RegisterActivity.this,"Felicitari! Contul a fost creat!",Toast.LENGTH_SHORT).show();
                                        baraDeIncarcare.dismiss();
                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        baraDeIncarcare.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Eroare. Incercati din nou", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else {
                    Toast.makeText(RegisterActivity.this,"Username-ul " +username+ " exista deja",Toast.LENGTH_SHORT).show();
                    baraDeIncarcare.dismiss();
                    Toast.makeText(RegisterActivity.this,"Introduceti alt username.",Toast.LENGTH_SHORT).show();

                     Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                     startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            });
    }
}