package behavior;

public class Success_then_Fail implements BehaveNode {

    // action node has no child

    State state;
    NodeType type = NodeType.ACTION;

    int now;
    Agent agent;
    double param;
    int failAfter;

    //constructor
    public Success_then_Fail(double param) {
        this.param = param;
        state = new State();
        state.setInvalid();

    }

    @Override
    public void init(int now, Agent agent) {
        this.now = now;
        this.agent = agent;
        state.setInitiated();
        failAfter = (int)param;

    }

    @Override
    public void tick(int now) {

        if (failAfter <=0) {
            state.setFailure();

        } else {


            state.setSuccess();



        }
        failAfter-=1;

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
