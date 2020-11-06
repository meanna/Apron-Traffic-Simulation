package behavior;



import java.util.ArrayList;

public class Sequence implements BehaveNode {


    State state;
    NodeType type = NodeType.COMBINATOR;
    ArrayList<BehaveNode> childs;

    int now;
    Agent agent;

    BehaveNode runningChild;
    int i;

    public Sequence() {
        state = new State();
        state.setInvalid();

        childs = new ArrayList<BehaveNode>();


    }




    @Override
    public void init(int now, Agent agent) {
        this.now = now;
        this.agent = agent;

        runningChild = null;
        i = 0;

        for (BehaveNode child : childs) {
            child.init(now, agent);

        }

        state.setInitiated();
    }


    @Override
    public void tick(int now) {
        StateKind currentState = getStateKind();

        if(currentState == StateKind.RUNNING){
           i = childs.indexOf(runningChild);
        }
        while(i < childs.size()){
            childs.get(i).tick(now);
            StateKind s = childs.get(i).getStateKind();
            if(s == StateKind.RUNNING){
                runningChild =   childs.get(i);
                state.setRunning();
                break;
            }else if(s == StateKind.FAILURE){
                state.setFailure();
                break;
            }else if(s == StateKind.SUCCESS && i == childs.size()-1){
                state.setSuccess();
                break;
            }else{
                i++;
            }

        }


        System.out.println(toString() + "          " + this.getStateKind());

    }


    public void addChild(BehaveNode child) {

        this.childs.add(child);

    }


    public void printTree(){
        System.out.print(this.getClass().getSimpleName());

        System.out.print("[");
        for(int i = 0; i< childs.size() ; i++){

            childs.get(i).printTree();
            if(i < childs.size()-1){
                System.out.print(" , ");

            }


        }
        System.out.print("]");

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


