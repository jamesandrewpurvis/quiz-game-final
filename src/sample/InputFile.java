package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class InputFile 
{
	//fields
	private String mFilePath = null;
	private File mFile = null;
	public Stack<Object> mQuestions = null;
	public Stack<Object> mAnswers = null;

	//our constructor for our inputfile class
	public InputFile(String filepath)
	{
		//These two stacks hold our information, we can pop them right off the top
		mAnswers = new Stack();
		mQuestions = new Stack();

		//if our filepath isn't empty we are good to go
		if (filepath.isEmpty() != true)
		{
		  mFilePath = filepath;
		}
		
		try //lets try to open the file
		{
			mFile = new File(mFilePath);
		}
		catch(Exception e) //we couldn't find it, let the user know
		{
			System.out.println(e.getMessage());
		}
	}

	//this method let's us load from our input file
	public void loadFromFile()
	{
		//our pointer/count
		int count = 1;

		//error handling
		try
		{
			//scanner opens our file for us
			Scanner mScanner = new Scanner(mFile);

			//while we have more lines to go through
			while(mScanner.hasNext())
			{
				//grab a line
				String mLine = mScanner.nextLine();

				//if our count is even it's a answer, else a question
				if (count % 2 == 0)
				{
					//use split, so we can split after the space
					String mAnswer1 = mLine.split(" ")[0];
					String mAnswer2 = mLine.split(" ")[1];
					String mAnswer3 = mLine.split(" ")[2];
					String mAnswer4 = mLine.split(" ")[3];
					mAnswers.push(mAnswer1); //push to our stack
					mAnswers.push(mAnswer2); //push to our stack
					mAnswers.push(mAnswer3); //push to our stack
					mAnswers.push(mAnswer4); //push to our stack
				}
				else //this is a question
				{
					//use split so we can split the info after the space
					String mQuestion1 = mLine.split(" ")[0];
					String mQuestion2 = mLine.split(" ")[1];
					//push to the stack
					mQuestions.push(mQuestion1);
					mQuestions.push(mQuestion2);
				}

				//increment our pointer
				count++;
			}

		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}



}
