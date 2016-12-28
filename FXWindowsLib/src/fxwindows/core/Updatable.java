package fxwindows.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public abstract class Updatable {

    private static final List<Updatable> additionQueue = new ArrayList<>();
    private static final List<Updatable> updatables = new ArrayList<>();

    private boolean unregisterRequested;
    private boolean unregistered;

    public Updatable() {
        unregisterRequested = false;
        unregistered = true;
        register();
    }

    public static int getAmountUpdatables() {
        return updatables.size();
    }

    public static void updateAll(long currentTimeMillis) {
        ListIterator<Updatable> adder = additionQueue.listIterator();
        while (adder.hasNext()) {
            Updatable u = adder.next();
            updatables.add(u);
            adder.remove();
        }
        ListIterator<Updatable> it = updatables.listIterator();
        while (it.hasNext()) {
            Updatable u = it.next();
            u.update(currentTimeMillis);
            if (u.unregisterRequested) {
                it.remove();
                u.unregistered = true;
            }
        }
    }

    protected boolean isUnregistered() {
        return unregisterRequested || unregistered;
    }

    protected void register() {
        if (unregistered) additionQueue.add(this);
        unregistered = false;
        unregisterRequested = false;
    }

    protected void unregister() {
        unregisterRequested = true;
    }

    public abstract void update(long currentTimeMillis);
}
