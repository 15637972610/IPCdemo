package com.dkp.ipcdemo.provider;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dkp on 2018/9/20.
 */

public class Book3 implements Parcelable {
    public int bookId;
    public String bookName;
    protected Book3(Parcel in) {
        bookId = in.readInt();
        bookName  = in.readString();
    }
    public Book3(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public static final Creator<Book3> CREATOR = new Creator<Book3>() {
        @Override
        public Book3 createFromParcel(Parcel in) {
            return new Book3(in);
        }

        @Override
        public Book3[] newArray(int size) {
            return new Book3[size];
        }
    };

    public Book3() {

    }

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
