package com.example.demo14;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


import java.util.List;

public interface IconInterface {
     void heartImageShow(SpaceShip spaceShip, AdditionalHeart additionalHeart, Pane map, List< ImageView > hearts);
     void energyImageShow(SpaceShip spaceShip,  Pane map, List< ImageView > energy);
     void interfaceSettings(Label countDeadEnemy, Pane map, Label missedEnemy, List< ImageView > hearts);
     void updateLabel();

}
