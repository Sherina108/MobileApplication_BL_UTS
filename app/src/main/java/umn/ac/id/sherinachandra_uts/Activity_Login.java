package umn.ac.id.sherinachandra_uts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_Login extends AppCompatActivity {
    private EditText UserName;
    private EditText Password;
    private Button btnLogin1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);

        UserName = (EditText) findViewById(R.id.etUsername);
        Password = (EditText) findViewById(R.id.etPassword);
        btnLogin1 = (Button) findViewById(R.id.btnLogin1);

        btnLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserName.getText().toString().equals("uasmobile") &&
                        Password.getText().toString().equals("uasmobilegenap")) {
                    startActivity(new Intent(Activity_Login.this, ActivityList.class));
                }
            }
        });
    }
}