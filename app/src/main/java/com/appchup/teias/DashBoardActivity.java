package com.appchup.teias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final TextView tvDashboard = (TextView) findViewById(R.id.tvDashboard);
        final ProgressBar pbLoadingIndicator = (ProgressBar) findViewById(R.id.pbLoadingIndicator);
        //final TextView tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        //final TextView tvEmail = (TextView) findViewById(R.id.tvEmail);

        final SharedPreferences prefs = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        pbLoadingIndicator.setVisibility(View.VISIBLE);
        //tvWelcome.setText(prefs.getString("token",null));
        //tvEmail.setText(prefs.getString("email", null));

        Retrofit builder = new Retrofit.Builder()
                .baseUrl("http://35.192.14.183/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        UserClient userClient  = builder.create(UserClient.class);
        Call<Dashboard> call = userClient.getDashboard(prefs.getString("token", null));



        call.enqueue(new Callback<Dashboard>() {
            @Override
            public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {
                if (response.body().getSuccess()) {
                    //Toast.makeText(DashBoardActivity.this,"Dashboard Successful",Toast.LENGTH_LONG).show();
                    pbLoadingIndicator.setVisibility(View.INVISIBLE);
                    List<String> titles = Helper.parseProjects(response.body().getProjects());
                    for(String title:titles){
                        tvDashboard.append(title + "\n\n\n");
                    }
                    //tvDashboard.setText(response.body().getSuccess().toString());

                }
                else {
                    Toast.makeText(DashBoardActivity.this,"Dashboard failed!!:(",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Dashboard> call, Throwable t) {
                Toast.makeText(DashBoardActivity.this,"Error:(",Toast.LENGTH_LONG).show();
            }
        });



    }
}
