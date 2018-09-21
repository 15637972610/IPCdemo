// IOnNewBookArrivedListener.aidl
package com.dkp.ipcdemo.aidl.demo2;
import com.dkp.ipcdemo.aidl.demo2.Book2;

// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book2 newBook);

}
