package com.example.demo14;


import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Iterator;
import java.util.List;

import static java.lang.Thread.sleep;

public class Projectile {
    private ImageView projectile;
    public static Timeline timeTickPlayer;

    public ImageView getProjectile() {
        return projectile;
    }
    public static void updateCordinates(List<Projectile> FlyingProjectiles,Pane map){

        timeTickPlayer = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            Iterator<Projectile> iterator = FlyingProjectiles.iterator();
            while (iterator.hasNext()) {
                Projectile projectile = iterator.next();
                    projectile.getProjectile().setY(projectile.getProjectile().getY() - 5);
                    if (projectile.getProjectile().getY() <= 0) {
                        iterator.remove(); // Удаление элемента через итератор
                        projectile.destroy(FlyingProjectiles, map);
                    }

            }
        }));

        timeTickPlayer.setCycleCount(Animation.INDEFINITE);
        timeTickPlayer.play();

    }
    public void destroy(List<Projectile> projectiles, Pane map){
        projectiles.remove(this);
        map.getChildren().remove(projectile);
    }

    public Projectile(SpaceShip spaceShip) {
        projectile = new ImageView(getClass().getResource("Projectile.png").toString());
        projectile.setX(spaceShip.getShipImage().getX()+40);projectile.setY(spaceShip.getShipImage().getY()-20);
    }


}
