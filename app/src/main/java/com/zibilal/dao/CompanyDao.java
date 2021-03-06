package com.zibilal.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.zibilal.dao.Company;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMPANY".
*/
public class CompanyDao extends AbstractDao<Company, Long> {

    public static final String TABLENAME = "COMPANY";

    /**
     * Properties of entity Company.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Ranking = new Property(2, int.class, "ranking", false, "RANKING");
        public final static Property Create_date = new Property(3, java.util.Date.class, "create_date", false, "CREATE_DATE");
        public final static Property Update_date = new Property(4, java.util.Date.class, "update_date", false, "UPDATE_DATE");
    };


    public CompanyDao(DaoConfig config) {
        super(config);
    }
    
    public CompanyDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMPANY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"RANKING\" INTEGER NOT NULL ," + // 2: ranking
                "\"CREATE_DATE\" INTEGER NOT NULL ," + // 3: create_date
                "\"UPDATE_DATE\" INTEGER NOT NULL );"); // 4: update_date
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMPANY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Company entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getRanking());
        stmt.bindLong(4, entity.getCreate_date().getTime());
        stmt.bindLong(5, entity.getUpdate_date().getTime());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Company readEntity(Cursor cursor, int offset) {
        Company entity = new Company( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // name
            cursor.getInt(offset + 2), // ranking
            new java.util.Date(cursor.getLong(offset + 3)), // create_date
            new java.util.Date(cursor.getLong(offset + 4)) // update_date
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Company entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setRanking(cursor.getInt(offset + 2));
        entity.setCreate_date(new java.util.Date(cursor.getLong(offset + 3)));
        entity.setUpdate_date(new java.util.Date(cursor.getLong(offset + 4)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Company entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Company entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
