package app.pricetag.com.price_tag.dao;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by shekhar on 2/9/14.
 */
public class DialogDao extends DialogFragment {

  public DialogDao() {
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the Builder class for convenient dialog construction
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("Title")
        .setPositiveButton("Fire", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // FIRE ZE MISSILES!
          }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // User cancelled the dialog
          }
        });
    // Create the AlertDialog object and return it
    return builder.create();
  }
}