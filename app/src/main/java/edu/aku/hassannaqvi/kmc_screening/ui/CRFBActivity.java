package edu.aku.hassannaqvi.kmc_screening.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.aku.hassannaqvi.kmc_screening.R;
import edu.aku.hassannaqvi.kmc_screening.contracts.FormsContract;
import edu.aku.hassannaqvi.kmc_screening.core.DatabaseHelper;
import edu.aku.hassannaqvi.kmc_screening.core.MainApp;
import edu.aku.hassannaqvi.kmc_screening.databinding.ActivityBBinding;
import edu.aku.hassannaqvi.kmc_screening.other.DiseaseCode;
import edu.aku.hassannaqvi.kmc_screening.util.Util;
import edu.aku.hassannaqvi.kmc_screening.validation.ValidatorClass;

import static edu.aku.hassannaqvi.kmc_screening.core.MainApp.fc;

public class CRFBActivity extends AppCompatActivity {

    ActivityBBinding bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        bi = DataBindingUtil.setContentView(this, R.layout.activity_b);
        bi.setCallback(this);

//        setTitle(R.string.f9aHeading);
        List<String> Dieascodelist = new ArrayList<>(DiseaseCode.HmDiseaseCode.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, Dieascodelist);
        bi.crb12.setThreshold(1); //will start working from first character
        bi.crb12.setAdapter(adapter);


        setupViews();
    }

    private void setupViews() {

    }

    public void seearch() {

    }
    public void BtnContinue() {
        if (formValidation()) {

            try {
                SaveDraft();
                if (UpdateDB()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), EndingActivity.class).putExtra("complete", true));
                } else {
                    Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean UpdateDB() {

        DatabaseHelper db = new DatabaseHelper(this);

        // 2. insert form
        Long rowId;
        rowId = db.addForm(fc);
        if (rowId > 0) {
            fc.set_ID(String.valueOf(rowId));
            fc.setUID((fc.getDeviceID() + fc.get_ID()));
            db.updateFormID(fc);
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;

        }
    }

    private void SaveDraft() throws JSONException {


        fc = new FormsContract();
        SharedPreferences sharedPref = getSharedPreferences("tagName", MODE_PRIVATE);
        fc.setTagID(sharedPref.getString("tagName", null));
        fc.setFormDate((DateFormat.format("dd-MM-yyyy HH:mm", new Date())).toString());
        fc.setDeviceID(MainApp.deviceId);
        fc.setUser(MainApp.userName);

        fc.setAppversion(MainApp.versionName + "." + MainApp.versionCode);

        JSONObject f1 = new JSONObject();
        Util.setGPS(this);
        fc.setF1(String.valueOf(f1));


        JSONObject CRFA = new JSONObject();


        CRFA.put("crb01", bi.crb01.getText().toString());

        CRFA.put("crb02", bi.crb02.getText().toString());
        CRFA.put("crb03", bi.crb03.getText().toString());

        CRFA.put("crb04a", bi.crb04a.getText().toString());
        CRFA.put("crb04b", bi.crb04b.getText().toString());
        CRFA.put("crb04c", bi.crb04c.getText().toString());

        CRFA.put("crb05", bi.crb05.getText().toString());

        CRFA.put("crb06", bi.crb06.getText().toString());

        CRFA.put("crb07a", bi.crb07a.getText().toString());
        CRFA.put("crb07b", bi.crb07b.getText().toString());
        CRFA.put("crb07c", bi.crb07c.getText().toString());
        CRFA.put("crb07d", bi.crb07d.getText().toString());

        CRFA.put("crb08", bi.crb08.getText().toString());

        CRFA.put("crb09a", bi.crb09a.getText().toString());
        CRFA.put("crb09b", bi.crb09b.getText().toString());
        CRFA.put("crb09c", bi.crb09c.getText().toString());

        CRFA.put("crb10",
                bi.crb10a.isChecked() ? "1"
                        : bi.crb10b.isChecked() ? "2"
                        : "0");

        CRFA.put("crb11", bi.crb11.getText().toString());

        CRFA.put("crb12", DiseaseCode.HmDiseaseCode.get(bi.crb12.getText().toString()));


        CRFA.put("crb13",
                bi.crb13a.isChecked() ? "1"
                        : bi.crb13b.isChecked() ? "2"
                        : bi.crb13c.isChecked() ? "3"
                        : bi.crb13d.isChecked() ? "4"
                        : "0");

        CRFA.put("crb14a", bi.crb14a.getText().toString());
        CRFA.put("crb14b", bi.crb14b.getText().toString());
        CRFA.put("crb14c", bi.crb14c.getText().toString());


        fc.setCRFA(String.valueOf(CRFA));

        fc.setFormType("CRFB");
        fc.setstudyid(bi.crb01.getText().toString());


    }

    private boolean formValidation() {

        return ValidatorClass.EmptyCheckingContainer(this, bi.GrpCRFB);


    }


    public void BtnEnd() {

        MainApp.endActivity(this, this);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back.", Toast.LENGTH_SHORT).show();
    }
}
