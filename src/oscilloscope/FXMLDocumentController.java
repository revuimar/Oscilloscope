/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oscilloscope;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;

import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.FileChooser;

import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
/**
 *
 * @author Revison
 */
public class FXMLDocumentController implements Initializable {
    //input values
    FileChooser filechoice;
    DataGeneration dataX;
    DataGeneration dataY;
    DataGeneration dataXY;
    DataGeneration dataCurrent;
    DataGeneration dataDivided;
    
    @FXML private ScatterChart chartArea;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private TextField sampling;
    @FXML private TextField xPos;
    @FXML private TextField yPos;
    @FXML private TextField vDiv;
    @FXML private TextField timeDiv;
    @FXML private ComboBox source;
    
    @FXML public SwingNode samplingKnobJ;
    @FXML public SwingNode xPosKnobJ;
    @FXML public SwingNode yPosKnobJ;
    @FXML public SwingNode vDivKnobJ;
    @FXML public SwingNode timeDivKnobJ;
    private Knob samplingKnob;
    private Knob timeDivknob;
    private Knob vDivKnob;
    private Knob yPosknob;
    private Knob xPosknob;
    
    private void swapGraphData(DataGeneration sourceData){
        if(!chartArea.getData().isEmpty()){
            chartArea.getData().clear();
        }
        try{
            chartArea.getData().addAll(FXCollections.observableArrayList(sourceData.toObservable()));
        }catch(Exception e){
            System.out.println("data corrupted");
        }
    }
    @FXML
    public void selectXCSV(ActionEvent event){
        File file = filechoice.showOpenDialog(null);
        if (file == null) {
            System.out.println("Open command cancelled by user.");
        }
        else{
            dataX.loadCSV(file);
        }
    }
    @FXML
    protected void selectYCSV(ActionEvent event){
        //not very smart TODO refactor
        File file = filechoice.showOpenDialog(null);
        if (file == null) {
            System.out.println("Open command cancelled by user.");
        }
        else{
            dataY.loadCSV(file);
        }
    }
    @FXML
    protected void choiceActionPerformed(ActionEvent event){
        //not java style needs refactoring!
        //better!
        switch((String)source.getValue()){
            case "X":
                if(dataX.isEmpty()){
                    System.out.println("X channel data unavalible");
                    break;
                }
                dataCurrent = (dataX);
                //chartArea.getData().addAll(dataX.dataSeries);
                break;
            case "Y": 
                if(dataY.isEmpty()){
                    System.out.println("Y channel data unavalible");
                    break;
                }
                dataCurrent = (dataY);
                //chartArea.getData().addAll(dataX.dataSeries);
                break;
            case "XY": 
                if(dataY.isEmpty() && dataX.isEmpty()){
                    System.out.println("one or more channels unavalible");
                    break;
                }
                dataXY = DataGeneration.resolveLissajou(dataX,dataY);
                dataCurrent = (dataXY);
                break;
            default : dataCurrent = null;
        }
    }
    
    @FXML
    protected void handleStartClick(MouseEvent event){
        swapGraphData(dataCurrent);
        sampling.setText("1");
        vDiv.setText("1");
        timeDiv.setText("1");
        yPos.setText("0");
        xPos.setText("0");
        dataCurrent.calculateDataboundaries(xAxis,yAxis);
    }
    @FXML
    protected void enterYPos(ActionEvent event) {
        int value = Integer.valueOf(yPos.getText());
        switch (value) {
            case 1:
                dataCurrent = dataCurrent.moveYDirection(DataGeneration.RIGHT);
                break;
            case -1:
                dataCurrent = dataCurrent.moveYDirection(DataGeneration.LEFT);
                break;
            default:
                yPos.setText("0");
                return;
        }
        swapGraphData(dataCurrent);
        //dataCurrent.calculateDataboundaries(xAxis,yAxis);
        System.out.println(dataCurrent.get(0));
    }
    @FXML
    
    protected void enterXPos(ActionEvent event) {
        int value = Integer.valueOf(xPos.getText());
        switch (value) {
            case 1:
                dataCurrent = dataCurrent.moveXDirection(DataGeneration.RIGHT);
                break;
            case -1:
                dataCurrent = dataCurrent.moveXDirection(DataGeneration.LEFT);
                break;
            default:
                xPos.setText("0");
                return;
        }
        swapGraphData(dataCurrent);
        dataCurrent.calculateDataboundaries(xAxis,yAxis);
        System.out.println(dataCurrent.get(0));
    }
    @FXML
    protected void enterVDiv(ActionEvent event){
        Double value = Double.valueOf(vDiv.getText());
        if(value < 0.1 && value > 4) {
            vDiv.setText("0");
            return;
        }
        dataDivided = dataCurrent.voltageDivision(value);
        swapGraphData(dataDivided);
        //knob3.value = Double.valueOf(value);
    }
    @FXML
    protected void enterTimeDiv(ActionEvent event){
        int value = Integer.valueOf(timeDiv.getText());
        if(((String)source.getValue()).equals("XY")){
            dataDivided = dataCurrent.timeDivision(value,DataGeneration.LISSA);
        }
        else{
            dataDivided = dataCurrent.timeDivision(value,DataGeneration.NORMAL);
            dataDivided.calculateDataboundaries(xAxis, yAxis);
        }
        swapGraphData(dataDivided);
        //dataDivided.calculateDataboundaries(xAxis, yAxis);
        //knob2.value = Double.valueOf(value);
        //knob2.fireEvent();
    }
    @FXML
    protected void enterSamplingValue(ActionEvent event) {
        int value = 0;
        try{
            value = Integer.parseInt(sampling.getText());
        }catch(NumberFormatException e){
            sampling.setText("1");
            return;
        }
        switch(value){
            case 0:
            case 1:
                sampling.setText("1");
                swapGraphData(dataCurrent);
                dataCurrent.calculateDataboundaries(xAxis, yAxis);
                return;
        }
        dataDivided = dataCurrent.stripData(value);
        dataDivided.calculateDataboundaries(xAxis, yAxis);
        swapGraphData(dataDivided);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataX = new DataGeneration();
        dataY = new DataGeneration();
        dataXY = new DataGeneration();
        dataCurrent = new DataGeneration();
        yAxis.setTickLabelsVisible(false);
        yAxis.setOpacity(0);
        xAxis.setTickLabelsVisible(false);
        xAxis.setOpacity(0);
        chartArea.setLegendVisible(false);
        source.getItems().addAll("X","Y","XY");
        filechoice = new FileChooser();
        samplingKnob = new Knob(1,10,1);
        timeDivknob = new Knob(1,5,1);
        vDivKnob = new Knob(1,15,10);
        yPosknob = new Knob();
        xPosknob = new Knob();
        samplingKnobJ.setContent(samplingKnob);
        timeDivKnobJ.setContent(timeDivknob);
        vDivKnobJ.setContent(vDivKnob);
        yPosKnobJ.setContent(yPosknob);
        xPosKnobJ.setContent(xPosknob);
        
        
        samplingKnob.addKnobListener((KnobEvent event) -> {
            System.out.println("Value is " + event.getValue( ));
            if(event.getValue() == 0) sampling.setText("1");
            else if(event.getValue()>0)
                sampling.setText(Integer.toString(event.getValue()));
            else return;
            Platform.runLater(() -> {sampling.fireEvent(new ActionEvent());});
        });
        timeDivknob.addKnobListener((KnobEvent event) -> {
            System.out.println("Value is " + event.getValue( ));
            if(event.getValue() == 0) timeDiv.setText("1");
            else if(event.getValue() >0) 
                timeDiv.setText(Integer.toString(event.getValue()));
            else return;
            Platform.runLater(() -> {timeDiv.fireEvent(new ActionEvent());});
        });
        vDivKnob.addKnobListener((KnobEvent event) -> {
            System.out.println("Value is " + event.getValue( ));
            if(event.getValue() == 0)vDiv.setText("1");
            if(event.getValue()<=10)
                vDiv.setText(Double.toString((event.getValue())/10.0));
            else
                vDiv.setText(Integer.toString(event.getValue()-10));
            Platform.runLater(() -> {vDiv.fireEvent(new ActionEvent());});
        });
        yPosknob.addKnobListener((KnobEvent event) -> {
            System.out.println("Value is " + event.getValue());
            if(yPosknob.value>yPosknob.previousValue) yPos.setText("1");
            else if(yPosknob.value<yPosknob.previousValue) yPos.setText("-1");
            else yPos.setText("0");
            Platform.runLater(() -> {yPos.fireEvent(new ActionEvent());});//cross thread Swing -> FX
        });
        ((Knob)xPosKnobJ.getContent()).addKnobListener((KnobEvent event) -> {
            System.out.println("Value is " + event.getValue());
            if(xPosknob.value>xPosknob.previousValue) xPos.setText("1");
            else if(xPosknob.value<xPosknob.previousValue) xPos.setText("-1");
            else xPos.setText("0");
            Platform.runLater(() -> {xPos.fireEvent(new ActionEvent());});//cross thread Swing -> FX
        });
        
    }    
    
}
