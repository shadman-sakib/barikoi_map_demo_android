package com.barikoi.mapdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.barikoi.mapdemo.adapter.ExampleAdapter
import java.util.Arrays

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val examples: ArrayList<String> = ArrayList()
        examples.add("Add a simple map")
        examples.add("change style")
        examples.add("View current location")
        examples.add("Add a marker")
        examples.add( "Add a line")
        examples.add("Add a polygon")
        examples.add("View current location")
        examples.add("Add a geometry")

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewExamples)

        val exampleAdapter= ExampleAdapter(examples)
        exampleAdapter.setOnClickListener(object: ExampleAdapter.OnClickListener{
            override fun onClick(position: Int, data: String) {
                var intent = Intent(this@MainActivity, AddMapActivity::class.java)
                when(position){
                    0 -> intent= Intent(this@MainActivity, AddMapActivity::class.java)
                    2 -> intent= Intent(this@MainActivity, AddMapActivity::class.java)
                    3 -> intent= Intent(this@MainActivity, MarkerMapActivity::class.java)
                    4 -> intent = Intent(this@MainActivity, LineMapActivity::class.java)
                    5 -> intent = Intent(this@MainActivity, PolygonMapActivity::class.java)
                    6 -> intent = Intent(this@MainActivity, LocationMapActivity::class.java)
                    7 -> intent = Intent(this@MainActivity, GeometryMapActivity::class.java)
                }

                startActivity(intent)
            }
        })
        recyclerView.adapter = exampleAdapter




    }
}