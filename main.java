package calculator;

import java.text.DecimalFormat;

import static calculator.__View.*;

public class __Model {

    private static boolean firstDigit = true;  // Mark whether the user pressed the first number of the entire expression, or the first number after the operator
    private static double resultNum = 0.0000;   // intermediate result of calculation
    private static String operator = "=";   // The operator of the current operation (you need to restore it to "=" when you press "C")
    private static boolean operateValidFlag = true;   // Determine whether the operation is legal

    public static void pressBackspace() {
        String text = resultText.getText();
        int i = text.length();
        if (i > 0) {
            text = text.substring(0, i - 1);  // Backspace, remove the last character of the text
            if (text.length() == 0) {
                // If the text has no content, initialize the various values ​​of the calculator
                resultText.setText("0");
                firstDigit = true;
                operator = "=";
            } else {
                // display new text
                resultText.setText(text);
            }
        }
    }

    /**
           * Handle the event that the C key is pressed
     */
    public static void pressC() {
        // Initialize various values ​​of the calculator
        resultText.setText("0");
        firstDigit = true;
        operator = "=";
    }

    /**
           * Handle the event that the number key is pressed
     */
    public static void pressNumber(String key) {
        if (firstDigit) {
            // The input is the first number
            resultText.setText(key);
        } else if ((key.equals(".")) && (!resultText.getText().contains("."))) {
            // The input is a decimal point, and there is no decimal point before, the decimal point will be attached to the back of the result text box
            resultText.setText(resultText.getText() + ".");
        } else if (!key.equals(".")) {
            // If the input is not a decimal point, append the number to the back of the result text box
            resultText.setText(resultText.getText() + key);
        }
        firstDigit = false;
    }

    /**
           * Handle the event that the operator key is pressed
     */
    //Only a number of operations
    public static void singleOperator(String key) {
        operator = key;  // The operator is the button pressed by the user
        switch (operator) {
            case "1⁄x":
                // reciprocal operation
                if (resultNum == 0) {
                    operateValidFlag = false;  //The operation is illegal
                    resultText.setText("Zero has no countdown");
                } else {
                    resultNum = 1 / getNumberFromText();
                    omitDecimal(resultNum);
                }
                break;
            case "√x":
                // Square root operation
                if (resultNum < 0) {
                    operateValidFlag = false;  //The operation is illegal
                    resultText.setText("The root sign cannot be negative");
                } else {
                    resultNum = Math.sqrt(getNumberFromText());
                    omitDecimal(resultNum);
                }
                break;
            case "x²":
                // Square operation
                resultNum = getNumberFromText() * getNumberFromText();
                omitDecimal(resultNum);
                break;
            case "%":
                // Percent sign operation, divide by 100
                resultNum = getNumberFromText() / 100;
                resultText.setText(String.valueOf(resultNum));
                break;
            case "+/-":
                // Positive and negative operations
                resultNum = getNumberFromText() * (-1);
                if (operateValidFlag) {
                    // When the operation is legal, the result is a decimal with 4 digits after the decimal point, and the integer is output normally
                    omitDecimal(resultNum);
                }
                firstDigit = true;
                operateValidFlag = true;
                break;
        }
    }

    //Need two operations
    public static void doubleOperator(String key) {
        switch (operator) {
            case "÷":
                // Division operation
                // If the value in the current result text box is equal to 0
                if (getNumberFromText() == 0.0) {
                    operateValidFlag = false;  //The operation is illegal
                    resultText.setText("Divisor cannot be zero");
                } else {
                    resultNum /= getNumberFromText();
                }
                break;
            case "+":
                // addition operation
                resultNum += getNumberFromText();
                break;
            case "-":
                // subtraction operation
                resultNum -= getNumberFromText();
                break;
            case "×":
                // multiplication
                resultNum *= getNumberFromText();
                break;
            case "=":
                // Assignment operation
                resultNum = getNumberFromText();
                break;
        }
        omitDecimal(resultNum);
        operator = key;  // The operator is the button pressed by the user
        firstDigit = true;
        operateValidFlag = true;
    }

    public static void omitDecimal(double resultNum) {
        long t1;
        double t2;
        t1 = (long) resultNum;
        t2 = resultNum - t1;
        if (t2 == 0) {
            resultText.setText(String.valueOf(t1));
        } else {
            resultText.setText(String.valueOf(new DecimalFormat("0.00000000").format(resultNum)));
        }
    }

    /**
           * Get the number from the result text box
     */
    public static double getNumberFromText() {
        double result = 0;
        try {
            result = Double.parseDouble(resultText.getText());
        } catch (NumberFormatException ignored) {
        }
        return result;
    }


}


package calculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static calculator.__Controller.addListener;

public class __View {
    public static JFrame frame = new JFrame();
    public final static String[] keys = {"%", "CE", "C", "Back", "1⁄x", "x²", "√x", "÷", "7", "8", "9", "×", "4", "5", "6", "-", "1", "2", "3", "+", "+/-", "0", ".", "="};
    public static MyButton[] buttons = new MyButton[keys.length];  //Button on the calculator
    public static JTextField resultText = new JTextField("0");  //Display the calculation result text box

    public __View() {
        init();  // Initialize the calculator
        addListener();
    }

    /**
           * Initialize the calculator
     */
    private void init() {
        Color color1 = new Color(200, 200, 200);  //background color
        Color color2 = new Color(140, 140, 140);  //Equal sign exclusive color
        Color color3 = new Color(230, 230, 230);  //Function key and operator color
        Color color4 = new Color(240, 240, 240);  //Digital color
        // Create a drawing board and place the text box
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(resultText);
        resultText.setFont(new Font("Kai body", Font.BOLD, 42));  //Set the font and size of the text in the text box, bold
        resultText.setHorizontalAlignment(JTextField.RIGHT);  //The content in the text box is right aligned
        resultText.setEditable(false);  //Cannot modify the result text box
        resultText.setBorder(null);  //Delete the border of the text box
        // Set the background color of the text box
        resultText.setBackground(color1);
        // Initialize the button on the calculator and place the button in a drawing board
        JPanel keysPanel = new JPanel();
        // Using a grid layout, a grid of 6 rows and 4 columns, the horizontal and vertical spacing between the grids is 2 pixels
        keysPanel.setLayout(new GridLayout(6, 4, 2, 2));
        //Initialize function button
        for (int i = 0; i < 8; i++) {
            buttons[i] = new MyButton(keys[i], color3);
            keysPanel.add(buttons[i]);
            buttons[i].setBackground(color3);
            buttons[i].setForeground(Color.black);
            buttons[i].setFont(new Font(Font.SERIF, Font.PLAIN, 18));
            buttons[i].setBorderPainted(false);  //Remove the border of the button
        }
        //Initialize operator and number key button
        for (int i = 8; i < keys.length; i++) {
            if ((i + 1) % 4 == 0) buttons[i] = new MyButton(keys[i], color3);
            else buttons[i] = new MyButton(keys[i], color4);
            keysPanel.add(buttons[i]);
            buttons[i].setForeground(Color.black);
            buttons[i].setFont(new Font(Font.SERIF, Font.PLAIN, 18));
            buttons[i].setBorderPainted(false);  //Remove the border of the button
        }
        buttons[23].setBackground(color2);  //'=' symbol key uses special color
        keysPanel.setBackground(color1);
        //Place the panel where the text box is located in the north and the keysPanel panel in the middle of the calculator
        frame.getContentPane().add("North", textPanel);
        frame.getContentPane().add("Center", keysPanel);
        //Set the borders of the two panels, try to restore the win10 calculator
        textPanel.setBorder(BorderFactory.createMatteBorder(25, 3, 1, 3, color1));
        keysPanel.setBorder(BorderFactory.createMatteBorder(6, 3, 3, 3, color1));
        ImageIcon imageIcon=new ImageIcon("1.ico");
        frame.setIconImage(imageIcon.getImage());
        frame.setTitle("Calculator");
        frame.setSize(360, 450);
        frame.setLocation(500, 300);
        frame.setResizable(true);  // Allow to modify the size of the calculator window
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}

package calculator;

import static calculator.__View.*;
import static calculator.__Model.*;

public class __Controller {
    public static void addListener() {
        // Add event listeners for each button
        for (int i = 0; i < keys.length; i++) {
            buttons[i].addActionListener(e -> {
                String command = e.getActionCommand();  // Get event source
                if (command.equals(keys[3])) {
                    // The user pressed the "Back" key
                    pressBackspace();
                } else if (command.equals(keys[1])) {
                    // The user pressed the "CE" key
                    resultText.setText("0");
                } else if (command.equals(keys[2])) {
                    // The user pressed the "C" key
                    pressC();
                } else if ("0123456789.".contains(command)) {
                    // The user pressed the number key or the decimal point key
                    pressNumber(command);
                } else if (command.equals(keys[0]) || command.equals(keys[4]) || command.equals(keys[5]) || command.equals(keys[6]) || command.equals(keys[20])) {
                    // The user presses the arithmetic key that only needs one number (see the reciprocal, %, square root, square, take positive and negative numbers)
                    singleOperator(command);
                } else {
                    doubleOperator(command);
                }
            });
        }
    }

}

package calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyButton extends JButton {
    private int isMouseEntered = 1;// Whether the mouse enters the button
    Color color0;
    Color color1 = new Color(200, 200, 200);

    public MyButton(String buttonText, Color color) {
        super(buttonText);
        color0 = color;
        //Add mouse monitor
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //When the mouse enters, the mouse enter state is changed to TRUE, and the button is redrawn
                isMouseEntered = 0;
                repaint();
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isMouseEntered = 1;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                isMouseEntered = 2;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isMouseEntered = 0;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw gradient background color
        switch (isMouseEntered) {
            case 0:
                this.setBackground(color1);
                break;
            case 1:
                this.setBackground(color0);
                break;
            case 2:
            default:
                break;
        }
        super.paintComponent(g);

    }

}


package calculator;

public class CalculatorMain {
    public CalculatorMain(){}
    public static void main(String[] args) {
        new __View();
    }
}

