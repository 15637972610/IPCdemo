package com.dkp.ipcdemo.aidl.demo2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dkp on 2018/9/20.
 */

public class Book2 implements Parcelable {
    private int bookId;
    private String bookName;
    protected Book2(Parcel in) {
        bookId = in.readInt();
        bookName  = in.readString();
    }
    public Book2(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public static final Creator<Book2> CREATOR = new Creator<Book2>() {
        @Override
        public Book2 createFromParcel(Parcel in) {
            return new Book2(in);
        }

        @Override
        public Book2[] newArray(int size) {
            return new Book2[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(bookId);
        out.writeString(bookName);
    }
}
