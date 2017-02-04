package drew.runnergame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class obstacle {

    private int height;
    private int x = 600;
    private int speed;
    private Bitmap image;
    public boolean living;


    public obstacle(Resources res, int platformHeight, int speed, int platformX, boolean living) {
        this.speed = speed;
        this.image = BitmapFactory.decodeResource(res, R.drawable.block);
        this.image = Bitmap.createScaledBitmap(this.image, 110, 110, false);
        this.height = platformHeight - 110;
        Random randInt = new Random();
        this.x = platformX + randInt.nextInt(500);

    }

    public Bitmap getImage(){
        return this.image;
    }

    public int getX(){
        return this.x;
    }

    public int getHeight(){
        return this.height;
    }

    public void update(){
        this.x-=this.speed;
        if (this.x<0){
            this.living = false;
        }
    }
}