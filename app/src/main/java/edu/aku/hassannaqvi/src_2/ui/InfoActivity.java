package edu.aku.hassannaqvi.src_2.ui;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import edu.aku.hassannaqvi.src_2.R;
import edu.aku.hassannaqvi.src_2.contracts.FormsContract;
import edu.aku.hassannaqvi.src_2.contracts.UCsContract;
import edu.aku.hassannaqvi.src_2.contracts.VillagesContract;
import edu.aku.hassannaqvi.src_2.core.DatabaseHelper;
import edu.aku.hassannaqvi.src_2.core.MainApp;
import edu.aku.hassannaqvi.src_2.databinding.ActivityInfoBinding;
import edu.aku.hassannaqvi.src_2.util.Util;
import edu.aku.hassannaqvi.src_2.validation.ValidatorClass;

import static edu.aku.hassannaqvi.src_2.core.MainApp.fc;

public class InfoActivity extends AppCompatActivity {


    ActivityInfoBinding bi;
    Collection<UCsContract> ucsList;
    ArrayList<String> ucsName;
    HashMap<String, String> ucsMap;

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

//        db = new DatabaseHelper(this);
//        villagesMap = new HashMap<>();
//        villagesNames = new ArrayList<>();
//        villagesNames.add("Select Village Name-");
//        bi.f1a01.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if (!s.toString().equals("")) {
//                    if (s.toString().length() == 2) {
//                        villagesList = db.getVillages(bi.f1a01.getText().toString());
//                        for (VillagesContract hf : villagesList) {
//                            villagesNames.add(hf.getVillageName());
//                            villagesMap.put(hf.getVillageName(), hf.getVillageCode());
//                        }
//
//                        bi.f1a02.setAdapter(new ArrayAdapter<>(InfoActivity.this, android.R.layout.simple_spinner_dropdown_item, villagesNames));
//
//                    }
//                } else {
//                    if (villagesList.size() != 0) {
//                        villagesMap.clear();
//                        villagesNames.clear();
//                        villagesList.clear();
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

//        ucsList = db.getUCsList();
//        ucsName = new ArrayList<>();
//        ucsMap = new HashMap<>();
//        ucsName.add("-Select District-");
//
//        for (UCsContract dc : ucsList) {
//            ucsName.add(dc.getUcsName());
//            ucsMap.put(dc.getUcsName(), dc.getUccode());
//        }
//
//        bi.f1a01.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ucsName));
//
//        bi.f1a01.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if (bi.f1a01.getSelectedItemPosition() != 0) {
//
////                    districtCode = ucsMap.get(bi.districtSpinner.getSelectedItem().toString());
//                    villagesList = db.getVillages(ucsMap.get(bi.f1a01.getSelectedItem().toString()));
//                    villagesMap = new HashMap<>();
//                    villagesNames = new ArrayList<>();
//                    villagesNames.add("Select Village Name-");
//
//                    for (VillagesContract hf : villagesList) {
//                        villagesNames.add(hf.getVillageName());
//                        villagesMap.put(hf.getVillageName(), hf.getVillageCode());
//                    }
//
//                    bi.f1a02.setAdapter(new ArrayAdapter<>(InfoActivity.this, android.R.layout.simple_spinner_dropdown_item, villagesNames));
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


    }

    public void BtnContinue() {
        if (formValidation()) {
            try {
                SaveDraft();
                if (UpdateDB()) {
                    finish();
                    //startActivity(new Intent(getApplicationContext(), F1SectionBActivity.class));
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
