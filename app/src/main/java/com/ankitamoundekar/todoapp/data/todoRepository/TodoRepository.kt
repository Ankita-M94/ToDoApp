package com.ankitamoundekar.todoapp.data.todoRepository

import androidx.lifecycle.LiveData
import com.ankitamoundekar.todoapp.data.ToDoDao
import com.ankitamoundekar.todoapp.data.model.ToDoData

class TodoRepository(private val todoDao :ToDoDao) {
    val getAllData :LiveData<List<ToDoData>> = todoDao.getAllData()
    val sortByHighPriority :LiveData<List<ToDoData>> = todoDao.sortByPriorityHigh()
    val sortByLowPriority :LiveData<List<ToDoData>> = todoDao.sortByPriorityLow()

    suspend fun insertData(toDoData: ToDoData)
    {
        todoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData)
    {
        todoDao.updateData(toDoData)
    }

    suspend fun deleteItem(toDoData: ToDoData)
    {
        todoDao.deleteData(toDoData)
    }

    suspend fun deleteAll()
    {
        todoDao.deleteAllData()
    }

    fun searchDatabase(searchQuery:String):LiveData<List<ToDoData>>
    {
        return todoDao.searchDatabase(searchQuery)
    }
}