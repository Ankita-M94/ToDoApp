package  com.ankitamoundekar.todoapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ankitamoundekar.todoapp.R
import com.ankitamoundekar.todoapp.data.model.ToDoData
import com.ankitamoundekar.todoapp.data.todoViewModel.SharedViewModel
import com.ankitamoundekar.todoapp.data.todoViewModel.ToDoViewModel
import com.ankitamoundekar.todoapp.databinding.FragmentToDoListBinding
import com.ankitamoundekar.todoapp.fragments.list.adapter.ListAdapter
import com.ankitamoundekar.todoapp.fragments.list.adapter.SwipeToDelete
import com.ankitamoundekar.todoapp.utils.hideKeyBoard
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ToDoListFragment : Fragment(),SearchView.OnQueryTextListener {

    val todoViewModel :ToDoViewModel by viewModels()
    val todoListAdapter : ListAdapter by lazy { ListAdapter() }
    val sharedViewModel :SharedViewModel by viewModels()

    var _binding :FragmentToDoListBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment Using Databinding
        _binding= FragmentToDoListBinding.inflate(inflater,container,false)

        binding.lifecycleOwner=this
        binding.sharedViewModel=sharedViewModel
        //Set Up RecyclerView
        setUpRecyclerView()

        //Observe LiveData
        todoViewModel.getAllData.observe(viewLifecycleOwner, Observer {data ->
            sharedViewModel.checkIfDataBaseIsEmpty(data)
            todoListAdapter.setData(data)
        })

        //Set Menu
        setHasOptionsMenu(true)

        //hide keybord
        hideKeyBoard(requireActivity())
        return binding.root
    }

     fun setUpRecyclerView() {
        val recyclerView= binding.recyclerView
        recyclerView.adapter= todoListAdapter
        recyclerView.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
         recyclerView.itemAnimator=SlideInUpAnimator().apply {
             addDuration=300
         }
         swipeToDelete(recyclerView)
    }

    fun swipeToDelete(recyclerView: RecyclerView)
    {
        //Swipe to dlete item
        val swipeToDeleteCallback= object : SwipeToDelete() {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem=todoListAdapter.dataList[viewHolder.adapterPosition]
                todoViewModel.deleteItem(deletedItem)
                //Restore Deleted Item
                restoreDeletedItem(viewHolder.itemView,deletedItem)            }
        }

        val itemTouchHelper=ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun restoreDeletedItem(view:View,deletedItem:ToDoData)
    {
        val snackBar=Snackbar.make(view,"Deleted : '${deletedItem.title}'",Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo"){
            todoViewModel.insertData(deletedItem)
//            todoListAdapter.notifyItemChanged(position)
        }
        snackBar.show()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!=null)
        {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query!=null)
        {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery :String ="%$query%"
        todoViewModel.searchDatabase(searchQuery).observe(this, Observer {list ->
            list?.let {
                todoListAdapter.setData(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_todo_list_fragment,menu)
        val search =menu.findItem(R.id.search)
        val searchView :SearchView?=search.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.delete_all->
                confirmAllItemRemoval()
            R.id.menu_priority_high->todoViewModel.sortByHighPriority.observe(this, Observer { todoListAdapter.setData(it) })
            R.id.menu_priority_low->todoViewModel.sortByLowPriority.observe(this, Observer { todoListAdapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    //Show Alert Dialogue to Confirm Removal of all Item DB
    fun confirmAllItemRemoval() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setPositiveButton("YES"){_,_ ->
            todoViewModel.deleteAll()
            Toast.makeText(requireContext(),"Successfully Removed Everything!...", Toast.LENGTH_SHORT).show()
        }
        alertDialog.setNegativeButton("NO"){_,_ -> }
        alertDialog.setTitle("Delete everything?")
        alertDialog.setMessage("Are you sure you want to remove everything'?")
        alertDialog.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


}