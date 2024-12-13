package com.example.uipj.ui.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.uipj.databinding.FragmentHomeBinding;
import com.example.uipj.ui.home.HomeViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Thêm logic xử lý sự kiện cho tất cả các nút
        setupButtonListeners(root);

        return root;
    }

    private void setupButtonListeners(View root) {
        // Lấy tất cả các View con trong layout và gán sự kiện click
        for (int i = 0; i < ((ViewGroup) root).getChildCount(); i++) {
            View child = ((ViewGroup) root).getChildAt(i);
            if (child instanceof ViewGroup) {
                setupButtonListeners(child); // Đệ quy cho các ViewGroup con
            } else {
                child.setOnClickListener(v -> {
                    String idName = getResources().getResourceEntryName(v.getId());
                    Toast.makeText(getContext(), "Nút có ID là " + idName + " đã được kích hoạt", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}