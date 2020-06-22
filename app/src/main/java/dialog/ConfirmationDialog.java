package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.balance.R;

public class ConfirmationDialog extends DialogFragment {

    private ConfirmationDialogListener listener = null;

    public void setListener(ConfirmationDialogListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.remove)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      if(listener !=null){
                          listener.onPositiveBtnClicked();
                      }
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      if(listener != null){
                          listener.onNegativeBtnClicked();
                        }
                    }
                })
                .create();
    }
}
