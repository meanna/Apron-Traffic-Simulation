package Implementation;


import Interfaces.INode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

class AirportEdge extends Line {

    //TODO display direction
    AirportEdge(INode in, INode out){
        super(in.getPosition().getXPos()+50,in.getPosition().getYPos()*(-1)+50,out.getPosition().getXPos()+50,out.getPosition().getYPos()*(-1)+50);
    }
}
