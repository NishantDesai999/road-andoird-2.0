package ml.uncoded.margsahayak;

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
         settigs = (TextView) view.findViewById(R.id.bottomsheet_settings);
         logout = (TextView) view.findViewById(R.id.bottomsheet_logout);

         contactus.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });

         settigs.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });

         logout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 SharedPrefrenceUser.getInstance(getActivity()).logout();
                 getActivity().finish();
             }
         });

        return view;
    }

}
