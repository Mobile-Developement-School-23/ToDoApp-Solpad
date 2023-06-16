package com.example.todoapp.model

class TodoItemsRepository {
    companion object{
        var list = mutableListOf<TodoItem>(
            TodoItem("1","Bro",Importance.URGENT,false,
            20231106,20231107,1343805819061),
            TodoItem("2","Bro",Importance.LOW,false,
                20231106,20231107,1343805819061),
            TodoItem("3","Местные жители временно дали семье квартиру в долг — сумма растёт, платить нечем. Устроиться на высокооплачиваемую работу они не могут из-за гражданства, а оформление паспортов не в силах потянуть финансово. Семья просит помощи у подписчиков RT.",
                Importance.LOW, false,20231106,20231107,1343805819061),
            TodoItem("4","Bro",Importance.ORDINARY,true,
                20231106,20231107,1343805819061),

            )

    }

    fun deleteTodoItems(item:TodoItem){
        list.remove(item)
    }

    fun getTodoItems():List<TodoItem>{
        return list
    }

    fun setTodoItems(item:TodoItem){
        list.add(item)
    }

    fun changeTodoItems(item: TodoItem){

    }
}