package drew.runnergame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import java.util.Random;

public class game extends Activity {

    Canvas canv;
    runnerView runnerView1;
    Bitmap runner;
    Bitmap platform;
    Bitmap obstacle;
    Bitmap obstacleMap;
    int numberOfObstacles;
    long lastFrameTime;
    int fps;
    int score;
    int hi;
    boolean jumping;
    int obstacleSpeed;
    int obstacleX = 500;
    int obstacleY;
    int runnerX;
    int runnerY;
    int blockSize;
    int numBlocksWide;
    int numBlocksHigh;
    int platformHeight = 1200;
    int platformX;
    int frameWidth;
    int frameHeight;
    Rect obstacleRect;
    Rect destRect;


    @Override
    protected void onCreate (Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        configureDisplay();
        runnerView1 = new runnerView(this);
        setContentView(runnerView1);
        obstacleMap = BitmapFactory.decodeResource(getResources(), R.drawable.block);
        frameWidth = 110;
        frameHeight = 110;
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
            obstacleSpeed = 10;
            jumping = false;
            getRunner();
            getPlatform();

        }
        public void getObstacle() {
            obstacleX --;
            if (obstacleX == 0){
                obstacleX = 500;
            }
            frameHeight = platformHeight-110;
        }
        public void getRunner() {
            runnerX = (numBlocksWide / 4);
            runnerY = (numBlocksHigh / 2);
        }
        public void getPlatform() {
            Random randInt = new Random();
            int dif = randInt.nextInt(150);
            int platformRandomness = randInt.nextInt(2);
            switch (platformRandomness) {
                case 0:
                    platformHeight = platformHeight + dif;
                    break;
                case 1:
                    platformHeight = platformHeight - dif;
                    break;
            }
            if (platformHeight < 600){
                platformHeight = platformHeight + 150;
            }
            if (platformHeight > 1800){
                platformHeight = platformHeight - 150;
            }
            }

        @Override
        public void run() {
            while (playingRunner) {
                getObstacle();
                updateGame();
                controlFPS();
                drawGame();
            }
        }
        public void updateGame(){
            if(obstacleX == runnerX && obstacleY != runnerY){
                numberOfObstacles += 1;
                score += numberOfObstacles;
                getObstacle();
                //add sound code
            }
            boolean dead = false;
            if (obstacleX == runnerX && obstacleY == runnerY){
                dead = true;
            }
            if (dead){
                score =0;
                //start over
                getRunner();
                getObstacle();
                getPlatform();
            }
        }
        public void drawGame(){
            if (ourHolder.getSurface().isValid()){
                canv = ourHolder.lockCanvas();
                canv.drawColor(Color.WHITE);
                paint.setColor(Color.BLACK);
                paint.setTextSize(75);
                canv.drawText("  Score: " + score + "                           High Score: "
                        + hi, numBlocksWide, 2200, paint);
                //canv.drawBitmap(obstacle, obstacleX*blockSize, platformHeight - 110, paint);
                obstacleRect = new Rect(obstacleX + frameWidth - 1, 0,
                        obstacleX + frameWidth + frameWidth - 1, frameHeight);
                destRect = new Rect(500, platformHeight-110 , 610, platformHeight);
                canv.drawBitmap(obstacleMap, obstacleRect,destRect,paint);
                canv.drawBitmap(runner, 30, platformHeight - 280, paint);
                canv.drawBitmap(platform, 0, platformHeight, paint);
                ourHolder.unlockCanvasAndPost(canv);
            }
        }
        public void controlFPS(){
            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = 100 - timeThisFrame;
            if (timeThisFrame > 0) {
                fps = (int) (1000 / timeThisFrame);
            }
            if (timeToSleep > 0){
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
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                jumping = true;
                runnerY = numBlocksHigh / 2 + blockSize * 5;
            } else {
                jumping = false;
                runnerY = (numBlocksHigh / 2);
            }
            return false;
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
    protected void onPause(){
        super.onPause();
        runnerView1.pause();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            runnerView1.pause();

            Intent i = new Intent(this, drew.runnergame.main.class);
            startActivity(i);
            finish();
            return true;
        }
        return false;
    }
    //SOUNDCODE METHOD HERE
    public void configureDisplay(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        blockSize = screenWidth/10;
        numBlocksWide = 10;
        runner = BitmapFactory.decodeResource(getResources(), R.drawable.stick_figure);
        platform = BitmapFactory.decodeResource(getResources(), R.drawable.platform);
        obstacle = BitmapFactory.decodeResource(getResources(), R.drawable.block);
        runner = Bitmap.createScaledBitmap(runner, blockSize*2, blockSize*2, false);
        platform = Bitmap.createScaledBitmap(platform, blockSize*6, blockSize/2, false);
        obstacle = Bitmap.createScaledBitmap(obstacle, blockSize/3*2, blockSize/5*4, false);
    }
}
