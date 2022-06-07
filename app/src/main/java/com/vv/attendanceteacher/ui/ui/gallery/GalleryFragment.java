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

import com.google.gson.JsonArray;
import com.vv.attendanceteacher.RetrofitClient;
import com.vv.attendanceteacher.databinding.FragmentGalleryBinding;
import com.vv.attendanceteacher.models.AttendanceStatusModel;
import com.vv.attendanceteacher.models.ClassDropdown;
import com.vv.attendanceteacher.models.DivisonDropDown;
import com.vv.attendanceteacher.models.SubjectModel;
import com.vv.attendanceteacher.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import org.json.JSONArray;
import java.util.Arrays;
import retrofit2.converter.gson.GsonConverterFactory;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    int i=1;
    String temp="";
    ArrayList<String> bluetoothIds = new ArrayList<>();
    Boolean scannedDevices = false;
    BluetoothAdapter mBluetoothAdapter;
    String classId;
    String divisonId;
    String subjectId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        scannedDevices = false;

        final Spinner spinnerClass = binding.spinnerClass;
        final Spinner spinnerDivision = binding.spinnerDivision;
        final Spinner spinnerSubjects = binding.spinnerSubject;
        final Button scanDevices = binding.scanDevices;
        final TextView b = binding.bluetoothId;
        scanDevices.setText("Scan Devices");
        scanDevices.setEnabled(false);

        scanDevices.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if(scannedDevices){
                    /// Upload Data
                    Call<AttendanceStatusModel> call = RetrofitClient.getInstance().getMyApi().takeAttendance(classId,divisonId,"2022-06-08",subjectId, new JSONArray(Arrays.asList(bluetoothIds)));
                    call.enqueue(new Callback<AttendanceStatusModel>() {
                        @Override
                        public void onResponse(Call<AttendanceStatusModel> call, Response<AttendanceStatusModel> response) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<AttendanceStatusModel> call, Throwable t) {
                            Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    getContext().registerReceiver(receiver, filter);
                    i = 1;
                    temp = "";
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    mBluetoothAdapter.startDiscovery();
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
                    new CountDownTimer(20000, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            scannedDevices = true;
                            binding.scanDevices.setText("Mark Attendance");
                            Toast.makeText(context, "Scanning Completed", Toast.LENGTH_SHORT).show();
                        }
                    }.start();
                    Toast.makeText(context, "Started Scanning", Toast.LENGTH_SHORT).show();
                }
                if(!bluetoothIds.contains(deviceHardwareAddress)){
                    temp = temp.concat("\n"+ i + " - " + deviceName + " " +deviceHardwareAddress);
                    i++;
                    binding.bluetoothId.setText(temp);
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