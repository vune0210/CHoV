package com.example.uipj.ui.activities.folder

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uipj.R
import com.example.uipj.adapter.flashcard.SetFolderViewAdapter
import com.example.uipj.data.dao.FolderDAO
import com.example.uipj.data.model.FlashCard
import com.example.uipj.databinding.ActivityViewFolderBinding
import com.example.uipj.databinding.DialogCreateFolderBinding
import com.example.uipj.preferen.UserSharePreferences
import com.example.uipj.ui.activities.learn.QuizFolderActivity
import com.example.uipj.ui.activities.set.AddFlashCardActivity
import com.kennyc.bottomsheet.BottomSheetListener
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment
import com.saadahmedev.popupdialog.PopupDialog
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener
import com.squareup.picasso.Picasso


class ViewFolderActivity : AppCompatActivity(), BottomSheetListener {
    private val binding by lazy { ActivityViewFolderBinding.inflate(layoutInflater) }
    private val folderDAO by lazy { FolderDAO(this) }
    private lateinit var adapter: SetFolderViewAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupFolderDetails()
        setupRecyclerView()
        setupLearnButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupFolderDetails() {
        val id = intent.getStringExtra("id")
        val userSharePreferences = UserSharePreferences(this)
        val folder = folderDAO.getFolderById(id)
        binding.folderNameTv.text = folder.name
        Picasso.get().load(userSharePreferences.avatar).into(binding.avatarIv)
        binding.userNameTv.text = userSharePreferences.userName
        binding.termCountTv.text = folderDAO.getAllFlashCardByFolderId(id).size.toString() + " flashcards"
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecyclerView() {
        val id = intent.getStringExtra("id")
        adapter = SetFolderViewAdapter(folderDAO.getAllFlashCardByFolderId(id) as ArrayList<FlashCard>, false)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.setRv.layoutManager = linearLayoutManager
        binding.setRv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setupLearnButton() {
        val id = intent.getStringExtra("id")
        binding.learnThisFolderBtn.setOnClickListener {
            val newIntent = Intent(this, QuizFolderActivity::class.java)
            newIntent.putExtra("id", id)
            startActivity(newIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.vew_folder_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu) {
            BottomSheetMenuDialogFragment.Builder(
                context = this,
                sheet = R.menu.folder_menu,
                title = "Folder Menu",
                listener = this
            ).show(supportFragmentManager, "Menu")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSheetDismissed(bottomSheet: BottomSheetMenuDialogFragment, `object`: Any?, dismissEvent: Int) {
        Log.d("TAG", "onSheetDismissed: ")
    }


    @SuppressLint("SetTextI18n")
    override fun onSheetItemSelected(bottomSheet: BottomSheetMenuDialogFragment, item: MenuItem, `object`: Any?) {
        when (item.itemId) {
            R.id.edit_folder -> {
                handleEditFolder()
            }

            R.id.delete_folder -> {

                handleDeleteFolder()

            }

            R.id.add_set -> {
                handleAddSet()

            }

            R.id.share -> {

            }
        }
    }

    private fun handleAddSet() {
        val id = intent.getStringExtra("id")
        val newIntent = Intent(this, AddFlashCardActivity::class.java)
        newIntent.putExtra("id_folder", id)
        startActivity(newIntent)
    }

    private fun handleDeleteFolder() {
        PopupDialog.getInstance(this)
            .standardDialogBuilder()
            .createStandardDialog()
            .setHeading("Delete Folder")
            .setDescription("Are you sure you want to delete this folder?")
            .setIcon(R.drawable.ic_delete)
            .build(object : StandardDialogActionListener {
                override fun onPositiveButtonClicked(dialog: Dialog) {
                    if (folderDAO.deleteFolder(intent.getStringExtra("id")) > 0L) {
                        PopupDialog.getInstance(this@ViewFolderActivity)
                            .statusDialogBuilder()
                            .createSuccessDialog()
                            .setHeading("Success")
                            .setDescription("Delete successfully")
                            .build(Dialog::dismiss)
                            .show();
                    } else {
                        PopupDialog.getInstance(this@ViewFolderActivity)
                            .statusDialogBuilder()
                            .createErrorDialog()
                            .setHeading(getString(R.string.error))
                            .setDescription(getString(R.string.delete_set_error))
                            .build(Dialog::dismiss)
                            .show();
                    }
                }

                override fun onNegativeButtonClicked(dialog: Dialog) {
                    dialog.dismiss()
                }
            })
            .show()
    }

    @SuppressLint("SetTextI18n")
    private fun handleEditFolder() {

        val builder = AlertDialog.Builder(this)
        val dialogBinding = DialogCreateFolderBinding.inflate(layoutInflater)

        //set data
        val id = intent.getStringExtra("id")
        val folder = folderDAO.getFolderById(id)
        dialogBinding.folderEt.setText(folder.name)
        dialogBinding.descriptionEt.setText(folder.description)

        builder.setView(dialogBinding.root)
        builder.setCancelable(true)
        val dialog = builder.create()

        dialogBinding.folderEt.requestFocus()
        dialogBinding.cancelTv.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.okTv.setOnClickListener {
            val name = dialogBinding.folderEt.text.toString()
            val description = dialogBinding.descriptionEt.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter folder name", Toast.LENGTH_SHORT).show()
            } else {
                folder.name = name
                folder.description = description
                if (folderDAO.updateFolder(folder) > 0L) {
                    Toast.makeText(this, "Update folder successfully", Toast.LENGTH_SHORT).show()
                    //refresh data folder
                    binding.folderNameTv.text = folder.name
                    binding.termCountTv.text = folderDAO.getAllFlashCardByFolderId(id).size.toString() + " flashcards"
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Update folder failed", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        dialog.show()

    }

    override fun onSheetShown(bottomSheet: BottomSheetMenuDialogFragment, `object`: Any?) {
        Log.d("TAG", "onSheetShown: ")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        //refresh data adapter
        val id = intent.getStringExtra("id")
        adapter = SetFolderViewAdapter(folderDAO.getAllFlashCardByFolderId(id) as ArrayList<FlashCard>, false)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.setRv.layoutManager = linearLayoutManager
        binding.setRv.adapter = adapter
        adapter.notifyDataSetChanged()

        setupFolderDetails()

    }
}