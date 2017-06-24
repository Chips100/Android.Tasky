package com.tasky.android;

/**
 * Provides methods for common options in Activities.
 */
public class ActivityTools {
    /**
     * Delays the specified action to run later.
     * @param runnable Runnable that defines the action to be run later.
     * @param milliseconds Time in milliseconds for which the action should be delayed.
     */
    public static void delay(Runnable runnable, long milliseconds) {
        new android.os.Handler().postDelayed(runnable, milliseconds);
    }
}