package orm;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configure implements ISession {
    // static Connection c = null;
    public static   String dataBaseName;
    public static  DataBaseHelper    myDbHelper ;
    private static boolean reloadBase=false;

    private SQLiteDatabase sqLiteDatabaseForReadable=null;
    private SQLiteDatabase sqLiteDatabaseForWritable=null;


    public static  String getBaseName(){
        return dataBaseName;
    }

    public static Configure getSession(){
        return new Configure();
    }



    private Configure(){


        sqLiteDatabaseForReadable=GetSqLiteDatabaseForReadable();
        sqLiteDatabaseForWritable=GetSqLiteDatabaseForWritable();
    }





    private static SQLiteDatabase GetSqLiteDatabaseForReadable() throws SQLException {
        return myDbHelper.openDataBaseForReadable();
    }
    private static SQLiteDatabase GetSqLiteDatabaseForWritable() throws SQLException {
        return myDbHelper.openDataBaseForWritable();
    }



    public  Configure(String dataBaseName,Context context,boolean reloadBase){
        Configure.reloadBase =reloadBase;
        new Configure( dataBaseName,  context);
    }

    public Configure(String dataBaseName,Context context){
        Configure.dataBaseName =dataBaseName;

        myDbHelper= new DataBaseHelper(context, Configure.dataBaseName);
        if(reloadBase){
            myDbHelper.getReadableDatabase();
            try {
                myDbHelper. copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database -"+e.getMessage());
            }
        }
        else{
            if(!myDbHelper.checkDataBase()){
                myDbHelper.createDataBase();
            }

        }


    }

    @Override
    public <T> int update(T item)  {
        SQLiteDatabase con=sqLiteDatabaseForWritable;
        cacheMetaDate d= CacheDictionary.getCacheMetaDate(item.getClass());
        ContentValues values = getContentValues(item, d);
        Object key=null;
        try {
            key= item.getClass().getField(d.keyColumn.fieldName).get(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert key != null;
        if(d.isIAction()){
            ((IActionOrm)item).actionBeforeUpdate(item);
        }
        int i= con.update(d.tableName, values, d.keyColumn.columName + " = ?",new String[]{key.toString()} );
        if(i==-1){
            try {
                throw new Exception("Not Update");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            if(d.isIAction()){
                ((IActionOrm)item).actionAfterUpdate(item);
            }
        }
        return i;
    }

    @Override
    public <T> int insert(T item) {
        SQLiteDatabase con=sqLiteDatabaseForWritable;
        cacheMetaDate d= CacheDictionary.getCacheMetaDate(item.getClass());
        ContentValues values = getContentValues(item, d);
        if(d.isIAction()){
            ((IActionOrm)item).actionBeforeInsert(item);
        }
        int i= (int) con.insert(d.tableName,null,values);
        if(i==-1){
            try {
                throw new Exception("Not insert");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            if(d.isIAction()){
                ((IActionOrm)item).actionAfterInsert(item);
            }
        }
        try {
            item.getClass().getField(d.keyColumn.fieldName).set(item,i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  i;
    }

    private <T> ContentValues getContentValues(T item, cacheMetaDate<?> d) {
        ContentValues values = new ContentValues();
        for (ItemField str : d.listColumn) {
            if(str.type==String.class)
                try {
                    values.put(str.columName, (String) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            if(str.type==int.class)
                try {
                    values.put(str.columName, (int) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if(str.type==long.class)
                try {
                    values.put(str.columName, (long) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if(str.type==short.class)
                try {
                    values.put(str.columName, (short) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if(str.type==byte.class)
                try {
                    values.put(str.columName, (byte) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            if(str.type==Short.class)
                try {
                    values.put(str.columName, (Short) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if(str.type==Long.class)
                try {
                    values.put(str.columName, (Long) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            if(str.type==Integer.class)
                try {
                    values.put(str.columName, (Integer) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if(str.type==Double.class)
                try {
                    values.put(str.columName, (Double) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            if(str.type==Float.class)
                try {
                    values.put(str.columName, (Float) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }






            if(str.type==byte[].class)
                try {
                    values.put(str.columName, (byte[]) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            if(str.type==double.class)
                try {
                    values.put(str.columName, (double) item.getClass().getField(str.fieldName).get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            if(str.type==boolean.class)
                try {
                    boolean val= (boolean) item.getClass().getField(str.fieldName).get(item);
                    if(val){
                        values.put(str.columName, 1);
                    }else{
                        values.put(str.columName, 0);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return values;
    }

    @Override
    public <T> int delete(T item) {
        SQLiteDatabase con=sqLiteDatabaseForWritable;
        cacheMetaDate d = CacheDictionary.getCacheMetaDate(item.getClass());

        Object key=null;
        try {
            key= item.getClass().getField(d.keyColumn.fieldName).get(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert key != null;
        if(d.isIAction()){
            ((IActionOrm)item).actionBeforeDelete(item);
        }
        int res= con.delete(d.tableName,d.keyColumn.columName+"=?",new String[]{key.toString()});
        if(res!=0){
            if(d.isIAction()){
                ((IActionOrm)item).actionAfterDelete(item);
            }
        }
        return  res;
    }

    @Override
    public <T> List<T> getList(Class<T> tClass, String where, Object... objects)  {
        List<T> list=new ArrayList<>();
        SQLiteDatabase con;
        try {
            con=sqLiteDatabaseForReadable;
            cacheMetaDate d=CacheDictionary.getCacheMetaDate(tClass);
            Cursor c=null;
            if(where==null&& objects ==null) {
                String[] li=d.getStringSelect();
                c = con.query(d.tableName,li,null,null,null, null,null,null);
            }
            String[] sdd=d.getStringSelect();
            if(where!=null&& objects ==null) {
                c = con.query(d.tableName,sdd,where,null,null, null,null,null);
            }
            if(where!=null&& objects !=null) {
                String[] lstr=new String[objects.length];
                for (int i=0;i< objects.length;i++){
                    lstr[i]=String.valueOf(objects[i]);
                }
                c = con.query(d.tableName,sdd,where,lstr,null, null,null,null);
            }
            if (c != null) {
                try{
                    if (c.moveToFirst()) {
                        do {
                            Object sd = tClass.newInstance();
                            Companaund(d.listColumn,d.keyColumn, c, sd);
                            list.add((T) sd);
                        } while (c.moveToNext());
                    }
                }finally {
                    c.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void Companaund(List<ItemField> listIf, ItemField key, Cursor c, Object o)  {
        for (ItemField str : listIf) {
            int i = c.getColumnIndex(str.columName);
            if (str.type ==int.class){
                try {
                    o.getClass().getField(str.fieldName).setInt(o,c.getInt(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==String.class){
                try {
                    o.getClass().getField(str.fieldName).set(o, c.getString(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==double.class){
                try {
                    o.getClass().getField(str.fieldName).setDouble(o, c.getDouble(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==float.class){
                try {
                    o.getClass().getField(str.fieldName).setFloat(o, c.getFloat(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==long.class){
                try {
                    o.getClass().getField(str.fieldName).setLong(o, c.getLong(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==short.class){
                try {
                    o.getClass().getField(str.fieldName).setShort(o, c.getShort(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==byte[].class){
                try {
                    o.getClass().getField(str.fieldName).set(o, c.getBlob(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==byte.class){
                try {
                    o.getClass().getField(str.fieldName).setByte(o, (byte) c.getLong(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==Integer.class){
                try {
                    int ii=c.getInt(i);
                    o.getClass().getField(str.fieldName).set(o, ii);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ////////
            if (str.type ==Double.class){
                try {
                    Double d=c.getDouble(i);
                    o.getClass().getField(str.fieldName).set(o,d );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==Float.class){
                try {
                    Float f=c.getFloat(i);
                    o.getClass().getField(str.fieldName).set(o, f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==Long.class){
                try {
                    Long l=c.getLong(i);
                    o.getClass().getField(str.fieldName).set(o, l);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==Short.class){
                try {
                    Short sh=c.getShort(i);
                    o.getClass().getField(str.fieldName).set(o,sh );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (str.type ==boolean.class){
                boolean val;
                val = c.getInt(i) != 0;
                try {
                    o.getClass().getField(str.fieldName).setBoolean(o, val);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            o.getClass().getField(key.fieldName).set(o, c.getInt(c.getColumnIndex(key.columName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T get(Class<T> tClass, Object id) {
        cacheMetaDate d=CacheDictionary.getCacheMetaDate(tClass);
        List<T> res= getList(tClass, d.keyColumn.columName + "=?", id);
        if(res.size()==0) return null;
        if(res.size()>1){
            try {
                throw new Exception("more than one- Error data");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res.get(0);
    }

    @Override
    public <T> Object executeScalar(String sql,Object ... objects) {
        List<String> arrayList = new ArrayList<>();
        String[] array =null;
        if(objects!=null&&objects.length>0){
            for (Object object : objects) {
                arrayList.add(String.valueOf(object));
            }
            array = new String[arrayList.size()];
            arrayList.toArray(array);
        }
        return InnerListExe(sql, array);
    }

    @Override
    public void execSQL(String sql, Object ... objects) {
        List<String> arrayList = new ArrayList<>();
        String[] array;
        if(objects!=null&&objects.length>0){
            for (Object object : objects) {
                arrayList.add(String.valueOf(object));
            }
            array = new String[arrayList.size()];
            arrayList.toArray(array);
            sqLiteDatabaseForWritable.execSQL(sql,array);
        }else{
            sqLiteDatabaseForWritable.execSQL(sql);
        }
    }


    @Override
    public void beginTransaction() {
        myDbHelper.getWritableDatabase().beginTransaction();
    }

    @Override
    public void commitTransaction() {
        myDbHelper.getWritableDatabase().setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        myDbHelper.getWritableDatabase().endTransaction();
    }

    @Override
    public void close() {
     myDbHelper.close();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Object InnerListExe(String sql, String[] strings){
        Cursor c;
        if(strings==null){
            c=sqLiteDatabaseForReadable.rawQuery(sql,null);
        }else
        {
            c=sqLiteDatabaseForReadable.rawQuery(sql,strings);
        }
        if (c != null) {
            try{
                if (c.moveToFirst()) {
                    do {
                        int i= c.getType(0);
                        if(i==0){
                            return null;
                        }
                        if(i==1){
                            return c.getInt(0);
                        }
                        if(i==2){
                            return c.getFloat(0);
                        }
                        if(i==3){
                            return c.getString(0);
                        }
                        if(i==4){
                            return c.getBlob(0);
                        }
                    } while (c.moveToNext());

                }
            }finally {
                c.close();
            }
        }
        return null;
    }

    public static void createBase(String path) {
        File f = new File(path);
        if(f.exists()) return;
        f.getParentFile().mkdirs();
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String pizdaticusKey(ItemField field){
        if(field.type==double.class||field.type==float.class||field.type==Double.class||field.type==Float.class){
            return " REAL ";
        }
        if(field.type==int.class||field.type==long.class||field.type==short.class||field.type==byte.class||field.type==Integer.class||
                field.type==Long.class||field.type==Short.class||field.type==Byte.class){
            return " INTEGER ";
        }
        if(field.type==String.class){
            return " TEXT ";
        }
        if(field.type==boolean.class){
            return " BOOL ";
        }
        return "";
    }
    static String pizdaticusField(ItemField field){
        if(field.type==double.class||field.type==float.class||field.type==Double.class||field.type==Float.class){
            return " REAL DEFAULT 0, ";
        }
        if(field.type==int.class||field.type==Enum.class||field.type==long.class||field.type==short.class||field.type==byte.class||field.type==Integer.class||
                field.type==Long.class||field.type==Short.class){
            return " INTEGER DEFAULT 0, ";
        }
        if(field.type==String.class){
            return " TEXT, ";
        }
        if(field.type==boolean.class||field.type==Boolean.class){
            return " BOOL DEFAULT 0, ";
        }

        if(field.type==byte[].class){
            return " BLOB, ";
        }
        return "";
    }

    public static void createTable(Class<?> aClass) {
        cacheMetaDate date=  CacheDictionary.getCacheMetaDate(aClass);
        StringBuilder sb=new StringBuilder("CREATE TABLE "+date.tableName+" (" );
        sb.append(date.keyColumn.columName).append(" ");
        sb.append(pizdaticusKey(date.keyColumn));
        sb.append("PRIMARY KEY, ");
        for ( Object f : date.listColumn) {
            ItemField ff= (ItemField) f;
            sb.append(ff.columName);
            sb.append(pizdaticusField(ff));
        }
        String s=sb.toString().trim();
        String ss=s.substring(0,s.length()-1);
        String sql=ss+")";
        Configure.getSession().execSQL(sql,null);
    }

//    public static void createTable(List<Class<?>> aClassL){
//        for (Class<?> aClass : aClassL) {
//            createTable(aClass);
//        }
//    }

//    public static void createTable(Class<?> aClass,Object o){
//        createTable(aClass);
//        Configure.getSession().insert(o);
//    }

//    public static void createTable(Class<?> aClass,List<Object> objectList){
//        createTable(aClass);
//        for (Object o : objectList) {
//            Configure.getSession().insert(o);
//        }
//    }
}