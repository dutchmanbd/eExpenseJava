package backbencers.nub.dailycostcalc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import backbencers.nub.dailycostcalc.R;
import backbencers.nub.dailycostcalc.database.ExpenseDataSource;
import backbencers.nub.dailycostcalc.model.Debit;

public class DebitEditorActivity extends AppCompatActivity {

    private String TAG = DebitEditorActivity.class.getSimpleName();

    private static EditText etDebitDate;
    private ImageButton ibDebitCalendar;
    private AutoCompleteTextView actvDebitCategory;
    private EditText etDebitDescription;
    private EditText etDebitAmount;
    private ExpenseDataSource expenseDataSource;
    private ArrayList<String> categoriesString = new ArrayList<>();
    private Intent ebitIntent;
    private String activityType;
    private int debitId;
    private Debit debit;
    private boolean debitHasChanged = false;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            debitHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_editor);
    }
}
