package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface CustomTimerDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(CustomTimerEntity customTimer);
    
    @Transaction
    default int append(CustomTimerEntity customTimer) {
        insert(customTimer);
        return Math.toIntExact(insert(customTimer));
    }
    
    @Query("SELECT * FROM timers WHERE id = 0")
    CustomTimerEntity findTimer();
    
    @Query("UPDATE timers SET cumulativeTime = :cumTime, startTime = :startTime, taskTime = :taskTime, taskStartTime = :taskStartTime WHERE id = 0")
    void updateTimer(long cumTime, long startTime, long taskTime, long taskStartTime);
    
    /*
     * Getters for CustomTimerEntity fields
     */
    @Query("SELECT ongoing FROM timers WHERE id = 0")
    boolean getOngoing();
    
    @Query("SELECT cumulativeTime FROM timers WHERE id = 0")
    long getCumulativeTime();
    
    @Query("SELECT taskTime FROM timers WHERE id = 0")
    long getTaskTime();
    
    @Query("SELECT startTime FROM timers WHERE id = 0")
    long getStartTime();
    
    @Query("SELECT taskStartTime FROM timers WHERE id = 0")
    long getTaskStartTime();
    
    /*
     * Setters for CustomTimerEntity fields
     */
    @Query("UPDATE timers SET ongoing = :ongoing WHERE id = 0")
    void setOngoing(boolean ongoing);
    
    @Query("UPDATE timers SET startTime = :startTime WHERE id = 0")
    void setStartTime(long startTime);
    
    @Query("UPDATE timers SET taskStartTime = :taskStartTime WHERE id = 0")
    void setTaskStartTime(long taskStartTime);
    
    @Query("UPDATE timers SET taskTime = :taskTime WHERE id = 0")
    void setTaskTime(long taskTime);
    
    @Query("UPDATE timers SET cumulativeTime = :cumulativeTime WHERE id = 0")
    void setCumulativeTime(long cumulativeTime);

    @Query("DELETE FROM timers")
    void clearAll();
}
