package com.vv.attendanceteacher.ui.ui.gallery;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.vv.attendanceteacher.RetrofitClient;
import com.vv.attendanceteacher.adapter.TakeAttendanceAdapter;
import com.vv.attendanceteacher.databinding.FragmentGalleryBinding;
import com.vv.attendanceteacher.models.AttendanceStatusModel;
import com.vv.attendanceteacher.models.ClassDropdown;
import com.vv.attendanceteacher.models.DivisonDropDown;
import com.vv.attendanceteacher.models.ShowAttendanceModel;
import com.vv.attendanceteacher.models.SubjectModel;
import com.vv.attendanceteacher.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.Arrays;
import retrofit2.converter.gson.GsonConverterFactory;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    ArrayList<String> bluetoothIds = new ArrayList<>();
    ArrayList<String> rollNos = new ArrayList<>();
    BluetoothAdapter mBluetoothAdapter;
    String classId;
    String divisonId;
    String subjectId;
    Boolean updateAttendance = false;

    CountDownTimer countDownTimer;
    RecyclerView recyclerView;
    TakeAttendanceAdapter takeAttendanceAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Spinner spinnerClass = binding.spinnerClass;
        final Spinner spinnerDivision = binding.spinnerDivision;
        final Spinner spinnerSubjects = binding.spinnerSubject;
        final Button scanDevices = binding.scanDevices;
        final Button takeAttendance = binding.takeAttendance;
        final TextView status = binding.status;

        updateAttendance = false;

        status.setVisibility(View.INVISIBLE);
        scanDevices.setText("Scan Devices");
        takeAttendance.setText("Mark Attendance");
        scanDevices.setEnabled(false);

        recyclerView = (RecyclerView) binding.recyclerView;

        takeAttendance.setEnabled(false);

        scanDevices.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if(mBluetoothAdapter==null)
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if(!mBluetoothAdapter.isDiscovering()){
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    getContext().registerReceiver(receiver, filter);
                    mBluetoothAdapter.startDiscovery();
                }
            }
        });

        takeAttendance.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                /// Upload Data
                if(!mBluetoothAdapter.isDiscovering()) {
                    if(updateAttendance){
                        for (ShowAttendanceModel showAttendanceModel:
                             takeAttendanceAdapter.getList()) {
                            if(showAttendanceModel.getStatus().equals("1")){
                                rollNos.add(showAttendanceModel.getAdmissionNumber());
                            }
                        }
                        Call<AttendanceStatusModel> call = RetrofitClient.getInstance().getMyApi().updateAttendance(classId,divisonId,"2022-06-05",subjectId,new JSONArray(Arrays.asList(rollNos)));
                        call.enqueue(new Callback<AttendanceStatusModel>() {
                            @Override
                            public void onResponse(Call<AttendanceStatusModel> call, Response<AttendanceStatusModel> response) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<AttendanceStatusModel> call, Throwable t) {
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Call<AttendanceStatusModel> call = RetrofitClient.getInstance().getMyApi().takeAttendance(classId, divisonId, "2022-06-05", subjectId, new JSONArray(Arrays.asList(bluetoothIds)));
                        call.enqueue(new Callback<AttendanceStatusModel>() {
                            @Override
                            public void onResponse(Call<AttendanceStatusModel> call, Response<AttendanceStatusModel> response) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Call<List<ShowAttendanceModel>> showAttendanceModelCall = RetrofitClient.getInstance().getMyApi().showAttendance(classId, divisonId, "2022-06-05", subjectId);
                                showAttendanceModelCall.enqueue(new Callback<List<ShowAttendanceModel>>() {
                                    @Override
                                    public void onResponse(Call<List<ShowAttendanceModel>> call, Response<List<ShowAttendanceModel>> response) {
                                        List<ShowAttendanceModel> showAttendanceModel = response.body();
                                        status.setVisibility(View.VISIBLE);
                                        takeAttendanceAdapter = new TakeAttendanceAdapter(showAttendanceModel);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        recyclerView.setAdapter(takeAttendanceAdapter);
                                    }

                                    @Override
                                    public void onFailure(Call<List<ShowAttendanceModel>> call, Throwable t) {
                                        Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<AttendanceStatusModel> call, Throwable t) {
                                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    Toast.makeText(getContext(), "Please Wait for Scanning to Complete !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Call<List<ClassDropdown>> call = RetrofitClient.getInstance().getMyApi().getClassDropDown(HomeActivity.teacherId);
        call.enqueue(new Callback<List<ClassDropdown>>() {
            @Override
            public void onResponse(Call<List<ClassDropdown>> call, Response<List<ClassDropdown>> response) {
                List<ClassDropdown> classDropdowns = response.body();
                ArrayList<String> c = new ArrayList<>();
                for (int i=0;i<classDropdowns.size();i++){
                    c.add(classDropdowns.get(i).getClassName());
                }
                ArrayAdapter classAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,c);
                spinnerClass.setAdapter(classAdapter);

                spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        classId = classDropdowns.get(i).getId();
                        fallbackData();
                        Call<List<DivisonDropDown>> call = RetrofitClient.getInstance().getMyApi().getDivisionDropDown(classDropdowns.get(i).getId());
                        call.enqueue(new Callback<List<DivisonDropDown>>() {
                            @Override
                            public void onResponse(Call<List<DivisonDropDown>> call, Response<List<DivisonDropDown>> response) {
                                List<DivisonDropDown> divisonDropDowns = response.body();
                                ArrayList<String> c = new ArrayList<>();
                                for (int i=0;i<divisonDropDowns.size();i++){
                                    c.add(divisonDropDowns.get(i).getClassArmName());
                                }
                                ArrayAdapter divisionAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,c);
                                spinnerDivision.setAdapter(divisionAdapter);

                                spinnerDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        divisonId = divisonDropDowns.get(i).getId();
                                        fallbackData();
                                        Call<List<SubjectModel>> call = RetrofitClient.getInstance().getMyApi().getSubjectDropDown(HomeActivity.teacherId);
                                        call.enqueue(new Callback<List<SubjectModel>>() {
                                            @Override
                                            public void onResponse(Call<List<SubjectModel>> call, Response<List<SubjectModel>> response) {
                                                List<SubjectModel> subjectModels = response.body();
                                                ArrayList<String> c = new ArrayList<>();
                                                for (int i=0;i<subjectModels.size();i++){
                                                    c.add(subjectModels.get(i).getSubjectName());
                                                }
                                                ArrayAdapter subjectAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,c);
                                                spinnerSubjects.setAdapter(subjectAdapter);
                                                scanDevices.setEnabled(true);
                                                spinnerSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        subjectId = subjectModels.get(i).getId();
                                                        fallbackData();
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(Call<List<SubjectModel>> call, Throwable t) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<List<DivisonDropDown>> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<ClassDropdown>> call, Throwable t) {

            }
        });
        return root;
    }

    @SuppressLint("MissingPermission")
    private void fallbackData(){
        binding.scanDevices.setText("Scan Devices");
        binding.takeAttendance.setText("Mark Attendance");
        binding.status.setVisibility(View.INVISIBLE);
        binding.takeAttendance.setEnabled(false);
        bluetoothIds.clear();
        if(takeAttendanceAdapter!=null) takeAttendanceAdapter.clearAll();
        if(countDownTimer!=null) countDownTimer.cancel();
        if(mBluetoothAdapter!=null && mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();
    }


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        public void onReceive(Context context, Intent intent) {
            Log.d("Hello","Initial");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                @SuppressLint("MissingPermission") String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if(bluetoothIds.isEmpty()){
                    countDownTimer = new CountDownTimer(20000, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            binding.scanDevices.setText("Rescan Devices");
                            binding.takeAttendance.setEnabled(true);
                            Toast.makeText(context, "Scanning Completed", Toast.LENGTH_SHORT).show();
                        }
                    };
                    countDownTimer.start();
                    Toast.makeText(context, "Started Scanning", Toast.LENGTH_SHORT).show();
                }
                if(!bluetoothIds.contains(deviceHardwareAddress)){
                    bluetoothIds.add(deviceHardwareAddress);
                }
            }

        }


    };

    @SuppressLint("MissingPermission")
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(receiver.isOrderedBroadcast()) {
            mBluetoothAdapter.cancelDiscovery();
            getContext().unregisterReceiver(receiver);
        }
        binding = null;
    }
}