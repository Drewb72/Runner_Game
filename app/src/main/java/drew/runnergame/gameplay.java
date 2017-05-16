package drew.runnergame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Gameplay extends Activity {

    Canvas canv;
    runnerView runnerView1;
    Runner runner1;
    Platform plat1;
    Platform plat2;
    int hi;
    int score;
    int fps;
    int height;
    int width;
    int runnerHeight;
    long lastFrameTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runnerView1 = new runnerView(this);
        setContentView(runnerView1);
    }

    class runnerView extends SurfaceView implements Runnable {
        Thread ourThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playingRunner;
        Paint paint;

        public runnerView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();
            plat1 = new Platform(getResources(), 0);
            plat2 = new Platform(getResources(), 1200);
            height = plat1.getHeight();
            width = plat1.getWidth();
            runner1 = new Runner(getResources(), 200, height - width);
        }


        @Override
        public void run() {
            while (playingRunner) {
                //updateGame();
                controlFPS();
                drawGame();
                if (!plat1.isLiving()) {
                    plat1 = new Platform(getResources(), 1400);
                }
                if (!plat2.isLiving()) {
                    plat2 = new Platform(getResources(), 1400);
                    runnerHeight = runner1.getHeight();
                }
                /*if (plat1.getX() >= 0 && plat1.getX() <= plat1.getLength()){
                    height = plat1.getHeight();
                    width = plat1.getWidth();
                }
                if (plat2.getX() >= 0 && plat2.getX() <= plat2.getLength()) {
                    height = plat2.getHeight();
                    width = plat2.getWidth();
                }*/
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                runner1.jump();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                runner1.dontJump();
            }
            return true;
        }


        public void drawGame() {
            if (ourHolder.getSurface().isValid()) {
                canv = ourHolder.lockCanvas();
                canv.drawColor(Color.WHITE);
                paint.setColor(Color.BLACK);
                paint.setTextSize(75);
                canv.drawText("  Score: " + score + "                           High Score: "
                        + hi, 10, 2200, paint);
                canv.drawBitmap(plat1.getImage(), plat1.getX(), plat1.getHeight(), paint);
                canv.drawBitmap(plat1.obs.getImage(), plat1.obs.getX(), plat1.obs.getHeight(), paint);
                plat1.update();
                canv.drawBitmap(plat2.getImage(), plat2.getX(), plat2.getHeight(), paint);
                canv.drawBitmap(plat2.obs.getImage(), plat2.obs.getX(), plat2.obs.getHeight(), paint);
                plat2.update();
                //runner1.collidePlatform(plat1);
                plat1.collideRunner(runner1);
                if (runner1.isUnderPlat()) {
                    Log.i("onPlat", "X-Axis");
                }
                //runner1.collidePlatform(plat2);
                plat2.collideRunner(runner1);
                runner1.update(plat1);
                runner1.update(plat2);
                canv.drawBitmap(runner1.getImage(), runner1.getX(), runner1.getHeight() - 300, paint);
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
            lastFrameTime = (System.currentTimeMillis());
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            runnerView1.pause();
            Intent i = new Intent(this, Main.class);
            startActivity(i);
            finish();
            return true;
        }
        return false;
    }
}
//SOUNDCODE METHOD HERE
