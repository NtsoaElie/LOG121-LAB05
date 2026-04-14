package com.example.lab5.view;

import com.example.lab5.model.ImageModel;
import com.example.lab5.model.Observer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;

public class ThumbnailView extends Pane implements Observer {

    private ImageModel imageModel;
    private ImageView imageView;

    public ThumbnailView(ImageModel imageModel) {
        this.imageModel = imageModel;
        this.imageView = new ImageView();

        this.imageView.setFitWidth(200);
        this.imageView.setPreserveRatio(true);

        this.getChildren().add(imageView);
    }

    @Override
    public void update() {
        if (imageModel.getImagePath() != null) {
            Image img = new Image(new File(imageModel.getImagePath()).toURI().toString());
            imageView.setImage(img);
        }
    }
}
