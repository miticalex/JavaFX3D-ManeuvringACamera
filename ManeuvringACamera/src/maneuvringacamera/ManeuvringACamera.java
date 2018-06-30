package maneuvringacamera;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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
    private static final double TRANSLATION_SPEED = 1.0;
    private static final double SCROLLING_SPEED = 1.0;
    
    Group root = new Group();
    
    PerspectiveCamera camera;
    Group holder;
    
    Cylinder cylinder;
    Box box;
    Sphere sphere;

    //camera transforms
    private Translate holderTranslate = new Translate();
    private Rotate holderRotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate holderRotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate holderRotateZ = new Rotate(0, Rotate.Z_AXIS);
    
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
        camera.setFarClip(100000.0);
        camera.setTranslateZ(INITIAL_CAMERA_DISTANCE);
        holder = new Group(camera);
        holder.getTransforms().addAll(holderTranslate, holderRotateZ, holderRotateY, holderRotateX);
        
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
        scene.addEventHandler(ScrollEvent.SCROLL, e -> onScroll(e));
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e-> onKeyPressed(e));
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
        
        double speedModificator = 1.0;
        if (mouseEvent.isControlDown())
            speedModificator *= CTRL_FACTOR;
        if (mouseEvent.isAltDown())
            speedModificator *= ALT_FACTOR;
        
        if (mouseEvent.isPrimaryButtonDown()) {
            holderRotateY.setAngle(holderRotateY.getAngle() - mouseMovedX*ROTATION_SPEED*speedModificator);
            holderRotateX.setAngle(holderRotateX.getAngle() + mouseMovedY*ROTATION_SPEED*speedModificator);
        }
        else if (mouseEvent.isSecondaryButtonDown()) {
            camera.setTranslateX(camera.getTranslateX() + mouseMovedX*TRANSLATION_SPEED*speedModificator);
            camera.setTranslateY(camera.getTranslateY() + mouseMovedY*TRANSLATION_SPEED*speedModificator);
        }
    }

    private void onScroll(ScrollEvent scrollEvent) {
        double speedModificator = 1.0;
        if (scrollEvent.isControlDown())
            speedModificator *= CTRL_FACTOR;
        if (scrollEvent.isAltDown())
            speedModificator *= ALT_FACTOR;
        
        camera.setTranslateZ(camera.getTranslateZ() + scrollEvent.getDeltaY()*SCROLLING_SPEED*speedModificator);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        double speedModificator = 1.0; 
        if (keyEvent.isControlDown())
            speedModificator *= CTRL_FACTOR;
        if (keyEvent.isAltDown())
            speedModificator *= ALT_FACTOR;
        
        switch (keyEvent.getCode()) {
            case PAGE_UP:
                holderRotateZ.setAngle(holderRotateZ.getAngle() + ROTATION_SPEED*speedModificator);
                break;
            case PAGE_DOWN:
                holderRotateZ.setAngle(holderRotateZ.getAngle() - ROTATION_SPEED*speedModificator);
                break;
            case LEFT:
                if (camera.getRotationAxis() != Rotate.Y_AXIS){
                    camera.setRotationAxis(Rotate.Y_AXIS);
                    camera.setRotate(0);
                }
                
                camera.setRotate(camera.getRotate() - ROTATION_SPEED*speedModificator);
                break;
            case RIGHT:
                if (camera.getRotationAxis() != Rotate.Y_AXIS){
                    camera.setRotationAxis(Rotate.Y_AXIS);
                    camera.setRotate(0);
                }
                
                camera.setRotate(camera.getRotate() + ROTATION_SPEED*speedModificator);
                break;
            case UP:
                if (camera.getRotationAxis() != Rotate.X_AXIS){
                    camera.setRotationAxis(Rotate.X_AXIS);
                    camera.setRotate(0);
                }
                
                camera.setRotate(camera.getRotate() + ROTATION_SPEED*speedModificator);
                break;
            case DOWN:
                if (camera.getRotationAxis() != Rotate.X_AXIS){
                    camera.setRotationAxis(Rotate.X_AXIS);
                    camera.setRotate(0);
                }
                
                camera.setRotate(camera.getRotate() - ROTATION_SPEED*speedModificator);
                break;
            case ADD: case PLUS:
                if (camera.getRotationAxis() != Rotate.Z_AXIS){
                    camera.setRotationAxis(Rotate.Z_AXIS);
                    camera.setRotate(0);
                }
                
                camera.setRotate(camera.getRotate() + ROTATION_SPEED*speedModificator);
                break;
            case SUBTRACT: case MINUS:
                if (camera.getRotationAxis() != Rotate.Z_AXIS){
                    camera.setRotationAxis(Rotate.Z_AXIS);
                    camera.setRotate(0);
                }
                
                camera.setRotate(camera.getRotate() - ROTATION_SPEED*speedModificator);
                break;
            case W:
                holderTranslate.setZ(holderTranslate.getZ() + TRANSLATION_SPEED*speedModificator);
                break;
            case S:
                holderTranslate.setZ(holderTranslate.getZ() - TRANSLATION_SPEED*speedModificator);
                break;
            case D:
                holderTranslate.setX(holderTranslate.getX() + TRANSLATION_SPEED*speedModificator);
                break;
            case A:
                holderTranslate.setX(holderTranslate.getX() - TRANSLATION_SPEED*speedModificator);
                break;
            case B:
                holderTranslate.setY(holderTranslate.getY() + TRANSLATION_SPEED*speedModificator);
                break;
            case Z:
                holderTranslate.setY(holderTranslate.getY() - TRANSLATION_SPEED*speedModificator);
                break;
            default:
                break;
        }
    }

}
