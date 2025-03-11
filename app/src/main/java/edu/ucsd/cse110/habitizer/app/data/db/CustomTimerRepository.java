package edu.ucsd.cse110.habitizer.app.data.db;

import android.util.Log;

import edu.ucsd.cse110.habitizer.lib.domain.CustomTimer;

public class CustomTimerRepository {
    private final CustomTimerDao customTimerDao;
    
    private final String TAG = "CustomTimerRepository";
    
    public CustomTimerRepository(CustomTimerDao customTimerDao) {
        this.customTimerDao = customTimerDao;
    }
    
    public CustomTimer getTimer() {
        CustomTimerEntity timerEntity = customTimerDao.findTimer();
        return timerEntity.toCustomTimer();
    }
    
    public void updateTimer() {
//        if (!customTimerDao.getOngoing()) return;
        
        long cumulativeTime1 = customTimerDao.getCumulativeTime();
        Log.d(TAG, "Cumulative time Before: " + cumulativeTime1);
        long l = System.currentTimeMillis();
        Log.d(TAG, "Current time: " + l);
        long startTime1 = customTimerDao.getStartTime();
        Log.d(TAG, "Start time Before: " + startTime1);
        
        long cumulativeTime = cumulativeTime1 + l - startTime1;
        Log.d(TAG, "Cumulative time: " + cumulativeTime);
        
        long startTime = System.currentTimeMillis();
        long taskTime = customTimerDao.getTaskTime() + System.currentTimeMillis() - customTimerDao.getTaskStartTime();
        long taskStartTime = System.currentTimeMillis();
        customTimerDao.updateTimer(cumulativeTime, startTime, taskTime, taskStartTime);
    }
    
    public void startTimer() {
        if (customTimerDao.getOngoing()) return;
        
        customTimerDao.setOngoing(true);
        customTimerDao.setStartTime(System.currentTimeMillis());
        Log.d(TAG, "Start time: " + customTimerDao.getStartTime());
        customTimerDao.setTaskStartTime(System.currentTimeMillis());
        Log.d(TAG, "Task start time: " + customTimerDao.getTaskStartTime());
    }
    
    public long pauseTimer() {
        if (!customTimerDao.getOngoing()) return 0;
        
        updateTimer();
        customTimerDao.setOngoing(false);
        return customTimerDao.getCumulativeTime() / 1000;
    }
    
    public long getCumulativeTime() {
        updateTimer();
        long time = customTimerDao.getCumulativeTime() / 1000;
        Log.d(TAG, "Cumulative time: " + customTimerDao.getCumulativeTime());
        return time;
    }
    
    public long getTaskTime() {
        updateTimer();
        customTimerDao.setTaskStartTime(System.currentTimeMillis());
        long taskTimeSeconds = customTimerDao.getTaskTime() / 1000;
        customTimerDao.setTaskTime(0);
        return taskTimeSeconds;
    }
    
    public long getTaskTimeNoReset() {
        updateTimer();
        customTimerDao.setTaskStartTime(System.currentTimeMillis());
        return customTimerDao.getTaskTime() / 1000;
    }
    
    public void addSeconds(long seconds) {
        updateTimer();
        customTimerDao.setCumulativeTime(customTimerDao.getCumulativeTime() + seconds * 1000);
        customTimerDao.setTaskTime(customTimerDao.getTaskTime() + seconds * 1000);
    }
}
