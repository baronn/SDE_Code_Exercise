package com.example.sde;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sde.databinding.ActivityMainBinding;
import com.example.sde.presentation.DriverAdapter;
import com.example.sde.presentation.MainViewModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final int FILE_REQUEST_CODE = 234;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private DriverAdapter driverAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setEvents();

        viewModel.getDrivers().observe(this, drivers -> {
            driverAdapter.setValues(drivers);
            driverAdapter.notifyDataSetChanged();
        });

        viewModel.getProposal().observe(this, band->{
            if (band){
                AlertGenerationUtils.showConfirmAlert(
                        getString(R.string.route_proposal),
                        String.format(getString(R.string.proposal_text), viewModel.getProposedShipment().getDriver(), viewModel.getProposedShipment().getShipment()),
                        this,
                        (dialogL, whichL) -> viewModel.proposalTaken(),
                        (dialogCIN, whichCIN) -> viewModel.proposalDecline(),
                        getString(R.string.take),
                        getString(R.string.reject)
                );
            }
        });

        viewModel.getUpdatePosition().observe(this, position -> driverAdapter.updateStatus(position));

        viewModel.getMsjDialog().observe(this, msj -> AlertGenerationUtils.showAlert(getString(R.string.notification),msj,this));

        checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            loadInformation(grantResults.length > 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == FILE_REQUEST_CODE){
            if( resultCode == Activity.RESULT_OK && data != null){
                viewModel.getFileData(readTextFromFile(data.getData(),1));
            }
            else{
                viewModel.getFileData(readTextFromFile(null,2));
            }
        }
    }

    private void setEvents() {
        setRecycler();

        binding.bntSelected.setOnClickListener(v -> {
            driverAdapter.clear();
            binding.rvDrivers.setAdapter(null);
            driverAdapter = null;
            setRecycler();
            loadInformation(true);
        });
    }

    private void setRecycler(){
        driverAdapter = new DriverAdapter(new ArrayList<>());
        binding.rvDrivers.setLayoutManager( new LinearLayoutManager(this));
        binding.rvDrivers.setAdapter(driverAdapter);

        driverAdapter.setOnItemClickListener(v -> {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            viewModel.proposeShipment(holder.getAdapterPosition());
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,this.getResources().getConfiguration().orientation);
        binding.rvDrivers.addItemDecoration(dividerItemDecoration);
    }

    private void checkPermission()  {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){
            loadInformation(true);
        }else{
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    private void loadInformation(boolean permissionGranted) {
        if(permissionGranted){
            Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFileIntent.setType("application/json");
            // Only return URIs that can be opened with ContentResolver
            chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

            chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
            startActivityForResult(chooseFileIntent, FILE_REQUEST_CODE);
        }
    }

    private String readTextFromFile(Uri uri, int option) {
        StringBuilder stringBuilder = new StringBuilder();
        try{
            InputStream stream = option == 1 ? getContentResolver().openInputStream(uri) : getAssets().open("data.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            stream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


}