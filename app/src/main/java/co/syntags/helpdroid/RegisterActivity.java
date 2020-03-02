package co.syntags.helpdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    Button signUp;
    EditText emailet, passEt, nameEt, fmn1et, fmn2et, fmn3et, selfnumet;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signUp = findViewById(R.id.signUpBtn);
        emailet = findViewById(R.id.emailEt);
        passEt = findViewById(R.id.passwordEt);
        nameEt = findViewById(R.id.nameEt);
        fmn1et = findViewById(R.id.fmn1et);
        fmn2et = findViewById(R.id.fmn2et);
        fmn3et = findViewById(R.id.fmn3et);
        selfnumet = findViewById(R.id.selfnumEt);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = emailet.getText().toString();
                String pass = passEt.getText().toString();
                String name = nameEt.getText().toString();
                String fmn1 = fmn1et.getText().toString();
                String fmn2 = fmn2et.getText().toString();
                String fmn3 = fmn3et.getText().toString();
                String selfnum = selfnumet.getText().toString();
                if (!email.isEmpty() && !pass.isEmpty() && !name.isEmpty() && !fmn1.isEmpty() && !fmn2.isEmpty() && !fmn3.isEmpty() && !selfnum.isEmpty()){
                    if (isEmailValid(email)){
                        signUpWithEmail(email, pass, name, selfnum, fmn1, fmn2, fmn3);
                    }else{
                        Toast.makeText(RegisterActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Details Properly", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUpWithEmail(final String email, String pass, final String name, final String selfnum, final String fmn1, final String fmn2, final String fmn3) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String uid = firebaseAuth.getCurrentUser().getUid();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("Email", email);
                            userData.put("Name", name);
                            userData.put("Self Mobile No", selfnum);
                            userData.put("Family Member 1", fmn1);
                            userData.put("Family Member 2", fmn2);
                            userData.put("Family Member 3", fmn3);
                            firebaseDatabase.getReference().child(uid).setValue(userData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("error", e.getMessage());
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public boolean isEmailValid(String text){
        return text.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
    }
}
