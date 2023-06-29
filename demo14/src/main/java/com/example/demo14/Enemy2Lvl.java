package com.example.demo14;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.List;



public class Enemy2Lvl extends Enemy1Lvl{

    public Enemy2Lvl(List< Enemy1Lvl > enemies, Pane map, String path) {
        super(enemies, map,path,75,125,6,2);

    }

    @Override
    public void move() {
        body.setLayoutY(body.getLayoutY() + 3.0);
        heartsImage.forEach(heartsImage->{
            heartsImage.setLayoutY(heartsImage.getLayoutY()+3.0);
        });
        moveTime = new Timeline(new KeyFrame(Duration.millis(10),event -> move()));
        moveTime.play();
    }


    @Override
    public String toString() {
        return "Enemy2Lvl{" +
                "heartImage=" + heartsImage +
                '}';
    }
}
