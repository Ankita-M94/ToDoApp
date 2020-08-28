package com.ankitamoundekar.todoapp.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ankitamoundekar.todoapp.R
import com.ankitamoundekar.todoapp.data.model.Priority
import com.ankitamoundekar.todoapp.data.model.ToDoData
import com.ankitamoundekar.todoapp.data.todoViewModel.SharedViewModel
import com.ankitamoundekar.todoapp.data.todoViewModel.ToDoViewModel
import com.ankitamoundekar.todoapp.databinding.FragmentUpdateNotesBinding
import kotlinx.android.synthetic.main.fragment_add_note.view.*
import kotlinx.android.synthetic.main.fragment_update_notes.*
import kotlinx.android.synthetic.main.fragment_update_notes.view.*
import kotlinx.android.synthetic.main.fragment_update_notes.view.update_tittle_et
import kotlinx.android.synthetic.main.row_layout.view.*

class UpdateNotesFragment : Fragment() {

    val args by navArgs<UpdateNotesFragmentArgs>()

    val sharedViewModel: SharedViewModel by viewModels()
    private val todoViewModel: ToDoViewModel by viewModels()
    var _binding:FragmentUpdateNotesBinding?=null
    val binding get() =_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentUpdateNotesBinding.inflate(inflater,container,false)

        binding.lifecycleOwner=this
        binding.args=args

        binding.updatePrioritiesSpinner.onItemSelectedListener = sharedViewModel.listner
        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_update, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.update->
                updateData()
            R.id.menu_delete->
                confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateData() {
        //Update current Item
        val currentTitle = update_tittle_et.text.toString()
        val currentDescription = update_description_et.text.toString()
        val currentSpinerItem = update_priorities_spinner.selectedItem.toString()

        val validate = sharedViewModel.verifyDataFromUser(currentTitle, currentDescription)
        if (validate) {
            val updatedItem = ToDoData(
                args.currentItem.id,
                currentTitle,
                sharedViewModel.parsePriority(currentSpinerItem),
                currentDescription
            )
            todoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(), "Successfully Updated!...", Toast.LENGTH_SHORT).show()
//            Navigate Back
            findNavController().navigate(R.id.action_updateNotesFragment_to_toDoListFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all Fields...", Toast.LENGTH_SHORT)
                .show()
        }
    }

    //Alert Dialogue to delete Item or not
    fun confirmItemRemoval() {
        val alertDialog =AlertDialog.Builder(requireContext())
        alertDialog.setPositiveButton("YES"){_,_ ->
            todoViewModel.deleteItem(args.currentItem)
            Toast.makeText(requireContext(),"Successfully Removed ${args.currentItem.title}",Toast.LENGTH_SHORT).show()

            //Naviagte back
            findNavController().navigate(R.id.action_updateNotesFragment_to_toDoListFragment)
        }

        alertDialog.setNegativeButton("NO"){_,_ -> }
        alertDialog.setTitle("Delete '${args.currentItem.title}'?")
        alertDialog.setMessage("Are you sure you want to remove '${args.currentItem.title}'?")
        alertDialog.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}