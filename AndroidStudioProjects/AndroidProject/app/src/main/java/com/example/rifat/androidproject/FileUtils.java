package com.example.rifat.androidproject;


import android.content.Context;
import android.content.ContextWrapper;

import java.io.File;

public class FileUtils {

    public static boolean FileExistance(ContextWrapper context, String fname){
        File file = context.getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }



}
