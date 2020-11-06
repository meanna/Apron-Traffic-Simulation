package Implementation;

import Interfaces.IAircraft;
import Interfaces.IFlight;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class AirportAircraft extends ImageView {


    //private Image plane = new Image("Implementation/planeIcon.png", 40, 40, false, false,false);

    AirportAircraft(IFlight flight) {

        super (new Image("Implementation/planeIcon.png", 40, 40, false, false,false));

        IAircraft aircraft = flight.GetAircraft();

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(100);
        colorAdjust.setHue(100);

        //set velocity based brightness
        //hier ist nur die frage in welcher range velocity ist...
        colorAdjust.setBrightness(aircraft.GetVelocity());
        colorAdjust.setSaturation(100);

        this.setEffect(colorAdjust);

        this.setX(aircraft.GetPosition().getXPos()+50);
        this.setY(aircraft.GetPosition().getYPos()*(-1)+50);
        this.setRotate(45);
    }
}