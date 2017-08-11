package com.practice.mstripesapp;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geodatabase.ShapefileFeatureTable;
import com.esri.core.geometry.AngularUnit;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.renderer.UniqueValueRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

import org.codehaus.jackson.map.JsonMappingException;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by This Pc on 09-06-2017.
 */
public class map_display extends AppCompatActivity {

    final int numofLayer=2;

    private MapView mMapView;
    LocationDisplayManager mldisplaymanager;
    static double locationx,locationy;
    gpsService mService;
    boolean mBound = false;
    private final String TAG="MAP";
    GraphicsLayer trialLayer=new GraphicsLayer();
    GraphicsLayer layer3;
    Envelope eve=new Envelope();
    ShapefileFeatureTable shapefileFeatureTable;
    double deltalat,deltalong;
    Point point;
    int mvalue;
    ShapeFiles fileList;
    StyleFactory styleFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        styleFactory=new StyleFactory(getApplicationContext());
        setContentView(R.layout.map_display);
        mMapView = (MapView) findViewById(R.id.mapView);
        String path=getExternalCacheDir().getAbsolutePath()+"/Mstripes/";
        String shapefilepath;
        fileList=new ShapeFiles(getApplicationContext());
        List<String> files=fileList.getList();
        GeometryEngine ge=new GeometryEngine();

        FeatureLayer[] featureLayers=new FeatureLayer[files.size()];

        if (files.isEmpty())
            Toast.makeText(this, "No Map Found", Toast.LENGTH_LONG).show();

        try {
        for (String fname : files)
        {
            shapefilepath = path + fname;
            ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(shapefilepath);
            featureLayers[files.indexOf(fname)] = new FeatureLayer(shapefileFeatureTable);
            featureLayers[files.indexOf(fname)].setRenderer(styleFactory.getRenderer(featureLayers[files.indexOf(fname)].getGeometryType().toString(), files.indexOf(fname)));
            mMapView.addLayer(featureLayers[files.indexOf(fname)]);
        }

        } catch (FileNotFoundException ex) {
            Toast.makeText(this, "No Map Found", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
            return;
        }


    //    Log.v(TAG, String.valueOf(shapefileFeatureTable.getNumberOfFeatures()));
  //      Log.v(TAG, String.valueOf(chowkiFeatureTable.getNumberOfFeatures()));
//        FeatureLayer chowkifeatureLayer = new FeatureLayer(chowkiFeatureTable);
 //       chowkifeatureLayer.setRenderer(new SimpleRenderer(new SimpleMarkerSymbol(
 //               Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE)));
  //      mMapView.addLayer(chowkifeatureLayer);

//        final GraphicsLayer glayer=new GraphicsLayer();
//        mMapView.addLayer(glayer);

 //       makeGrid();
//        mMapView.addLayer(trialLayer);
//        layer3=new GraphicsLayer();
 //       mMapView.addLayer(layer3);


   final     TextView latTextView = (TextView) findViewById(R.id.lat);
   final     TextView logTextView = (TextView) findViewById(R.id.log);

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (source == mMapView && status == OnStatusChangedListener.STATUS.INITIALIZED) {

                    mldisplaymanager = mMapView.getLocationDisplayManager();
                    mldisplaymanager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);

                    mldisplaymanager.setLocationListener(new LocationListener() {

                        boolean locationChanged = false;

                        // Zooms to the current location when first GPS fix arrives.
                        @Override
                        public void onLocationChanged(Location loc) {
                            if (!locationChanged) {

                                locationChanged = true;

                                double locy = loc.getLatitude();
                                double locx = loc.getLongitude();
                                locationx = locx;
                                locationy = locy;
                                Point wgspoint = new Point(locx, locy);
                                Point mapPoint = (Point) GeometryEngine
                                        .project(wgspoint,
                                                SpatialReference.create(4326),
                                                mMapView.getSpatialReference());

                                Unit mapUnit = mMapView.getSpatialReference()
                                        .getUnit();

                                double zoomWidth = Unit.convertUnits(
                                        10, Unit.create(mapUnit.getID()),
                                        mapUnit);
                                Envelope zoomExtent = new Envelope(mapPoint,
                                        zoomWidth, zoomWidth);
                                mMapView.setExtent(zoomExtent);

                            }
                        }

                        @Override
                        public void onProviderDisabled(String arg0) {
                        }

                        @Override
                        public void onProviderEnabled(String arg0) {
                        }

                        @Override
                        public void onStatusChanged(String arg0, int arg1,
                                                    Bundle arg2) {
                        }
                    });

                    mldisplaymanager.start();
                }

                if (source == mMapView && status == OnStatusChangedListener.STATUS.LAYER_LOADED) {

                }
            }
        });

//        LocalBroadcastManager.getInstance(this).registerReceiver(
//                new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        double latitude = intent.getDoubleExtra(gpsService.EXTRA_LATITUDE, 0);
//                        double longitude = intent.getDoubleExtra(gpsService.EXTRA_LONGITUDE, 0);
//                        latTextView.setText("Lat: " + latitude);
//                        logTextView.setText("Long: " + longitude);
//                        locationy = latitude;
//                        locationx = longitude;
//
//                        glayer.removeAll();
//                        if (locationy >= 30.4100 && locationy <= 30.4200 && locationx >= 77.9600 && locationx <= 77.9700) {
//                            SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 25, SimpleMarkerSymbol.STYLE.CIRCLE);
//                            Point pnt = new Point(locationx, locationy);
//                            Graphic graphic = new Graphic(pnt, sms);
//                            glayer.addGraphic(graphic);
//                        }
//
//                    }
//                }, new IntentFilter(gpsService.ACTION_LOCATION_BROADCAST)
//        );
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mMapView.pause();
        stopService(new Intent(this, gpsService.class));
    }

    @Override
    protected void onResume()
    {super.onResume();
        mMapView.unpause();
        startService(new Intent(this, gpsService.class));}

    @Override
    protected void onStop()
    {
        super.onStop();
    }
}
