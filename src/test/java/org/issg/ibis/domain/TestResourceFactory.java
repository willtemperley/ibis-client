package org.issg.ibis.domain;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestResourceFactory {
  
  public static File baseDir = new File("src/test");

  public static String getFileContents(String filename) throws FileNotFoundException, IOException {
    File f = new File(baseDir, filename);
    FileInputStream fis = new FileInputStream(f); 
    
    DataInputStream in = new DataInputStream(fis);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    
    String strLine;
    List<String> lines = new ArrayList<String>();
    
    // Read File Line By Line
    while ((strLine = br.readLine()) != null) {
      lines.add(strLine);
    }
    br.close();
    return Joiner.on("\n").join(lines);
  }
  
  public static InputStream getFileInputStream(String filename) throws FileNotFoundException {
    File f = new File(baseDir, filename);
    FileInputStream fis = new FileInputStream(f);
    return fis; 
  }

  public static Injector getInjector() {
    Injector injector = Guice.createInjector(new TestPersistModule());
    return injector;
  }
  
//  public static CSVReader getFileReader(String fileName) throws FileNotFoundException {
//    CSVReader reader = new CSVReader(new FileReader(new File(baseDir, fileName)));
//    return reader;
//  }

}
