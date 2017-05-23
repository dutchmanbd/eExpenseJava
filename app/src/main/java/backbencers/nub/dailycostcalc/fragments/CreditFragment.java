package backbencers.nub.dailycostcalc.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import backbencers.nub.dailycostcalc.R;
import backbencers.nub.dailycostcalc.adapter.CreditListAdapter;
import backbencers.nub.dailycostcalc.database.ExpenseDataSource;
import backbencers.nub.dailycostcalc.model.Credit;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditFragment extends Fragment {

    private static final String TAG = CreditFragment.class.getSimpleName();

    private List<Credit> creditList;
    private ExpenseDataSource expenseDataSource;
    private ProgressBar loadingCreditProgressBar;
    private CreditListAdapter adapter;
    private ListView creditListView;
    private TextView creditEmptyView;

    public CreditFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_credit, container, false);

        creditListView = (ListView) view.findViewById(R.id.lv_credits);
        creditEmptyView = (TextView) view.findViewById(R.id.empty_view_credit);
        loadingCreditProgressBar = (ProgressBar) view.findViewById(R.id.pb_loding_credits);

        expenseDataSource = new ExpenseDataSource(getContext());

        //readMessages();

        //creditList = expenseDataSource.getAllCredits();

        //CreditListAdapter adapter = new CreditListAdapter(getContext(), creditList);

        //creditListView.setAdapter(adapter);

        //new LoadCreditTask().execute();

        loadCredits();

        creditListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showCreditDetailInDialog(position);
            }
        });

        return view;
    }

    private void showCreditDetailInDialog(int position) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        Credit credit = (Credit) creditListView.getItemAtPosition(position);
        builder.setTitle(credit.getCreditDate())
                .setMessage(credit.getCreditDescription())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
    }

    private void loadCredits() {
        loadingCreditProgressBar.setVisibility(View.VISIBLE);
        creditListView.setAdapter(new CreditListAdapter(getContext(), new ArrayList<Credit>()));
        expenseDataSource.deleteAllCredits();

        Credit credit = new Credit();
        credit.setCreditCategory("Bank");

        Cursor creditCursor = getActivity().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (creditCursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for (int idx = 0; idx < creditCursor.getColumnCount(); idx++) {
                    //msgData += " " + cursor.getColumnName(idx) + " : " + cursor.getString(idx) + "\n";
                    Timestamp timestamp;
                    Date date;

                    if (creditCursor.getColumnName(idx).equals("date")) {
                        timestamp = new Timestamp(creditCursor.getLong(idx));
                        date = new Date(timestamp.getTime());
                        String fullDateString = date.toString();
                        String monthDateString = fullDateString.substring(4, 10);
                        String yearString = fullDateString.substring(30, 34);
                        String dateString = monthDateString + ", " + yearString;
                        credit.setCreditDate(dateString);
                        //Log.i(CreditFragment.class.getSimpleName(), "Date: " + dateString);
                    }

                    if (creditCursor.getColumnName(idx).equals("body")) {
                        // TODO: add logic to add only the credit messages
                        String messageBodyNormal = creditCursor.getString(idx);
                        String messageBodyLowerCase = messageBodyNormal.toLowerCase();

                        if (messageBodyLowerCase.contains("credited") || messageBodyLowerCase.contains("cash in") || messageBodyLowerCase.contains("received")) {
                            Log.i(TAG, creditCursor.getString(idx));

                            credit.setCreditDescription(messageBodyNormal);

                            credit.setCreditAmount(findCreditAmountFromMessageBody(messageBodyLowerCase));

                            expenseDataSource.insertCredit(credit);
                        }
                    }

                }
                //Log.i(CreditFragment.class.getSimpleName(), credit.getCreditDate() + "\n\n");
                //pbLoading.setVisibility(View.INVISIBLE);
                // use msgData
//                if (msgData.toLowerCase().contains("credited")) {
//                    tvMessages.append(msgData + "\n\n\n");
//                }
            } while (creditCursor.moveToNext());
        } else {
            // empty box, no SMS
            //pbLoading.setVisibility(View.INVISIBLE);
            //tvMessages.append("\n\nNo messages found!\n\n");
        }

        try {
            if (creditCursor != null) {
                creditCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            creditList = expenseDataSource.getAllCredits();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadingCreditProgressBar.setVisibility(View.INVISIBLE);

        if (creditList.size() == 0) {
            creditEmptyView.setVisibility(View.VISIBLE);
        } else {
            adapter = new CreditListAdapter(getContext(), creditList);
            creditListView.setAdapter(adapter);
        }
    }

    private Double findCreditAmountFromMessageBody(String messageBody) {
        int indexOfTaka = -1;
        if (messageBody.contains("bdt")) {
            indexOfTaka = messageBody.indexOf("bdt");
        } else if (messageBody.contains("tk")) {
            indexOfTaka = messageBody.indexOf("tk");
        }

        double creditAmount = 0;
        if (indexOfTaka != -1) {
            for (int i=indexOfTaka; i<messageBody.length(); i++) {
                char c = messageBody.charAt(i);
                if (Character.isDigit(c)) {
                    int digit = (int) c - 48;
                    creditAmount = (creditAmount * 10) + digit;
                } else if (c == '.') {
                    break;
                }
            }
        }

        return creditAmount;
    }

    /*private void readMessages() {
        // public static final String INBOX = "content://sms/inbox";
        // public static final String SENT = "content://sms/sent";
        // public static final String DRAFT = "content://sms/draft";

        //pbLoading.setVisibility(View.VISIBLE);

        Credit credit = new Credit();
        credit.setCreditCategory("Bank");

        Cursor cursor = getActivity().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    //msgData += " " + cursor.getColumnName(idx) + " : " + cursor.getString(idx) + "\n";
                    Timestamp timestamp;
                    Date date;

                    if (cursor.getColumnName(idx).equals("date")) {
                        timestamp = new Timestamp(cursor.getLong(idx));
                        date = new Date(timestamp.getTime());
                        String fullDateString = date.toString();
                        String monthDateString = fullDateString.substring(4, 10);
                        String yearString = fullDateString.substring(30, 34);
                        String dateString = monthDateString + ", " + yearString;
                        credit.setCreditDate(dateString);
                        Log.i(CreditFragment.class.getSimpleName(), "Date: " + dateString);
                    }

                    if (cursor.getColumnName(idx).equals("body")) {
                        // TODO: add logic to add only the credit messages
                        credit.setCreditDescription(cursor.getString(idx));
                    }

                    credit.setCreditAmount(5000.0);
                    //expenseDataSource.insertCredit(credit);
                }
                Log.i(CreditFragment.class.getSimpleName(), credit.getCreditDate()+"\n\n");
                //pbLoading.setVisibility(View.INVISIBLE);
                // use msgData
//                if (msgData.toLowerCase().contains("credited")) {
//                    tvMessages.append(msgData + "\n\n\n");
//                }
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
            //pbLoading.setVisibility(View.INVISIBLE);
            //tvMessages.append("\n\nNo messages found!\n\n");
        }

        creditList = expenseDataSource.getAllCredits();

        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*private class LoadCreditTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            loadingCreditProgressBar.setVisibility(View.VISIBLE);
            expenseDataSource.deleteAllCredits();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Credit credit = new Credit();
            credit.setCreditCategory("Bank");

            Cursor creditCursor = getActivity().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

            if (creditCursor.moveToFirst()) { // must check the result to prevent exception
                do {
                    String msgData = "";
                    for (int idx = 0; idx < creditCursor.getColumnCount(); idx++) {
                        //msgData += " " + cursor.getColumnName(idx) + " : " + cursor.getString(idx) + "\n";
                        Timestamp timestamp;
                        Date date;

                        if (creditCursor.getColumnName(idx).equals("date")) {
                            timestamp = new Timestamp(creditCursor.getLong(idx));
                            date = new Date(timestamp.getTime());
                            String fullDateString = date.toString();
                            String monthDateString = fullDateString.substring(4, 10);
                            String yearString = fullDateString.substring(30, 34);
                            String dateString = monthDateString + ", " + yearString;
                            credit.setCreditDate(dateString);
                            //Log.i(CreditFragment.class.getSimpleName(), "Date: " + dateString);
                        }

                        if (creditCursor.getColumnName(idx).equals("body")) {
                            // TODO: add logic to add only the credit messages
                            if (creditCursor.getString(idx).toLowerCase().contains("telvo")) {
                                Log.i(TAG, creditCursor.getString(idx));
                                credit.setCreditDescription(creditCursor.getString(idx));
                                credit.setCreditAmount(5000.0);
                                expenseDataSource.insertCredit(credit);
                            }
                        }

                    }
                    //Log.i(CreditFragment.class.getSimpleName(), credit.getCreditDate() + "\n\n");
                    //pbLoading.setVisibility(View.INVISIBLE);
                    // use msgData
//                if (msgData.toLowerCase().contains("credited")) {
//                    tvMessages.append(msgData + "\n\n\n");
//                }
                } while (creditCursor.moveToNext());
            } else {
                // empty box, no SMS
                //pbLoading.setVisibility(View.INVISIBLE);
                //tvMessages.append("\n\nNo messages found!\n\n");
            }

            try {
                if (creditCursor != null) {
                    creditCursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                creditList = expenseDataSource.getAllCredits();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            loadingCreditProgressBar.setVisibility(View.INVISIBLE);

            if (creditList.size() == 0) {
                creditEmptyView.setVisibility(View.VISIBLE);
            } else {
                adapter = new CreditListAdapter(getContext(), creditList);
                creditListView.setAdapter(adapter);
            }
        }
    }*/

}
