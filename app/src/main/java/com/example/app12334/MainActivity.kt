package com.example.app12334


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener, OnItemClickListener {
    val movies: MutableList<Movie> = mutableListOf()
    var type: String = "popular"
    val client = OkHttpClient()
    val url: String = "https://api.themoviedb.org/3/movie/"
    lateinit var adapter: RecycleAdapter
    var n = 1
    var currentitem = 0
    var totalitem = 0
    var scrolloutitems: Int = 0
    var isscrolling = true
    companion object{
        lateinit var  dataBaseHandler:DataBaseHandler
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = RecycleAdapter(this, movies, this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        moviesshow.layoutManager = layoutManager
        moviesshow.adapter = adapter
        toolbar_title.text = "Popular Movies"
        val actionBarToggle = ActionBarDrawerToggle(this, drawer,toolbar, R.string.Open, R.string.Close)
        actionBarToggle.setDrawerIndicatorEnabled(true)
        drawer.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()
        //nav_view.setNavigationItemSelectedListener(this)
        dataBaseHandler= DataBaseHandler(this)

        if (dataConnectivity()) {
            dataBaseHandler.deleteAllrecords();
            dataBaseHandler.deleterecord();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
        callApi()
        moviesshow.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isscrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentitem = layoutManager.getChildCount()
                totalitem = layoutManager.getItemCount()
                scrolloutitems = layoutManager.findFirstVisibleItemPosition()
                if (isscrolling && currentitem + scrolloutitems >= totalitem) {
                    isscrolling = false
                    n = n + 1
                    if(dataConnectivity()){
                        callApi()
                    }


                }
            }
        })


    }

    override fun onNavigationItemSelected(menuitem: MenuItem): Boolean {
        when (menuitem.itemId) {
            R.id.popular123 -> {
                type = "popular"
                movies.clear()
                callApi()
                moviesshow.adapter = adapter
                toolbar_title.text = "Popular Movies"
            }
            R.id.toprated1 -> {
                type = "top_rated"
                movies.clear()
                callApi()
                moviesshow.adapter = adapter
                toolbar_title.text = "Toprated Movies"

            }
            R.id.upcoming1 -> {
                type = "upcoming"
                movies.clear()
                callApi()
                moviesshow.adapter = adapter
                toolbar_title.text = "Upcomig Movies"
            }
            R.id.nowplaying1 -> {
                type = "now_playing"
                movies.clear()
                callApi()
                moviesshow.adapter = adapter
                toolbar_title.text = "Nowplaying Movies"

            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
//     override fun onBackPressed() {
//         if (drawer.isDrawerOpen(GravityCompat.START)) {
//             drawer.closeDrawer(GravityCompat.START)
//         } else {
//             super.onBackPressed()
//         }
//     }

    fun callApi() {
        if (dataConnectivity()){
        val request = Request.Builder()
            .url(url + type + "?api_key=dc6f1f3d0d9c4f810e00f3dbbeb235fd&language=en-US&page=" + n)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {

                Log.d("Debugging", "failure" + e)
            }

            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onResponse(call: okhttp3.Call, response: Response) {
                runOnUiThread(Runnable() {
                    kotlin.run {
                        Log.d("Debugging", "success" )
                        val body = response?.body?.string()
                        val json = JSONObject(body)
                        val jsonarr = json.getJSONArray("results")
                        for (i in 0..jsonarr.length() - 1) {
                            val json = jsonarr.getJSONObject(i)
                            var id: Int = json.getInt("id")
                            var title: String = json.getString("title")
                            //Log.d("Debugging",""+title)
                            var overview: String = json.getString("overview")
                            var releasedate: String = json.getString("release_date")
                            val imageurl =
                                "https://image.tmdb.org/t/p/original" + json.getString("poster_path")
                            var movie = Movie(id, title, overview, releasedate, imageurl)
                            movies.add(movie)
                            Log.d("Debugging","pk")
                            dataBaseHandler.insertData(movie,type)
                            adapter.notifyDataSetChanged()

                        }

                    }
                })

            }

        })

    }
        else{

           var cursor =dataBaseHandler.getAllData(type)
            while (cursor!!.moveToNext()){
                var movieid =cursor.getInt(cursor.getColumnIndex("MOVIEID"))
               var title= cursor.getString(cursor.getColumnIndex("TITLE"))
                var releaedate =cursor.getString(cursor.getColumnIndex("RELEASEDTE"))
                var overview = cursor.getString(cursor.getColumnIndex("OVERVIEW"))
               var imageurl= cursor.getString(cursor.getColumnIndex("IMAGEURL"))

                var movie =Movie(movieid,title,overview,releaedate,imageurl)
                movies.add(movie)
                adapter.notifyDataSetChanged()
            }
        }
}


    override fun onItemClicked(id: Int,position:Int) {
       val intent = Intent(this,MovieDetails ::class.java)
        intent.putExtra("MOVIEID",id)
        intent.putExtra("dataconnectivity",dataConnectivity())
        startActivity(intent)
    }

    fun dataConnectivity(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }


}




