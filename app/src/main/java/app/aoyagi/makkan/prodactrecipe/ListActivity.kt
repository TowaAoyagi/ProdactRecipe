package app.aoyagi.makkan.prodactrecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_list.*
import java.util.*

class ListActivity : AppCompatActivity() {


    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        supportActionBar?.hide()
        toolbar2.title = "Todo"
        toolbar2.setNavigationIcon(R.drawable.ic_view_headline_white_24dp)
        toolbar2.inflateMenu(R.menu.liet_menu)
        toolbar2.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.all -> {
                    makeAdapter(readAll())
                }
                R.id.active -> {
                    makeAdapter(readNotCheck())
                }
                R.id.completed -> {
                    makeAdapter(readCheck())
                }

            }
            true
        }


        val todoList = readAll()
        makeAdapter(todoList)


        fab2.setOnClickListener {
            val intent = Intent(this, EditTodoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun create(title: String, means: String, check: Boolean) {
        realm.executeTransaction {
            val realmInfo = realm.createObject(RealmInfo::class.java, UUID.randomUUID().toString())
            realmInfo.title = title
            realmInfo.means = means
            realmInfo.check = check
        }
    }

    fun makeAdapter(todoList: RealmResults<RealmInfo>) {
        val realmAdapter = RealmAdapter(this, todoList, object : RealmAdapter.OnItemClickListener {
            override fun onItemClick(item: RealmInfo) {
                // クリック時の処理
                val intent = Intent(this@ListActivity, OneViewActivity::class.java)
                intent.putExtra("title", item.title)
                intent.putExtra("means", item.means)
                intent.putExtra("check", item.check)
                intent.putExtra("id", item.id)
                startActivity(intent)
                finish()
            }

            override fun onCheckboxClicked(view: View, item: RealmInfo) {
                if (view is CheckBox) {
                    var checked: Boolean = view.isChecked
                    Log.d("checked", item.title)
                    when (checked) {
                        true -> {
                            create(item.title, item.means, checked)
                            delete(item.id)

                        }
                        false -> {
                            create(item.title, item.means, checked)
                            delete(item.id)
                        }
                    }
                }
            }
        }, true)

        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = realmAdapter
    }

    fun readAll(): RealmResults<RealmInfo> {
//        チェックボックスにチェックがついてる順にソート
        return realm.where(RealmInfo::class.java).findAll().sort("check", Sort.ASCENDING)
    }

    fun readCheck(): RealmResults<RealmInfo> {
        return realm.where(RealmInfo::class.java).equalTo("check", true).findAll()
    }

    fun readNotCheck(): RealmResults<RealmInfo> {
        return realm.where(RealmInfo::class.java).equalTo("check", false).findAll()
    }

    fun delete(id: String) {
        realm.executeTransaction {
            val task = realm.where(RealmInfo::class.java).equalTo("id", id).findFirst()
                ?: return@executeTransaction
            task.deleteFromRealm()
        }
    }

}
