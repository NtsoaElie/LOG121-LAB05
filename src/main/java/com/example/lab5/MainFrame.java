package com.example.lab5;

import com.example.lab5.controller.Invoker;
import com.example.lab5.controller.MouseController;
import com.example.lab5.controller.SaveCommand;
import com.example.lab5.model.ImageModel;
import com.example.lab5.model.Perspective;
import com.example.lab5.view.PerspectiveView;
import com.example.lab5.view.ThumbnailView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class MainFrame extends Application {

    @Override
    public void start(Stage primaryStage) {
        ImageModel imageModel = new ImageModel();
        Perspective perspective1 = new Perspective();
        Perspective perspective2 = new Perspective();

        MouseController controleur1 = new MouseController(perspective1);
        MouseController controleur2 = new MouseController(perspective2);

        ThumbnailView thumbnailView = new ThumbnailView(imageModel);
        PerspectiveView vuePerspective1 = new PerspectiveView(imageModel, perspective1, controleur1);
        PerspectiveView vuePerspective2 = new PerspectiveView(imageModel, perspective2, controleur2);

        imageModel.addObserver(thumbnailView);
        imageModel.addObserver(vuePerspective1);
        imageModel.addObserver(vuePerspective2);

        perspective1.addObserver(vuePerspective1);
        perspective2.addObserver(vuePerspective2);

        MenuBar menuBar = new MenuBar();
        Menu menuFichier = new Menu("Fichier");
        MenuItem itemOuvrir = new MenuItem("Ouvrir une image...");
        MenuItem itemSauvegarder = new MenuItem("Sauvegarder");
        MenuItem itemLoader = new MenuItem("Charger une sauvegarde");

        Menu menuEdition = new Menu("Edition");
        MenuItem itemDefaire = new MenuItem("Defaire (Undo)");

        menuFichier.getItems().addAll(itemOuvrir, itemLoader, itemSauvegarder);
        menuEdition.getItems().add(itemDefaire);
        menuBar.getMenus().addAll(menuFichier, menuEdition);

        itemOuvrir.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                imageModel.setImagePath(file.getAbsolutePath());
                perspective1.reset();
                perspective2.reset();
            }
        });

        itemSauvegarder.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier de sauvegarde", "*.ser"));
            fileChooser.setInitialFileName("sauvegarde");

            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                File normalizedFile = ensureSerExtension(file);
                SaveCommand saveCmd = new SaveCommand(imageModel, perspective1, perspective2, normalizedFile);
                Invoker.getInstance().executeCommand(saveCmd);
            }
        });

        itemDefaire.setOnAction(e -> Invoker.getInstance().undo());

        itemLoader.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier de sauvegarde", "*.ser"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    ImageModel loadedModel = (ImageModel) ois.readObject();
                    Perspective loadedP1 = (Perspective) ois.readObject();
                    Perspective loadedP2 = (Perspective) ois.readObject();

                    imageModel.setImagePath(loadedModel.getImagePath());

                    perspective1.setZoom(loadedP1.getZoom());
                    perspective1.setTransX(loadedP1.getTransX());
                    perspective1.setTransY(loadedP1.getTransY());
                    perspective1.notifyObservers();

                    perspective2.setZoom(loadedP2.getZoom());
                    perspective2.setTransX(loadedP2.getTransX());
                    perspective2.setTransY(loadedP2.getTransY());
                    perspective2.notifyObservers();

                } catch (Exception ex) {
                    showError("Chargement impossible",
                            "Le fichier de sauvegarde est invalide ou incompatible.\n\n" +
                                    "Details: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
                }
            }
        });

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setLeft(thumbnailView);

        HBox centreBox = new HBox(10, vuePerspective1, vuePerspective2);
        root.setCenter(centreBox);

        Scene scene = new Scene(root, 1000, 600);
        scene.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.Z) {
                Invoker.getInstance().undo();
            }
        });
        primaryStage.setTitle("Laboratoire 5 - Editeur d'images MVC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private File ensureSerExtension(File file) {
        if (file.getName().toLowerCase().endsWith(".ser")) {
            return file;
        }
        return new File(file.getParentFile(), file.getName() + ".ser");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
