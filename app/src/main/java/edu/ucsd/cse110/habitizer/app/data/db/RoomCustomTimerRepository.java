package edu.ucsd.cse110.habitizer.app.data.db;

import android.util.Log;

import edu.ucsd.cse110.habitizer.lib.domain.CustomTimer;

public class RoomCustomTimerRepository {
    private final CustomTimerDao customTimerDao;
    
    private final String TAG = "CustomTimerRepository";
    
    public RoomCustomTimerRepository(CustomTimerDao customTimerDao) {
        this.customTimerDao = customTimerDao;
    }

    public void clear() {
        customTimerDao.clearAll();
    }
    
    public void saveTimer() {
        CustomTimerEntity timerEntity = new CustomTimerEntity(0, 0, false, 0, 0);
        customTimerDao.append(timerEntity);
    }
    
    public void updateTimer() {
        if (!customTimerDao.getOngoing()) return;
        
        long cumulativeTime1 = customTimerDao.getCumulativeTime();
        long l = System.currentTimeMillis();
        long startTime1 = customTimerDao.getStartTime();
        
        long cumulativeTime = cumulativeTime1 + l - startTime1;
        
        long startTime = System.currentTimeMillis();
        long taskTime = customTimerDao.getTaskTime() + System.currentTimeMillis() - customTimerDao.getTaskStartTime();
        long taskStartTime = System.currentTimeMillis();
        customTimerDao.updateTimer(cumulativeTime, startTime, taskTime, taskStartTime);
    }
    
    public void startTimer() {
        if (customTimerDao.getOngoing()) return;
        
        customTimerDao.setOngoing(true);
        customTimerDao.setStartTime(System.currentTimeMillis());
        customTimerDao.setTaskStartTime(System.currentTimeMillis());
    }
    
    public void startTimerOnAppRestart() {
        customTimerDao.setStartTime(System.currentTimeMillis());
        customTimerDao.setTaskStartTime(System.currentTimeMillis());
    }
    
    public long pauseTimer() {
        if (!customTimerDao.getOngoing()) return 0;
        
        updateTimer();
        customTimerDao.setOngoing(false);
        return customTimerDao.getCumulativeTime() / 1000;
    }

    public boolean getOngoing() {
        return customTimerDao.getOngoing();
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
