package com.example.fbuapplication.fragments.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class DoNotSendDialogFragment extends DialogFragment {

    public void sendBackResult(boolean toSend) {

        DoNotSendDialogListener listener = (DoNotSendDialogListener) getTargetFragment();
        listener.onFinishDoNotSendDialog(toSend);
        dismiss();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Negative sentiment detected. Are you sure you want to send this message?")
                .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        sendBackResult(true);
                    }
                })
                .setNegativeButton("DON'T SEND", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        sendBackResult(false);
                    }
                });

        return builder.create();
    }

    public interface DoNotSendDialogListener {
        void onFinishDoNotSendDialog(boolean toSend);
    }

}