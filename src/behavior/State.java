package behavior;


public class State {

   private  StateKind kind;



    public StateKind getKind() {
        return kind;
    }

    public void setKind(StateKind newKind) {
        this.kind = newKind;
    }

    public void setSuccess() {
        this.setKind(StateKind.SUCCESS);

    }

    public void setFailure() {
        this.setKind(StateKind.FAILURE);

    }

    public void setRunning() {
        this.setKind(StateKind.RUNNING);
    }

    public void setInvalid() {
        this.setKind(StateKind.INVALID);
    }

    public void setInitiated() {
        this.setKind(StateKind.INITIATED);
    }

    // use to compare the current state with some other state
    public boolean isEqualTo(StateKind otherKind){
        if(this.kind == otherKind){
            return true;
        }else{
            return false;
        }
    }


}
