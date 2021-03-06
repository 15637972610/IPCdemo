package com.dkp.ipcdemo.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dkp on 2018/9/20.
 */

public class Book implements Parcelable {
    private int bookId;
    private String bookName;
    protected Book(Parcel in) {
        bookId = in.readInt();
        bookName  = in.readString();
    }
    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
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
