package backbencers.nub.dailycostcalc.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import backbencers.nub.dailycostcalc.R;

public class AddDebitActivity extends AppCompatActivity {

    private ImageButton ibDebitSave, ibDebitCancel;
    private TextView tvDebitDate;

    private Spinner spCategory;

    private EditText etDebitDescription;

    private EditText etDebitTotalAmount;

    private Button btnDebitScan;

    private Context context;

    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }





    /*public DebitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_debit, container, false);

        context = getActivity().getApplicationContext();

        inits(view);

        setValues();

        Log.d("DebitFragment","OnCreateView");

        return view;
    }

    private void setValues(){

        //Fetch

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREF_DEBIT_NAME, MODE_PRIVATE);

        if(sharedPreferences.getBoolean(Constant.PREF_IS_DATA_AVAILABLE,false) == true) {

            Gson gson = new Gson();
            String json = sharedPreferences.getString(Constant.PREF_DEBIT_OBJECT, "");
            Log.d("DebitFragment", "json: " + json);

            Debit debit = gson.fromJson(json, Debit.class);

            if (debit != null) {
                spCategory.setSelection(list.indexOf(debit.getDebitCategory()));
                etDebitDescription.setText(debit.getDebitDescription());
                etDebitTotalAmount.setText(debit.getDebitAmount() + "");
            }

            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putBoolean(Constant.PREF_IS_DATA_AVAILABLE,false);
            prefsEditor.putString(Constant.PREF_DEBIT_OBJECT,"");

            prefsEditor.commit();
        }

    }

    private void inits(View view){

        ibDebitSave = (ImageButton) view.findViewById(R.id.ibDebitSave);
        ibDebitCancel = (ImageButton) view.findViewById(R.id.ibDebitCancel);

        tvDebitDate = (TextView) view.findViewById(R.id.tvDebitDate);

        spCategory = (Spinner) view.findViewById(R.id.spCategory);

        etDebitDescription = (EditText) view.findViewById(R.id.etDebitDescription);
        etDebitTotalAmount = (EditText) view.findViewById(R.id.etDebitTotalAmount);

        btnDebitScan = (Button) view.findViewById(R.id.btnDebitScan);

        String date = new SimpleDateFormat("MMM dd, yyyy").format(new Date());

        tvDebitDate.setText(date);

        setCategory();

        //Actions
        ibDebitSave.setOnClickListener(this);
        ibDebitCancel.setOnClickListener(this);
        btnDebitScan.setOnClickListener(this);

//        ibDebitSave.setVisibility(View.GONE);
//        ibDebitCancel.setVisibility(View.GONE);

    }

    private void setCategory(){

        String[] categorys = getResources().getStringArray(R.array.categorys);

        list = new ArrayList<>(Arrays.asList(categorys));

        // default selected item
        list.add(0,"Select Category");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, list);
        spCategory.setAdapter(adapter);

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.btnDebitScan){

            if(spCategory.getSelectedItemPosition() > 0){

                moveToScan();

            } else{
                spCategory.performClick();
            }


        } else if( id == R.id.ibDebitSave){

            if(spCategory.getSelectedItemPosition() > 0) {
                if (etDebitDescription.getText().toString().trim().length() > 0 && etDebitTotalAmount.getText().toString().trim().length() > 0) {

                    String date = tvDebitDate.getText().toString();
                    String category = spCategory.getSelectedItem().toString();
                    String descriptoin = etDebitDescription.getText().toString().trim();
                    String totalAmount = etDebitTotalAmount.getText().toString().trim();

                    Debit debit = new Debit(date, category, descriptoin, Double.parseDouble(totalAmount));

                    ExpenseDataSource expenseDataSource = new ExpenseDataSource(context);

                    if(expenseDataSource.insertDebit(debit)){

                        Toast.makeText(context, "Data inserted",Toast.LENGTH_SHORT).show();

                    } else{
                        Toast.makeText(context, "Fail to insert data",Toast.LENGTH_SHORT).show();
                    }



                }
            }

        } else if(id == R.id.ibDebitCancel){


        }

    }


    private void moveToScan(){

        Intent intent = new Intent(getActivity(), ScanActivity.class);
        intent.putExtra(Constant.CATEGORY_BUNDLE,spCategory.getSelectedItem().toString());
        startActivity(intent);
        ((Activity) getActivity()).overridePendingTransition(0,0);
        ((Activity) getActivity()).finish();

    }*/

}
