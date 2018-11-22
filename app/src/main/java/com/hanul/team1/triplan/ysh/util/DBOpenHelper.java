package com.hanul.team1.triplan.ysh.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;

import java.util.ArrayList;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String tableName = "plansort";

    public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + tableName + "(seq integer PRIMARY KEY, planid integer)";
        try{
            db.execSQL(sql);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initInsert(SQLiteDatabase db, ArrayList<PlanListDTO> dtos){
        db.beginTransaction();

        for(PlanListDTO vo : dtos){
            try{
                String sql = "insert into " + tableName +"(planid) values(" + vo.getPlanid() + ")";
                db.execSQL(sql);
                db.setTransactionSuccessful();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }
    }

}
