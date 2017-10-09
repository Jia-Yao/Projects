package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailField, passwordField, usernameField;
    String email, password, username;
    public static AmazonClientManager clientManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        setTitle("Create account");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        usernameField = (EditText) findViewById(R.id.usernameField);
        emailField = (EditText) findViewById(R.id.emailField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        clientManager = new AmazonClientManager(this);
    }

    public void onEnterPressed(View view) {

        username = usernameField.getText().toString();
        if (username.trim().equals("")){
            Toast.makeText(getApplicationContext(), "User name is required", Toast.LENGTH_SHORT).show();
            return;
        }

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
        imm.hideSoftInputFromWindow(usernameField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(emailField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
        new DynamoDBManagerTask().execute(DynamoDBManagerType.INSERT_USER);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        CharSequence usernameText = usernameField.getText();
        outState.putCharSequence("usernameText", usernameText);
        CharSequence emailText = emailField.getText();
        outState.putCharSequence("emailText", emailText);
        CharSequence passwordText = passwordField.getText();
        outState.putCharSequence("passwordText", passwordText);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        CharSequence usernameText = savedInstanceState.getCharSequence("usernameText");
        usernameField.setText(usernameText);
        CharSequence emailText = savedInstanceState.getCharSequence("emailText");
        emailField.setText(emailText);
        CharSequence passwordText = savedInstanceState.getCharSequence("passwordText");
        passwordField.setText(passwordText);
    }

    /* Database */
    private enum DynamoDBManagerType {
        INSERT_USER
    }

    private class DynamoDBManagerTaskResult {
        private DynamoDBManagerType taskType;
        private String tableStatus;
        private int resultStatus;

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

        public int getResultStatus(){
            return resultStatus;
        }

        public void setResultStatus(int resultStatus){
            this.resultStatus = resultStatus;
        }
    }

    private class DynamoDBManagerTask extends AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {

        protected DynamoDBManagerTaskResult doInBackground(DynamoDBManagerType... types) {

            String tableStatus = DynamoDBManager.getUsersTableStatus("createAccount");
            DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);

            if (types[0] == DynamoDBManagerType.INSERT_USER) {
                if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                    result.setResultStatus(DynamoDBManager.insertUser(username, email, password));
                }
            }

            return result;
        }

        protected void onPostExecute(DynamoDBManagerTaskResult result) {

            if (!result.getTableStatus().equalsIgnoreCase("ACTIVE")) {
                Toast.makeText(getApplicationContext(), "Database is not ready yet", Toast.LENGTH_SHORT).show();
            } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE") && result.getTaskType() == DynamoDBManagerType.INSERT_USER) {
                switch (result.getResultStatus()){
                    case 0:
                        Intent i = new Intent(CreateAccountActivity.this, HomeActivity.class);
                        i.putExtra("username", username);
                        startActivity(i);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "User name already in use", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "Email already in use", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }
    }
}
