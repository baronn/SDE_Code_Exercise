package com.example.sde;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class AlertGenerationUtils {

    public static void showAlert(String title, String msg, Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(msg).setTitle(title);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void showConfirmAlert(String title, String msg, Context ctx,
                                               DialogInterface.OnClickListener listenerCallbackYes,
                                               DialogInterface.OnClickListener listenerCallbackNo,
                                               String btnYesText, String btnNoText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(msg).setTitle(title);
        builder.setPositiveButton(btnYesText, listenerCallbackYes);
        builder.setNegativeButton(btnNoText, listenerCallbackNo);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }


}
