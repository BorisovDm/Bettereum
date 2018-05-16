package com.example.android.azadmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by parsh on 14.05.2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Unbinder unbinder;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected void showProgress(Context context, final String msgContent) {
//        progressDialog = new ProgressDialog(context, R.style.my_dialog);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage(msgContent);
//        progressDialog.show();
    }

    protected void hideProgress() {
//        progressDialog.dismiss();
    }

    protected void showMessage(Context context, final String msgContent) {
        Toast.makeText(context, msgContent, Toast.LENGTH_SHORT).show();
    }
}

