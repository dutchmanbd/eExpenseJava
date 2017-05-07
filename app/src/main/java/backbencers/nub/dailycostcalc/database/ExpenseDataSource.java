package backbencers.nub.dailycostcalc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

import backbencers.nub.dailycostcalc.constant.Constant;
import backbencers.nub.dailycostcalc.model.Credit;
import backbencers.nub.dailycostcalc.model.Debit;

/**
 * Created by Nishad on 21-Jul-16.
 */
public class ExpenseDataSource {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public ExpenseDataSource(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    // open the database for operation
    public void open() {
        database = databaseHelper.getWritableDatabase();
    }

    // close the database
    public void close() {
        database.close();
    }

    // add a debit to Debit table
    public boolean insertDebit(Debit debit) {
        this.open();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.COL_DEBIT_DATE, debit.getDebitDate());
        contentValues.put(Constant.COL_DEBIT_CATEGORY, debit.getDebitCategory());
        contentValues.put(Constant.COL_DEBIT_DESCRIPTION, debit.getDebitDescription());
        contentValues.put(Constant.COL_DEBIT_AMOUNT, debit.getDebitAmount());

        long inserted = database.insert(Constant.TABLE_DEBIT, null, contentValues);

        this.close();

        return inserted>0 ? true:false;
    }

    // add a credit to Credit table
    public boolean insertCredit(Credit credit) {
        this.open();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.COL_CREDIT_DATE, credit.getCreditDate());
        contentValues.put(Constant.COL_CREDIT_CATEGORY, credit.getCreditCategory());
        contentValues.put(Constant.COL_CREDIT_DESCRIPTION, credit.getCreditDescription());
        contentValues.put(Constant.COL_CREDIT_AMOUNT, credit.getCreditAmount());

        long inserted = database.insert(Constant.TABLE_CREDIT, null, contentValues);

        this.close();

        return inserted>0 ? true:false;
    }

    // get a single debit from the table by debit id
    public Debit getDebit(int id) {
        this.open();

        Cursor cursor = database.query(Constant.TABLE_DEBIT, new String[] {Constant.COL_ID,
                        Constant.COL_DEBIT_DATE, Constant.COL_DEBIT_CATEGORY, Constant.COL_DEBIT_DESCRIPTION,
                        Constant.COL_DEBIT_AMOUNT}, Constant.COL_ID + " = " + id, null, null, null, null);

        cursor.moveToFirst();
        Debit debit = createDebit(cursor);
        cursor.close();
        this.close();

        return debit;
    }

    // get a single credit from the table by credit id
    public Credit getCredit(int id) {
        this.open();

        Cursor cursor = database.query(Constant.TABLE_CREDIT, new String[] {Constant.COL_ID,
                Constant.COL_CREDIT_DATE, Constant.COL_CREDIT_CATEGORY, Constant.COL_CREDIT_DESCRIPTION,
                Constant.COL_CREDIT_AMOUNT}, Constant.COL_ID + " = " + id, null, null, null, null);

        cursor.moveToFirst();
        Credit credit = createCredit(cursor);
        cursor.close();
        this.close();

        return credit;
    }

    // return all debits from debit table
    public ArrayList<Debit> getAllDebits() {
        ArrayList<Debit> debits = new ArrayList<>();
        this.open();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Constant.TABLE_DEBIT, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i=0; i<cursor.getCount(); i++) {
                Debit debit = createDebit(cursor);
                debits.add(debit);
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();

        return debits;
    }

    // return all credits from credit table
    public ArrayList<Credit> getAllCredits() {
        ArrayList<Credit> credits = new ArrayList<>();
        this.open();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Constant.TABLE_CREDIT, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i=0; i<cursor.getCount(); i++) {
                Credit credit = createCredit(cursor);
                credits.add(credit);
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();

        return credits;
    }

    // update a debit with a given value
    public boolean updateDebit(int id, Debit debit) {
        this.open();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.COL_DEBIT_DATE, debit.getDebitDate());
        contentValues.put(Constant.COL_DEBIT_CATEGORY, debit.getDebitCategory());
        contentValues.put(Constant.COL_DEBIT_DESCRIPTION, debit.getDebitDescription());
        contentValues.put(Constant.COL_DEBIT_AMOUNT, debit.getDebitAmount());

        int updated = database.update(Constant.TABLE_DEBIT, contentValues,
                        Constant.COL_ID + " = " + id, null);
        this.close();

        return updated>0 ? true:false;
    }

    // update a credit with a given value
    public boolean updateCredit(int id, Credit credit) {
        this.open();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.COL_CREDIT_DATE, credit.getCreditDate());
        contentValues.put(Constant.COL_CREDIT_CATEGORY, credit.getCreditCategory());
        contentValues.put(Constant.COL_CREDIT_DESCRIPTION, credit.getCreditDescription());
        contentValues.put(Constant.COL_CREDIT_AMOUNT, credit.getCreditAmount());

        int updated = database.update(Constant.TABLE_CREDIT, contentValues,
                Constant.COL_ID + " = " + id, null);
        this.close();

        return updated>0 ? true:false;
    }

    // delete a debit from debit table by debit id
    public boolean deleteDebit(int id) {
        this.open();

        int deleted = database.delete(Constant.TABLE_DEBIT, Constant.COL_ID + " = " + id, null);
        this.close();

        return deleted>0 ? true:false;
    }

    // delete a credit from credit table by credit id
    public boolean deleteCredit(int id) {
        this.open();

        int deleted = database.delete(Constant.TABLE_CREDIT, Constant.COL_ID + " = " + id, null);
        this.close();

        return deleted>0 ? true:false;
    }

    // create a debit from cursor data
    private Debit createDebit(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Constant.COL_ID));
        String debitDate = cursor.getString(cursor.getColumnIndex(Constant.COL_DEBIT_DATE));
        String debitCategory = cursor.getString(cursor.getColumnIndex(Constant.COL_DEBIT_CATEGORY));
        String debitDescription = cursor.getString(cursor.getColumnIndex(Constant.COL_DEBIT_DESCRIPTION));
        Double debitAmount = cursor.getDouble(cursor.getColumnIndex(Constant.COL_DEBIT_AMOUNT));

        Debit debit = new Debit(id, debitDate, debitCategory, debitDescription, debitAmount);

        return debit;
    }

    // delete all credits
    public void deleteAllCredits() {
        this.open();

        database.delete(Constant.TABLE_CREDIT, null, null);

        this.close();
    }

    // create a credit from cursor data
    private Credit createCredit(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Constant.COL_ID));
        String creditDate = cursor.getString(cursor.getColumnIndex(Constant.COL_CREDIT_DATE));
        String creditCategory = cursor.getString(cursor.getColumnIndex(Constant.COL_CREDIT_CATEGORY));
        String creditDescription = cursor.getString(cursor.getColumnIndex(Constant.COL_CREDIT_DESCRIPTION));
        Double creditAmount = cursor.getDouble(cursor.getColumnIndex(Constant.COL_CREDIT_AMOUNT));

        Credit credit = new Credit(id, creditDate, creditCategory, creditDescription, creditAmount);

        return credit;
    }
}
