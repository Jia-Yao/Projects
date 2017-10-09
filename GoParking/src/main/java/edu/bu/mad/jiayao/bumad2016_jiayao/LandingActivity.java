package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }

    public void onLoginPressed(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void onCreateAccountPressed(View view) {
        Intent i = new Intent(this, CreateAccountActivity.class);
        startActivity(i);
    }
}
