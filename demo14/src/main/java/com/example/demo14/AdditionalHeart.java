package com.example.demo14;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


import java.util.Random;

public class AdditionalHeart extends UnknownObject{
    public Timeline heartTime;
    public AdditionalHeart(Pane map) {
        super("heart.png",50,50);
        map.getChildren().add(body);
        createRandom();
        move();
    }

    @Override
    public void createRandom() {
        Random randomX = new Random(); Random randomY = new Random();
        int randomXCord = randomX.nextInt(501);
        int randomYCord = randomY.nextInt(50 - 25 + 1) - 50;
        body.setLayoutX(randomXCord);
        body.setTranslateY(randomYCord);
    }
    @Override
    public void move() {
        body.setLayoutY(body.getLayoutY()+4);
        heartTime = new Timeline(new KeyFrame(Duration.millis(10), event -> move()));
        heartTime.play();
    }
    public void destroy(Pane map){
        map.getChildren().remove(body);
        body = new ImageView();
    }
}
