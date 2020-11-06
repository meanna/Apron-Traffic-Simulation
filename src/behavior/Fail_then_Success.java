package behavior;

public class Fail_then_Success implements BehaveNode {

    // action node has no child

    State state;
    NodeType type = NodeType.ACTION;

    int now;
    Agent agent;
    double param;
    int failAfter;

    //constructor
    public Fail_then_Success(double param) {
        this.param = param;
        state = new State();
        state.setInvalid();
        failAfter = (int)param;

    }

    @Override
    public void init(int now, Agent agent) {
        this.now = now;
        this.agent = agent;
        state.setInitiated();


    }

    @Override
    public void tick(int now) {
        System.out.println(failAfter);

        if (failAfter <=0) {
           state.setSuccess();

        } else {

            state.setFailure();

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
