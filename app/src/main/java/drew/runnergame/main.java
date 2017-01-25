package drew.runnergame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class main extends Activity{

    int numFrames = 30;
    int frameWidth = 100;
    int frameHeight = 150;
    int frameNumber = numFrames;
    int screenWidth;
    int screenHeight;
    int hi;
    int fps;
    long lastFrameTime;
    Rect runnerRect;
    Rect destRect;
    Bitmap runnerMap;
    Canvas canv;
    runnerView runnerView1;
    Intent j;
    //Button btn[] = new Button[3];
    //int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = (size.x);
        screenHeight = (size.y);
        Resources res = getResources();
        runnerMap = BitmapFactory.decodeResource(res, R.drawable.stick_figure);
        runnerView1 = new runnerView(this);
        setContentView(runnerView1);
        j = new Intent (this, game.class);
    }
       /* btn[0] = (Button) findViewById(R.id.button);
        btn[1] = (Button) findViewById(R.id.button2);
        btn[2] = (Button) findViewById(R.id.button3);
        for (int x = 0; x < 3; x++) ;
        btn[x].setOnClickListener(this);*/
    //Add sound code later here
/*
@Override
    public void onClick(View view) {
        if (view == findViewById(R.id.button)) {
            Intent e = new Intent(Intent.ACTION_MAIN);
            e.addCategory(Intent.CATEGORY_HOME);
            e.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(e);
        }
        if (view == findViewById(R.id.button2)) {
            setContentView(runnerView1);
            j = new Intent (this, game.class);
        }
        if (view == findViewById(R.id.button3)) {
            setContentView(runnerView1);
            j = new Intent (this, game.class);
        }
    }
*/

    class runnerView extends SurfaceView implements Runnable {
        Thread ourThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playingRunner;
        Paint paint;

        public runnerView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();
            frameWidth = (runnerMap.getWidth()/3);
            frameHeight = (runnerMap.getHeight());
        }

        @Override
        public void run() {
            while (playingRunner) {
                update();
                draw();
                controlFPS();
            }
        }

        public void update() {
            runnerRect = new Rect((30*frameNumber + frameWidth) - 1, 0,
            (30*frameNumber + frameWidth + frameWidth) - 1, frameHeight);
            frameNumber--;
            if (frameNumber == -30) {
                frameNumber = numFrames;
            }
        }

        public void draw() {
            if (ourHolder.getSurface().isValid()) {
                canv = ourHolder.lockCanvas();
                canv.drawColor(Color.WHITE);
                paint.setColor(Color.BLACK);
                paint.setTextSize(100);
                canv.drawText("Tap to Begin Running", screenWidth/6, screenHeight - 500, paint);
                paint.setTextSize(75);
                canv.drawText("High Score: " + hi, screenWidth/3, screenHeight - 250, paint);
                destRect = new Rect(0, 30, 1450, 2000);
                canv.drawBitmap(runnerMap, runnerRect, destRect, paint);
                ourHolder.unlockCanvasAndPost(canv);
            }
        }

        public void controlFPS() {
            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = 15 - timeThisFrame;
            if (timeThisFrame > 0) {
                fps = (int) (1000 / timeThisFrame);
            }
            if (timeToSleep > 0) {
                try {
                    ourThread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                }
            }
            lastFrameTime = System.currentTimeMillis();
        }
        public void pause() {
            playingRunner = false;
            try {
                ourThread.join();
            } catch (InterruptedException e) {
            }
        }

        public void resume() {
            playingRunner = true;
            ourThread = new Thread(this);
            ourThread.start();
        }
        @Override
            public boolean onTouchEvent (MotionEvent motionEvent) {
            startActivity (j);
            return true;
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        while (true) {
            runnerView1.pause();
            break;
        }
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        runnerView1.resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        runnerView1.pause();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            runnerView1.pause();
            finish();
            return true;
        }
        return false;
    }
}

