package com.example.fbuapplication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapplication.R;


public class DoNotSendDialogFragment extends DialogFragment {
    // Defines the listener interface
    //dialog fragment may be invoked within the context of another fragment.
    //In this case, we may want to pass the date back not to the activity but instead to the parent fragment.
    public interface DoNotSendDialogListener {
        void onFinishDoNotSendDialog(boolean toSend);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult(boolean toSend) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        DoNotSendDialogListener listener = (DoNotSendDialogListener) getTargetFragment();
        listener.onFinishDoNotSendDialog(toSend);
        dismiss();
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

//
//        Bundle mArgs = getArguments();
//        String myValue = mArgs.getString("keyUsed to send it...");

        builder.setMessage("Negative sentiment detected. Are you sure you want to send this message?")
                .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        sendBackResult(true);
                    }
                })
                .setNegativeButton("DON'T SEND", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialogs
                        sendBackResult(false);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}