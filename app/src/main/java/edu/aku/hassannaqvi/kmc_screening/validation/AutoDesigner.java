package edu.aku.hassannaqvi.kmc_screening.validation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.solver.widgets.ChainHead;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.edittextpicker.aliazaz.EditTextPicker;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.lang.reflect.Field;

import edu.aku.hassannaqvi.kmc_screening.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by ali.azaz on 12/04/17.
 * modified by ramsha.seed on 7/8/2018
 */

public abstract class AutoDesigner {


    public  static  SharedPreferences prefs;
    public static boolean EmptyTextBox(Context context, EditText txt, String msg) {
        if (TextUtils.isEmpty(txt.getText().toString())) {
            FancyToast.makeText(context, "ERROR(empty): " + msg, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            txt.setError("This data is Required! ");    // Set Error on last radio button
            txt.setFocusableInTouchMode(true);
            txt.requestFocus();
            Log.i(context.getClass().getName(), context.getResources().getResourceEntryName(txt.getId()) + ": This data is Required!");
            return false;
        } else {
            txt.setError(null);
            txt.clearFocus();
            return true;
        }

    }

    private static boolean EmptyEditTextPicker(Context context, EditText txt, String msg) {
        String messageConv = "";
        boolean flag = true;
        if (!((EditTextPicker) txt).isEmptyTextBox()) {
            flag = false;
            messageConv = "ERROR(empty)";
        } else if (!((EditTextPicker) txt).isRangeTextValidate()) {
            flag = false;
            messageConv = "ERROR(range)";
        } else if (!((EditTextPicker) txt).isTextEqualToPattern()) {
            flag = false;
            messageConv = "ERROR(pattern)";
        }

        if (!flag) {
            FancyToast.makeText(context, messageConv + ": " + msg, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            Log.i(context.getClass().getName(), context.getResources().getResourceEntryName(txt.getId()) + ": " + messageConv);
            return false;
        } else {
            txt.setError(null);
            txt.clearFocus();
            return true;
        }

    }




    public static boolean EmptySpinner(Context context, Spinner spin, String msg) {
        if (spin.getSelectedItem() == "....") {
            FancyToast.makeText(context, "ERROR(Empty)" + msg, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            ((TextView) spin.getSelectedView()).setText("This Data is Required");
            ((TextView) spin.getSelectedView()).setTextColor(Color.RED);
            spin.setFocusableInTouchMode(true);
            spin.requestFocus();
            Log.i(context.getClass().getName(), context.getResources().getResourceEntryName(spin.getId()) + ": This data is Required!");
            return false;
        } else {
            ((TextView) spin.getSelectedView()).setError(null);
            return true;
        }
    }

    public static boolean EmptyRadioButton(Context context, RadioGroup rdGrp, RadioButton rdBtn, String msg,int txtsize) {


            boolean rdbFlag = true;
            for (int j = 0; j < rdGrp.getChildCount(); j++) {
                View innerV = rdGrp.getChildAt(j);
                if (innerV instanceof EditText) {


                }
                else if(innerV instanceof  RadioButton)
                {
                    RadioButton rb=(RadioButton) innerV;
                    rb.setTextSize(txtsize);
                }



        }
            return  true;
    }


    public static boolean EmptyCheckBox(Context context, LinearLayout container, CheckBox cbx, String msg) {

        Boolean flag = false;
        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            if (v instanceof CheckBox) {
                CheckBox cb = (CheckBox) v;
                cb.setError(null);

                if (!cb.isEnabled()) {
                    flag = true;
                    continue;
                } else {
                    if (!flag)
                        flag = false;
                }

                if (cb.isChecked()) {
                    flag = true;

                    for (int j = 0; j < container.getChildCount(); j++) {
                        View innerV = container.getChildAt(j);
                        if (innerV instanceof EditText) {
                            if (getIDComponent(cb).equals(innerV.getTag())) {
                                if (innerV instanceof EditTextPicker)
                                    flag = EmptyEditTextPicker(context, (EditText) innerV, getString(context, getIDComponent(innerV)));
                                else
                                    flag = EmptyTextBox(context, (EditText) innerV, getString(context, getIDComponent(innerV)));
                            }
                        }
                    }
//                    break;
                }
            }
        }
        if (!flag) {
            FancyToast.makeText(context, "ERROR(empty): " + msg, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            cbx.setError("This data is Required!");    // Set Error on last radio button

            Log.i(context.getClass().getName(), context.getResources().getResourceEntryName(cbx.getId()) + ": This data is Required!");
            return false;
        }
        return true;
    }



    public static boolean EmptyCheckingContainer(Context context, ViewGroup lv) {


        prefs=context.getSharedPreferences("desing", MODE_PRIVATE);

        int Qlabel = prefs.getInt("qtxt", 0); //0 is the default value.

        int rblabel = prefs.getInt("rbtxt", 0); //0 is the default value.

        int cblable = prefs.getInt("cbtxt", 0); //0 is the default value.
        int edlable = prefs.getInt("edtxt", 0); //0 is the default value.


        for (int i = 0; i < lv.getChildCount(); i++) {

            View view = lv.getChildAt(i);

            if (view.getVisibility() == View.GONE || !view.isEnabled())
                continue;

            if (view instanceof CardView) {
                if (!EmptyCheckingContainer(context, (ViewGroup) view)) {
                    return false;
                }
            } else if (view instanceof TextView) {
                TextView tx = (TextView) view;
                tx.setTextSize(Qlabel);

            } else if (view instanceof RadioGroup) {


                boolean radioFlag = false;
                View v = null;
                for (byte j = 0; j < ((RadioGroup) view).getChildCount(); j++) {
                    if (((RadioGroup) view).getChildAt(j) instanceof RadioButton) {
                        v = ((RadioGroup) view).getChildAt(j);
                        radioFlag = true;
                        break;
                    }
                }

                if (!radioFlag) continue;

                if (v != null) {

                    String asNamed = getString(context, getIDComponent(view));

                    if (!EmptyRadioButton(context, (RadioGroup) view, (RadioButton) v, asNamed, rblabel)) {
                        // return false;
                    }
                }
            } else if (view instanceof EditText) {

                EditText ed = (EditText) view;
                ed.setTextSize(edlable);

            } else if (view instanceof CheckBox) {

                CheckBox cb = (CheckBox) view;
                cb.setTextSize(cblable);

            }
        }
        return true;
    }

    public static String getIDComponent(View view) {
        String[] idName = (view).getResources().getResourceName((view).getId()).split("id/");

        return idName[1];
    }

    private static String getString(Context context, String idName) {

        Field[] fields = R.string.class.getFields();
        for (final Field field : fields) {

            if (field.getName().split("R$string.")[0].equals(idName)) {
                try {
                    int id = field.getInt(R.string.class); //id of string

                    return context.getString(id);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}
