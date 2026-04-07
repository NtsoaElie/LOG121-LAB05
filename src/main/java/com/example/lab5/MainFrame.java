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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainFrame extends Application {

    @Override
    public void start(Stage primaryStage) {

        // --- 1. INSTANCIATION DU MODÈLE ---
        ImageModel imageModel = new ImageModel();
        Perspective perspective1 = new Perspective();
        Perspective perspective2 = new Perspective(); // L'énoncé demande une 2e et 3e vue [cite: 140]

        // --- 2. INSTANCIATION DES CONTRÔLEURS ---
        MouseController controleur1 = new MouseController(perspective1);
        MouseController controleur2 = new MouseController(perspective2);

        // --- 3. INSTANCIATION DE LA VUE ---
        ThumbnailView thumbnailView = new ThumbnailView(imageModel);
        PerspectiveView vuePerspective1 = new PerspectiveView(imageModel, perspective1, controleur1);
        PerspectiveView vuePerspective2 = new PerspectiveView(imageModel, perspective2, controleur2);

        // --- 4. CONNEXION DU PATRON OBSERVATEUR ---
        // Les vues s'abonnent aux modèles pour être notifiées des changements
        imageModel.addObserver(thumbnailView);
        imageModel.addObserver(vuePerspective1);
        imageModel.addObserver(vuePerspective2);

        perspective1.addObserver(vuePerspective1);
        perspective2.addObserver(vuePerspective2);

        // --- 5. CRÉATION DU MENU ---
        MenuBar menuBar = new MenuBar();
        Menu menuFichier = new Menu("Fichier");
        MenuItem itemOuvrir = new MenuItem("Ouvrir une image...");
        MenuItem itemSauvegarder = new MenuItem("Sauvegarder"); // Demandé par l'énoncé

        Menu menuEdition = new Menu("Édition");
        MenuItem itemDefaire = new MenuItem("Défaire (Undo)");

        menuFichier.getItems().addAll(itemOuvrir, itemSauvegarder);
        menuEdition.getItems().add(itemDefaire);
        menuBar.getMenus().addAll(menuFichier, menuEdition);

        // --- 6. ACTIONS DES MENUS (Le Contrôleur de Menu) ---
        itemOuvrir.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                imageModel.setImagePath(file.getAbsolutePath()); // Modifie le modèle, ce qui notifie les vues !
                perspective1.reset();
                perspective2.reset();
            }
        });

        itemSauvegarder.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier de sauvegarde", "*.ser"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                SaveCommand saveCmd = new SaveCommand(imageModel, perspective1, perspective2, file);
                Invoker.getInstance().executeCommand(saveCmd);
            }
        });

        itemDefaire.setOnAction(e -> {
            Invoker.getInstance().undo(); // Fait appel au Singleton pour défaire la dernière action
        });

        // --- 7. ASSEMBLAGE DE L'INTERFACE (LAYOUT) ---
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setLeft(thumbnailView);

        // On met les deux perspectives au centre, côte à côte
        HBox centreBox = new HBox(10, vuePerspective1, vuePerspective2);
        root.setCenter(centreBox);

        // --- 8. AFFICHAGE DE LA FENÊTRE ---
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Laboratoire 5 - Éditeur d'images MVC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
