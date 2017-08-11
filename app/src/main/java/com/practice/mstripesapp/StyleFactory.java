package com.practice.mstripesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by This Pc on 27-07-2017.
 */
public class StyleFactory {

    Map<Integer,Integer> Colors;
    Context mcontext;

    public StyleFactory(Context context)
    {
        mcontext=context;
        Colors =new HashMap<>();
        Colors.put(0,android.R.color.holo_blue_light);
        Colors.put(1,android.R.color.holo_green_light);
        Colors.put(2,android.R.color.darker_gray);
        Colors.put(3,android.R.color.holo_orange_dark);
    }

    public SimpleRenderer getRenderer(String filetype,int index)
    {
        if ("POLYGON".equalsIgnoreCase(filetype))
        {
            return new SimpleRenderer(new SimpleFillSymbol(Color.argb(10,255,0,100),SimpleFillSymbol.STYLE.SOLID));
        }
        else if ("POLYLINE".equalsIgnoreCase(filetype))
        {
            Log.v("TAG","here");
            return new SimpleRenderer(new SimpleLineSymbol(ContextCompat.getColor(mcontext, Colors.get(index % 4)),1f));
        }
        else if ("POINT".equalsIgnoreCase(filetype))
        {
            return new SimpleRenderer(new SimpleMarkerSymbol(Color.BLUE,5,SimpleMarkerSymbol.STYLE.CIRCLE));
        }

        return null;
    }

}
