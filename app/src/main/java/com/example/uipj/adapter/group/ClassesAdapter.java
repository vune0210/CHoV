package com.example.uipj.adapter.group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uipj.data.dao.GroupDAO;
import com.example.uipj.data.dao.UserDAO;
import com.example.uipj.data.model.Group;
import com.example.uipj.data.model.User;
import com.example.uipj.databinding.ItemClassesAdminBinding;
import com.squareup.picasso.Picasso;


import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ClassesViewHolder>{
    private final Context context;
    private final List<Group> classes;

    public ClassesAdapter(Context context, List<Group> classes) {
        this.context = context;
        this.classes = classes;
    }

    @NonNull
    @Override
    public ClassesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemClassesAdminBinding binding = ItemClassesAdminBinding.inflate(inflater, parent, false);
        return new ClassesViewHolder(binding.getRoot());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClassesViewHolder holder, int position) {
        Group group = classes.get(position);

        holder.binding.classNameTv.setText("Class: "+group.getName());
        GroupDAO groupDAO = new GroupDAO(context);
        int numberMember = groupDAO.getNumberMemberInClass(group.getId()) + 1;
        int numberSet = groupDAO.getNumberFlashCardInClass(group.getId());
        holder.binding.numberUserTv.setText(numberMember + " members");
        holder.binding.numberSetTv.setText(numberSet + " sets");

        UserDAO userDAO = new UserDAO(context);
        User user = userDAO.getUserById(group.getUser_id());
        Log.d("User_id",group.getUser_id());
        if (user != null){
            holder.binding.userNameTv.setText(user.getUsername());
            Log.d("Username",user.getUsername());
            Picasso.get().load(user.getAvatar()).into(holder.binding.avatarIv);
        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class ClassesViewHolder extends RecyclerView.ViewHolder {
        private final ItemClassesAdminBinding binding;

        public ClassesViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemClassesAdminBinding.bind(itemView);
        }
    }
}
