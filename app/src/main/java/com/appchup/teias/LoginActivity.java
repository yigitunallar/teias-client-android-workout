package com.appchup.teias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private final String AUTHORIZED_USER = "user";
    private final String AUTHORIZED_EMAIL = "email";
    private final String RECEIVED_TOKEN = "token";

    SharedPreferences preferences;

    Retrofit builder = new Retrofit.Builder()
            .baseUrl("http://35.192.14.183/api/")
            .addConverterFactory(GsonConverterFactory.create()).build();

    UserClient userClient  = builder.create(UserClient.class);

    private EditText etEmail;
    private EditText etPassword;
    private Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);

        preferences = getSharedPreferences(AUTHORIZED_USER, Context.MODE_PRIVATE);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }
    private void login(){
        Login login = new Login(etEmail.getText().toString(), etPassword.getText().toString());
        Call<Result> call = userClient.login(login);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body().getSuccess()!="false") {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(AUTHORIZED_EMAIL,etEmail.getText().toString());
                    editor.putString(RECEIVED_TOKEN, response.body().getToken());
                    editor.commit();
                    //Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Login failed!!:(",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Error:(",Toast.LENGTH_LONG).show();
            }
        });

    }


}
