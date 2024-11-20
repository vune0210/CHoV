package com.example.uipj.adapter.group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.uipj.data.dao.GroupDAO;
import com.example.uipj.data.model.Group;
import com.example.uipj.databinding.ItemClassBinding;
import com.example.uipj.preferen.UserSharePreferences;
import com.example.uipj.ui.activities.classes.ViewClassActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private final Context context;
    private final ArrayList<Group> classes;

    public ClassAdapter(Context context, ArrayList<Group> classes) {
        this.context = context;
        this.classes = classes;
    }

    @NonNull
    @NotNull
    @Override
    public ClassAdapter.ClassViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemClassBinding binding = ItemClassBinding.inflate(inflater, parent, false);
        return new ClassViewHolder(binding.getRoot());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ClassAdapter.ClassViewHolder holder, int position) {
        Group group = classes.get(position);
        holder.binding.classNameTv.setText(group.getName());
        UserSharePreferences userSharePreferences = new UserSharePreferences(context);
        if (Objects.equals(group.getUser_id(), userSharePreferences.getId())) {
            holder.binding.isAdminTv.setVisibility(View.VISIBLE);
        }
        GroupDAO groupDAO = new GroupDAO(context);
        int numberMember = groupDAO.getNumberMemberInClass(group.getId()) + 1;
        int numberSet = groupDAO.getNumberFlashCardInClass(group.getId());
        holder.binding.numberUserTv.setText(numberMember + " members");
        holder.binding.numberSetTv.setText(numberSet + " sets");

        holder.itemView.setOnClickListener(v -> {
            userSharePreferences.setClassId(group.getId());
            Intent intent = new Intent(context, ViewClassActivity.class);
            intent.putExtra("id", group.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        private final ItemClassBinding binding;

        public ClassViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemClassBinding.bind(itemView);
        }
    }
}
