package com.example.app12334

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_layout.*
import kotlinx.android.synthetic.main.custom_layout.view.*
import kotlinx.android.synthetic.main.custom_layout.view.poster
import kotlinx.android.synthetic.main.viewdetails_layout.*
import kotlinx.android.synthetic.main.viewdetails_layout.movietitle
import kotlinx.android.synthetic.main.viewdetails_layout.releasedate
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.io.Serializable


class MovieDetails :AppCompatActivity() ,Serializable {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewdetails_layout)

        val bundle:Bundle?=intent.extras
        val id=bundle!!.getInt("MOVIEID")
        val client = OkHttpClient()
        var dataconnectivity =bundle.getBoolean("dataconnectivity")
        val db :DataBaseHandler = MainActivity.dataBaseHandler
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/"+id+"?api_key=dc6f1f3d0d9c4f810e00f3dbbeb235fd&language=en-US")
            .build()
        if(dataconnectivity) {

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {

                    Log.d("Debugging", "failure" + e)
                }

                @RequiresApi(Build.VERSION_CODES.KITKAT)
                override fun onResponse(call: okhttp3.Call, response: Response) {
                    runOnUiThread(Runnable() {
                        kotlin.run {
                            val JsonData = response.body!!.string()
                            val jsonObject = JSONObject(JsonData)
                            if (jsonObject != null) {

                            }
                            val budget2 = "$" + jsonObject.getString("budget")
                            val title2 = jsonObject.getString("title")
                            val overview2 = jsonObject.getString("overview")
                            val status2 = jsonObject.getString("status")
                            val revenue2 = "$" + jsonObject.getString("revenue")
                            val runtime2 = jsonObject.getString("runtime") + "m"
                            val releasedate2 = jsonObject.getString("release_date")
                            var originallanguage2 =
                                jsonObject.getString("original_language")
                            if (originallanguage2 == "en") {
                                originallanguage2 = "English"
                            } else if (originallanguage2 == "cn") {
                                originallanguage2 = "Chinese"
                            } else if (originallanguage2 == "hi") {
                                originallanguage2 = "Hindi"
                            }
                            val posterpath2 =
                                "https://image.tmdb.org/t/p/original" + jsonObject.getString("poster_path")
                            val genres2 = jsonObject.getJSONArray("genres")
                            var gen = ""
                            for (i in 0..genres2.length() - 1) {
                                var j = genres2.getJSONObject(i)
                                gen = gen + j.getString("name") + "  "

                            }
                            movietitle.text = title2
                            budget1.text = budget2
                            revenue1.text = revenue2
                            runtime1.text = runtime2
                            releasedate.text = releasedate2
                            originallan.text = originallanguage2
                            generes1.text = gen
                            moviestatus.text = status2
                            overview1.text = overview2
                            Picasso.get().load(posterpath2).into(movieposter)
                            var movieinfo = MovieInfo(
                                id,
                                title2,
                                overview2,
                                releasedate2,
                                posterpath2,
                                budget2,
                                revenue2,
                                runtime2,
                                originallanguage2,
                                gen,
                                status2
                            )
                            db.insertData1(movieinfo)

                        }


                    })
                }
            })

        }
        else
        {
            Log.d("debugging",""+id)
            var cursor= db.getAllData1(id)
            while (cursor!!.moveToNext()){
                movietitle.text  =   cursor.getString(cursor.getColumnIndex("TITLE"))
                budget1.text   =    cursor.getString(cursor.getColumnIndex("BUDGET"))
                revenue1.text  =    cursor.getString(cursor.getColumnIndex("REVENUE"))
                runtime1.text =     cursor.getString(cursor.getColumnIndex("RUNTIME"))
                originallan.text =   cursor.getString(cursor.getColumnIndex("ORIGINALLAN"))
                generes1.text =      cursor.getString(cursor.getColumnIndex("GENERES"))
                moviestatus.text =   cursor.getString(cursor.getColumnIndex("STATUS"))
                overview1.text =     cursor.getString(cursor.getColumnIndex("OVERVIEW"))
                var posterpath=cursor.getString(cursor.getColumnIndex("IMAGEURL"))
                releasedate.text =   cursor.getString(cursor.getColumnIndex("RELEASEDTE"))
                Picasso.get().load(posterpath).into(movieposter)

            }
        }

    }
}