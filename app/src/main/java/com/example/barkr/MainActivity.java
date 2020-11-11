package com.example.barkr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //private FirebaseAuth mAuth;
    TextView signUp;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mAuth = FirebaseAuth.getInstance();

        signUp = findViewById(R.id.signupText);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //check if user is signed in
        //FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void login()
    {
        //Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v)
    {
        if(v == signUp)
        {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
        }
    }
}
