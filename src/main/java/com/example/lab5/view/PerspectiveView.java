package com.example.lab5.view;

import com.example.lab5.controller.MouseController;
import com.example.lab5.model.ImageModel;
import com.example.lab5.model.Observer;
import com.example.lab5.model.Perspective;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;

public class PerspectiveView extends Pane implements Observer {

    private ImageModel imageModel;
    private Perspective perspective;
    private MouseController mouseController;
    private ImageView imageView;

    private double lastMouseX;
    private double lastMouseY;

    public PerspectiveView(ImageModel imageModel, Perspective perspective, MouseController mouseController) {
        this.imageModel = imageModel;
        this.perspective = perspective;
        this.mouseController = mouseController;

        this.imageView = new ImageView();
        this.getChildren().add(imageView);

        this.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        this.setPrefSize(400, 500);
        this.setClip(new javafx.scene.shape.Rectangle(400, 500));

        initialiserEvenementsSouris();
    }

    private void initialiserEvenementsSouris() {
        this.setOnMousePressed(e -> {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        });

        this.setOnMouseDragged(e -> {
            double dx = e.getX() - lastMouseX;
            double dy = e.getY() - lastMouseY;
            mouseController.gererTranslation(dx, dy);

            lastMouseX = e.getX();
            lastMouseY = e.getY();
        });

        this.setOnScroll(e -> {
            double facteur = e.getDeltaY() > 0 ? 1.1 : 0.9;
            mouseController.gererZoom(facteur);
        });
    }

    @Override
    public void update() {
        if (imageModel.getImagePath() != null) {
            Image img = new Image(new File(imageModel.getImagePath()).toURI().toString());
            imageView.setImage(img);
        }

        imageView.setTranslateX(perspective.getTransX());
        imageView.setTranslateY(perspective.getTransY());
        imageView.setScaleX(perspective.getZoom());
        imageView.setScaleY(perspective.getZoom());
    }
}
