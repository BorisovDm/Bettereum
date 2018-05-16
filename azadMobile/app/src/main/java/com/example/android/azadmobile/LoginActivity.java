package com.example.android.azadmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.widget.EditText;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.widget.EditText;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by parsh on 14.05.2018.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.textInputEditTextLogin)
    EditText loginEditText;
    @BindView(R.id.textInputEditTextPassword)
    EditText passwordEditText;
    @BindView(R.id.appCompatButtonLogin)
    AppCompatButton loginButton;

    private APIInterface apiInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @OnClick(R.id.appCompatButtonLogin)
    public void onLoginButtonClicked() {
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (login.isEmpty() || password.isEmpty()) {
            showMessage(LoginActivity.this, "Fill all spaces");
            return;
        }

        loginButton.setEnabled(false);
        showProgress(LoginActivity.this, "Data sending...");

        HashMap<String, String> request = new HashMap<>();
        request.put("login", login);
        request.put("password", password);

        Call<Integer> call = apiInterface.getUserBalance(request);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d("LoginActivity", "onResponse: " + response.code());
                Log.d("LoginActivity", "onResponse: " + response.toString());

                hideProgress();
                loginButton.setEnabled(true);

                if (response.code() == 403) {
                    showMessage(LoginActivity.this,"403 Error");
                    return;
                } else if (response.code() == 404) {
                    showMessage(LoginActivity.this,"404 Error");
                    return;
                }

                hideProgress();
                loginButton.setEnabled(true);

                Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                mainActivityIntent.putExtra(Constants.BALANCE, response.body());
                startActivity(mainActivityIntent);
                finish();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("LoginActivity", "onResponse: " + t.getMessage());

                hideProgress();
                loginButton.setEnabled(true);
                showMessage(LoginActivity.this,"Error");
            }
        });
    }
}

