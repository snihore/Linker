package com.srv.linker;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProgressAndResult{

    private Context context;

    private Button btn;
    private TextView finishTag;
    private ProgressBar progressBar;

    public ProgressAndResult(Context context, Button btn, ProgressBar progressBar) {
        this.context = context;
        this.btn = btn;
        this.progressBar = progressBar;
    }

    public ProgressAndResult(Context context, TextView finishTag, ProgressBar progressBar) {
        this.context = context;
        this.finishTag = finishTag;
        this.progressBar = progressBar;
    }

    public void showToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void toggleProgressBar01(boolean flag){
        if(flag){
            btn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            btn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void toggleProgressBar02(boolean flag){
        if(flag){
            progressBar.setVisibility(View.VISIBLE);
            finishTag.setVisibility(View.INVISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            finishTag.setVisibility(View.VISIBLE);
        }
    }

}
