package com.vv.attendanceteacher.ui.ui.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.vv.attendanceteacher.R;
import com.vv.attendanceteacher.RetrofitClient;
import com.vv.attendanceteacher.databinding.FragmentSlideshowBinding;
import com.vv.attendanceteacher.databinding.FragmentUpdateStudentIdBinding;
import com.vv.attendanceteacher.models.AttendanceStatusModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateStudentIdFragment extends Fragment {


    private FragmentUpdateStudentIdBinding binding;


    Button scan,upload;
    ListView listView;
    WifiManager wifiManager;
    ArrayList<String> arrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUpdateStudentIdBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        scan = root.findViewById(R.id.button);
        listView = root.findViewById(R.id.listView);
        upload = root.findViewById(R.id.button2);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!wifiManager.isWifiEnabled()){
                    wifiManager.setWifiEnabled(true);
                }
                if(!wifiManager.startScan()){
                    scanFailure();
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayList.size()!=0) {
                    for (String str : arrayList) {
                        String[] temp = str.split("-");
                        Call<AttendanceStatusModel> call = RetrofitClient.getInstance().getMyApi().updateWifi(temp[0].trim(), temp[1].trim().toUpperCase(Locale.ROOT));
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
                }else{
                    Toast.makeText(getContext(), "Scan for devices First", Toast.LENGTH_SHORT).show();
                }
            }
        });


        wifiManager = (WifiManager)
                getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess();
                } else {
                    // scan failure handling
                    scanFailure();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getContext().getApplicationContext().registerReceiver(wifiScanReceiver, intentFilter);

        return root;
    }

    private void scanSuccess() {
        arrayList.clear();
        List<ScanResult> results = wifiManager.getScanResults();
        Toast.makeText(getContext(), "Results : " + results.size(), Toast.LENGTH_SHORT).show();
        for(ScanResult scanResult : results){
            arrayList.add(scanResult.SSID +"-" + scanResult.BSSID);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
    }

    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        arrayList.clear();
        List<ScanResult> results = wifiManager.getScanResults();
        Toast.makeText(getContext(), "Results : " + results.size(), Toast.LENGTH_SHORT).show();
        for(ScanResult scanResult : results){
            arrayList.add(scanResult.SSID +"-" + scanResult.BSSID);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}