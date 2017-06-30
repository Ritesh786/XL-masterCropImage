package com.extralarge.fujitsu.xl.Spinner;

import android.content.Context;
import android.util.AttributeSet;

import com.extralarge.fujitsu.xl.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by lue on 30-06-2017.
 */

public class MySpinnerLight extends MaterialBetterSpinner {
    public MySpinnerLight(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        setUpController(arg0,arg1);
    }

    private void setUpController(Context arg0, AttributeSet arg1) {
        setFocusFraction(1f);
        setFloatingLabel(FLOATING_LABEL_NORMAL);
        setFloatingLabelTextColor(getResources().getColor(R.color.black));
        setBaseColor(getResources().getColor(R.color.black));
        setTextColor(getResources().getColor(R.color.black));
        setHintTextColor(getResources().getColor(R.color.black));
        setPrimaryColor(getResources().getColor(R.color.colorPrimary));
        setErrorColor(getResources().getColor(R.color.colorPrimary));
        setHelperTextColor(getResources().getColor(R.color.orange));
        setUnderlineColor(getResources().getColor(R.color.orange));
        //     setTextSize(MyConstants.convertDpToPixel(8));

    }

    public MySpinnerLight(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        setUpController(arg0,arg1);
    }

    public MySpinnerLight(Context context) {
        super(context);
    }

//    @Override
//
//    protected void performFiltering(CharSequence text, int keyCode) {
//        try {
//            super.performFiltering(text, keyCode);
//        }catch (Exception e){
//
//        }
//    }
}