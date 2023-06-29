package com.example.demo14;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy1Lvl extends UnknownObject{
    public static int missedEnemeis = 0;
    private int pointsHP;
    private int damage;
    protected List<ImageView> heartsImage = new ArrayList<>();
    @Override
    public String toString() {
        return "Enemy1Lvl{" +
                "heartImage=" + heartsImage +
                '}';
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Enemy1Lvl(List<Enemy1Lvl> enemies, Pane map, String path, int WIDTH, int HEIGHT, int pointsHP, int damage) {
        super(path,WIDTH,HEIGHT);
        this.pointsHP = pointsHP;
        this.damage = damage;
        for (int i = 0; i < pointsHP; i++) {
            heartsImage.add( new ImageView(new Image(getClass().getResource("heart.png").toString())));
            heartsImage.get(i).setFitHeight(25);
            heartsImage.get(i).setFitWidth(25);
            map.getChildren().add(heartsImage.get(i));
        }
        enemies.add(this);

        map.getChildren().add(body);
        createRandom();
        move();
    }

    public int getPointsHP() {
        return pointsHP;
    }

    @Override
    public void createRandom(){
        Random randomX = new Random(); Random randomY = new Random();
        int randomXCord = randomX.nextInt(501);
        int randomYCord = randomY.nextInt(50 - 25 + 1) - 50;
        body.setLayoutX(randomXCord);
        body.setLayoutY(randomYCord);
        System.out.println(body.getLayoutX());
        System.out.println(body.getLayoutY());

        for (int i =0;i < pointsHP;i++) {
            if(pointsHP==1){
                heartsImage.get(0).setLayoutX(randomXCord+12.5);
                heartsImage.get(0).setLayoutY(randomYCord+body.getFitHeight()+5);
            } else {
                heartsImage.get(i).setLayoutX(randomXCord + i * 12.5);
                heartsImage.get(i).setLayoutY(randomYCord + body.getFitHeight() + 5);
            }
        }
    }
    public Timeline moveTime;
    public void move(){

        body.setLayoutY(body.getLayoutY() + 5.0);
        heartsImage.forEach(heartsImage->{
            heartsImage.setLayoutY(heartsImage.getLayoutY()+5.0);
        });
        moveTime = new Timeline(new KeyFrame(Duration.millis(10),event -> move()));
        moveTime.play();

    }
    public void dead(List< Enemy1Lvl > enemies, Pane map){
        enemies.remove(this);
        map.getChildren().remove(body);
        heartsImage.forEach(x->map.getChildren().remove(x));
    }
    public void setPointsHP(int pointsHP) {this.pointsHP = pointsHP;}

}
