/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oscilloscope;
import java.io.File;
import javafx.scene.chart.XYChart;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;

/**
 *
 * @author Revison
 */
public class DataGeneration extends ArrayList{
    public static final boolean RIGHT = true;
    public static final boolean LEFT = false;
    public static final boolean UP = true;
    public static final boolean DOWN = false;
    public static final int LISSA = 1;
    public static final int NORMAL = 0;
    public DataGeneration(){
        super();
    }
    public DataGeneration(ArrayList array){
        this.addAll(array);
    }
    public DataGeneration(ObservableList array){
        this.addAll(array);
    }
    
    void loadCSV(File file){
         List<List<Double>> inputList = new ArrayList<>();
        try{
          InputStream inputFS = new FileInputStream(file);
          BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
          // skip the header of the csv
          inputList = br.lines().skip(0).map(mapToItem).collect(Collectors.toList());
          try{
          br.close();
          }catch(IOException e){
              System.out.println("Error buffer not closed");
          }
        } catch (FileNotFoundException e) {
            System.out.println("Error file not found");
        }
        if(!this.isEmpty()){
            this.clear();
        }
        this.addAll(inputList);
        System.out.println(this.size());
        System.out.println(((List)this.get(0)).size());
    }
    private final Function<String, List<Double>> mapToItem = (line) -> {
        String[] stringValues = line.split(",");// a CSV has comma separated lines
        
        Double[] doubleValues = Arrays.stream(stringValues)
                        .map(Double::valueOf)
                        .toArray(Double[]::new);
        
        List<Double> item = (Arrays.asList(doubleValues));  
        //more initialization goes here
        return item;
    };
    public void calculateDataboundaries(NumberAxis x, NumberAxis y){
        Double maxY = 0.0;
        Double minY = 0.0;
        Double minX = 0.0;
        Double maxX = 0.0;
        boolean first = true;
        for (int i=0; i<((List)this.get(0)).size();i++){
            Double currentListYValue = (Double)((List)this.get(1)).get(i);
            Double currentListXValue = (Double)((List)this.get(0)).get(i);
            if(first){
                maxY = currentListYValue;
                minY = currentListYValue;
                minX = currentListXValue;
                maxX = currentListXValue;
                first = false;
                continue;
            }
            if(currentListYValue > maxY){
                maxY = currentListYValue;
            }
            else if(currentListYValue < minY){
                minY = currentListYValue;
            }
            if(currentListXValue > maxX){
                maxX = currentListXValue;
            }
            else if(currentListXValue < minX){
                minX = currentListXValue;
            }
        }
        x.setAutoRanging(false);
        x.setTickUnit(Math.abs(maxX-minX)/11);
        x.setLowerBound(minX);
        x.setUpperBound(maxX);
        
        y.setAutoRanging(false);
        y.setTickUnit(Math.abs(maxY-minY)/11);
        y.setLowerBound(minY);
        y.setUpperBound(maxY);
        System.out.println("Minx: "+ minX + " MaxX: "+ maxX+" MiY: "+minY+" MaxY: "+maxY);
    }
    public final DataGeneration stripData(int factor){
        DataGeneration newSeries = new DataGeneration();
        newSeries.add(0,new ArrayList<>());
        newSeries.add(1,new ArrayList<>());
        for(int i=0;i<((List)this.get(0)).size();i++){
            if(i % factor != 0){
                try{
                    ((List)newSeries.get(0)).add(((Double)((List)this.get(0)).get(i)));
                    ((List)newSeries.get(1)).add(((Double)((List)this.get(1)).get(i)));
                }catch(ArrayIndexOutOfBoundsException e){
                    return this;
                }
            }
        }
        return newSeries;
    }
    DataGeneration moveYDirection(boolean direction){
        DataGeneration newSeries = new DataGeneration();
        newSeries.add(0,(List)this.get(0));
        newSeries.add(1,new ArrayList<>());
        for(int i = 0; i<((List)this.get(1)).size();i++){
            ((List)newSeries.get(1)).add(((Double)((List)this.get(1)).get(i))
               + ((direction) ? (0.1):(-0.1)));
            
        }
        return newSeries;
    }
    
    DataGeneration moveXDirection(boolean direction, int mode){
        DataGeneration newSeries = new DataGeneration();
        Iterator<List> listX = ((List)this.get(0)).iterator();
        Iterator<List> listY = ((List)this.get(1)).iterator();
        if(mode == LISSA){
            try{
                newSeries.add(0,new ArrayList<>());
                newSeries.add(1,(List)this.get(1));
                for(int i = 0; i<((List)this.get(0)).size();i++){
                ((List)newSeries.get(0)).add(((Double)((List)this.get(0)).get(i))
                   + ((direction) ? (0.1):(-0.1)));
                }
            }catch(java.lang.ArrayIndexOutOfBoundsException e){
                return this;
            }
            return newSeries;
        }
        else{
            try{
        newSeries.add(0,new ArrayList<>());
        newSeries.add(1,new ArrayList<>());
        boolean first = true;
        int lastIndex = ((List)this.get(0)).size()-1;
        Double x = null, y = null;
        if(mode == LISSA){
            
        }
        if(direction == LEFT){
            x = 2 * ((Double)((List)this.get(0)).get(lastIndex)) - (Double)((List)this.get(0)).get(lastIndex-1);
            y = (Double)((List)this.get(1)).get(0);
        }
        else if(direction == RIGHT){
            x =  - ((Double)((List)this.get(0)).get(1)) + 2* ((Double)((List)this.get(0)).get(0));
            y = (Double)((List)this.get(1)).get(lastIndex);
        }
        
        while (listX.hasNext() && listY.hasNext()) {
            if(first){
                if(direction == LEFT){
                    listX.next();
                    listY.next();
                }
                else if(direction == RIGHT){
                    ((List)newSeries.get(0)).add(x);
                    ((List)newSeries.get(1)).add(y);
                }
                first = false;
            }
            ((List)newSeries.get(0)).add(listX.next());
            ((List)newSeries.get(1)).add(listY.next());
        }
        if(direction == RIGHT){
             ((List)newSeries.get(0)).remove(lastIndex+1);
             ((List)newSeries.get(1)).remove(lastIndex+1);
        }
        if(direction == LEFT){
            ((List)newSeries.get(0)).add(x);
            ((List)newSeries.get(1)).add(y);
        }
            }catch(java.lang.ArrayIndexOutOfBoundsException e){
                return this;
            }
        }
        return newSeries;
    }
    DataGeneration voltageDivision(Double value){
        DataGeneration newSeries = new DataGeneration();
        newSeries.add(0,(List)this.get(0));
        newSeries.add(1,new ArrayList<>());
        for(int i = 0; i<((List)this.get(1)).size();i++){
            ((List)newSeries.get(1)).add(((Double)((List)this.get(1)).get(i))/value);
        }
        return newSeries;
    }
    DataGeneration timeDivision(int value,int mode){
        DataGeneration newSeries = new DataGeneration();
        
        if(mode == LISSA){
            newSeries.add(0, new ArrayList<>());
            newSeries.add(1,(List)this.get(1));
            for(int i = 0; i<((List)this.get(1)).size();i++){
                ((List)newSeries.get(0)).add(((Double)((List)this.get(0)).get(i))/value);
            }
            return newSeries;
        }
        
        Double xInterval = ((Double)((List)this.get(0)).get(1)) - ((Double)((List)this.get(0)).get(0));
        int lastIndex = ((List)this.get(0)).size()-1;
        Double lastX = (Double)((List)this.get(0)).get(lastIndex);
        boolean first = true;
        
        newSeries.add(0,new ArrayList<>());
        newSeries.add(1,new ArrayList<>());
        for(int i = 0; i<value; i++){
            if(first){
                for(int k = 0; k<((List)this.get(0)).size();k++){
                ((List)newSeries.get(0)).add((Double)((List)this.get(0)).get(k));
                ((List)newSeries.get(1)).add((Double)((List)this.get(1)).get(k));
                }
                first = false;
                continue;
            }
            for(int j = 0; j<((List)this.get(0)).size();j++){
                ((List)newSeries.get(0)).add(lastX + xInterval * (j+1));
                ((List)newSeries.get(1)).add((Double)((List)this.get(1)).get(j));
            }
            lastX = (Double)((List)newSeries.get(0)).get(((List)newSeries.get(0)).size()-1);
        }
        return newSeries;
    }
    
    
    static DataGeneration resolveLissajou(DataGeneration dataX, DataGeneration dataY){
        DataGeneration lissajouCurve = new DataGeneration();
        
        Iterator<List> listX = ((List)dataX.get(1)).iterator();
        Iterator<List> listY = ((List)dataY.get(1)).iterator();
        lissajouCurve.add(0,new ArrayList<>());
        lissajouCurve.add(1,new ArrayList<>());
        while (listX.hasNext() && listY.hasNext()) {
            ((List)lissajouCurve.get(0)).add(listX.next());
            ((List)lissajouCurve.get(1)).add(listY.next());
        }
        return lissajouCurve;
    }
    ObservableList toObservable(){
        ObservableList<XYChart.Series<Double, Double>> list = FXCollections.observableArrayList();
        Series<Double, Double> series = new Series<Double, Double>();
        for(int i = 0; i<((List)this.get(0)).size();i++){
            try{
            series.getData().add(new XYChart.Data(((List)this.get(0)).get(i),((List)this.get(1)).get(i)));
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println("out of bounds!");
                return list;
            }
        }
        list.addAll(series);
        return list;
    }
}
