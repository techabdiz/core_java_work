package com.deadspider.impls;

import com.deadspider.interfaces.BaseInterface;

public class BaseInterfaceImpl implements BaseInterface {

    public void run() {
        System.out.println("print something else");
    }

    @Override
    public void doSomeThing() {
        System.out.println("Unlike you... i a actually do somethin... !");
    }
}
