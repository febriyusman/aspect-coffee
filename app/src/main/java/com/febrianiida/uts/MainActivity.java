package com.febrianiida.uts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginbutton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validasi login sederhana
                if (username.equals("Febri") && password.equals("123")) {
                    Toast.makeText(MainActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                    // Pindah ke MenuActivity (ganti dengan nama activity yang sesuai)
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish(); // Optional: Menutup MainActivity agar tidak bisa kembali ke halaman login
                } else {
                    Toast.makeText(MainActivity.this, "Username atau password salah!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    }
