package fxwindows.core;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;

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

    private ReadOnlyBooleanWrapper unregisterRequested = new ReadOnlyBooleanWrapper();
    private boolean unregistered;

    public Updatable() {
        unregistered = true;
        register();
    }

    public ReadOnlyBooleanProperty unregisteredProperty() {
        return unregisterRequested.getReadOnlyProperty();
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
            if (u.unregisterRequested.get()) {
                it.remove();
                u.unregistered = true;
            }
        }
    }

    protected boolean isUnregistered() {
        return unregisterRequested.get();
    }

    protected void register() {
        if (unregistered) additionQueue.add(this);
        unregistered = false;
        unregisterRequested.set(false);
    }

    protected void unregister() {
        unregisterRequested.set(true);
    }

    public abstract void update(long currentTimeMillis);
}
