package com.tikal.ortal.camera;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;

    float[] gData = new float[3]; // gravity or accelerometer

    float[] mData = new float[3]; // magnetometer

    float[] rMat = new float[9];

    float[] iMat = new float[9];

    float[] orientation = new float[3];

    private int mAzimuth;

    private CameraPreview mPreview;

    private RelativeLayout mLayout;

    private static final String TAG = "Main Activity";

    private OkHttpClient client;

    public int angle;

    private int points;

    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        mText = (TextView) findViewById(R.id.text);
        client = new OkHttpClient();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Request request = new Request.Builder()
                .url("https://api.myjson.com/bins/28l0a")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String stringBody = response.body().string();
                Log.e(TAG, stringBody);
                try {
                    JSONObject json = new JSONObject(stringBody);
                    angle = json.optInt("angle");
                    points = json.optInt("points");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, angle + "-" + points);
            }
        });
        getWindow().setFormat(PixelFormat.UNKNOWN);


    }

    @Override
    protected void onResume() {
        super.onResume();

        CrowdPointer crowdPointer = new CrowdPointer();

        crowdPointer.findCrowd(getDummyArray(),13);

        mPreview = new CameraPreview(this, 0);
        RelativeLayout.LayoutParams previewLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mLayout.addView(mPreview, 0, previewLayoutParams);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private ArrayList<Point> getDummyArray() {
        ArrayList<Point> array = new ArrayList<>();
        array.add(new Point(4,44));
        array.add(new Point(4,44));
        array.add(new Point(44,44));
        array.add(new Point(77,77));
        array.add(new Point(44,-44));
        array.add(new Point(434,-434));
        array.add(new Point(44,-47));
        array.add(new Point(-44,44));
        array.add(new Point(-4,44));
        array.add(new Point(-4,44));
        array.add(new Point(-44,-44));
        return array;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        mLayout.removeView(mPreview); // This is necessary.
        mPreview = null;
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] data;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_GRAVITY:
                gData = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                gData = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mData = event.values.clone();
                break;
            default:
                return;
        }

        if (SensorManager.getRotationMatrix(rMat, iMat, gData, mData)) {
            mAzimuth =
                    (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360)
                            % 360;
        }
        mText.setText(String.valueOf(mAzimuth));

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
