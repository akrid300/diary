package com.example.agenda.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.agenda.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DrawActivity extends AppCompatActivity {

    public static List<Pair<Float,Float>> pointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Draw");

        DrawView drawView = new DrawView(this);
        setContentView(drawView);
        if(pointList == null){
            pointList = new ArrayList<>();
        }

    }


    public class DrawView extends View{

        private Context mContext;
        private Path freePath;
        private Paint pathPaint;
        private Path pointerPath;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Paint bitmapPaint;
        private static final float TOUCH_TOLERANCE = 4;
        private float mx;
        private float my;

        public DrawView(Context context) {
            super(context);

            mContext = context;

            freePath = new Path();
            pointerPath = new Path();

            pathPaint = new Paint(Color.GREEN);
            pathPaint.setStyle(Paint.Style.STROKE);
            pathPaint.setStrokeWidth(12.f);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(mBitmap, 0, 0, bitmapPaint);

            canvas.drawPath(freePath, pathPaint);
            canvas.drawPath(pointerPath, pathPaint);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }



        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    touchDown(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touchUp(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchMove(x,y);
                    invalidate();
                    break;
            }
            return true;
        }


        private void touchDown(float x, float y) {
            freePath.reset();
            freePath.moveTo(x,y);
            mx = x;
            my = y;
            pointList.add(new Pair<>(x, y));
        }

        private void touchMove(float x, float y) {
            float dx = Math.abs(x - mx);
            float dy = Math.abs(y - my);

            //We don't need to take all the points of the trail that our finger makes.
            if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){

                freePath.quadTo(mx, my, (x + mx)/2, (y + my)/2);
                mx = x;
                my = y;
                pointerPath.reset();
                pointerPath.addCircle(mx, my, 20, Path.Direction.CW);
                pointList.add(new Pair<>(x,y));
            }
        }

        private void touchUp(float x, float y) {
            freePath.lineTo(mx, my);
            pointerPath.reset();
            mCanvas.drawPath(freePath, pathPaint);
            freePath.reset();

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Would you like to save it?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    saveImage();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        private void saveImage() {
            Intent resultIntent = new Intent();

            byte[] byteArray = bitmapToByteArray(mBitmap);
            resultIntent.putExtra("drawing", byteArray);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }


    public static byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        return  byteArray;
    }
}
