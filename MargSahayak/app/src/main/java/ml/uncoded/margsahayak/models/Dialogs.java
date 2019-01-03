package ml.uncoded.margsahayak.models;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import ml.uncoded.margsahayak.R;

public class Dialogs extends DialogFragment {
    TextView textView;
    Button btnOk;
    View.OnClickListener clickListener;
    public String strErrMsg;
    public Dialogs(){}
    @SuppressLint("ValidFragment")
    public Dialogs(String s,View.OnClickListener clickListener){
        this.clickListener=clickListener;
        strErrMsg=s;
    }
    @SuppressLint("ValidFragment")
    public Dialogs(String s){
        strErrMsg=s;
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.custom_dialog_layout, container, false);
        textView=(TextView)view.findViewById(R.id.customDialog_tv_message);
        btnOk =(Button) view.findViewById(R.id.customDialog_btn_Ok);
        textView.setText(strErrMsg);
        if(clickListener==null){
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        else{

            btnOk.setOnClickListener(clickListener);

        }


        return view;
    }

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }



}