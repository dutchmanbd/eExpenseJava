package backbencers.nub.dailycostcalc.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import backbencers.nub.dailycostcalc.R;

public class AddCreditActivity extends AppCompatActivity {

    private static EditText etDate;
    private ImageButton ibCalendar;
    private AutoCompleteTextView actvCategory;
    private EditText etDescription;
    private EditText etAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeViews();
        setInitialDate();

        ibCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
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
        }
        return super.onOptionsItemSelected(item);
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
