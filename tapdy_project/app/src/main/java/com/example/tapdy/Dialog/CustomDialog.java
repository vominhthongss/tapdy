package com.example.tapdy.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.tapdy.R;

public class CustomDialog extends Dialog
{
    ImageView YES,NO;
    Activity context;
    public static Boolean Yes_LuuLai=false;
    public CustomDialog(@NonNull final Context context)
    {
        super(context);
        this.context= (Activity) context;
        setContentView(R.layout.dialog_form);
        YES = findViewById(R.id.YES);
        NO = findViewById(R.id.NO);
        setTitle("Xác nhận");
        setCanceledOnTouchOutside(false);
        YES.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //((Activity) context).finish();
                Yes_LuuLai=true;
                dismiss();
            }
        });
        NO.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                dismiss();
            }
        });
    }
}
