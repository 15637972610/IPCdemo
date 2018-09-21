package com.dkp.ipcdemo.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by dkp on 2018/9/21.
 */

public class BookProVider extends ContentProvider {
    private static final String TAG = "BookProVider";

    //1.创建uri和uri_code
    private static final String AUTHORITY = "com.dkp.ipcdemo.provider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");
    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;
    //2.通过UriMatcher将uri和uri_code 关联起来
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate current thread =" + Thread.currentThread());
        mContext =getContext();
        initProviderData();
        return true;
    }

    private void initProviderData() {
        mDb = new DbOpenHelper(mContext,DbOpenHelper.DB_NAME,null,DbOpenHelper.DB_VERSION).getWritableDatabase();
        mDb.execSQL("delete from " +DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("delete from " +DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("insert into book values(3,'Android'); ");
        mDb.execSQL("insert into book values(4,'IOS'); ");
        mDb.execSQL("insert into book values(5,'H5'); ");
        mDb.execSQL("insert  into user values(1,'jake',1); ");
        mDb.execSQL("insert into user values(2,'tom',0); ");

    }

    //3.根据Uri查询表名
    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query current thread =" + Thread.currentThread());
        String tableName = getTableName(uri);
        if (tableName==null){
            throw new IllegalArgumentException("unsupported URI :" +uri);
        }
        return mDb.query(tableName,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG, "getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.d(TAG, "insert");
        String tableName = getTableName(uri);
        if (tableName==null){
            throw new IllegalArgumentException("unsupported URI :" +uri);
        }
        mDb.insert(tableName,null,contentValues);
        mContext.getContentResolver().notifyChange(uri,null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete");
        String tableName = getTableName(uri);
        if (tableName==null){
            throw new IllegalArgumentException("unsupported URI :" +uri);
        }
        int count = mDb.delete(tableName,selection,selectionArgs);
        if (count> 0 ){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update");
        String tableName = getTableName(uri);
        if (tableName==null){
            throw new IllegalArgumentException("unsupported URI :" +uri);
        }
        int row =mDb.update(tableName,contentValues,selection,selectionArgs);
        if (row>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }
}
