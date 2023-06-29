package com.example.demo14;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Effects {
    public static DropShadow DropShadow(Color color){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(5.0);dropShadow.setHeight(5.0);dropShadow.setRadius(2.0);
        dropShadow.setOffsetX(0.0);dropShadow.setOffsetY(0.0);
        dropShadow.setSpread(1);
        dropShadow.setColor(color);
        return dropShadow;
    }
    public static DropShadow DropShadow(Color color, double radius, double width, double height,double spread){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(width);dropShadow.setHeight(height);dropShadow.setRadius(radius);
        dropShadow.setOffsetX(0.0);dropShadow.setOffsetY(0.0);
        dropShadow.setSpread(spread);
        dropShadow.setColor(color);
        return dropShadow;
    }
    public static FillTransition missEnemy(Pane map){
        Rectangle bottomArea = new Rectangle(564, 550);
        bottomArea.setY(875);
        map.getChildren().add(bottomArea);

        FillTransition fillTransition = new FillTransition(Duration.seconds(0.5), bottomArea);
        fillTransition.setFromValue(Color.RED);
        fillTransition.setToValue(Color.TRANSPARENT);
        fillTransition.setCycleCount(1); // Количество повторений мигания
        fillTransition.setAutoReverse(false);
        return fillTransition;
    }

    public void destroyEffect(ImageView object, Pane map){
        ImageView effect = new ImageView(new Image(getClass().getResource("EffectDestroy.png").toString()));
        effect.setFitWidth(50);effect.setFitHeight(50);
        effect.setX(object.getLayoutX());effect.setY(object.getLayoutY());


        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), effect);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(1); // Бесконечное мигание
        fadeTransition.setAutoReverse(false); // Мигание вперед-назад

        // Установка режима смешивания, чтобы изображение было прозрачным
        effect.setBlendMode(BlendMode.SRC_OVER);// Мигание вперед-назад

        map.getChildren().add(effect);
        fadeTransition.play();
    }
    public void showIcon(String path, Pane map,int x,int y){
        ImageView effect = new ImageView(new Image(getClass().getResource(path).toString()));
        effect.setOpacity(0.3);

        effect.setX(x);effect.setY(y);


        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), effect);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(1); // Бесконечное мигание
        fadeTransition.setAutoReverse(false); // Мигание вперед-назад

        // Установка режима смешивания, чтобы изображение было прозрачным
        effect.setBlendMode(BlendMode.SRC_OVER);// Мигание вперед-назад

        map.getChildren().add(effect);
        fadeTransition.play();
    }
    public void shakeScreen(Pane map,int shakePower,int count) {
        TranslateTransition shakeTransition = new TranslateTransition(Duration.millis(100), map);
        shakeTransition.setFromX(shakePower);
        shakeTransition.setToX(0);
        shakeTransition.setCycleCount(count);
        shakeTransition.setAutoReverse(true);

        // Обработчик события окончания анимации
        shakeTransition.setOnFinished(event -> {
            map.setTranslateX(0); // Возвращаем панель в исходное положение по оси X
        });

        shakeTransition.play();
    }
}
