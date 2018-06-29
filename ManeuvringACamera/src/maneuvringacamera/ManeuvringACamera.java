package maneuvringacamera;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

/**
 *
 * @author AM
 */
public class ManeuvringACamera extends Application {
    private static final double HEIGHT = 600;
    private static final double WIDTH = 600;
    
    private static final double INITIAL_CAMERA_DISTANCE = -1000;
    
    Group root = new Group();
    
    PerspectiveCamera camera;
    Group holder;
    
    Cylinder cylinder;
    Box box;
    Sphere sphere;
    
    @Override
    public void start(Stage window) {
        cylinder = makeCylinder(70, 140, Color.CYAN);
        box = makeBox(140, 140, 50, Color.BLUEVIOLET);
        box.setTranslateX(-150);
        sphere = makeSphere(70, Color.DARKBLUE);
        sphere.setTranslateX(150);
        
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(5000.0);
        camera.setTranslateZ(INITIAL_CAMERA_DISTANCE);
        holder = new Group(camera);
        
        root.getChildren().addAll(cylinder, box, sphere, holder);
        Scene scene = new Scene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setCamera(camera);
        window.setTitle("Maneuvring A Camera");
        window.setScene(scene);
        window.show();
    }
    
    private Cylinder makeCylinder(double r, double h, Color color){
        Cylinder cylinder = new Cylinder(r,h);
        cylinder.setMaterial(new PhongMaterial(color));
        
        return cylinder;
    }
    
    private Box makeBox(double x, double y, double z, Color color){
        Box box = new Box(140,140,50);
        box.setMaterial(new PhongMaterial(color));
        
        return box;
    }
    
    private Sphere makeSphere(double r, Color color){
        Sphere sphere = new Sphere(70);
        sphere.setMaterial(new PhongMaterial(color));
        
        return sphere;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
