package maneuvringacamera;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 *
 * @author AM
 */
public class ManeuvringACamera extends Application {
    private static final double HEIGHT = 600;
    private static final double WIDTH = 600;
    
    private static final double INITIAL_CAMERA_DISTANCE = -1000;
    
    private static final double CTRL_FACTOR = 0.1;
    private static final double ALT_FACTOR = 10.0;
    private static final double ROTATION_SPEED = 1.0;
    
    Group root = new Group();
    
    PerspectiveCamera camera;
    Group holder;
    
    Cylinder cylinder;
    Box box;
    Sphere sphere;

    //camera transforms
    private Rotate rX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate rZ = new Rotate(0, Rotate.Z_AXIS);
    
    private double mousePositionX, mousePositionY;
    private double oldMousePositionX, oldMousePositionY;
    private double mouseMovedX, mouseMovedY, stepZ;
    
    @Override
    public void start(Stage window) {
        cylinder = makeCylinder(70, 140, Color.CYAN);
        box = makeBox(140, 140, 50, Color.BLUEVIOLET);
        box.setTranslateX(-150);
        sphere = makeSphere(70, Color.DARKBLUE);
        sphere.setTranslateX(150);
        
        PointLight pointLight = new PointLight();
        pointLight.setTranslateX(200);
        pointLight.setTranslateY(-100);
        pointLight.setTranslateZ(-200);
        
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(5000.0);
        camera.setTranslateZ(INITIAL_CAMERA_DISTANCE);
        holder = new Group(camera);
        holder.getTransforms().addAll(rZ, rY, rX);
        
        root.getChildren().addAll(cylinder, box, sphere, pointLight, holder);
        Scene scene = new Scene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setCamera(camera);
        handleEvents(scene, camera);
        
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
    
    private void handleEvents(Scene scene, PerspectiveCamera camera) {
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> onMousePressed(e));//fetching initial mouse coordinates before the DRAG started
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> onMouseDragged(e));
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    //fetching initial mouse coordinates before the drag started
    private void onMousePressed(MouseEvent mouseEvent) {
        oldMousePositionX = mousePositionX = mouseEvent.getSceneX();
        oldMousePositionY = mousePositionY = mouseEvent.getSceneY();
    }
    
    private void onMouseDragged(MouseEvent mouseEvent) {
        oldMousePositionX = mousePositionX;
        oldMousePositionY = mousePositionY;
        mousePositionX = mouseEvent.getSceneX();
        mousePositionY = mouseEvent.getSceneY();
        
        mouseMovedX = (mousePositionX - oldMousePositionX);
        mouseMovedY = (mousePositionY - oldMousePositionY);
        
        double rotationSpeed = ROTATION_SPEED;
        if (mouseEvent.isControlDown())
            rotationSpeed *= CTRL_FACTOR;
        if (mouseEvent.isAltDown())
            rotationSpeed *= ALT_FACTOR;
        
        if (mouseEvent.isPrimaryButtonDown()) {
            rY.setAngle(rY.getAngle() - mouseMovedX*rotationSpeed);
            rX.setAngle(rX.getAngle() + mouseMovedY*rotationSpeed);
        }
    }

}
