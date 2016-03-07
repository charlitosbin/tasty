package com.example.tasty.Utils;


import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;

public final class Util {

    public static void example()
    {
        System.out.println("hola");
    }

    public static Snackbar createSnackbar(LinearLayout ll, String title){
        Snackbar snack = Snackbar.make(ll,title,Snackbar.LENGTH_LONG);

        return snack;
    }
}