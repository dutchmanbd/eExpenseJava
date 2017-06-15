package backbencers.nub.dailycostcalc.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import backbencers.nub.dailycostcalc.R;
import backbencers.nub.dailycostcalc.database.ExpenseDataSource;
import backbencers.nub.dailycostcalc.model.Category;
import backbencers.nub.dailycostcalc.model.Credit;

public class CreditEditorActivity extends AppCompatActivity {

    private String TAG = CreditEditorActivity.class.getSimpleName();

    private static EditText etDate;
    private ImageButton ibCalendar;
    private AutoCompleteTextView actvCategory;
    private EditText etDescription;
    private EditText etAmount;
    private ExpenseDataSource expenseDataSource;
    ArrayList<String> cat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_editor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expenseDataSource = new ExpenseDataSource(this);

        initializeViews();
        setInitialDate();

        ibCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        ArrayList<String> categories = getCategoriesFromDatabase();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, categories);
        actvCategory.setAdapter(adapter);
        actvCategory.setThreshold(1);
    }

    private ArrayList<String> getCategoriesFromDatabase() {
        ArrayList<Category> categories = expenseDataSource.getAllCategories();

        for (int i=0; i<categories.size(); i++) {
            String c = categories.get(i).getCategoryName();
            cat.add(c);
        }

        return cat;
    }

    private void initializeViews() {
        etDate = (EditText) findViewById(R.id.edit_text_date);
        ibCalendar = (ImageButton) findViewById(R.id.image_button_calendar);
        actvCategory = (AutoCompleteTextView) findViewById(R.id.auto_complete_category);
        etDescription = (EditText) findViewById(R.id.edit_text_description);
        etAmount = (EditText) findViewById(R.id.edit_text_amount);
    }

    private void setInitialDate() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        etDate.setText(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_credit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_save_credit:
                saveCredit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCredit() {
        String date = "";
        String category = "";
        String description = "";
        String amount = "";

        if (TextUtils.isEmpty(etDate.getText().toString())) {
            Toast.makeText(this, "Please enter or select a date!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(actvCategory.getText().toString())) {
            Toast.makeText(this, "Please enter a category!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etDescription.getText().toString())) {
            Toast.makeText(this, "Please enter description!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etAmount.getText().toString())) {
            Toast.makeText(this, "Please enter amount!", Toast.LENGTH_SHORT).show();
        } else {
            date = etDate.getText().toString().trim();
            category = actvCategory.getText().toString().trim();
            description = etDescription.getText().toString().trim();
            amount = etAmount.getText().toString().trim();

            if (!isExistedCategory(category)) {
                expenseDataSource.insertCategory(new Category(category));
            }

            Credit credit = new Credit(date, category, description, new Double(amount), (int) (System.currentTimeMillis()%100000000));
            Log.e(TAG, "system currentTimeMillis: " + System.currentTimeMillis()%100000000);

            boolean inserted = expenseDataSource.insertCredit(credit);
            if (inserted) {
                Toast.makeText(this, "Credit saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save credit!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isExistedCategory(String category) {
        for (String s : cat) {
            if (s.equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month += 1;
            String finalDay = day > 9 ? (""+day) : ("0"+day);
            String finalMonth = month > 9 ? (""+month) : ("0"+month);
            String finalYear = ""+year;
            etDate.setText("");
            etDate.setText(finalDay + "-" + finalMonth + "-" + finalYear);
        }
    }
}