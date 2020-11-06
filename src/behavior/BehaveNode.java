package behavior;


public interface BehaveNode {

    public void init(int now, Agent agent);

    public void tick(int now);


    public State getState();
    public StateKind getStateKind();

    public void printTree();


}
