package com.example.demo14;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.demo14.Projectile.updateCordinates;

public class Main extends Application implements IconInterface,GameSettings {
    public static final Pane map = new Pane();
    private Scene scene;
    private final HitCheck hitCheck = new HitCheck();
    private static final SpaceShip spaceShip = new SpaceShip();
    public static final List< Projectile > FlyingProjectiles = new ArrayList<>();
    public static final List< Enemy1Lvl > enemies = new ArrayList<>();
    public static Projectile FlyingProjectileEnemy;
    public static AdditionalHeart additionalHeart = new AdditionalHeart(map);
    private static final Label countDeadEnemy = new Label("Очков: " + spaceShip.points);
    private static final Label missedEnemy = new Label("Пропущено врагов: " + Enemy1Lvl.missedEnemeis);
    public static final List< ImageView > hearts = new ArrayList<>(5);
    public static final List< ImageView > energy = new ArrayList<>();
    public static Timeline enemy1LvlSpawn;
    public static  Timeline enemy2LvlSpawn;
    public static Timeline additionalHeartTime;
    public static Timeline backTime;
    private final ImageView background = new ImageView(new Image(getClass().getResource("background.jpg").toString()));
    private final ImageView backgroundCopy = new ImageView(new Image(getClass().getResource("background.jpg").toString()));
    public static ImageView pauseImage = new ImageView(new Image(Main.class.getResource("button-pause.png").toString()));

    @Override
    public void start(Stage stage) {

        interfaceSettings(countDeadEnemy,map,missedEnemy,hearts);

        scene = new Scene(map, 559, 900);
        scene.setOnKeyPressed(event -> spaceShip.control.handleKeyPress(event.getCode()));
        scene.setOnKeyReleased(event -> spaceShip.control.handleKeyRelease(event.getCode()));

        stage.setScene(scene);
        stage.setTitle("SpaceTrack");
        stage.getIcons().add(new Image(getClass().getResource("icon.png").toString()));
        stage.show();
        StartGame();
    }
    public void StartGame() {
        updateBackground();
        CreateNewEnemy();
        spaceShip.shoot(map, FlyingProjectiles);
        updateCordinates(FlyingProjectiles,map);
        hitCheck.start();
        CreateAdditionalHeart();
    }
    class HitCheck extends AnimationTimer {

        @Override
        public void handle(long l) {
            System.out.println(spaceShip.getPointHP());

            Iterator< Enemy1Lvl > enemyIterator = enemies.listIterator();
            while (enemyIterator.hasNext()) {
                Enemy1Lvl currentEnemy = enemyIterator.next();
                //Проверка на столкновение
                if (spaceShip.getShipImage().getBoundsInParent().intersects(currentEnemy.body.getBoundsInParent())) {

                    if (currentEnemy instanceof Enemy2Lvl) {
                        energyImageShow(spaceShip, map, energy);
                    }
                    new Effects().destroyEffect(currentEnemy.body, map);
                    enemyIterator.remove();
                    currentEnemy.dead(enemies, map);
                    spaceShip.setPointHP(spaceShip.getPointHP() - currentEnemy.getDamage());
                    if (spaceShip.getPointHP() <= 0) {

                        spaceShip.control.setPaused(true);
                        Platform.runLater(() -> showLoseDialog("Ви програли," +
                                "\nваш рахунок : " + countDeadEnemy.getText()));
                    }
                    for (int i = 0; i < currentEnemy.getDamage(); i++) {
                        map.getChildren().remove(hearts.get(hearts.size() - 1));
                        hearts.remove(hearts.get(hearts.size() - 1));
                    }
                    new Effects().shakeScreen(map,-5,2);

                    break;
                }
                //Проверка на расположения игрока возле сердца
                if (spaceShip.getPointHP() < 5 && spaceShip.getShipImage().getBoundsInParent()
                        .intersects(additionalHeart.body.getBoundsInParent())) {
                    heartImageShow(spaceShip, additionalHeart, map, hearts);
                    break;
                }
                //Проверка на то прошел ли враг
                if (currentEnemy.body.getLayoutY() > 900) {

                    Enemy1Lvl.missedEnemeis++;
                    if (Enemy1Lvl.missedEnemeis >= 10) {
                        spaceShip.control.setPaused(true);
                        showLoseDialog("Ви програли з рахунком:" + spaceShip.points);
                    }
                    enemyIterator.remove();
                    currentEnemy.dead(enemies, map);
                    updateLabel();
                    // Создание эффекта мигания
                    Effects.missEnemy(map).play();
                    break;

                }
                if (spaceShip.getMoode().equals(Moode.DEFOLT)) {
                    //Проверка на касание снарядов
                    Iterator< Projectile > projectileIterator = FlyingProjectiles.listIterator();
                    while (projectileIterator.hasNext()) {
                        Projectile currentProjectile = projectileIterator.next();
                        //Проверка попадания у врага
                        if (currentEnemy.body.getBoundsInParent().contains(currentProjectile.getProjectile().getBoundsInParent())) {

                            new Effects().destroyEffect(currentEnemy.body, map);
                            currentEnemy.setPointsHP(currentEnemy.getPointsHP() - 1);

                            projectileIterator.remove();
                            currentProjectile.destroy(FlyingProjectiles, map);


                            if (currentEnemy.getPointsHP() <= 0) {
                                enemyIterator.remove();
                                currentEnemy.dead(enemies, map);
                                if (currentEnemy instanceof Enemy2Lvl) {
                                    energyImageShow(spaceShip, map, energy);

                                }
                            }
                            map.getChildren().remove(currentEnemy.heartsImage.get(currentEnemy.heartsImage.size() - 1));
                            currentEnemy.heartsImage.remove(currentEnemy.heartsImage.get(currentEnemy.heartsImage.size() - 1));


                            spaceShip.points++;
                            updateLabel();
                            break;
                        }
                        //Проверка попадания у игрока
                        /*
                            if (currentProjectile.getProjectile().getBoundsInParent().intersects(spaceShip.getShipImage().getBoundsInParent())) {

                                new Effects().destroyEffect(spaceShip.getShipImage(), map);
                                new Effects().shakeScreen(map, - 5, 2);

                                spaceShip.setPointHP(spaceShip.getPointHP() - 1);
                                currentProjectile.destroy(FlyingProjectiles, map);

                                map.getChildren().remove(hearts.get(hearts.size() - 1));
                                hearts.remove(hearts.get(hearts.size() - 1));


                                if (spaceShip.getPointHP() <= 0) {
                                    spaceShip.control.setPaused(true);
                                    Platform.runLater(() -> showLoseDialog("Ви програли," +
                                            "\nваш рахунок : " + countDeadEnemy.getText()));
                                }
                                break;
                            }
                        */
                    }
                } else if (spaceShip.getMoode().equals(Moode.LASER)) {
                    if (spaceShip.getLaser().getLaserImage().intersects(currentEnemy.body.getBoundsInParent())) {
                        new Effects().destroyEffect(currentEnemy.body, map);
                        enemyIterator.remove();
                        currentEnemy.dead(enemies, map);
                        spaceShip.points++;
                        updateLabel();
                        break;

                    }
                }
            }

        }
    }
    @Override
    public  void CreateNewEnemy() {
         enemy1LvlSpawn = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            Enemy1Lvl enemy1Lvl = new Enemy1Lvl(enemies, map, "Enemy.png", 50, 50, 1,1);
        }));
         enemy2LvlSpawn = new Timeline(new KeyFrame(Duration.seconds(4), event -> {
            Enemy2Lvl enemy2Lvl = new Enemy2Lvl(enemies, map, "Enemy2LVL.png");

        }));
        enemy1LvlSpawn.setCycleCount(Animation.INDEFINITE);
        enemy2LvlSpawn.setCycleCount(Animation.INDEFINITE);
        enemy1LvlSpawn.play();
        enemy2LvlSpawn.play();
    }
    @Override
    public  void CreateAdditionalHeart() {
        additionalHeartTime = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            additionalHeart = new AdditionalHeart(map);
        }));
        additionalHeartTime.setCycleCount(Animation.INDEFINITE);
        additionalHeartTime.play();
    }
    @Override
    public void updateBackground() {
        backTime = new Timeline(new KeyFrame(Duration.millis(6), event -> {
            double speed = 1.0;

            background.setTranslateY(background.getTranslateY() + speed);
            backgroundCopy.setTranslateY(backgroundCopy.getTranslateY() + speed);

            if (background.getTranslateY() >= 0) {
                if (backgroundCopy.getTranslateY() >= 0 && background.getTranslateY() >= 0) {
                    background.setTranslateY(1);
                    backgroundCopy.setTranslateY(1);
                }
                background.setTranslateY(backgroundCopy.getTranslateY() - background.getImage().getHeight());
            }
        }));
        backTime.setCycleCount(Animation.INDEFINITE);
        backTime.play();
    }

    @Override
    public void updateLabel() {
        map.getChildren().remove(countDeadEnemy);
        map.getChildren().remove(missedEnemy);
        countDeadEnemy.setText("Очков: " + spaceShip.points);
        missedEnemy.setText("Пропущено врагов: " + Enemy1Lvl.missedEnemeis);
        map.getChildren().add(countDeadEnemy);
        map.getChildren().add(missedEnemy);
    }

    @Override
    public void heartImageShow(SpaceShip spaceShip, AdditionalHeart additionalHeart, Pane map, List< ImageView > hearts) {
        spaceShip.setPointHP(spaceShip.getPointHP() + 1);
        additionalHeart.destroy(map);
        ImageView heart = new ImageView(new Image(getClass().getResource("heart.png").toString()));
        map.getChildren().add(heart);
        heart.setFitWidth(50);
        heart.setFitHeight(50);
        hearts.add(heart);
        hearts.get(hearts.indexOf(heart)).setX(hearts.get(hearts.indexOf(heart) - 1).getX() + 25);
        hearts.get(hearts.indexOf(heart)).setY(10);
    }

    @Override
    public void energyImageShow(SpaceShip spaceShip, Pane map, List< ImageView > energy) {

        if (spaceShip.getPointEnergy() != 3) {
            spaceShip.setPointEnergy(spaceShip.getPointEnergy() + 1);
            ImageView energetik = new ImageView(new Image(getClass().getResource("energy.png").toString()));
            map.getChildren().add(energetik);
            energetik.setFitWidth(50);
            energetik.setFitHeight(50);
            energy.add(energetik);
            if (energy.indexOf(energetik) == 0) {
                energy.get(0).setX(415);
            } else {
                energy.get(energy.indexOf(energetik)).setX(energy.get(energy.indexOf(energetik) - 1).getX() + 25);
            }
            energy.get(energy.indexOf(energetik)).setY(50);
        }
    }
    @Override
    public void interfaceSettings(Label countDeadEnemy, Pane map, Label missedEnemy, List< ImageView > hearts) {
        background.setFitWidth(564);background.setFitHeight(1080);
        backgroundCopy.setFitWidth(564);backgroundCopy.setFitHeight(1080);


        countDeadEnemy.setFont(Font.font(24.0));
        missedEnemy.setFont(Font.font(24.0));

        countDeadEnemy.setStyle("-fx-background-color: rgb(110,35,35,0.5); -fx-text-fill:" +
                " #9f0000; -fx-font-family: 'Century Gothic'; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 1);");

        countDeadEnemy.setLayoutY(countDeadEnemy.getLayoutY() + 10);
        countDeadEnemy.setLayoutX(5);
        missedEnemy.setLayoutX(5);
        missedEnemy.setLayoutY(countDeadEnemy.getLayoutY() + 50);
        missedEnemy.setStyle(countDeadEnemy.getStyle());


        pauseImage.setFitHeight(50);pauseImage.setFitWidth(60);
        pauseImage.setEffect(Effects.DropShadow(Color.DARKRED));
        pauseImage.setLayoutY(815);pauseImage.setLayoutX(10);

        map.getChildren().add(background);
        map.getChildren().add(backgroundCopy);
        map.getChildren().add(spaceShip.getShipImage());
        map.getChildren().add(countDeadEnemy);
        map.getChildren().add(missedEnemy);
        map.getChildren().add(pauseImage);

        for (int i = 0; i < 5; i++) {
            hearts.add(new ImageView(new Image(getClass().getResource("heart.png").toString())));
            hearts.get(i).setFitHeight(50);
            hearts.get(i).setFitWidth(50);
            hearts.get(i).setX(400 + i * 25 + 15);
            hearts.get(i).setY(10);
            map.getChildren().add(hearts.get(i));
        }
    }

    private void showLoseDialog (String message) {

        Main.enemy1LvlSpawn.stop();
        Main.enemy2LvlSpawn.stop();
        Main.additionalHeartTime.stop();
        Main.additionalHeart.heartTime.stop();
        if (spaceShip.getMoode().equals(Moode.LASER)) {
            spaceShip.coldownLaser.stop();
        }
        //Projectile.timeTickEnemy.stop();
        Projectile.timeTickPlayer.stop();
        Main.backTime.stop();
        for (Enemy1Lvl enemy1Lvl : Main.enemies) {
            enemy1Lvl.moveTime.stop();
        }

        for (Enemy1Lvl enemy1Lvl: enemies){
            map.getChildren().remove(enemy1Lvl.body);
            for (int i = 0; i < enemy1Lvl.heartsImage.size(); i++) {
                map.getChildren().remove(enemy1Lvl.heartsImage.get(i));
            }
        }
        for (Projectile projectile: FlyingProjectiles){
            map.getChildren().remove(projectile.getProjectile());
        }
        for (ImageView energy:energy){
            map.getChildren().remove(energy);
        }
        spaceShip.coldown.stop();
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType restartButton = new ButtonType("Ок");
        alert.getButtonTypes().add(restartButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == restartButton) {
                Platform.exit();
            }
        });
    }
        public static void main (String[]args){
            launch();
        }
    }
