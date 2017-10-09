package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    String email, password, username;
    public static AmazonClientManager clientManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        setTitle("Log in");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        emailField = (EditText) findViewById(R.id.emailField);
        passwordField = (EditText) findViewById(R.id.passwordField);

        clientManager = new AmazonClientManager(this);
    }

    public void onEnterPressed(View view) {

        email = emailField.getText().toString();
        if (email.trim().equals("")){
            Toast.makeText(getApplicationContext(), "Email is required", Toast.LENGTH_SHORT).show();
            return;
        }

        password = passwordField.getText().toString();
        if (password.trim().equals("")){
            Toast.makeText(getApplicationContext(), "Password is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // All fields have been filled
        // hide virtual keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
        new DynamoDBManagerTask().execute(DynamoDBManagerType.LOGIN_USER);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        CharSequence emailText = emailField.getText();
        outState.putCharSequence("emailText", emailText);
        CharSequence passwordText = passwordField.getText();
        outState.putCharSequence("passwordText", passwordText);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        CharSequence emailText = savedInstanceState.getCharSequence("emailText");
        emailField.setText(emailText);
        CharSequence passwordText = savedInstanceState.getCharSequence("passwordText");
        passwordField.setText(passwordText);
    }

    /* Database */
    private enum DynamoDBManagerType {
        LOGIN_USER
    }

    private class DynamoDBManagerTaskResult {
        private DynamoDBManagerType taskType;
        private String tableStatus;
        private String resultUsername;

        public DynamoDBManagerType getTaskType() {
            return taskType;
        }

        public void setTaskType(DynamoDBManagerType taskType) {
            this.taskType = taskType;
        }

        public String getTableStatus() {
            return tableStatus;
        }

        public void setTableStatus(String tableStatus) {
            this.tableStatus = tableStatus;
        }

        public String getResultUsername(){
            return resultUsername;
        }

        public void setResultUsername(String resultUsername){
            this.resultUsername = resultUsername;
        }
    }

    private class DynamoDBManagerTask extends AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {

        protected DynamoDBManagerTaskResult doInBackground(DynamoDBManagerType... types) {

            String tableStatus = DynamoDBManager.getUsersTableStatus("login");
            DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);

            if (types[0] == DynamoDBManagerType.LOGIN_USER) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    try {
                        result.setResultUsername(DynamoDBManager.loginUser(email, password));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            return result;
        }

        protected void onPostExecute(DynamoDBManagerTaskResult result) {

            if (!result.getTableStatus().equalsIgnoreCase("ACTIVE")) {
                Toast.makeText(getApplicationContext(), "Database is not ready yet", Toast.LENGTH_SHORT).show();
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.LOGIN_USER) {
                if (result.getResultUsername() == null){
                    Toast.makeText(getApplicationContext(), "Invalid email/password combination", Toast.LENGTH_SHORT).show();
                } else{
                    username = result.getResultUsername();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    i.putExtra("username", username);
                    startActivity(i);
                }
            }
        }
    }
}
