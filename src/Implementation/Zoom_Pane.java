package Implementation;


import javafx.scene.Group;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

public class Zoom_Pane extends AnchorPane {

    final double SCALE_DELTA = 1.01;

    public Group content = new Group();

    public Zoom_Pane() {
        super();
        getChildren().add(content);
        content.setAutoSizeChildren(true);
        setOnScroll((ScrollEvent event) -> {
            event.consume();
            if (event.getDeltaY() == 0) {
                return;
            }

            double scaleFactor
                    = (event.getDeltaY() > 0)
                    ? SCALE_DELTA
                    : 1 / SCALE_DELTA;

            content.setScaleX(content.getScaleX() * scaleFactor);
            content.setScaleY(content.getScaleY() * scaleFactor);
        });
    }
}
