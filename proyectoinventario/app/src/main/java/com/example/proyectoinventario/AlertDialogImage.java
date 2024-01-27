package com.example.proyectoinventario;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AlertDialogImage extends DialogFragment {

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        View viewImage = getLayoutInflater().inflate(R.layout.view_image,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView = viewImage.findViewById(R.id.imageViewww);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageView.setImageDrawable(((ProductoInfoInventActivity)getActivity()).ivSelect.getDrawable());
        }
        builder.setView(viewImage);
        return builder.create();
    }
}
