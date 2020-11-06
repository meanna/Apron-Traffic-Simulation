package behavior;

public class While implements BehaveNode {

    State state;
    NodeType type = NodeType.DECORATOR;
    BehaveNode child;


    int now;
    Agent agent;


    boolean successAtleastOnce;




    //constructor
    public While() {
        state = new State();
        state.setInvalid();




    }

    @Override
    public void init(int now, Agent agent) {

        this.now = now;
        this.agent = agent;
        this.successAtleastOnce = false;

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

        if (successAtleastOnce && s ==StateKind.FAILURE){
            state.setSuccess();
        }else if(s ==StateKind.FAILURE){
            state.setFailure();
        }else if(s ==StateKind.SUCCESS){
            this.successAtleastOnce = true;
            state.setRunning();
        }else if(s ==StateKind.RUNNING){
            state.setRunning();
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

