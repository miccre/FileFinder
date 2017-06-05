package com.dominikgames.filefinder.filefinder;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;


public class MainActivity extends AppCompatActivity {

    private LayoutInflater mInflater;
    Button buttonOpenDialog;
    Button buttonUp;
    Button vyber;
    TextView textFolder;
    TextView urlAdress;
    TextView adresar;
    SeekBar seekBar;

    int countFiles;
    EditText count;
    Heap heap;
    Activity mActivity;
    static final int CUSTOM_DIALOG_ID = 0;
    ListView dialog_ListView;


    File root, root2;
    File curFolder, curFolder2;
    String path;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayList<String> paths = new ArrayList<>();

    ListView list;
    ArrayAdapter<String> adapter;
    ArrayList<Data> fileList2 = new ArrayList<>();
    private List<String> fileList = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivity = this;
        mInflater = LayoutInflater.from(this);
        setTitle("");
        list = (ListView) findViewById(R.id.list);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        list.setAdapter(adapter);

        count = (EditText) findViewById(R.id.etCount);
        adresar = (TextView) findViewById(R.id.adresar);
        urlAdress = (TextView) findViewById(R.id.urlAdress);
        buttonOpenDialog = (Button) findViewById(R.id.opendialog);
        seekBar = (SeekBar) findViewById(R.id.seekBar2);

        root2 = new File("/storage");

        String name = NameOfCard(root2);

        if(name.equals(""))
            root2 = root;

        root2 = new File("/storage/" + name);
        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        curFolder = root;
        curFolder2 = root2;

    }


    public void addItems(String url) {
        listItems.add(url);
        adapter.notifyDataSetChanged();
    }

    public void filesList(View v) {

        try {
            countFiles = Integer.parseInt(count.getText().toString()) + 1;
        }
        catch (NumberFormatException e2) {
            Toast.makeText(this, "Zadaj počet súborov!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(listItems.isEmpty()){
            Toast.makeText(this, "Pridaj adresáre!!", Toast.LENGTH_SHORT).show();
            return;
        }
        int sum = 0;
        heap = new Heap(countFiles);
        heap.expandArray();
        int count = countFiles;
        while(!paths.isEmpty()) {
            String adress = paths.get(0);
            paths.remove(0);
            File directory = new File(adress);
            File[] files = directory.listFiles();
            sum += files.length;
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    Log.i("Abs path ", files[i].getAbsolutePath());
                    paths.add(files[i].getAbsolutePath());
                }else {
                    final String url= files[i].getAbsolutePath();
                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(100);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable(){
                                public void run() {
                                    urlAdress.setText(url);
                                }});
                        }
                    };
                    new Thread(runnable).start();

                    FileWithUrl f = new FileWithUrl(files[i].getAbsolutePath(), files[i].getName(), files[i].length());
                    if(count-- > 1)
                        heap.insert(f);
                    else{
                        if(f.size > heap.heap[1].size){
                            heap.removeMin();
                            heap.insert(f);
                        }
                    }

                }
            }
        }
        if(sum < countFiles)
            countFiles = sum;

        ArrayList<SearchResult> searchResults = GetSearchResults();
        list.setAdapter(new MyCustomBaseAdapter(this, searchResults));

        listItems.clear();
        list.setOnItemClickListener(null);
        adresar.setText("Nájdené súbory(" + (countFiles-1) +"):");
    }

    Dialog dialog = null;
    public void showDialog2(View v) {

        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (storage != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Povol aplikácii pristup do pamäte!!", Toast.LENGTH_SHORT).show();
            return;
        }
        int seekValue = seekBar.getProgress();
        String x = "Value: " + Integer.toString(seekValue);
        Log.i("Seek", x);
        curFolder = root;
        if(seekValue == 0)
            curFolder = root2;

        adresar.setText("Zvolené adresáre:");

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {
                paths.remove(position);
                adapter.remove(listItems.get(position));
                adapter.notifyDataSetChanged();
            }
        });

        switch (0) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Custom Dialog");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                vyber = (Button) dialog.findViewById(R.id.ok2);
                buttonUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                });
                vyber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        urlAdress.setText(path);
                        paths.add(path);
                        addItems(path);
                        //filesList();
                    }
                });
                dialog_ListView = (ListView) dialog.findViewById(R.id.dialoglist);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File selected = new File(fileList.get(position));
                        if(selected.isDirectory()) {
                            ListDir(selected);
                        } else {
                            Toast.makeText(MainActivity.this, selected.toString() + " selected",
                                    Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });
                ListDir(curFolder);
                dialog.show();
                break;
        }
    }

    void ListDir(File f) {
        if(f.getPath().equals("/storage/emulated/0")) {
            buttonUp.setEnabled(false);
        } else {
            buttonUp.setEnabled(true);
        }

        curFolder = f;
        path = f.getPath().toString();

        textFolder.setText(f.getPath());

        File[] files = f.listFiles();
        fileList.clear();
        for(File file : files) {
            fileList.add(file.getPath());
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, fileList);
        dialog_ListView.setAdapter(directoryList);
    }

    /*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            // Use the provided utility method to parse the result
            List<Uri> files = Utils.getSelectedFilesFromResult(data);
            for (Uri uri: files) {
                Files file = Utils.getFileForUri(uri);
                // Do something with the result...
            }
        }
    }
    */

    private String NameOfCard(File f){
        File[] files = f.listFiles();
        String name = "";
        for(File file : files) {
            if(file.getName().equals("emulated"));
            else if(file.getName().equals("self"));
            else
                name = file.getName();
        }
        return name;
    }

    private ArrayList<SearchResult> GetSearchResults(){
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();

        for (int i = 1; i<countFiles; i++){
            SearchResult sr1 = new SearchResult();
            sr1.setName(heap.heap[i].name);
            sr1.setUrl(heap.heap[i].url);
            sr1.setSize(heap.heap[i].size);
            results.add(sr1);
        }

        return results;
    }
}