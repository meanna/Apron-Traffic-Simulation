package Implementation;

import Interfaces.ICrashListener;
import Interfaces.IModel;
import Interfaces.IRequiresModelUpdate;

/**
 * The model supplies it with the needed Data every tick
 *
 * Gets data in case of a crash from CrashOccurred
 */

public class Statistics implements IRequiresModelUpdate, ICrashListener
{
    //Fields: Lists and Counter of statistically relevant Data


    @Override
    public void Update(IModel model)
    {
        //Add info to all Lists & Counters
    }

    @Override
    public void CrashOccurred(CrashData data)
    {

    }

    public Statistics()
    {

    }
}
