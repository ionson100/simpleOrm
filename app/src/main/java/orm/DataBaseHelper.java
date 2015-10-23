package orm;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {



    private static String DB_PATH = "";
    private static final SQLiteDatabase myDataBase=null;
    private static Context mContext;

    public DataBaseHelper(Context context,String databasePath) {
        super(context, databasePath, null, 1);

        DB_PATH=databasePath;
        mContext = context;
    }

    public void createDataBase() {
        boolean dbExist = checkDataBase();
        if(dbExist){
        }else{
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,1);
        }catch(SQLiteException ignored){
            ignored.printStackTrace();
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null;// ? true : false;
    }


    public void copyDataBase() throws IOException{

        String fff=  Configure.dataBaseName;
        String baseName=fff.substring(fff.lastIndexOf(File.separator)+1);
        InputStream myInput = mContext.getAssets().open(baseName);
        OutputStream myOutput = new FileOutputStream(DB_PATH);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public SQLiteDatabase openDataBaseForReadable() throws SQLException {
        return this. getReadableDatabase();
    }

    public SQLiteDatabase openDataBaseForWritable() throws SQLException {
        return this. getWritableDatabase();
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public void Close(){
        close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
