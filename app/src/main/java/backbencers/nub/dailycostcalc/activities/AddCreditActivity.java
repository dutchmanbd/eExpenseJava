package backbencers.nub.dailycostcalc.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import backbencers.nub.dailycostcalc.R;

public class AddCreditActivity extends AppCompatActivity {

    private EditText etDate;
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
        setDate();
    }

    private void initializeViews() {
        etDate = (EditText) findViewById(R.id.edit_text_date);
        ibCalendar = (ImageButton) findViewById(R.id.image_button_calendar);
        actvCategory = (AutoCompleteTextView) findViewById(R.id.auto_complete_category);
        etDescription = (EditText) findViewById(R.id.edit_text_description);
        etAmount = (EditText) findViewById(R.id.edit_text_amount);
    }

    private void setDate() {
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
}
