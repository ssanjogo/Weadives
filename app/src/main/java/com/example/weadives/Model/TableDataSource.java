package com.example.weadives.Model;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.List;

public class TableDataSource {

    Context context;
    private String nameFile;
    List<String[]> data;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public TableDataSource(Context context, String nameFile) {
        this.context = context;
        this.nameFile = nameFile;
        File initialFile = new File (this.nameFile);
        InputStream in = null;
        try {
            in = new FileInputStream(initialFile);
        } catch (IOException e) {
            //System.out.println("Error opening csv file: " + e.getMessage());
            e.printStackTrace();
        }
        //System.out.println(in);
        InputStreamReader streamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        //System.out.println(streamReader);
        try (CSVReader reader = new CSVReader(streamReader)) {
            data = reader.readAll();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRowsCount(){
        return data.size();
    }
    public int getColumnsCount(){
        return data.get(0).length;
    }
    public String getFirstHeaderData(){
        return data.get(0)[0];
    }

    public String getRowHeaderData(int index){
        return data.get(index)[0];
    }

    public String getColumnHeaderData(int index){
        return data.get(0)[index];
    }

    public String getItemData(int rowIndex, int columnIndex){
        return data.get(rowIndex)[columnIndex];
    }
}
