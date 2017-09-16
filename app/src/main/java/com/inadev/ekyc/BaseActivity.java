package com.inadev.ekyc;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by OPTLPTP163 on 9/16/2017.
 */

public abstract class BaseActivity extends AppCompatActivity{

    private ProgressDialog progressDialog;

    void showProgress(Context context)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
    }

    protected void dissmissProgressDialog()
    {
        if(progressDialog!=null)
        {
            progressDialog.dismiss();
        }
    }
}
