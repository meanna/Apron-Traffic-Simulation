package Implementation;

import Interfaces.Helper;
import Interfaces.IModel;
import Interfaces.IView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {


        //Create model, view and controller



        IModel model = new Model();
        View view = new View(model, primaryStage);

          Timeline timeline = new Timeline(60, new KeyFrame(Duration.millis(1000), (x) -> model.UpdateTick()));

          timeline.setCycleCount(Animation.INDEFINITE);

         timeline.play();

        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
