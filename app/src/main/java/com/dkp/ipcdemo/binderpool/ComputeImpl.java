package com.dkp.ipcdemo.binderpool;

import android.os.RemoteException;

/**
 * Created by dkp on 2018/9/25.
 */

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }
}
