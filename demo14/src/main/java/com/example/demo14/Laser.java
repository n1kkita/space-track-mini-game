package com.example.demo14;


import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;


public class Laser {
    private ImageView laser;
    private Circle statLase;

    public Laser(SpaceShip spaceShip) {
        laser = new ImageView(getClass().getResource("laser.png").toString());
        laser.setX(spaceShip.getShipImage().getX()+32);laser.setY(spaceShip.getShipImage().getY()-1000);
        laser.setEffect(Effects.DropShadow(Color.RED,50.0,25.0,25.0,0));
        statLase = new Circle(25);
        Scale scale = new Scale(0.7, 0.5);
        statLase.getTransforms().add(scale);
        statLase.setLayoutX(spaceShip.getShipImage().getX()+48);statLase.setLayoutY(spaceShip.getShipImage().getY()-10);
        statLase.setEffect(getLaserImage().getEffect());

    }
    public Circle getStatLase() {
        return statLase;
    }
    public ImageView getLaserImage() {
        return laser;
    }

    public void setLaser(ImageView laser) {
        this.laser = laser;
    }
}
