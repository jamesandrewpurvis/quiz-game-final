package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
 //our GUI controls
    @FXML
   public Label mName;
    public Label mObject1;
    public Label mObject2;
    public Button mAnswer1;
    public Button mAnswer2;
    public Button mAnswer3;
    public Button mAnswer4;
    public Label mCountdown;
    public Label mPoints;
    public Label mRecap;
    public Button mReport;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
     //eventhandlers that handle our button presses, every time we hit an answer the buttons are disabled.
        mAnswer1.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent actionEvent) {
          Main.mEndCount = Main.mCounter;
          Main.checkAnswer(mPoints, mAnswer1, mObject1, mObject2);
          mAnswer1.setDisable(true);
          mAnswer2.setDisable(true);
          mAnswer3.setDisable(true);
          mAnswer4.setDisable(true);
         }
        });
     //eventhandlers that handle our button presses, every time we hit an answer the buttons are disabled.
     mAnswer2.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
       Main.mEndCount = Main.mCounter;
       Main.checkAnswer(mPoints, mAnswer2, mObject1, mObject2);
       mAnswer1.setDisable(true);
       mAnswer2.setDisable(true);
       mAnswer3.setDisable(true);
       mAnswer4.setDisable(true);
      }
     });
     //eventhandlers that handle our button presses, every time we hit an answer the buttons are disabled.
     mAnswer3.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
       Main.mEndCount = Main.mCounter;
       Main.checkAnswer(mPoints, mAnswer3, mObject1, mObject2);
       mAnswer1.setDisable(true);
       mAnswer2.setDisable(true);
       mAnswer3.setDisable(true);
       mAnswer4.setDisable(true);
      }
     });
     //eventhandlers that handle our button presses, every time we hit an answer the buttons are disabled.
     mAnswer4.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
       Main.mEndCount = Main.mCounter;
       Main.checkAnswer(mPoints, mAnswer4, mObject1, mObject2);
       mAnswer1.setDisable(true);
       mAnswer2.setDisable(true);
       mAnswer3.setDisable(true);
       mAnswer4.setDisable(true);
      }
     });
     //eventhandlers that handle our button presses, every time we hit an answer the buttons are disabled.
     mReport.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
       Main.printReport();
      }
     });
    }

}
