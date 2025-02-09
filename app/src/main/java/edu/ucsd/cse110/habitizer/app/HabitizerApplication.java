package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

// import data and domain

public class HabitizerApplication extends Application {
   // private InMemoryDataSource dataSource;
    //private routineRepository routineRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        //this.dataSource = InMemoryDataSource.fromDefault();
        //this.routineRepository = new RoutineRepository(dataSource);
    }

//    public routineRepository getRoutineRepository() {
//        return routineRepository;
//    }
}