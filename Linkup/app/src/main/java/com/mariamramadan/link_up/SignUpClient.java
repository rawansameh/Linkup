package com.mariamramadan.link_up;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpClient extends AppCompatActivity {

    EditText email, Password, ConfPassword, fname, lname;
    Button submit;
    ProgressDialog progressDialog;
    String EmailRegex= "^(.+)@(gmail|yahoo|hotmail|aucegypt){1}(.com|.edu|.org|.eg){1}$";
    Pattern PatternEmail = Pattern.compile(EmailRegex);
    Matcher EmailMatcher;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean ValidInput= true;


    public void writeNewUser(String PhoneNum, String email, String FirstName, String LastName, String Pass) {
        Map<String, Object> user = new HashMap<>();
        user.put("Phone", PhoneNum);
        user.put("FirstName", FirstName);
        user.put("LastName", LastName);
        user.put("Password", Pass);
        user.put("Email", email);

        db.collection("clients")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Intent toHome= new Intent(SignUpClient.this, ServicesMenu.class);
                        startActivity(toHome);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_client);
        email = (EditText) findViewById(R.id.EmailClient);
        fname = (EditText) findViewById(R.id.FNameClient);
        lname = (EditText) findViewById(R.id.LNameClient);
        Password = (EditText) findViewById(R.id.PassClient);
        ConfPassword = (EditText) findViewById(R.id.ConfPassClient);

        submit = (Button) findViewById(R.id.SubmitClient);
        progressDialog = new ProgressDialog(this);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String PhoneNum= getIntent().getStringExtra("Phone");
                String inputEmail = email.getText().toString();
                String inputFname = fname.getText().toString();
                String inputLname = lname.getText().toString();
                String inputPass = Password.getText().toString();
                String inputConfPass = ConfPassword.getText().toString();
                EmailMatcher = PatternEmail.matcher(inputEmail);
                if (TextUtils.isEmpty(inputFname))
                {
                    fname.requestFocus();
                    fname.setError("Cannot leave this field empty");
                    ValidInput=false;
                }
                if (TextUtils.isEmpty(inputLname))
                {
                    lname.requestFocus();
                    lname.setError("Cannot leave this field empty");
                    ValidInput=false;
                }
                if (!EmailMatcher.matches())
                {
                    email.requestFocus();
                    email.setError("Enter a valid email address");
                    ValidInput=false;
                }
                if (!(inputConfPass.equals(inputPass)) || TextUtils.isEmpty(inputPass) || TextUtils.isEmpty(inputConfPass))
                {
                    ConfPassword.requestFocus();
                    ConfPassword.setError("Passwords do not match");
                    ValidInput=false;
                }
                else
                {
                    if (ValidInput)
                    {
                        writeNewUser(PhoneNum, inputEmail, inputFname, inputLname, inputPass);
                    }
                }

            }

        });

    }

}