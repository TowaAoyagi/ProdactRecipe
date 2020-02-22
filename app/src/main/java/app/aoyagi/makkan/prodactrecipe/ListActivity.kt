package app.aoyagi.makkan.prodactrecipe

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_edit_todo.*
import kotlinx.android.synthetic.main.activity_edit_todo.view.*
import kotlinx.android.synthetic.main.activity_expression.*
import kotlinx.android.synthetic.main.activity_expression.view.*
import kotlinx.android.synthetic.main.activity_list.*
import java.nio.file.Files.delete
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

        val todoList = readAll()
        val adapter = RealmAdapter(this, todoList, object : RealmAdapter.OnItemClickListener {
            override fun onItemClick(item: RealmInfo) {
                // クリック時の処理
                val intent = Intent(this@ListActivity, OneViewActivity::class.java)
                intent.putExtra("title", item.title)
                intent.putExtra("means",item.means)
                intent.putExtra("check",item.check)
                intent.putExtra("id",item.id)
                startActivity(intent)
                finish()
            }
        }, true)

        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

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


    fun readAll(): RealmResults<RealmInfo> {
//        チェックボックスにチェックがついてる順にソート
        return realm.where(RealmInfo::class.java).findAll().sort("check", Sort.ASCENDING)
    }

    fun delete(id: String) {
        realm.executeTransaction {
            val task = realm.where(RealmInfo::class.java).equalTo("id", id).findFirst()
                ?: return@executeTransaction
            task.deleteFromRealm()
        }
    }

    fun delete(task: RealmInfo) {
        realm.executeTransaction {
            task.deleteFromRealm()
        }
    }

    fun deleteAll() {
        realm.executeTransaction {
            realm.deleteAll()
        }
    }
}
