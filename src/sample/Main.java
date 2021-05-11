package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.sql.ResultSet;
import java.util.*;

public class Main extends Application {

    //fields
    private String mPlayerName = null;
    private InputFile mInputFile = null;
    private Controller mController = null;
    public static int mCounter = 5;
    private static int mPlayerPoints = 0;
    private boolean mGameOver = false;
    private int mQuestionCounter = 0;
    public static int mEndCount = 0;
    private static DatabaseManager mDatabaseManager = null;
    private int mTotalCount = 0;


    @Override
    public void start(Stage primaryStage) throws Exception {
        //new instance of our databaseManager object
        mDatabaseManager = new DatabaseManager("127.0.0.1", 3306, "root", "newnew99");

        //mDatabaseManager.createTable("CREATE TABLE player_scores("
               // + "ID INT PRIMARY KEY AUTO_INCREMENT, "
              //  + "player_name VARCHAR (255) NOT NULL, "
              ///  + "player_score INTEGER NOT NULL, "
              //  + "total_time INTEGER) ");

        //load our xml file
        FXMLLoader mLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = mLoader.load();
       //used to grab our controller so we can link our controller with the xml of our project
        mController = mLoader.getController();
        //set the title of our application
        primaryStage.setTitle("Trivia Game");
        //set the scene height
        primaryStage.setScene(new Scene(root, 600, 600));
        //create a new question dialog and ask the user their name
        inputQuestion("What is your name?", "Trivia Alert!");
        //set the label's text to the player name
        mController.mName.setText(mPlayerName);
        //Create a new inputFile object instance, using our user directory for the project folder /inputfile.txt
        CodeSource codeSource = Launcher.class.getProtectionDomain().getCodeSource();
        File jarFile = new File(codeSource.getLocation().toURI().getPath());
        String jarDir = jarFile.getParentFile().getPath();
        mInputFile = new InputFile(jarDir + "/inputfile.txt");
        //load our input file
        mInputFile.loadFromFile();
        //do an intiaload and change the buttons and labels to their corredsponding input file values
        intialLoad();
        //show our stage
        primaryStage.show();
        //Create a new alert, and let the user after they hit ok, the game will start!
        Alert mGameStarting = new Alert(Alert.AlertType.INFORMATION);
        mGameStarting.setTitle("Game Starting!");
        mGameStarting.setHeaderText("The game is starting");
        mGameStarting.setContentText("The game will start when you press ok, so be ready!");
        mGameStarting.showAndWait();
        //In javaFX our threads have to be on the same thread as the application GUI, so I used a timer to handle background tasks
        Timer mBackgroundTask = new Timer();
        mBackgroundTask.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
              Platform.runLater(new Runnable() {
                  @Override
                  public void run() {
                      //our timer will run intiaload every 5 seconds, that allows our buttons and labels to change text
                      intialLoad();
                  }
              });
            }
        }, 5000, 5000);

        //timer that handles the countdown clock
        Timer mCountdownClock = new Timer();
        mBackgroundTask.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //this method will print our countdown clock
                    printCountdownClock(mCounter);
                   //subtrack one from our countdown clock
                    mCounter--;
                    //if our countdown clock hits zero, we reset it to 5
                    if (mCounter == 0)
                    {
                        //show our answered question in the timestamp
                        setTimeStamp();
                        //disable our buttons
                        mController.mAnswer1.setDisable(true);
                        mController.mAnswer2.setDisable(true);
                        mController.mAnswer3.setDisable(true);
                        mController.mAnswer4.setDisable(true);
                        //reset to five
                        mCounter = 5;
                    }
                }
            });
        }
    }, 0, 1000);
    };


    public static void main(String[] args) {
        launch(args);
    }

    //this method will generate a TextInputDialog
    public void inputQuestion(String question, String title)
    {
        //creates a new TextInputDialog
        TextInputDialog mQuestion = new TextInputDialog("Enter your name here: ");
        //sets the header and content text
        mQuestion.setHeaderText(title);
        mQuestion.setContentText(question);

        //we use this to grab the result of DialogBox
        Optional<String> mResult = mQuestion.showAndWait();

        //if the result is present aka it exists
        if (mResult.isPresent() == true)
        {
            //we will get the name!
            String mName = mResult.get();

            //let's make sure its a valid name
            if (mName.length() > 0 && checkName(mName) == true)
            {
                //set the name
                mPlayerName = mName;
            }
            else //not a valid name, didn't pass our checks, we use a recrusive method to keep the player in a loop until they give us a name
            {
                inputQuestion("What is your name? ", "Trivia Game Alert!");
            }
        }
        else //not a valid name
        {
            inputQuestion("What is your name? ", "Trivia Game Alert!");
        }
    }

    //this method checks user input, and ensures that the name is valid returns true or false
    public boolean checkName(String name)
    {
        //convert our name string to a charArray
        char[] mNameArray = name.toCharArray();

        //loop through each char and check
        for(int a = 0; a < name.length(); a++)
        {
            //check for blank spaces
            if (mNameArray[a] != ' ')
            {
                return true;
            }
        }

        return false;
    }

    //our method which allows us to refresh our GUI with new questions and answers
    public void intialLoad()
    {
        //we have a gameOver boolean that let's us know if the game is over, if it is we don't do anything just return
        if (mGameOver ==  true)
        {
            return;
        }

        //we make sure our stack still has questions and answers, if so we are good!
        if (mInputFile.mQuestions.empty() != true && mInputFile.mAnswers.empty() != true)
        {
            //lets alter our FX GUI and pop something from the top of the stack
            mController.mObject1.setText(mInputFile.mQuestions.pop().toString());
            mController.mObject2.setText(mInputFile.mQuestions.pop().toString());
            mController.mAnswer1.setText(mInputFile.mAnswers.pop().toString());
            mController.mAnswer2.setText(mInputFile.mAnswers.pop().toString());
            mController.mAnswer3.setText(mInputFile.mAnswers.pop().toString());
            mController.mAnswer4.setText(mInputFile.mAnswers.pop().toString());
        }
        else //the game is over
        {
            //we set game over to true
            mGameOver = true;
            //time for a new Alert letting the user know the game is over.
            Alert mGameEnding = new Alert(Alert.AlertType.INFORMATION);
            mGameEnding.setTitle("Game Ending!");
            mGameEnding.setHeaderText("The game is ending!");
            mGameEnding.setContentText("The game is over your score is " + mPlayerPoints);
            mGameEnding.showAndWait();
            //insert into the database the users score
            mDatabaseManager.INSERT("INSERT INTO player_scores(id, player_name, player_score, total_time) " + " VALUES('" + "0 " + "'," + "'" + mPlayerName + "', '" + mPlayerPoints + "', '" + mTotalCount + "')");
        }



    }

    public void printCountdownClock(int count)
    {
        //if the game is over we don't need to print a countdown clock
        if (mGameOver == true)
        {
            return;
        }

        //lets go for it
        try
        {
            //if our count is 5, we just started a new question
            if (count == 5)
            {
                //re-enable our buttons
                mController.mAnswer1.setDisable(false);
                mController.mAnswer2.setDisable(false);
                mController.mAnswer3.setDisable(false);
                mController.mAnswer4.setDisable(false);

            }
            //update our GUI
            mController.mCountdown.setText(String.valueOf(count));
        }
        //exception, let the user know.
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //this method checks our answer to ensure it's correct, if so we get a point.
    public static void checkAnswer(Label points, Button b, Label object1, Label object2)
    {
        //if we parse our object and it's greater than zero that means it's an integer!
        if (tryInt(object1.getText()) != 0 && tryInt(object2.getText()) != 0)
        {
            int mIntObject1 = tryInt(object1.getText());
            int mIntObject2 = tryInt(object2.getText());
            int mAnswer = (mIntObject1 + mIntObject2);

            //if our answer equals the button text
            if (String.valueOf(mAnswer).equals(b.getText()))
            {
                //give the user points
                mPlayerPoints++;
                //set the points GUI
                points.setText(String.valueOf(mPlayerPoints));
                //return
                return;
            }
        }
        //if it equals zero, this is probably a string.
        if(tryInt(object1.getText()) == 0 && tryInt(object2.getText()) == 0)
        {
            String mAnswer = object1.getText() + object2.getText();
            String mAnswer2 = object2.getText() + object1.getText();

            //if our Answer equals button's text
            if (b.getText().equals(mAnswer) || b.getText().equals(mAnswer2))
            {
                //give the player points
                mPlayerPoints++;
                //set the gui points
                points.setText(String.valueOf(mPlayerPoints));
                //return
                return;
            }
        }

        //try to parse and see if the numbers are a double
        double mDoubleObject = tryDouble(object1.getText());
        double mDoubleObject2 = tryDouble(object2.getText());

        //they're bigger than 0.0, which means they're either a double or float
        if (mDoubleObject > 0.0 && mDoubleObject2 > 0.0) {
           //if the label contains F it's a float
            if (object1.getText().toLowerCase().contains("f") == true || object2.getText().toLowerCase().contains("f") == true) {
                double mAnswer = (mDoubleObject + mDoubleObject2);
                String mAnswerString = String.valueOf(mAnswer) + "f";
                //if the button text equals the answer, we found the float
                if (b.getText().equals(mAnswerString)) {
                    //add points
                    mPlayerPoints++;
                    //set the gui points
                    points.setText(String.valueOf(mPlayerPoints));
                    return;
                }
            }
            else //it's just a regular double
            {
                double mAnswer = mDoubleObject + mDoubleObject2;

               //our button is the right answer
               if (b.getText().equals(String.valueOf(mAnswer)))
               {
                   //set the points
                   mPlayerPoints++;
                   //set the gui points
                   points.setText(String.valueOf(mPlayerPoints));
                   return;
               }
            }
        }

    }

    //this method is used to parse a string -> int
    public static int tryInt(String obj)
    {
        try {
           int mAnswer = Integer.parseInt(obj);
            return mAnswer;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    //this method is used to parse a string -> double
    public static double tryDouble(String obj)
    {
        try {
           double mAnswer = Double.parseDouble(obj);
           return mAnswer;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    //this method is used to parse a string -> float
    public static float tryFloat(String obj) {
        try {
            Float mAnswer = Float.parseFloat(obj);
            return mAnswer;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    //this method helps us with our timestamp area
    public void setTimeStamp()
    {
        //as long as the questionCounter is less than 10, we should update the timestamp (the game is still going)
        if (mQuestionCounter < 10) {
            mQuestionCounter++; //increment our question counter
            //grab the current stamp
            String mCurrentStamp = mController.mRecap.getText();
            //we use the difference between our endcount (what the counter clock was at when we hit the button and the max (5) to find the seconds)
            int mSecondCount = 5 - mEndCount;
            //we add our currentstamp and our new stamp together
            String newStamp = mCurrentStamp + " Question: " + mQuestionCounter + " " + "Time Elapsed: " + mSecondCount + " Seconds \n";
            //update our totalcount for later, when we insert our result into the DB
            mTotalCount = mTotalCount + mSecondCount;
            //call our controller and set the text
            mController.mRecap.setText(newStamp);
        }
    }

    //this method will help us print a report
    public static void printReport()
    {
        //SELECT all records from the player_scores table
        try {
            ResultSet mResult = mDatabaseManager.SELECT("SELECT * FROM PLAYER_SCORES");
            //create a new File/Printwriter and create a new file called Trivia Game Report.txt in our user directory
            FileWriter mWriter = new FileWriter(System.getProperty("user.dir")  + "report.txt");
            PrintWriter mWrite = new PrintWriter(mWriter);

            //write the file
            mWrite.write("##########\n");
            mWrite.write("HIGH SCORES\n");
            mWrite.write("############\n");
            mWrite.write("\n");
            mWrite.write("\n");

            //while the resultset is not empty
            while(mResult.next())
            {
                //get the data from the DB
                int mID = mResult.getInt(1);
                String mName = mResult.getString(2);
                int mScore = mResult.getInt(3);
                int mTotal = mResult.getInt(4);

                //let's write it to our file
                try {
                    mWrite.write("ID: " + mID  + "\n");
                    mWrite.write("Name: " + mName + "\n");
                    mWrite.write("Score: " + mScore + "\n");
                    mWrite.write("Total: " + mTotal + "\n");
                    mWrite.write("\n");
                    mWrite.write("\n");
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }



            }

            //close our writer
            mWrite.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }


}
