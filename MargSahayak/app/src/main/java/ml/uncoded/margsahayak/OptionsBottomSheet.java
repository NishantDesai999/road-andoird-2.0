package ml.uncoded.margsahayak;
import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ml.uncoded.margsahayak.Auth.SharedPrefrenceUser;

public class OptionsBottomSheet extends BottomSheetDialogFragment {

    public static OptionsBottomSheet getInstance(){
        return new OptionsBottomSheet();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.bottom_sheet_options,container,false);

        TextView filter, help, contactus, settigs, logout;
        contactus = (TextView) view.findViewById(R.id.bottomsheet_contactus);
        logout = (TextView) view.findViewById(R.id.bottomsheet_logout);
        settigs = (TextView) view.findViewById(R.id.bottomsheet_settings);
        help = (TextView) view.findViewById(R.id.bottomsheet_help);

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ContactUsActivity.class));
                dismiss();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefrenceUser.getInstance(getActivity()).logout();
                getActivity().finish();
            }
        });


        settigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SettingsActivity.class));
                dismiss();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager prefManager = new PrefManager(getActivity());
                prefManager.setfromSettingsToIntro(true);
                startActivity(new Intent(getActivity(),IntroActivity.class));
                dismiss();
            }
        });

        return view;
    }

}
