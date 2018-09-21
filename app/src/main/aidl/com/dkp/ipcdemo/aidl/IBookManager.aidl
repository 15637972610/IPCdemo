// IBookManager.aidl
package com.dkp.ipcdemo.aidl;
import com.dkp.ipcdemo.aidl.Book;//1.忘记导包
// Declare any non-default types here with import statements

interface IBookManager {


    List<Book> getBookList();
    void addBook(in Book book);
}
