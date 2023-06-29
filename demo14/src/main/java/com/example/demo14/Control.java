package com.example.demo14;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import static com.example.demo14.Main.map;

public class Control {
    private static final double SPEED = 5.5;
    private boolean upPressed = false;

    public boolean isUpPressed() {
        return upPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean coldwond = true;
    private SpaceShip spaceShip;

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    private boolean isPaused = false;
    public Control(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }
    public void handleKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case UP:
                upPressed = true;
                break;
            case DOWN:
                downPressed = true;
                break;
            case LEFT:
                leftPressed = true;
                break;
            case RIGHT:
                rightPressed = true;
                break;
            case X:
                if(spaceShip.getPointEnergy() != 0 &&!isPaused) {
                    spaceShip.setMoode(Moode.LASER);
                    if (coldwond = true) {
                        new Effects().shakeScreen(map,-5,30);
                        new AnimationTimer() {
                            @Override
                            public void handle(long now) {
                                coldwond = false;

                                spaceShip.lazer(map);
                            }
                        }.start();
                        spaceShip.coldownLaser = new Timeline(new KeyFrame(Duration.seconds(2.5), event -> {
                            coldwond = true;
                            spaceShip.setMoode(Moode.DEFOLT);
                            map.getChildren().remove(spaceShip.getLaser().getLaserImage());
                            map.getChildren().remove(spaceShip.getLaser().getStatLase());
                            spaceShip.setPointEnergy(spaceShip.getPointEnergy()-1);
                            map.getChildren().remove(Main.energy.get(Main.energy.size()-1));
                            Main.energy.remove(Main.energy.size()-1);
                            spaceShip.shoot(map, Main.FlyingProjectiles);
                        }));
                        spaceShip.coldownLaser.play();
                    }

                }
                break;
            case SPACE:
               pause();
                break;
            default:
                break;

        }

    }



    public void handleKeyRelease(KeyCode keyCode) {
        switch (keyCode) {
            case UP:
                upPressed = false;
                break;
            case DOWN:
                downPressed = false;
                break;
            case LEFT:
                leftPressed = false;
                break;
            case RIGHT:
                rightPressed = false;
                break;
            default:
                break;
        }
    }

    public void update() {
        if (! isPaused) {
            if (upPressed) {
                double newY = spaceShip.getShipImage().getY() - SPEED;
                if (newY >= 0) {
                    spaceShip.getShipImage().setY(newY);
                    if (spaceShip.getMoode().equals(Moode.LASER)) {
                        spaceShip.getLaser().getLaserImage().setY(newY - 1000);
                        spaceShip.getLaser().getStatLase().setLayoutY(newY);
                    }

                }
            }
            if (downPressed) {
                double newY = spaceShip.getShipImage().getY() + SPEED;
                if (newY <= 900 - spaceShip.getShipImage().getFitHeight()) {
                    spaceShip.getShipImage().setY(newY);
                    if (spaceShip.getMoode().equals(Moode.LASER)) {
                        spaceShip.getLaser().getLaserImage().setY(newY - 1000);
                        spaceShip.getLaser().getStatLase().setLayoutY(newY);
                    }
                }
            }
            if (leftPressed) {
                double newX = spaceShip.getShipImage().getX() - SPEED;
                if (newX >= 0) {
                    spaceShip.getShipImage().setX(newX);
                    if (spaceShip.getMoode().equals(Moode.LASER)) {
                        spaceShip.getLaser().getLaserImage().setX(newX + 32);
                        spaceShip.getLaser().getStatLase().setLayoutX(newX + 48);
                    }
                }
            }
            if (rightPressed) {
                double newX = spaceShip.getShipImage().getX() + SPEED;
                if (newX <= 475) {
                    spaceShip.getShipImage().setX(newX);
                    if (spaceShip.getMoode().equals(Moode.LASER)) {
                        spaceShip.getLaser().getLaserImage().setX(newX + 32);
                        spaceShip.getLaser().getStatLase().setLayoutX(newX + 48);
                    }
                }
            }
        }
    }
     void pause(){
        if (isPaused) {
            // Код для возобновления игры
            Main.enemy1LvlSpawn.play();
            Main.enemy2LvlSpawn.play();
            Main.additionalHeartTime.play();
            Main.additionalHeart.heartTime.play();
            if (spaceShip.getMoode().equals(Moode.LASER)) {
                spaceShip.coldownLaser.play();
            }
            //Projectile.timeTickEnemy.play();
            Projectile.timeTickPlayer.play();
            Main.backTime.play();
            for (Enemy1Lvl enemy1Lvl : Main.enemies) {
                enemy1Lvl.moveTime.play();
            }
            Main.pauseImage.setImage(new Image(Main.class.getResource("button-pause.png").toString()));
            spaceShip.coldown.play();
            isPaused = false; // Игра снова активна
            new Effects().showIcon("pause.png", map,160,350);
        } else {
            // Код для паузы игры
            Main.enemy1LvlSpawn.pause();
            Main.enemy2LvlSpawn.pause();
            Main.additionalHeartTime.pause();
            Main.additionalHeart.heartTime.pause();
            if (spaceShip.getMoode().equals(Moode.LASER)) {
                spaceShip.coldownLaser.pause();
            }
            Projectile.timeTickPlayer.pause();
            //Projectile.timeTickEnemy.pause();
            Main.backTime.pause();
            for (Enemy1Lvl enemy1Lvl : Main.enemies) {
                enemy1Lvl.moveTime.pause();
            }
            Main.pauseImage.setImage(new Image(Main.class.getResource("button-continue.png").toString()));
            spaceShip.coldown.pause();
            isPaused = true; // Игра на паузе
            new Effects().showIcon("continue.png", map,160,350);
        }
    }


}
