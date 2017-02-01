package drew.runnergame;

import drew.runnergame.platform;

public class obstacle {

    int platormHeight =

    public void updateObstacle(){
        obstacleX --;
        if (obstacleX == 0){
            obstacleX = 500;
        }
        frameHeight = platformHeight-110;
    }
}