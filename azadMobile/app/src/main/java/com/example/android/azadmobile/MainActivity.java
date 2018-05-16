package com.example.android.azadmobile;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    @BindView(R.id.wallet_balance)
    TextView balanceText;
    @BindView(R.id.appCompatButtonPlace)
    AppCompatButton placeButton;
    @BindView(R.id.challenges_ids)
    Spinner challengeSpinner;
    @BindView(R.id.teams_names)
    Spinner teamSpinner;
    @BindView(R.id.your_bet)
    EditText betEditText;

    private APIInterface apiInterface;
    private int balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Bundle args = getIntent().getExtras();
            if (args != null) {
                balance = args.getInt(Constants.BALANCE);
                balanceText.setText(String.valueOf(balance));
            }
        }

        apiInterface = APIClient.getClient().create(APIInterface.class);

        showProgress(MainActivity.this, "Please, wait...");
        Call<List<String>> challengesCall = apiInterface.getChallenges();
        challengesCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> challenges = response.body();
                if (challenges != null) {
                    Log.e("TAG", "onResponse: " + challenges.toString());
                }
                if (challenges != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                            R.layout.textview, challenges);
                    challengeSpinner.setAdapter(adapter);
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                hideProgress();
                Log.e("TAG", "onFailure: " + t.getMessage() );
                showMessage(MainActivity.this, t.getMessage());
            }
        });
    }

    @OnClick(R.id.appCompatButtonPlace)
    public void onPlaceButtonClicked() {
        String challenge = (String) challengeSpinner.getSelectedItem();
        String team = (String) teamSpinner.getSelectedItem();
        String bet = betEditText.getText().toString();

        if ((challenge == null || challenge.isEmpty())
                || (team == null || team.isEmpty())
                || bet.isEmpty()) {
            showMessage(MainActivity.this, "Fill all spaces");
            return;
        }

        if (Integer.valueOf(bet) > balance) {
            showMessage(MainActivity.this, "You don't have enough money");
            return;
        }

        placeButton.setEnabled(false);
        showProgress(MainActivity.this, "Data sending...");

        //todo send request

        hideProgress();
        placeButton.setEnabled(true);

        int requestCode = 0;
        if (requestCode == 0) {
            showMessage(MainActivity.this,"Bet was placed");
        } else {
            showMessage(MainActivity.this,"Error");
        }
    }

    @OnItemSelected(R.id.challenges_ids)
    public void onChallengeSelected() {
        showProgress(MainActivity.this, "Please, wait...");
        Log.e("TAG", "onChallengeSelected: " + challengeSpinner.getSelectedItem());
        Call<List<String>> teamsCall = apiInterface.getTeams((String) challengeSpinner.getSelectedItem());
        teamsCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> teams = response.body();
                if (teams != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                            R.layout.textview, teams);
                    teamSpinner.setAdapter(adapter);
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                hideProgress();
                showMessage(MainActivity.this, t.getMessage());
            }
        });
    }
}