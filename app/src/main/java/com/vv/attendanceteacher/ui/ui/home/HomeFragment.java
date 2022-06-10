package com.vv.attendanceteacher.ui.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vv.attendanceteacher.DataStore;
import com.vv.attendanceteacher.MainActivity;
import com.vv.attendanceteacher.R;
import com.vv.attendanceteacher.RetrofitClient;
import com.vv.attendanceteacher.databinding.FragmentHomeBinding;
import com.vv.attendanceteacher.ui.HomeActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    LinearLayout linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        linearLayout = root.findViewById(R.id.dashboardLayout);


        Call<List<List<String>>> listCall = RetrofitClient.getInstance().getMyApi().getTeacherDashboard(MainActivity.sharedPreferences.getString(DataStore.TEACHER_ID,"0"));
        listCall.enqueue(new Callback<List<List<String>>>() {
            @Override
            public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                int i=0;
                for(List<String> list : response.body()){
                    View view = inflater.inflate(R.layout.home_card,linearLayout,false);
                    TextView title = view.findViewById(R.id.card_title);
                    TextView value = view.findViewById(R.id.card_value);
                    ImageView img = view.findViewById(R.id.card_image);
                    title.setText(list.get(0));
                    value.setText(list.get(1));
                    if(i==0)
                        img.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_people_24));
                    else if(i==1)
                        img.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_computer_24));
                    else if(i==2)
                        img.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_alt_route_24));
                    else
                        img.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_calendar_today_24));
                    linearLayout.addView(view);
                    i++;
                }
            }

            @Override
            public void onFailure(Call<List<List<String>>> call, Throwable t) {
                Toast.makeText(getContext(),"Something went wrong. Try Again !!", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}