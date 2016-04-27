package gui;

import diverse.Log;
import diverse.circleCalc.Vector2;
import diverse.koordinat.Koordinat;
import diverse.koordinat.OpgaveRum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class WallValuesController {

	int xLength;
	int yWidth;
	
    @FXML
    private Button save;

    
    // TextFields er til indtastning af vægkoordinater
    @FXML
    private TextField W02_00;

    @FXML
    private TextField W03_01;

    @FXML
    private TextField W02_01;

    @FXML
    private TextField W03_02;

    @FXML
    private TextArea W01_00;

    @FXML
    private TextField W03_00;

    @FXML
    private TextField W00_01;

    @FXML
    private TextField W01_02;

    @FXML
    private TextField W00_00;

    @FXML
    private TextField W01_01;

    @FXML
    private TextField W01_04;

    @FXML
    private TextField W02_02;

    @FXML
    private TextField W03_03;

    @FXML
    private TextField W00_02;

    @FXML
    private TextField W01_03;

    @FXML
    private TextField W03_04;
    
    @FXML
    private TextField length;
    
    @FXML
    private TextField width;

    @FXML
    void saveMarkings(ActionEvent event) {
    	yWidth = Integer.parseInt(width.getText());
    	xLength= Integer.parseInt(length.getText());
    	System.out.println("Gemmer");
         String[] markings = { W00_00.getText(), W00_01.getText(), W00_02.getText(),
        		 			   W01_00.getText(), W01_01.getText(),W01_02.getText(), W01_03.getText(), W01_04.getText(),
        		 			   W02_00.getText(), W02_01.getText(), W02_02.getText(),
        		 			   W03_00.getText(), W03_03.getText(),W03_02.getText(), W03_03.getText(), W03_04.getText()};
         Log.writeWallMarking(markings);
        init();
    }
    
    // Ment som test kun, der skal tages stilling
    // til hvordan parametret initialiseres
    public void init(){
    	OpgaveRum or = new OpgaveRum(xLength, yWidth);
    	or.setMarkings();
    	or.writeMarkingsToLog();
    	Vector2[] koordinat = or.getMultiMarkings("W00_00");
    	for (int i = 0; i < koordinat.length; i++) {
			System.out.println(koordinat[i].toString());
    	}
    }

}
