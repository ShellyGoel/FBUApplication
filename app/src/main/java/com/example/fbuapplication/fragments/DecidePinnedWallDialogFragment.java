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

import java.lang.reflect.Array;
import java.util.ArrayList;


public class DecidePinnedWallDialogFragment extends DialogFragment {

    private boolean[] selectedItems = new boolean[] { false, false, false, true,
            true, false };
    private boolean[] clickedItems = selectedItems;

    // Defines the listener interface
    //dialog fragment may be invoked within the context of another fragment.
    //In this case, we may want to pass the date back not to the activity but instead to the parent fragment.
    public interface DecidePinnedWallDialogListener {
        void onFinishDecidePinnedWallDialog(boolean[] toSend);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult(boolean[] toSend) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        DecidePinnedWallDialogListener listener = (DecidePinnedWallDialogListener) getTargetFragment();
        listener.onFinishDecidePinnedWallDialog(toSend);
        dismiss();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] languages = getResources().getStringArray(
                R.array.walls_array);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity())
                .setTitle("Which wall do you want to pin the note to?")
                .setMultiChoiceItems(R.array.walls_array, selectedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                clickedItems[which]= isChecked;
//                                Toast.makeText(getActivity(), languages[which],
//                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItems = clickedItems;
                        sendBackResult(selectedItems);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = alertDialogBuilder.create();
        return dialog;
    }
//
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the Builder class for convenient dialog construction
//        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        ArrayList<Integer> pinTo = new ArrayList<>();
//        builder.setTitle("Which wall do you want to pin the note to?");
//        builder.setMultiChoiceItems(R.array.walls_array, null, new DialogInterface.OnMultiChoiceClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i, boolean b)
//            {
////                Toast.makeText(getActivity(), "item clicked at " + i, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//
//        Dialog dialog = builder.create();
//        return dialog;
//
////        builder.setTitle("Which wall do you want to pin the note to?")
////                .setItems(R.array.walls_array, new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int which) {
////                        // The 'which' argument contains the index position
////                        // of the selected item
////
////                        sendBackResult(which);
////                    }
////                });
////
////        return builder.create();
//
//    }


}


