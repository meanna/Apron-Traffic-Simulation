package behavior;


public class Loop implements BehaveNode {

    State state;
    NodeType type = NodeType.DECORATOR;
    BehaveNode child;


    int now;
    Agent agent;


    double param;
    int count;



    //constructor
    public Loop(double param) {
        state = new State();
        state.setInvalid();

        this.param = param;


    }

    @Override
    public void init(int now, Agent agent) {
        this.now = now;
        this.agent = agent;
        this.count = (int) param - 1;

        state.setInitiated();

        child.init(now, agent);

        if (child.getStateKind() == StateKind.INVALID) {
            state.setInvalid();
        }

    }

    @Override
    public void tick(int now) {

        child.tick(now);
        StateKind s = child.getStateKind();

        if (s == StateKind.RUNNING) {
            state.setRunning();
        } else if (count <= 0) {
            if (s == StateKind.SUCCESS) {
                state.setSuccess();
            } else if (s == StateKind.FAILURE) {
                state.setFailure();
            }

        } else {
            count--;
            state.setRunning();
        }

        System.out.println(toString() + "          " + this.getStateKind());


    }

    public void setChild(BehaveNode child) {
        this.child = child;

    }


    @Override

    public void printTree() {
        System.out.print(this.getClass().getSimpleName()+(int)param);

        System.out.print("[");
        child.printTree();

        System.out.print("]");

    }



    @Override
    public State getState() {
        return state;
    }

    @Override
    public StateKind getStateKind() {
        return this.state.getKind();
    }



}
