package com.example.systemcouse.cache;

import com.example.systemcouse.model.*;
import com.example.systemcouse.repository.ScoreRepository;
import com.example.systemcouse.repository.TaskRepository;
import com.example.systemcouse.repository.UserRepository;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.List;

public class MyCache {

    @EJB
    private UserRepository userrepository;
    @EJB
    private TaskRepository taskrepository;
    @EJB
    private ScoreRepository scorerepository;


    private Cache cache;

    public MyCache() {
    }

    public Cache createUserCache() {

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        MutableConfiguration mutableConfiguration = new MutableConfiguration();
        mutableConfiguration.setTypes(Integer.class, User_.class);

        Cache newCache = cacheManager.createCache("cache", mutableConfiguration);

        List<User_> dataList = userrepository.selectUsers();
        for (User_ user: dataList) {
            newCache.put(user.getUserId(), user);
        }

        return newCache;
    }

    public Cache createTaskCache() {

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        MutableConfiguration mutableConfiguration = new MutableConfiguration();
        mutableConfiguration.setTypes(Integer.class, Task.class);

        Cache newCache = cacheManager.createCache("cache", mutableConfiguration);

        List<Task> dataList = taskrepository.selectTasks();
        for (Task task: dataList) {
            newCache.put(task.getTaskId(), task);
        }

        return newCache;
    }

    public Cache createScoreCache() {

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        MutableConfiguration mutableConfiguration = new MutableConfiguration();
        mutableConfiguration.setTypes(Integer.class, Score.class);

        Cache newCache = cacheManager.createCache("cache", mutableConfiguration);

        List<Score> dataList = scorerepository.selectScores();
        for (Score score: dataList) {
            newCache.put(score.getScoreId(), score);
        }

        return newCache;
    }



    public void populateUserCache() {
        cache = createUserCache();
    }
    public void populateTaskCache() { cache = createTaskCache(); }
    public void populateScoreCache() { cache = createScoreCache(); }


    public Object getData(Integer key) {
        return (Object) cache.get(key);
    }

}