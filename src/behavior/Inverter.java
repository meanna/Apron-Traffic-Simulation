package behavior;

public class Inverter implements BehaveNode {

    State state;
    NodeType type = NodeType.DECORATOR;
    BehaveNode child;


    int now;
    Agent agent;


    double param;



    //constructor
    public Inverter(double param) {
        state = new State();
        state.setInvalid();

        this.param = param;


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

        child.tick(now);
        StateKind s = child.getStateKind();

        if(s ==StateKind.SUCCESS){
            state.setKind(StateKind.FAILURE);

        }else {
            if (s == StateKind.FAILURE) {
                state.setKind(StateKind.SUCCESS);

            } else {
                state.setKind(s);

            }


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
