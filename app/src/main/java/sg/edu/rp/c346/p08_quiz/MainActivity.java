package sg.edu.rp.c346.p08_quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {
    String folderLocation;
    Button btnsave, btnread, btnmap;
    EditText et;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnsave = findViewById(R.id.btnSave);
        et = findViewById(R.id.et);
        btnread = findViewById(R.id.btnread);
        tv = findViewById(R.id.tv);
        btnmap = findViewById(R.id.btnMap);

        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent o = new Intent(MainActivity.this, secondActivity.class);
                startActivity(o);
            }
        });


        int permissionCheck = PermissionChecker.checkSelfPermission
                (MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return;
        }
        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Quiz";

        if (checkPermission() == true) {
            File folder = new File(folderLocation);
            if (folder.exists() == false) {
                boolean result = folder.mkdir();
                if (result == true) {
                    Log.d("File Read/Write", "Folder created");
                }
            }
        } else {
            String msg = "Permission not granted to retrieve location info";
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        }
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coordinates = et.getText().toString();
                try {
                    String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Quiz";
                    File targetFile = new File(folderLocation, "quiz.txt");
                    FileWriter writer = new FileWriter(targetFile, false);
                    writer.write(coordinates);
                    writer.flush();
                    writer.close();
                    Toast.makeText(MainActivity.this, "File Created", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to write", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

        });
        btnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Quiz";
                File targetFile = new File(folderLocation, "quiz.txt");
                if (targetFile.exists() == true) {
                    String data = "";
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null) {
                            data += line + "\n";
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Failed to read", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    Log.d("content", data);
                    tv.setText(data);
                }
            }
        });


            }




        private boolean checkPermission(){
            int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                    MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                    MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                    || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
    }

