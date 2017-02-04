package drew.runnergame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class platform {
    private int height;
    private Bitmap image;
    private int speed;
    private int x;
    private boolean living;
    public obstacle obs;



    public platform(Resources res) {
        this.speed = 12;
        this.image = BitmapFactory.decodeResource(res, R.drawable.platform);
        this.image = Bitmap.createScaledBitmap(this.image, 600, 50, false);
        this.x = 400;
        this.living = true;
        this.height = 1200;
        obs = new obstacle(res, this.height, this.speed, this.x, this.living);
    }

    public Bitmap getImage(){
        return this.image;
    }

    public int getX(){
        return this.x;
    }

    public int getHeight(){
        Random randInt = new Random();
        int dif = randInt.nextInt(150);
        int platformRandomness = randInt.nextInt(2);
        switch (platformRandomness) {
            case 0:
                this.height += dif;
                break;
            case 1:
                this.height -= dif;
                break;
        }
        if (this.height < 600){
            this.height += 150;
        }
        if (this.height > 1800){
            this.height -= 150;
        }

        return this.height;
    }

    public boolean isLiving(){
        return this.living;
    }

    public void update(){
        this.x-=this.speed;
        if (this.x<0){
            this.living = false;
        }
        obs.update();
    }
}