package drew.runnergame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class gameplay extends Activity {

    Canvas canv;
    runnerView runnerView1;
    runner Runner;
    platform plat;
    Bitmap obstacleMap;
    int hi;
    int score;
    int fps;
    long lastFrameTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runnerView1 = new runnerView(this);
        setContentView(runnerView1);
        obstacleMap = BitmapFactory.decodeResource(getResources(), R.drawable.block);
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
            plat = new platform(getResources());

        }

        @Override
        public void run() {
            while (playingRunner) {
                //updateGame();
                controlFPS();
                drawGame();
            }
        }

        public void drawGame() {
            if (ourHolder.getSurface().isValid()) {
                canv = ourHolder.lockCanvas();
                canv.drawColor(Color.WHITE);
                paint.setColor(Color.BLACK);
                paint.setTextSize(75);
                canv.drawText("  Score: " + score + "                           High Score: "
                        + hi, 10, 2200, paint);
                canv.drawBitmap(plat.getImage(), plat.getX(), plat.getHeight(), paint);
                canv.drawBitmap(plat.obs.getImage(), plat.obs.getX(), plat.obs.getHeight(), paint);
                plat.update();
                ourHolder.unlockCanvasAndPost(canv);
            }
        }

        public void controlFPS() {
            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = 100 - timeThisFrame;
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

            Intent i = new Intent(this, drew.runnergame.main.class);
            startActivity(i);
            finish();
            return true;
        }
        return false;
    }
}
    //SOUNDCODE METHOD HERE
