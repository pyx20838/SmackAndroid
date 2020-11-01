package com.xmpp.smackchat.base.repository.database;


import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

public abstract class BaseDao<T extends BaseEntity> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(T obj);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertOrReplace(T obj);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    public abstract void update(T obj);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateOrReplace(T obj);

    @Delete()
    public abstract void delete(T obj);
}
