package Implementation;

import Interfaces.INode;

import java.util.List;

public class Node implements INode {
  private int number;
  private Coordinates position;
  private List<String> namesList;
  private List<String> restrictionsList;

  double x;
  double y;


  public Node(int number, double XWGS, double YWGS) {
    this.number = number;
    position = Coordinates.WGS(XWGS, YWGS);
  }

  public Node(int number, double XWGS, double YWGS, List<String> namesList, List<String> restrictionsList) {
    this.number = number;
    position = Coordinates.WGS(XWGS, YWGS);
    this.namesList = namesList;
    this.restrictionsList = restrictionsList;
  }


  /*----------------------------
    --------- Get & Set-----------
     -----------------------------*/

  @Override
  public int GetNumber() {
    return number;
  }
  @Override
  public Coordinates getPosition()
  {
    return position;
  }
  @Override
  public List<String> GetNames() {
    return namesList;
  }
  @Override
  public List<String> GetRestrictions() {
    return restrictionsList;
  }

  public void setNamesList(List<String> namesList) {
    this.namesList = namesList;
  }
  public void setRestrictionsList(List<String> restrictionsList) {
    this.restrictionsList = restrictionsList;
  }


  public Node(){

  }

  public Node(double x, double y){
    this.x = x;
    this.y = y;
  }

  public double getX(){
    return x;
  }

  public double getY(){
    return y;
  }
  public void setLocation(double x, double y){
    this.x = x;
    this.y = y;
  }

}
