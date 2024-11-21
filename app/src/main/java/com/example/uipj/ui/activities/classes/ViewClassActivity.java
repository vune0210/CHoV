package com.example.uipj.ui.activities.classes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.uipj.ui.activities.set.AddFlashCardToClassActivity;
import com.example.uipj.ui.activities.group.AddMemberActivity;
import com.example.uipj.ui.activities.group.EditClassActivity;
import com.example.uipj.R;
import com.example.uipj.adapter.group.MyViewClassAdapter;
import com.example.uipj.data.dao.GroupDAO;
import com.example.uipj.data.dao.UserDAO;
import com.example.uipj.data.model.Group;
import com.example.uipj.databinding.ActivityViewClassBinding;
import com.example.uipj.preferen.UserSharePreferences;
import com.google.android.material.tabs.TabLayout;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ViewClassActivity extends AppCompatActivity {
    private ActivityViewClassBinding binding;
    private GroupDAO groupDAO;
    int currentTabPosition = 0;
    private String id;
    private UserSharePreferences userSharePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewClassBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        MyViewClassAdapter myViewClassAdapter = new MyViewClassAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(myViewClassAdapter);

        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            myViewClassAdapter.notifyDataSetChanged();
        });

    }

    @SuppressLint("SetTextI18n")
    private void setUpData() {
        id = getIntent().getStringExtra("id");
        groupDAO = new GroupDAO(this);
        Group group = groupDAO.getGroupById(id);
        binding.classNameTv.setText(group.getName());
        binding.termCountTv.setText(groupDAO.getNumberFlashCardInClass(id) + " sets");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_set, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu) {
            new BottomSheetMenuDialogFragment.Builder(this)
                    .setSheet(R.menu.menu_view_class)
                    .setTitle("Class")
                    .setListener(new BottomSheetListener() {
                        @Override
                        public void onSheetShown(@NotNull BottomSheetMenuDialogFragment bottomSheetMenuDialogFragment, @Nullable Object o) {

                        }

                        @Override
                        public void onSheetItemSelected(@NotNull BottomSheetMenuDialogFragment bottomSheetMenuDialogFragment, @NotNull MenuItem menuItem, @Nullable Object o) {
                            if (menuItem.getItemId() == R.id.add_member) {
                                if (isOwner()) {
                                    holderAddMember();
                                } else {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .statusDialogBuilder()
                                            .createErrorDialog()
                                            .setHeading("Error")
                                            .setDescription("You are not the owner of this class!")
                                            .build(Dialog::dismiss)
                                            .show();
                                }
                            } else if (menuItem.getItemId() == R.id.add_sets) {
                                if (isOwner()) {
                                    handleAddSets();
                                } else {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .statusDialogBuilder()
                                            .createErrorDialog()
                                            .setHeading("Error")
                                            .setDescription("You are not the owner of this class!")
                                            .build(Dialog::dismiss)
                                            .show();
                                }

                            } else if (menuItem.getItemId() == R.id.edit_class) {
                                if (isOwner()) {
                                    handleEditClass();
                                } else {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .statusDialogBuilder()
                                            .createErrorDialog()
                                            .setHeading("Error")
                                            .setDescription("You are not the owner of this class!")
                                            .build(Dialog::dismiss)
                                            .show();
                                }

                            } else if (menuItem.getItemId() == R.id.delete_class) {
                                if (isOwner()) {
                                    handleDeleteClass();
                                } else {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .statusDialogBuilder()
                                            .createErrorDialog()
                                            .setHeading("Error")
                                            .setDescription("You are not the owner of this class!")
                                            .build(Dialog::dismiss)
                                            .show();
                                }

                            } else if (menuItem.getItemId() == R.id.leave_class) {
                                if (!isOwner()) {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .standardDialogBuilder()
                                            .createStandardDialog()
                                            .setHeading("Are you sure?")
                                            .setDescription("You will lose access to this class!")
                                            .setIcon(R.drawable.baseline_logout_24)
                                            .build(new StandardDialogActionListener() {
                                                @Override
                                                public void onPositiveButtonClicked(Dialog dialog) {
                                                    UserDAO userDAO = new UserDAO(ViewClassActivity.this);
                                                    if (userDAO.removeUserFromClass(userSharePreference.getId(), userSharePreference.getClassId()) > 0L) {
                                                        PopupDialog.getInstance(ViewClassActivity.this)
                                                                .statusDialogBuilder()
                                                                .createSuccessDialog()
                                                                .setHeading("Leave!")
                                                                .setDescription("Your class has left!.")
                                                                .build(Dialog::dismiss)
                                                                .show();
                                                    } else {
                                                        PopupDialog.getInstance(ViewClassActivity.this)
                                                                .statusDialogBuilder()
                                                                .createErrorDialog()
                                                                .setHeading("Error")
                                                                .setDescription("Something went wrong!")
                                                                .build(Dialog::dismiss)
                                                                .show();
                                                    }
                                                }

                                                @Override
                                                public void onNegativeButtonClicked(Dialog dialog) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                } else {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .statusDialogBuilder()
                                            .createErrorDialog()
                                            .setHeading("Error")
                                            .setDescription("You are the owner of this class!")
                                            .build(Dialog::dismiss)
                                            .show();
                                }
                            }
                        }

                        @Override
                        public void onSheetDismissed(@NotNull BottomSheetMenuDialogFragment bottomSheetMenuDialogFragment, @Nullable Object o, int i) {

                        }
                    })
                    .setCloseTitle(getString(R.string.close))
                    .setAutoExpand(true)
                    .setCancelable(true)
                    .show(getSupportFragmentManager());
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleAddSets() {
        Intent intent = new Intent(this, AddFlashCardToClassActivity.class);
        intent.putExtra("flashcard_id", id);
        startActivity(intent);
        finish();
    }

    private void handleEditClass() {
        Intent intent = new Intent(this, EditClassActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void handleDeleteClass() {
        PopupDialog.getInstance(ViewClassActivity.this)
                .standardDialogBuilder()
                .createStandardDialog()
                .setHeading("Are you sure?")
                .setDescription("You will not be able to recover this class!")
                .setIcon(R.drawable.ic_delete)
                .build(new StandardDialogActionListener() {
                    @Override
                    public void onPositiveButtonClicked(Dialog dialog) {
                        if (groupDAO.deleteClass(id) > 0L) {
                            PopupDialog.getInstance(ViewClassActivity.this)
                                    .statusDialogBuilder()
                                    .createSuccessDialog()
                                    .setHeading("Deleted!")
                                    .setDescription("Your class has been deleted.")
                                    .build(Dialog::dismiss)
                                    .show();
                        } else {
                            PopupDialog.getInstance(ViewClassActivity.this)
                                    .statusDialogBuilder()
                                    .createErrorDialog()
                                    .setHeading("Error")
                                    .setDescription("Something went wrong!")
                                    .build(Dialog::dismiss)
                                    .show();
                        }
                    }

                    @Override
                    public void onNegativeButtonClicked(Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void holderAddMember() {
        Intent intent = new Intent(this, AddMemberActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }

    private boolean isOwner() {
        userSharePreference = new UserSharePreferences(this);
        String currentUserId = userSharePreference.getId();
        String ownerId = groupDAO.getGroupById(id).getUser_id();
        return currentUserId.equals(ownerId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpData();
    }
}