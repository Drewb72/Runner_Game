package drew.runnergame;

import android.content.res.
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Random;

public class platform {
    private int height;
    private Bitmap image;
    private int speed;

    public platform(){
        this.speed = 5;;
        this.image =  BitmapFactory.decodeResource(getResources(), R.drawable.platform);
        this.height = 1200;
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
    }

    public int getHeight() {
        return this.height;
    }
}
