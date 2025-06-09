package com.example.skincareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccessKeyActivity extends AppCompatActivity {

    private EditText accessKeyInput;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acces_key);

        accessKeyInput = findViewById(R.id.accessKeyInput);
        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> {
            String accessKey = accessKeyInput.getText().toString().trim();
            if (TextUtils.isEmpty(accessKey)) {
                Toast.makeText(this, "Please enter the access key", Toast.LENGTH_SHORT).show();
                return;
            }
            if (accessKey.length() < 8) {
                Toast.makeText(this, "Access key must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!accessKey.matches("\\d+")) {
                Toast.makeText(this, "Access key must contain only numbers", Toast.LENGTH_SHORT).show();
                return;
            }
            // TODO: Validate access key if needed, then proceed
            // For now, just proceed to MainActivity
            Intent intent = new Intent(AccessKeyActivity.this, MainActivity.class);
            intent.putExtra("ACCESS_KEY", accessKey);
            startActivity(intent);
            finish();
        });
    }
}
