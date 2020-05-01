package com.example.app12334

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

val DATABASE_NAME="MY_MOVIE_DB"
val ID ="ID"
val TABLE_NAME ="MOVIE"
val TABLE_NAME1="MOVIEDETAILS"
val COLUMN_STATUS ="STATUS"
val COLUMN_TITLE ="TITLE"
val COLUMN_OVERVIEW ="OVERVIEW"
val COLUMN_RELEASEDATE ="RELEASEDTE"
val COLUMN_IMAGEURL ="IMAGEURL"
val COLUMN_MOVIEID="MOVIEID"
val COLUMN_CATEGORY="CATEGORY"
val COLUMN_REVENUE="REVENUE"
val COLUMN_RUNTIME="RUNTIME"
val COLUMN_BUDGET="BUDGET"
val COLUMN_ORIGINALLAN="ORIGINALLAN"
val COLUMN_GENERS="GENERES"


class DataBaseHandler(var context:Context) :SQLiteOpenHelper(context, DATABASE_NAME,null,1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "create table $TABLE_NAME($ID INTEGER PRIMARY KEY AUTOINCREMENT,$COLUMN_MOVIEID INTEGER, $COLUMN_TITLE VARCHAR(256), $COLUMN_OVERVIEW  VARCHAR(256), $COLUMN_RELEASEDATE  VARCHAR(256), $COLUMN_IMAGEURL  VARCHAR(256), $COLUMN_CATEGORY  VARCHAR(256));"
        val createtable2 =
            "create table  $TABLE_NAME1($ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_MOVIEID  VARCHAR(256), $COLUMN_TITLE VARCHAR(256), $COLUMN_OVERVIEW VARCHAR(256), $COLUMN_RELEASEDATE  VARCHAR(256), $COLUMN_IMAGEURL  VARCHAR(256),  $COLUMN_BUDGET  VARCHAR(256), $COLUMN_REVENUE VARCHAR(256), $COLUMN_RUNTIME VARCHAR(256), $COLUMN_STATUS  VARCHAR(256), $COLUMN_ORIGINALLAN VARCHAR(256), $COLUMN_GENERS  VARCHAR(256));"
        db?.execSQL(createTable)
        db?.execSQL(createtable2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertData(movie: Movie,category: String) {
        var db = this.writableDatabase
        var contextvalue = ContentValues()
        contextvalue.put(COLUMN_MOVIEID, movie.id)
        contextvalue.put(COLUMN_TITLE, movie.title)
        contextvalue.put(COLUMN_OVERVIEW, movie.overview)
        contextvalue.put(COLUMN_RELEASEDATE, movie.releasedate)
        contextvalue.put(COLUMN_IMAGEURL, movie.url)
        contextvalue.put(COLUMN_CATEGORY,category)
        var res= db.insert(TABLE_NAME, null, contextvalue)
        if(res== -1L ){
            Log.d("Debugging","notinsrted")
        }
        else{
            Log.d("Debugging","insrted")
        }

    }

    fun insertData1(movie: MovieInfo) {

        var db = this.writableDatabase
        var contextvalue = ContentValues()
        contextvalue.put(COLUMN_MOVIEID, movie.movieId)
        contextvalue.put(COLUMN_TITLE, movie.title)
        contextvalue.put(COLUMN_OVERVIEW, movie.overview)
        contextvalue.put(COLUMN_RELEASEDATE, movie.releasedate)
        contextvalue.put(COLUMN_IMAGEURL, movie.url)
        contextvalue.put(COLUMN_BUDGET, movie.budget)
        contextvalue.put(COLUMN_GENERS, movie.generes)
        contextvalue.put(COLUMN_REVENUE, movie.revenue)
        contextvalue.put(COLUMN_RUNTIME, movie.runtime)
        contextvalue.put(COLUMN_STATUS, movie.status)

        var result=db.insert(TABLE_NAME1, null, contextvalue)
        if(result== -1L ){
            Log.d("Debugging","notinsrted")
        }
        else{
            Log.d("Debugging","insrted")
        }

    }

    fun deleteAllrecords() {
        var db = this.writableDatabase
        db.execSQL("delete  from  $TABLE_NAME")
    }

    fun deleterecord(){
        var db = this.writableDatabase
        db.execSQL("delete  from $TABLE_NAME1")

    }
    fun getAllData1(movieid: Int): Cursor? {
        var db = this.readableDatabase
        return db.rawQuery("select * from $TABLE_NAME1 where Movieid= '"+movieid+"'", null)
    }
    fun getAllData(category: String): Cursor? {
        var db = this.readableDatabase
        return db.rawQuery("select * from $TABLE_NAME where Category='"+category+"'",null)

    }
}

