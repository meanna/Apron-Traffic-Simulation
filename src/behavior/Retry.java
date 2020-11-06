package behavior;

public class Retry implements BehaveNode {

    State state;
    NodeType type = NodeType.DECORATOR;
    BehaveNode child;


    int now;
    Agent agent;


    //constructor
    public Retry() {
        state = new State();
        state.setInvalid();




    }

    @Override
    public void init(int now, Agent agent) {

        this.now = now;
        this.agent = agent;

        state.setInitiated();

        child.init(now, agent);

        if (child.getStateKind() == StateKind.INVALID) {
            state.setInvalid();
        }

    }

    @Override
    public void tick(int now) {
        StateKind s = child.getStateKind();
        if (s == StateKind.FAILURE) {
            child.init(now, agent);
        }

        child.tick(now);
        s = child.getStateKind();

        switch (s) {
            case RUNNING:
                state.setRunning();
                break;

            case FAILURE:
                state.setRunning();
                break;
            case SUCCESS:
                state.setSuccess();
        }

        System.out.println(toString() + "          " + this.getStateKind());


    }

    public void setChild(BehaveNode child) {
        this.child = child;

    }


    @Override

    public void printTree() {
        System.out.print(this.getClass().getSimpleName());

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


