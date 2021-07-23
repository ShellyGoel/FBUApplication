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

import com.example.fbuapplication.MessagesInboxAdapter;
import com.example.fbuapplication.R;


public class DecidePinnedWallDialogFragment extends DialogFragment {


    // Defines the listener interface
    //dialog fragment may be invoked within the context of another fragment.
    //In this case, we may want to pass the date back not to the activity but instead to the parent fragment.
    public interface DecidePinnedWallDialogListener {
        void onFinishDecidePinnedWallDialog(int toSend);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult(int toSend) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        DecidePinnedWallDialogListener listener = (DecidePinnedWallDialogListener) getTargetFragment();
        listener.onFinishDecidePinnedWallDialog(toSend);
        dismiss();
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Which wall do you want to pin the note to?")
                .setItems(R.array.walls_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item

                        sendBackResult(which);
                    }
                });

        return builder.create();

    }


}


