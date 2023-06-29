package com.example.demo14;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;


public abstract class UnknownObject {
    protected ImageView body;
    public abstract void createRandom();
    public UnknownObject(String path,int WIDTH,int HEIGHT) {
        body = new ImageView(new Image(getClass().getResource(path).toString()));
        body.setFitHeight(HEIGHT);body.setFitWidth(WIDTH);
        body.setEffect(Effects.DropShadow(Color.DARKRED));
    }

    public abstract void move();
}
