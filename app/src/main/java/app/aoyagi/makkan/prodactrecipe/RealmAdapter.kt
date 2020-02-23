package app.aoyagi.makkan.prodactrecipe


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_expression.view.*

class RealmAdapter(
    private val context: Context,
    private var list: OrderedRealmCollection<RealmInfo>?,
    private var listener: OnItemClickListener,
    private val autoUpdate: Boolean
) :
//    継承するクラスの中身をここに直接書いてる？
    RealmRecyclerViewAdapter<RealmInfo, RealmAdapter.ViewHolder>(list, autoUpdate) {


    override fun getItemCount(): Int = list?.size ?: 0

    //    ここで中身をセットする
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task: RealmInfo = list?.get(position) ?: return

        holder.linear.setOnClickListener {
            listener.onItemClick(task)
        }
        holder.titleTextView.text = task.title
        holder.contentTextView.text = task.means
        holder.checkbox.isChecked = task.check
        holder.checkbox.setOnClickListener {
            listener.onCheckboxClicked(holder.checkbox,task)
        }


    }


    //    Inflaterをここで設定する
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.activity_expression, viewGroup, false)
        return ViewHolder(v)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.titleView
        var contentTextView: TextView = view.contentView
        val checkbox: CheckBox = view.checkBox
        val linear: LinearLayout = view.linear

    }


    interface OnItemClickListener {
        fun onItemClick(item: RealmInfo)
        fun onCheckboxClicked(view: View, item: RealmInfo)

    }
}