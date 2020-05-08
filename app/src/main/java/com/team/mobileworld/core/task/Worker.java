package com.team.mobileworld.core.task;

import androidx.annotation.WorkerThread;

public interface Worker {

    /**
     * Xử lý công việc dựa trên luồng worker
     */
    @WorkerThread
    public void hanlde();

}
