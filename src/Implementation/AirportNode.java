package Implementation;


import Interfaces.INode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.List;

class AirportNode extends Circle {

    private List<String> name;
    private int nr;
    private double XPos;
    private double YPos;


    AirportNode(INode node) {

        super(node.getPosition().getXPos()+50, node.getPosition().getYPos()*(-1)+50, 10, Color.CHOCOLATE);
        this.name = node.GetNames();
        this.nr=node.GetNumber();
        this.XPos=node.getPosition().getXPos()+50;
        this.YPos=node.getPosition().getYPos()*(-1)+50;



    }

    public int getNr(){return nr;}

    public double getXPos() {return XPos;}
    public double getYPos() {return YPos;}

}
