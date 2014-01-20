package com.flowpowered.engine.util.thread;

public interface CopySnapshotManager extends AsyncManager {
    /**
     * This method is called in order to update the snapshot at the end of each tick
     */
    void copySnapshotRun(int sequence);
}