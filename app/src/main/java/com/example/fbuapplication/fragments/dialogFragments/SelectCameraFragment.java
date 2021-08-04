package com.example.fbuapplication.fragments.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class SelectCameraFragment extends DialogFragment {

    public void sendBackResult(boolean toSend) {

        SelectCameraDialogListener listener = (SelectCameraDialogListener) getTargetFragment();
        listener.onFinishSelectCameraDialog(toSend);
        dismiss();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Do you want to take a photo or choose an image from your gallery?")
                .setPositiveButton("TAKE A PHOTO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        sendBackResult(true);
                    }
                })
                .setNegativeButton("CHOOSE GALLERY IMAGE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        sendBackResult(false);
                    }
                });

        return builder.create();
    }

    public interface SelectCameraDialogListener {
        void onFinishSelectCameraDialog(boolean toSend);
    }

}