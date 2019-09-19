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
import edu.aku.hassannaqvi.src_2.validation.ValidatorClass;

public class F2Activity extends AppCompatActivity {

    ActivityF1Binding bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bi = DataBindingUtil.setContentView(this, R.layout.activity_f1);
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
        f1.put("f1wgq1a", bi.f1wgq1a.isChecked() ? "1" : "0");
        f1.put("f1wgq1b", bi.f1wgq1b.isChecked() ? "2" : "0");
        f1.put("f1wgq1c", bi.f1wgq1c.isChecked() ? "3" : "0");
        f1.put("f1wgq1d", bi.f1wgq1d.isChecked() ? "4" : "0");
        f1.put("f1wgq1e", bi.f1wgq1e.isChecked() ? "5" : "0");
        f1.put("f1wgq1f", bi.f1wgq1f.isChecked() ? "6" : "0");
        f1.put("f1wgq1g", bi.f1wgq1g.isChecked() ? "7" : "0");
        f1.put("f1wgq1h", bi.f1wgq1h.isChecked() ? "8" : "0");
        f1.put("f1wgq1i", bi.f1wgq1i.isChecked() ? "9" : "0");
        f1.put("f1wgq1j", bi.f1wgq1j.isChecked() ? "10" : "0");
        f1.put("f1wgq1k", bi.f1wgq1k.isChecked() ? "11" : "0");
        f1.put("f1wgq2", bi.f1wgq2.getText().toString());
        f1.put("f1wgq3", bi.f1wgq3.getText().toString());
        f1.put("f1wgq4", bi.f1wgq4.getText().toString());
        f1.put("f1wgq5", bi.f1wgq5.getText().toString());


        f1.put("f1wgq6", bi.f1wgq6a.isChecked() ? "1" : bi.f1wgq6b.isChecked() ? "2" : bi.f1wgq6c.isChecked() ? "3"
                : bi.f1wgq696.isChecked() ? "96" : "0");
        f1.put("f1wgq696x", bi.f1wgq696x.getText().toString());
        f1.put("f1wgq7", bi.f1wgq7.getText().toString());

        MainApp.fc.setF1(String.valueOf(f1));

    }

    private boolean formValidation() {

        return ValidatorClass.EmptyCheckingContainer(this, bi.fldGrpF1);
    }

    public void BtnEnd() {

        MainApp.endActivity(this, this);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back.", Toast.LENGTH_SHORT).show();
    }
}
