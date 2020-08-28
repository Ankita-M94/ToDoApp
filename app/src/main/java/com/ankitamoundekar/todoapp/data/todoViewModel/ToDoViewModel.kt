package com.ankitamoundekar.todoapp.data.todoViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ankitamoundekar.todoapp.data.ToDoDatabase
import com.ankitamoundekar.todoapp.data.model.ToDoData
import com.ankitamoundekar.todoapp.data.todoRepository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) :     AndroidViewModel(application)
{
    private val todoDao=ToDoDatabase.getDatabase(application).todoDao()
    private val todoRepository :TodoRepository
    val getAllData : LiveData<List<ToDoData>>
    val sortByHighPriority :LiveData<List<ToDoData>>
    val sortByLowPriority :LiveData<List<ToDoData>>

    init {
        todoRepository=TodoRepository(todoDao)
        getAllData=todoRepository.getAllData
        sortByHighPriority=todoDao.sortByPriorityHigh()
        sortByLowPriority=todoDao.sortByPriorityLow()
    }

    fun insertData(toDoData: ToDoData)
    {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData)
    {
        viewModelScope.launch(Dispatchers.IO){
            todoRepository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoData)
    {
        viewModelScope.launch(Dispatchers.IO){
            todoRepository.deleteItem(toDoData)
        }
    }

    fun deleteAll()
    {
        viewModelScope.launch(Dispatchers.IO){
            todoRepository.deleteAll()
        }
    }
    fun searchDatabase(searchQuery:String):LiveData<List<ToDoData>>
    {
            return todoRepository.searchDatabase(searchQuery)
    }
}