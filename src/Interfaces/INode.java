package Interfaces;

import Implementation.Coordinates;
import Implementation.Node;

import java.util.List;

public interface INode
{
    int GetNumber();
    Coordinates getPosition();
    List<String> GetNames();
    List<String> GetRestrictions();

    //Factory
    static INode CreateInstance(int number, double XWGS, double YWGS, List<String> namesList, List<String> restrictionsList)
    {
        return new Node(number, XWGS, YWGS, namesList, restrictionsList);
    }
}
