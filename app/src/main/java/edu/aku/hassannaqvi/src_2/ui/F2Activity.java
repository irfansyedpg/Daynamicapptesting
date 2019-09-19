package edu.aku.hassannaqvi.src_2.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.src_2.R;
import edu.aku.hassannaqvi.src_2.core.DatabaseHelper;
import edu.aku.hassannaqvi.src_2.core.MainApp;
import edu.aku.hassannaqvi.src_2.databinding.ActivityF1Binding;
import edu.aku.hassannaqvi.src_2.databinding.ActivityF2Binding;
import edu.aku.hassannaqvi.src_2.validation.ValidatorClass;

public class F2Activity extends AppCompatActivity {

    ActivityF2Binding bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bi = DataBindingUtil.setContentView(this, R.layout.activity_f2);
        bi.setCallback(this);

//        setTitle(R.string.f9aHeading);

        setupViews();
    }

    private void setupViews() {

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

        // 2. UPDATE FORM ROWID
        int updcount = db.updatesF1();

        if (updcount == 1) {
            Toast.makeText(this, "Updating Database... Successful!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void SaveDraft() throws JSONException {


        JSONObject f1 = new JSONObject();
        f1.put("f2wgq1a", bi.f2wgq1a.isChecked() ? "1" : "0");
        f1.put("f2wgq1b", bi.f2wgq1b.isChecked() ? "2" : "0");
        f1.put("f2wgq1c", bi.f2wgq1c.isChecked() ? "3" : "0");
        f1.put("f2wgq1d", bi.f2wgq1d.isChecked() ? "4" : "0");
        f1.put("f2wgq1e", bi.f2wgq1e.isChecked() ? "5" : "0");
        f1.put("f2wgq1f", bi.f2wgq1f.isChecked() ? "6" : "0");
        f1.put("f2wgq1g", bi.f2wgq1g.isChecked() ? "7" : "0");
        f1.put("f2wgq1h", bi.f2wgq1h.isChecked() ? "8" : "0");
        f1.put("f2wgq1i", bi.f2wgq1i.isChecked() ? "9" : "0");
        f1.put("f2wgq1j", bi.f2wgq1j.isChecked() ? "10" : "0");
        f1.put("f2wgq1k", bi.f2wgq1k.isChecked() ? "11" : "0");

        f1.put("f2wgq2", bi.f2wgq2.getText().toString());
        f1.put("f2wgq3", bi.f2wgq3.getText().toString());
        f1.put("f2wgq4", bi.f2wgq4.getText().toString());


        f1.put("f2wgq5", bi.f2wgq5a.isChecked() ? "1" : bi.f2wgq5b.isChecked() ? "2" : bi.f2wgq5c.isChecked() ? "3"
                : bi.f2wgq596.isChecked() ? "96" : "0");
        
        f1.put("f2wgq596x", bi.f2wgq596x.getText().toString());

        f1.put("f2wgq6", bi.f2wgq6.getText().toString());


        MainApp.fc.setF1(String.valueOf(f1));

    }

    private boolean formValidation() {

        return ValidatorClass.EmptyCheckingContainer(this, bi.fldGrpF2);
    }

    public void BtnEnd() {

        MainApp.endActivity(this, this);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back.", Toast.LENGTH_SHORT).show();
    }
}
