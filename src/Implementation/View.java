package Implementation;


import Interfaces.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



public class View  {



    private Zoom_Pane zoomPane;
    private Group container;


    public View(IModel model,Stage primaryStage) {

        primaryStage.setTitle("Airport Simulator");
        Group root = new Group();
        Scene scene = new Scene(root, 1600, 900);
        Group stat =new Group();
        Scene scene_stat= new Scene(stat,1600,900);
        this.container=new Group();
        this.zoomPane=new Zoom_Pane();
        SplitPane splitPane = new SplitPane();
        splitPane.prefWidthProperty().bind(scene.widthProperty());
        splitPane.prefHeightProperty().bind(scene.heightProperty());



        VBox leftArea = new VBox();

        BorderPane borderPane = new BorderPane();
        ScrollPane scrollPane=new ScrollPane();
        scrollPane.setStyle("-fx-background: LIGHTGREEN;");

        borderPane.setCenter(this.zoomPane);
        scrollPane.setContent(borderPane);
        scrollPane.setPannable(true);



        leftArea.getChildren().addAll(scrollPane);

        //   IModel model = new Model();
        var nodes = model.getNodes();
        var edges = model.getEdges();
        var flights= model.GetFlights();

        this.updateAirport(nodes, edges, flights);

        this.zoomPane.content.getChildren().addAll(container);


        SplitPane splitPane2 = new SplitPane();
        splitPane2.prefWidthProperty().bind(scene.widthProperty());
        splitPane2.prefHeightProperty().bind(scene.heightProperty());


        VBox rightArea = new VBox();

        Slider slider = new Slider();

        slider.setMin(0);
        slider.setMax(86400);
        slider.setValue(0);

        //  slider.setShowTickLabels(true);
        //  slider.setShowTickMarks(true);

        slider.setBlockIncrement(1);

        Label infoLabel = new Label("-");
        infoLabel.setTextFill(Color.BLUE);


        // Adding Listener to value property.
        slider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {

                infoLabel.setText(""+newValue.intValue());
            }
        });

        Label label = new Label("Time (sec.): ");



        rightArea.setPadding(new Insets(20));
        rightArea.setSpacing(5);
        rightArea.getChildren().addAll(label,infoLabel,slider);



        Button btn1 = new Button();
        btn1.setText("Stop");
        btn1.setTranslateX(20);
        btn1.setTranslateY(100);

        Button btn2 = new Button();
        btn2.setText("Statistics");
        btn2.setTranslateX(20);
        btn2.setTranslateY(200);


        Button btn3 = new Button();
        btn3.setText("Airport");


        rightArea.getChildren().addAll(btn1,btn2);

        btn2.setOnAction(e -> {
            primaryStage.setScene(scene_stat);

        });


        splitPane2.getItems().add(rightArea);
        splitPane.getItems().add(leftArea);
        splitPane.getItems().add(splitPane2);


        splitPane.setDividerPosition(0,0.75);

        HBox hbox = new HBox();
        hbox.getChildren().add(splitPane);
        root.getChildren().add(hbox);



        Text text_stat=new Text("Number of nodes: "+nodes.size()+"\n"+"Number of edges: "+edges.size());
        text_stat.setX(50);
        text_stat.setY(60);
        text_stat.setFont(Font.font(null, FontWeight.BOLD, 30));
        stat.getChildren().addAll(btn3,text_stat);
        btn3.setOnAction(e -> {
            primaryStage.setScene(scene);

        });


        primaryStage.setScene(scene);

    }



    public void updateAirport(List<INode> nodes, List<IEdge> edges, List<IFlight> flights){


        //    flights.forEach((f)->{  System.out.println(f.GetFlightLog());});


       /* Image image1 = new Image("Implementation/plane.png", 40,
                40, false, false,false);
        ImageView iv1 = new ImageView(image1);

        Image image2 = new Image("Implementation/plane.png", 20,
                20, false, false,false);
        ImageView iv2 = new ImageView(image2);


        iv1.setX(800);
        iv1.setY(800);

        iv2.setX(800);
        iv2.setY(850);
        this.container.getChildren().addAll(iv1,iv2);

*/
        edges.forEach((edge) -> {



            //Filter nodes to get in node
            List<INode> in_nodes = nodes.stream()
                    .filter(n -> Objects.equals(n.GetNumber(), Integer.parseInt(edge.GetInNodeNumber())))
                    .collect(Collectors.toList());
            List<INode> out_nodes = nodes.stream()
                    .filter(n -> Objects.equals(n.GetNumber(), Integer.parseInt(edge.GetOutNodeNumber())))
                    .collect(Collectors.toList());

            AirportEdge e = new AirportEdge(in_nodes.get(0), out_nodes.get(0));

            e.setStrokeWidth(5);
            e.setStroke(Color.CHOCOLATE);
            this.container.getChildren().add(e);


        });

        nodes.forEach((node) -> {
            AirportNode n = new AirportNode(node);
            this.container.getChildren().add(n);


            Text text_nr=new Text(""+n.getNr()+"");
            text_nr.setX(n.getXPos());
            text_nr.setY(n.getYPos()-10);
            text_nr.setFont(Font.font(null, FontWeight.BOLD, 10));
            this.container.getChildren().addAll(text_nr);



        });

        flights.forEach((flight) -> {
            AirportAircraft a = new AirportAircraft(flight);
            this.container.getChildren().add(a);
        });




    }



}
















