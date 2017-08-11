package com.practice.mstripesapp;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by This Pc on 26-07-2017.
 */
public class ShapeFiles {

   static List<String> Files;

    public ShapeFiles(Context context)
    {

        if(Files==null) {
            Files=new ArrayList<>();
            String path = context.getExternalCacheDir().getAbsolutePath() + "/Mstripes";
            File dir = new File(path);
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".shp") || name.endsWith(".SHP");
                }
            };

            File[] files = dir.listFiles(filter);
            for (int i = 0; i < files.length; i++) {
                Files.add(files[i].getName());
            }
        }
    }

    public List<String> getList()
    {return Files;}

    public void addFile(String filename)
    {
        if (filename.endsWith(".shp") || filename.endsWith(".SHP"))
        {
            if (filename.startsWith("/"))
              filename=filename.substring(1);

            if (!Files.contains(filename))
                Files.add(filename);
        }
    }

    public void clearList()
    {
        Files.clear();
    }


}
