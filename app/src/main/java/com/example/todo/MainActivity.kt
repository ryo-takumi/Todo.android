package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    lateinit var realm: Realm
    lateinit var result: RealmResults<TaskDB>
    lateinit var task_list: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAddNewWord.setOnClickListener {
            val intent = Intent(this@MainActivity,EditActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_status),getString(R.string.status_add))
            startActivity(intent)
        }

        listView.setOnItemClickListener(this)
        listView.setOnItemLongClickListener(this)

    }




    override fun onResume() {
        super.onResume()

        realm = Realm.getDefaultInstance()
        result = realm.where(TaskDB::class.java).findAll().sort("strTime")

        task_list = ArrayList<String>()

        val length = result.size

        for (i in 0 .. length-1) {
            if (result[i]?.finishFrag!!) {
                task_list.add(result[i]!!.strTime + ":" + result[i]!!.strTask + "✔️")
            } else {
                task_list.add(result[i]!!.strTime + ":" + result[i]!!.strTask)
            }
        }

        adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, task_list)
        listView.adapter = adapter

    }

    override fun onPause() {
        super.onPause()

        realm.close()
    }




    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectDB = result[position]
        val strSelectTime = result[position]!!.strTime
        val strSelectTask = result[position]!!.strTask
        val strSelectFrag = result[position]!!.finishFrag
        val intent = Intent(this@MainActivity, EditActivity::class.java)
        intent.putExtra(getString(R.string.intent_key_time),strSelectTime)
        intent.putExtra(getString(R.string.intent_key_task),strSelectTask)
        intent.putExtra(getString(R.string.intent_key_frag),strSelectFrag)
        intent.putExtra(getString(R.string.intent_key_position),position)
        intent.putExtra(getString(R.string.intent_key_status),getString(R.string.status_change))
        startActivity(intent)

    }





    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {

        val selectDB = result[position]!!
        val dialog = AlertDialog.Builder(this@MainActivity).apply{
            setTitle(selectDB.strTask + "の削除")
            setMessage("削除しても良いですか？")
            setPositiveButton("yes"){dialog, which ->

                realm.beginTransaction()
                selectDB.deleteFromRealm()

                realm.commitTransaction()

                task_list.removeAt(position)
                listView.adapter = adapter
            }
            setNegativeButton("no"){dialog, which ->

            }
            show()
        }





        return true

    }
}