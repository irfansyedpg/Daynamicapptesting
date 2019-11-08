package edu.aku.hassannaqvi.kmc_screening.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.aku.hassannaqvi.kmc_screening.R;
import edu.aku.hassannaqvi.kmc_screening.contracts.FormsContract;
import edu.aku.hassannaqvi.kmc_screening.core.DatabaseHelper;
import edu.aku.hassannaqvi.kmc_screening.core.MainApp;
import edu.aku.hassannaqvi.kmc_screening.databinding.ActivityABinding;
import edu.aku.hassannaqvi.kmc_screening.databinding.ActivitydesignBinding;
import edu.aku.hassannaqvi.kmc_screening.generated.callback.OnClickListener;
import edu.aku.hassannaqvi.kmc_screening.other.DiseaseCode;
import edu.aku.hassannaqvi.kmc_screening.util.Util;
import edu.aku.hassannaqvi.kmc_screening.validation.ValidatorClass;

import static edu.aku.hassannaqvi.kmc_screening.core.MainApp.fc;

public class DesingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    ActivitydesignBinding bi;

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        bi = DataBindingUtil.setContentView(this, R.layout.activitydesign);
        bi.setCallback(this);

        events();

    }

    public  void events()
    {

        bi.rbSmall.setOnCheckedChangeListener(this);

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(buttonView==bi.rbSmall)
        {

            bi.labqexp.setTextSize(16);
        }

    }
}
