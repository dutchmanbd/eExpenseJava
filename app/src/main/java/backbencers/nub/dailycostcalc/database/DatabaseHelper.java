package backbencers.nub.dailycostcalc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import backbencers.nub.dailycostcalc.constant.Constant;

/**
 * Created by Nishad on 21-Jul-16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // query to create debit table in the database
    private static String CREATE_DEBIT_TABLE = "CREATE TABLE " + Constant.TABLE_DEBIT + " (" + Constant.COL_ID + " INTEGER PRIMARY KEY, " + Constant.COL_DEBIT_DATE + " TEXT, " + Constant.COL_DEBIT_CATEGORY + " TEXT, " + Constant.COL_DEBIT_DESCRIPTION + " TEXT, " + Constant.COL_DEBIT_AMOUNT + " DOUBLE);";

    // query to create credit table in the database
    private static String CREATE_CREDIT_TABLE = "CREATE TABLE " + Constant.TABLE_CREDIT + " (" + Constant.COL_ID + " INTEGER PRIMARY KEY, " + Constant.COL_CREDIT_DATE + " TEXT, " + Constant.COL_CREDIT_CATEGORY + " TEXT, " + Constant.COL_CREDIT_DESCRIPTION + " TEXT, " + Constant.COL_CREDIT_AMOUNT + " DOUBLE);";

    // query to create category table
    private static String CREATE_CATEGORY_TABLE = "CREATE TABLE " + Constant.TABLE_CATEGORY + " ("+ Constant.COL_ID + " INTEGER PRIMARY KEY, " + Constant.COL_CATEGORY_NAME + " TEXT);";

    // constructor
    public DatabaseHelper(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DEBIT_TABLE);
        db.execSQL(CREATE_CREDIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_DEBIT);
        db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_CREDIT);
        onCreate(db);
    }

}
