package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "tasks",
        foreignKeys = @ForeignKey(
                entity = RoutineEntity.class, // References RoutineEntity
                parentColumns = "id",         // Primary key in RoutineEntity
                childColumns = "routineId",   // Foreign key in TaskEntity
                onDelete = ForeignKey.CASCADE // Ensures tasks are deleted if routine is deleted
        )
)
public class TaskEntity {


    @ColumnInfo(name = "routineId", index = true)
    public Integer routineId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "timeSpent")
    public int timeSpent;
}
