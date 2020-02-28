package co.syntags.helpdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {
    Button signUp;
    EditText emailet, passEt;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signUp = findViewById(R.id.signUpBtn);
        emailet = findViewById(R.id.emailEt);
        passEt = findViewById(R.id.passwordEt);
        firebaseAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = emailet.getText().toString();
                String pass = passEt.getText().toString();
                if(!email.isEmpty() && !pass.isEmpty()){
                    if (isEmailMatches(email)){
                        signUpWithEmail(email, pass);
                    }else{
                        Toast.makeText(RegisterActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void signUpWithEmail(String email, String pass){
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                            Log.e("data", "successs");
                        Log.e("currentuser", firebaseAuth.getCurrentUser().getUid());
                    }
                });
    }
    public boolean isEmailMatches(String text){
        return text.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
    }
}
