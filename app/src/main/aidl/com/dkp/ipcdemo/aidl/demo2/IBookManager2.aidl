// IBookManager2.aidl
package com.dkp.ipcdemo.aidl.demo2;
import com.dkp.ipcdemo.aidl.demo2.Book2;
import com.dkp.ipcdemo.aidl.demo2.IOnNewBookArrivedListener;//忘记导报
// Declare any non-default types here with import statements

interface IBookManager2 {
        List<Book2> getBookList();
        void addBook(in Book2 book);
        void registerListener(IOnNewBookArrivedListener listener);
        void unregisterListener(IOnNewBookArrivedListener listener);
}
