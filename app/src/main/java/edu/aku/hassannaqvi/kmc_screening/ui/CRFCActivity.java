package edu.aku.hassannaqvi.kmc_screening.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import edu.aku.hassannaqvi.kmc_screening.JSONModels.JSONModelCRFA;
import edu.aku.hassannaqvi.kmc_screening.JsonUtils.JSONUtils;
import edu.aku.hassannaqvi.kmc_screening.R;
import edu.aku.hassannaqvi.kmc_screening.contracts.FormsContract;
import edu.aku.hassannaqvi.kmc_screening.core.DatabaseHelper;
import edu.aku.hassannaqvi.kmc_screening.core.MainApp;
import edu.aku.hassannaqvi.kmc_screening.databinding.ActivityBBinding;
import edu.aku.hassannaqvi.kmc_screening.databinding.ActivityCBinding;
import edu.aku.hassannaqvi.kmc_screening.other.DiseaseCode;
import edu.aku.hassannaqvi.kmc_screening.util.Util;
import edu.aku.hassannaqvi.kmc_screening.validation.ValidatorClass;

import static edu.aku.hassannaqvi.kmc_screening.core.MainApp.fc;

public class CRFCActivity extends AppCompatActivity {

    private static final String TAG = "CRFCActivity";
   public static ActivityCBinding bi;
    DatabaseHelper db;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    public  static boolean days_21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_c);
        bi.setCallback(this);
        db = new DatabaseHelper(this);

        days_21=true;

        get_data_recylceview("0");



    }

    public  void day21()
    {

        get_data_recylceview("0");
        days_21=true;

    }

    public  void day28()
    {
        get_data_recylceview("1");

        days_21=false;

    }

    public  void get_data_recylceview(String crfstatus)
    {
        // list here
        List<String> list =getdata(crfstatus);

        if(crfstatus=="0")
        {
            bi.btn21.setText("21 Days Follow-Up"+" ("+list.size()+")");
        }
        else
        {
            bi.btn48.setText("28 Days Follow-Up"+" ("+list.size()+")");
        }


        if(list == null)
            return;


        Collections.sort(list);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_survey_completed);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SurveyCompletedCustomAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);

    }




    public  List<String> getdata(String crfstatus)
    {
        List<String> lst_string= new ArrayList<String>();
        List<FormsContract> lst = db.getsFormContractCRFC(crfstatus);
        if (lst!= null) {

            for(FormsContract fc: lst)
            {

                JSONModelCRFA crfa = JSONUtils.getModelFromJSON(fc.getCRFA(), JSONModelCRFA.class);
                String stringg="";
                stringg=crfa.getCra01()+"-"+crfa.getCra02()+"-"+crfa.getCra04()+"-"+crfa.getCra05()+"-"+
                        crfa.getCra03a()+"/"+ crfa.getCra03b()+"/" +crfa.getCra03c();
                lst_string.add(stringg);

            }


        }

        return lst_string;
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


        /*
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


*/

    }







    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back.", Toast.LENGTH_SHORT).show();
    }

}

class  SurveyCompletedCustomAdapter extends RecyclerView.Adapter{

    Context mContext;
    List<String> mList;
    public SurveyCompletedCustomAdapter(Context context, List<String> list){
        mContext = context;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crfcitems, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ViewHolder vh = (ViewHolder) holder;

        //rejected items..
       // if(mList.get(position).contains("-00")) {
         //   vh.itemView.setBackgroundColor(Color.parseColor("#FFB7BC"));
       // }



        vh.studyid.setText(mList.get(position).split("-")[0]);

        vh.opdnum.setText(mList.get(position).split("-")[1]);

        vh.name.setText(mList.get(position).split("-")[2]);
        vh.fname.setText(mList.get(position).split("-")[3]);

        vh.serial.setText(position+1 +"");

        String Pdate=mList.get(position).split("-")[4];

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate=null;

        Calendar c = Calendar.getInstance();



        try {
             strDate = sdf.parse(Pdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.setTime(strDate);


        if(CRFCActivity.days_21==true) {
            vh.txtdate.setText("Date Dute to Notify LHS");


            c.add(Calendar.DATE, 23);


                Pdate = sdf.format(c.getTime());

            vh.date.setText(Pdate.toString());

        }
        else
        {
            vh.txtdate.setText("Date 28-Days Follow up Due");

            c.add(Calendar.DATE, 31);

            Pdate = sdf.format(c.getTime());

        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(CRFCActivity.days_21==true)
                {
                    String studyid=vh.studyid.getText().toString();

                    String datetotnotify=vh.date.getText().toString();

                    dialog21days(studyid,datetotnotify);
                }
                else
                {
                    String studyid=vh.studyid.getText().toString();

                    String datetotnotify=vh.date.getText().toString();

                    dialog28days(studyid,datetotnotify);
                }
            }
        });


    }

    public  void dialog21days(final String studyid, final String datetotnotify)
    {
        // 21 days
        AlertDialog.Builder b = new AlertDialog.Builder(mContext);

        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.crfc21d);

       final EditText day21=dialog.findViewById(R.id.day21);
       final EditText month21=dialog.findViewById(R.id.month21);
       final EditText year21=dialog.findViewById(R.id.year21);
       final EditText lhscode=dialog.findViewById(R.id.lhscode);

       final Button btncancel=dialog.findViewById(R.id.btn_End);
       final Button btnok=dialog.findViewById(R.id.btn_Continue);




        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // String studid=vh.
                if(

                                day21.getText().length()==0 ||
                                month21.getText().length()==0 ||
                                        lhscode.getText().length()==0 ||
                                year21.getText().length()==0)
                {

                    Toast.makeText(mContext,"Please enter the data",Toast.LENGTH_LONG).show();
                    return;
                }


                try {

                    fc = new FormsContract();
                    JSONObject CRFC = new JSONObject();

                    CRFC.put("crc08", lhscode.getText().toString());
                    CRFC.put("crc09a", day21.getText().toString());
                    CRFC.put("crc09b", month21.getText().toString());
                    CRFC.put("crc09c", year21.getText().toString());
                    CRFC.put("crc10", datetotnotify);


                    fc.setcrfc21(String.valueOf(CRFC));

                    if (UpdateDBCRF21(studyid)) {

                        Toast.makeText(mContext, "Updated", Toast.LENGTH_SHORT).show();



                        CRFCActivity.bi.btn21.performClick();

                        dialog.dismiss();

                    } else {
                       Toast.makeText(mContext, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        dialog.show();

    }


    private boolean UpdateDBCRF21(String studyid) {
        DatabaseHelper db = new DatabaseHelper(mContext);

        int updcount = db.updatecrf21(studyid);

        if (updcount == 1) {

            return true;
        } else {
            Toast.makeText(mContext, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean UpdateDBCRF28(String studyid) {
        DatabaseHelper db = new DatabaseHelper(mContext);

        int updcount = db.updatecrf28(studyid);

        if (updcount == 1) {

            return true;
        } else {
            Toast.makeText(mContext, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    public  void dialog28days(final String studyid, final String date28days)
    {
        // 28 days
        AlertDialog.Builder b = new AlertDialog.Builder(mContext);

        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.crfc28d);

        final RadioButton crfc12a,crfc12b,crfc12c,crfc12d,crfc13a,crfc13b;

        crfc12a=dialog.findViewById(R.id.crfc12a);
        crfc12b=dialog.findViewById(R.id.crfc12b);
        crfc12c=dialog.findViewById(R.id.crfc12c);
        crfc12d=dialog.findViewById(R.id.crfc12d);

        crfc13a=dialog.findViewById(R.id.crfc13a);
        crfc13b=dialog.findViewById(R.id.crfc13b);


        Button btncancel=dialog.findViewById(R.id.btn_End);
        Button btnok=dialog.findViewById(R.id.btn_Continue);


        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // String studid=vh.
                if(
                       !crfc12a.isChecked() &
                               !crfc12b.isChecked() &
                               !crfc12c.isChecked() &
                               !crfc12d.isChecked()
                        )
                {

                    Toast.makeText(mContext,"Please select  the Discharge",Toast.LENGTH_LONG).show();
                    return;
                }

                if(

                                !crfc13a.isChecked() &
                                !crfc13b.isChecked() )
                {

                    Toast.makeText(mContext,"Please select status ",Toast.LENGTH_LONG).show();
                    return;
                }


                try {

                    fc = new FormsContract();
                    JSONObject CRFC = new JSONObject();


                    String crf12="0";
                    if(crfc12a.isChecked())
                    {
                        crf12="1";
                    }
                    else if(crfc12b.isChecked())
                    {
                        crf12="2";
                    }
                    else if(crfc12c.isChecked())
                    {
                        crf12="3";
                    }
                    else if(crfc12d.isChecked())
                    {
                        crf12="4";
                    }
                    else

                    {  crf12="0";

                    }


                    String crf13="0";
                    if(crfc13a.isChecked())
                    {
                        crf13="1";
                    }
                    else if(crfc13b.isChecked())
                    {
                        crf13="2";
                    }



                    CRFC.put("crc12", crf12);
                    CRFC.put("crc13", crf13);
                    CRFC.put("crc11", date28days);



                    fc.setcrfc21(String.valueOf(CRFC));

                    if (UpdateDBCRF28(studyid)) {

                        Toast.makeText(mContext, "Updated", Toast.LENGTH_SHORT).show();

                        CRFCActivity.bi.btn48.performClick();
                        dialog.dismiss();

                    } else {
                        Toast.makeText(mContext, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        dialog.show();

    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView studyid, serial,name,fname,date,txtdate,opdnum;

        public ViewHolder(View v) {
            super(v);

            studyid = (TextView) v.findViewById(R.id.studyid);
            serial = (TextView) v.findViewById(R.id.serial);
            name = (TextView) v.findViewById(R.id.name);
            fname = (TextView) v.findViewById(R.id.fname);
            txtdate = (TextView) v.findViewById(R.id.txtdate);
            date = (TextView) v.findViewById(R.id.date);

            opdnum = (TextView) v.findViewById(R.id.opdn);

        }
    }
}
