package Interfaces;

import Implementation.CrashData;

/**
 * Interface for each class that deals with Crashes in some form
 * Observer
 * Example: View and Statistics schould implement this
 */
public interface ICrashListener
{
    /**
     * Unique implementation in each implementing Class
     * What happens upon a crash:
     * EX: View Show a new window with crash message
     * @param data Enum; The Crash Data containing all the necessary
     *            information to determine what happens upon what crash
     */
    void CrashOccurred(CrashData data);
}
