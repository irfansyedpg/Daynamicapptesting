package edu.aku.hassannaqvi.kmc_screening.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import edu.aku.hassannaqvi.kmc_screening.R;
import edu.aku.hassannaqvi.kmc_screening.contracts.FormsContract;
import edu.aku.hassannaqvi.kmc_screening.contracts.TalukasContract;
import edu.aku.hassannaqvi.kmc_screening.contracts.UCsContract;
import edu.aku.hassannaqvi.kmc_screening.contracts.VillagesContract;
import edu.aku.hassannaqvi.kmc_screening.core.DatabaseHelper;
import edu.aku.hassannaqvi.kmc_screening.core.MainApp;
import edu.aku.hassannaqvi.kmc_screening.databinding.ActivityInfoBinding;
import edu.aku.hassannaqvi.kmc_screening.util.Util;
import edu.aku.hassannaqvi.kmc_screening.validation.ValidatorClass;

import static edu.aku.hassannaqvi.kmc_screening.core.MainApp.fc;

public class InfoActivity extends AppCompatActivity {


    ActivityInfoBinding bi;
    Collection<UCsContract> ucsList;
    ArrayList<String> ucsName;
    HashMap<String, String> ucsMap;
    Collection<TalukasContract> talukaList;
    ArrayList<String> talukaName;
    HashMap<String, String> talukaMap;

    Collection<VillagesContract> villagesList;
    ArrayList<String> villagesNames;
    HashMap<String, String> villagesMap;

    int length = 0;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bi = DataBindingUtil.setContentView(this, R.layout.activity_info);
        bi.setCallback(this);


//        setTitle(R.string.f1aHeading);

        setupViews();
    }

    private void setupViews() {

        MainApp.formType = getIntent().getStringExtra(MainApp.formType);
        db = new DatabaseHelper(this);
        talukaList = db.getTalukaList();
        talukaMap = new HashMap<>();
        talukaName = new ArrayList<>();
        talukaName.add("Select Taluka-");

        for (TalukasContract tc : talukaList) {
            talukaName.add(tc.getDistrictName());
            talukaMap.put(tc.getDistrictName(), tc.getDistrictCode());
        }

        bi.infotak.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, talukaName));

        bi.infotak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (bi.infotak.getSelectedItemPosition() != 0) {
                    ucsList = db.getUcs(talukaMap.get(bi.infotak.getSelectedItem().toString()));
                    ucsMap = new HashMap<>();
                    ucsName = new ArrayList<>();
                    ucsName.add("Select UC Name-");

                    for (UCsContract uc : ucsList) {
                        ucsName.add(uc.getUcsName());
                        ucsMap.put(uc.getUcsName(), uc.getUccode());
                    }

                    bi.infouc.setAdapter(new ArrayAdapter<>(InfoActivity.this, android.R.layout.simple_spinner_dropdown_item, ucsName));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bi.infouc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (bi.infouc.getSelectedItemPosition() != 0) {
                    villagesList = db.getVillages(ucsMap.get(bi.infouc.getSelectedItem().toString()));
                    villagesMap = new HashMap<>();
                    villagesNames = new ArrayList<>();
                    villagesNames.add("Select Village Name-");

                    for (VillagesContract vil : villagesList) {
                        villagesNames.add(vil.getVillageName());
                        villagesMap.put(vil.getVillageName(), vil.getVillageCode());
                    }

                    bi.infovil.setAdapter(new ArrayAdapter<>(InfoActivity.this, android.R.layout.simple_spinner_dropdown_item, villagesNames));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void BtnContinue() {
        if(bi.infotak.getSelectedItem().toString().length()==0 || bi.infotak.getSelectedItem().equals("Select Taluka-"))
        {
            Toast.makeText(this,"Please select Tehsil",Toast.LENGTH_LONG).show();
            return;

        }

        if(bi.infouc.getSelectedItem().toString().length()==0 || bi.infouc.getSelectedItem().equals("Select UC Name-"))
        {
            Toast.makeText(this,"Please select UC",Toast.LENGTH_LONG).show();
            return;

        }

        if(bi.infovil.getSelectedItem().toString().length()==0 || bi.infovil.getSelectedItem().equals("Select Village Name-"))
        {
            Toast.makeText(this,"Please select Vilage",Toast.LENGTH_LONG).show();
            return;

        }
        if (formValidation()) {
            try {
                SaveDraft();
                if (UpdateDB()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainApp.formType.equals("f1") ? F1Activity.class
                            : MainApp.formType.equals("f2") ? F2Activity.class : F3Activity.class));
                } else {
                    Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean UpdateDB() {

        Long rowId;
        DatabaseHelper db = new DatabaseHelper(this);
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
        fc.setFormType(MainApp.formType);
        fc.setUc(ucsMap.get(bi.infouc.getSelectedItem().toString()));
        fc.setVillage(villagesMap.get(bi.infovil.getSelectedItem().toString()));
        fc.setTalukdaCode(talukaMap.get(bi.infotak.getSelectedItem().toString()));
        fc.setAppversion(MainApp.versionName + "." + MainApp.versionCode);
        JSONObject f1 = new JSONObject();
        Util.setGPS(this);

        fc.setF1(String.valueOf(f1));

    }

    private boolean formValidation() {

        return ValidatorClass.EmptyCheckingContainer(this, bi.fldGrpSectionF1A);
    }

    public void BtnEnd() {

        MainApp.endActivity(this, this);
    }


}
