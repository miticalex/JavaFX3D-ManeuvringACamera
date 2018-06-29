/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maneuvringacamera;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author AM
 */
public class ManeuvringACamera extends Application {
    private static final double HEIGHT = 600;
    private static final double WIDTH = 600;
    
    Group root = new Group();
    
    @Override
    public void start(Stage window) {
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        
        window.setTitle("Maneuvring A Camera");
        window.setScene(scene);
        window.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
