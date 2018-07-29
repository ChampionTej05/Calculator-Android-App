package com.example.champion.calculatorapp;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Calculator_Full extends AppCompatActivity {

    // IDs of all the numeric buttons

     int[] numericButtons = {R.id.Button0, R.id.Button1, R.id.Button2, R.id.Button3, R.id.Button4, R.id.Button5,
            R.id.Button6, R.id.Button7, R.id.Button8, R.id.Button9};

    // IDs of all the operator buttons

     int[] operatorButtons = {R.id.ButtonAdd, R.id.ButtonSubtract, R.id.ButtonMultiply, R.id.ButtonDivide};



     TextView txtScreen;


//to check if the Lastly number is Set
     boolean lastNumericSet;


	//to check if the State is Error
	boolean stateError;


	//to check if the Lastly Dot is Set
     boolean lastDotSet;

	 //to check if the Result  is Set in Screen
     boolean ResultFound=false;

	 //to check if the operator has been set before
     boolean operatorSet=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Display Screenorient=getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(Screenorient.getHeight() > Screenorient.getWidth()){
            //portrait mode
            setContentView(R.layout.activity_main);
        }
        else{
            //Toast.makeText(this,"Landscape mode",Toast.LENGTH_SHORT).show();
            //Landscape mode
            setContentView(R.layout.landscape_cal);
        }


        // Find the TextView

        this.txtScreen = (TextView) findViewById(R.id.StringEdit);

        // Common listener for Numbers

        setCommonNumericClickListener();

        //Common listener for Operators

        setCommonOperatorOnClickListener();

    }



   

     void setNumericOnClickListener() {

        // Create a common OnClickListener

        View.OnClickListener listener = new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                

                Button button = (Button) v;

                if (stateError || ResultFound) {

                    // If current state is Error, clear the screen

                    txtScreen.setText(button.getText());

                    stateError = false;
                    ResultFound=false;

                } else {

                    // If not, already there is a valid expression so append to it

                    txtScreen.append(button.getText());

                }

                // Set the flag

                lastNumericSet = true;


            }

        };

        // Assign the listener to all the numeric buttons

        for (int id : numericButtons) {

            findViewById(id).setOnClickListener(listener);

        }

    }




    /**

     * Find and set OnClickListener to operator buttons, equal button and decimal point button.

     */

    boolean checkForNegativeSign(){
        String txt=txtScreen.getText().toString();
        if(txt.endsWith("+")){
            return true;
        }
        else
            return false;

    }

     void setOperatorOnClickListener() {

        // Create a common OnClickListener for operators

        View.OnClickListener listener = new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                // If the current state is Error do not append the operator

                // If the last input is number only, append the operator

                if ((lastNumericSet && !stateError) ||  operatorSet) {

                    Button button = (Button) v;
					
                        //explictly allows the subtract button to work after Positive button
						
                    if(operatorSet){
                        String s=txtScreen.getText().toString();
                        s=s.substring(0,s.length()-1)+button.getText().toString();
                        //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                        txtScreen.setText(s);
                    }
                    else{
                        txtScreen.append(button.getText());
                    }





                    lastNumericSet = false;
                    operatorSet=true;		
                    lastDotSet = false;    // Reset the DOT flag
                    ResultFound=false;

                }

            }

        };

        // Assign the listener to all the operator buttons

        for (int id : operatorButtons) {

            findViewById(id).setOnClickListener(listener);

        }

        // Decimal point

        findViewById(R.id.ButtonDot).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                if (lastNumeric && !stateError && !lastDot) {

                    txtScreen.append(".");

                    lastNumericSet = false;

                    lastDotSet = true;

                }

            }

        });

        // Clear button

        findViewById(R.id.ButtonClear).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                txtScreen.setText("");  // Clear the screen

                // Reset all the states and flags

                lastNumericSet = false;

                stateError = false;

                lastDotSet = false;
                operatorSet=false;

            }

        });

        // Equal button

        findViewById(R.id.ButtonEquals).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                onEqual();

            }

        });




    }




     void onEqual() {

        // If the current state is error, nothing to do.

        // If the last input is a number only, solution can be found.

        if (lastNumericSet && !stateError) {

            // Read the expression

            String txt = txtScreen.getText().toString();

            // Create an Expression (A class from exp4j library)

            Expression expression = new ExpressionBuilder(txt).build();

            try {

                // Calculate the result and display

                double result = expression.evaluate();
                //Truncate the value
                DecimalFormat dec=new DecimalFormat("#0.0000000");
                String s=(dec.format(result)).toString();






                txtScreen.setText(s);

                lastDotSet = true; // Result contains a dot as it is in Double value format
                ResultFound=true;

            }


            catch (ArithmeticException ex) {

                // Display an error message

                Toast.makeText(getApplicationContext(),"Division By Zero is not Allowed",Toast.LENGTH_SHORT).show();
                txtScreen.setText(null);

                stateError = true;

                lastNumericSet = false;

            }

        }

    }

}
