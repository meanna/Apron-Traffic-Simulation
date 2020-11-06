package behavior;

public class Success implements BehaveNode {

    // action node has no child

    State state;
    NodeType type = NodeType.ACTION;

    int now;
    Agent agent;
    double param;

    //constructor
    public Success(double param) {
        this.param = param;
        state = new State();
        state.setInvalid();

    }

    @Override
    public void init(int now, Agent agent) {
        this.now = now;
        this.agent = agent;
        state.setInitiated();


    }

    @Override
    public void tick(int now) {

        state.setSuccess();

        System.out.println(toString() + "          " + this.getStateKind());

    }

    public void printTree(){
        System.out.print(this.getClass().getSimpleName());


    }

    @Override
    public State getState() {
        return state;
    }
    @Override
    public StateKind getStateKind(){
        return state.getKind();
    }





}

