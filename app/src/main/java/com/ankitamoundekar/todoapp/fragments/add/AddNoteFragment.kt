package com.ankitamoundekar.todoapp.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.ankitamoundekar.todoapp.R
import com.ankitamoundekar.todoapp.data.model.Priority
import com.ankitamoundekar.todoapp.data.model.ToDoData
import com.ankitamoundekar.todoapp.data.todoViewModel.SharedViewModel
import com.ankitamoundekar.todoapp.data.todoViewModel.ToDoViewModel
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.fragment_add_note.view.*

class AddNoteFragment : Fragment() {

    private val todoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_add_note, container, false)
        //Set Menu
        setHasOptionsMenu(true)

        view.priorities_spinner.onItemSelectedListener=sharedViewModel.listner
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId ==R.id.menu_add)
        {
            insertDataToDB()
        }
        return super.onOptionsItemSelected(item)
    }

     fun insertDataToDB() {
        val mTittle=tittle_et.text.toString()
        val mPriority=priorities_spinner.selectedItem.toString()
        val mDescription=description_et.text.toString()

        var validate=sharedViewModel.verifyDataFromUser(mTittle,mDescription)
        if (validate)
        {
//            Insert Data to Database
            val todoData =ToDoData(0,mTittle,sharedViewModel.parsePriority(mPriority),mDescription)
            todoViewModel.insertData(todoData)
            Toast.makeText(requireContext(),"Successfuly added!...",Toast.LENGTH_SHORT).show()
            //Navigate Back
            findNavController().navigate(R.id.action_addNoteFragment_to_toDoListFragment)
        }else{
            Toast.makeText(requireContext(),"Please fill out all Fields...",Toast.LENGTH_SHORT).show()
        }
    }


}