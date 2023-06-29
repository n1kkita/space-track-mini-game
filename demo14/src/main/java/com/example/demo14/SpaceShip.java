package com.example.demo14;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import java.util.List;

public class SpaceShip {
    private ImageView shipImage;
    private Projectile[] projectile = new Projectile[2];
    private Moode moode;
    private Laser laser;
    private int pointHP = 5;
    public int points = 0;
    private int pointEnergy = 0;
    public Timeline coldown;
    public Timeline coldownLaser;
    public Control control;
    public void shoot(Pane map, List<Projectile> projectiles) {
        if(moode == Moode.DEFOLT) {

            projectile[0] = new Projectile(this);
            projectile[1] = new Projectile(this);
            map.getChildren().add(projectile[0].getProjectile());
            map.getChildren().add(projectile[1].getProjectile());
            projectiles.add(projectile[0]);projectiles.add(projectile[1]);
            projectile[0].getProjectile().setX(getShipImage().getX()+25);projectile[0].getProjectile().setY(getShipImage().getY()-20);
            projectile[1].getProjectile().setX(getShipImage().getX()+55);projectile[1].getProjectile().setY(getShipImage().getY()-20);

            coldown = new Timeline(new KeyFrame(Duration.millis(350), event -> shoot(map, projectiles)));
            coldown.play();
        }

    }
    public void lazer(Pane map){

        if(moode == Moode.LASER){

            map.getChildren().remove(laser.getLaserImage());
            map.getChildren().remove(laser.getStatLase());

            laser.getStatLase().setLayoutX(getShipImage().getX()+48);laser.getStatLase().setLayoutY(getShipImage().getY()-10);
            laser.getLaserImage().setX(getShipImage().getX()+32);laser.getLaserImage().setY(getShipImage().getY()-1000);


            map.getChildren().add(laser.getStatLase());
            map.getChildren().add(laser.getLaserImage());


        }
    }
    public SpaceShip() {
        shipImage = new ImageView(new Image(getClass().getResource("Ship.png").toString()));
        shipImage.setFitHeight(100);shipImage.setFitWidth(100);
        shipImage.setX(200);shipImage.setY(500);
        shipImage.setEffect(Effects.DropShadow(Color.DARKGREEN));
        control = new Control(this);
        moode = Moode.DEFOLT;
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                control.update();
            }
        }.start();
        laser = new Laser(this);
    }
    public int getPointHP() {return pointHP;}
    public void setPointHP(int pointHP) {this.pointHP = pointHP;}
    public ImageView getShipImage() {return shipImage;}
    public Moode getMoode() {
        return moode;
    }
    public void setMoode(Moode moode) {
        this.moode = moode;
    }
    public int getPointEnergy() {
        return pointEnergy;
    }

    public void setPointEnergy(int pointEnergy)
    {
        if(getPointEnergy()<4) {
            this.pointEnergy = pointEnergy;
        }
    }
    public Laser getLaser() {return laser;}


}
