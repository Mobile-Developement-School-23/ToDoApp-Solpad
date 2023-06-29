import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.TodoItemBinding
import com.example.todoapp.model.TodoItem
import com.example.todoapp.util.Utils

class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = TodoItemBinding.bind(view)
    var infoItem = binding.infoItem
    var deathlineItem = binding.deathlineItem
    var checkBoxItem = binding.checkboxItem
    var importanceItem = binding.importanceItem
    var textItem = binding.textItem

    fun bind(item: TodoItem) = with(binding) {
        textItem.text = item.content
        if (item.deadline != 0L) deathlineItem.text =
            item.deadline?.let { Utils().convertLongDeathlineToString(it) }
        else deathlineItem.text = "Дедлайна нет, гуляем"
        item.flag?.let { checkBoxItem.isChecked = item.flag!! }

    }


}