package ru.avalon.javapp.devj120.calcdemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                CalculatorFrame frame = new CalculatorFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

class CalculatorFrame extends JFrame {

    public CalculatorFrame() {

        setTitle("Калькулятор");
        CalculatorPanel panel = new CalculatorPanel();
        add(panel);
        pack();
        int width = 350;
        int height = 350;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        setBounds(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2, width, height);

    }
}

class CalculatorPanel extends JPanel {

    private JButton display;
    private JPanel panel;
    private JPanel panel2;
    private BigDecimal result;
    private String lastCommand;
    private boolean start;

    public CalculatorPanel() {
        setLayout(new BorderLayout());
        result = BigDecimal.ZERO;
        lastCommand = "=";
        start = true;

        display = new JButton("0");
        display.setEnabled(false);
        display.setFont(display.getFont().deriveFont(50f));
        add(display, BorderLayout.NORTH);

        ActionListener insert = new InsertAction();
        ActionListener command = new CommandAction();

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 5));
        addButton("7", insert);
        addButton("8", insert);
        addButton("9", insert);
        addButton("÷", command);

        addButton("xn", command);

        addButton("4", insert);
        addButton("5", insert);
        addButton("6", insert);
        addButton("*", command);

        addButton("x²", command);

        addButton("1", insert);
        addButton("2", insert);
        addButton("3", insert);
        addButton("-", command);

        addButton("²" + "\u221A" + "x", command);

        addButton("0", insert);
        addButton(".", insert);
        addButton("C", command);
        addButton("+", command);

        addButton("exp", command);

        add(panel, BorderLayout.CENTER);

        JButton button = new JButton("=");
        button.setFont(display.getFont().deriveFont(15f));
        button.addActionListener(command);
        add(button, BorderLayout.SOUTH);

    }

    private void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.setFont(button.getFont().deriveFont(15f));
        button.addActionListener(listener);
        panel.add(button);
    }

    private class InsertAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            String input = event.getActionCommand();
            int index = 0;

            if (start) {
                display.setText("");
                start = false;
            }

            if (display.getText().equals("") || display.getText().equals("0")) {
                if (input.equals(".")) {
                    display.setText("0" + input);
                } else {
                    display.setText(input);
                }

            } else {

                if (input.equals(".")) {

                    index = display.getText().indexOf(".");
                    if (index == -1) {
                        display.setText(display.getText() + input);
                    }

                } else {
                    display.setText(display.getText() + input);
                }
            }

        }
    }

    private class CommandAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            String command = event.getActionCommand();

            if (command.equals("C")) {
                start = false;
                result = BigDecimal.ZERO;
                display.setText(result.toString());
            } else if (command.equals("x²")) {
                
                start = false;
                double dl = 2.0;
                result = new BigDecimal(Math.pow(new Double(display.getText()), dl));
                display.setText(result.toString());

            } else if (command.equals("²" + "\u221A" + "x")) {
                
                start = false;
                result = new BigDecimal(Math.sqrt(new Double(display.getText())));
                display.setText(result.toString());    
            
            } else if (command.equals("exp")) {
                
                start = false;
                result = new BigDecimal(Math.exp(new Double(display.getText())));
                display.setText(result.toString());     
                
                
            } else {
                if (start) {
                    //if (command.equals("-")) {
                    //    display.setText(command);
                    //    start = false;
                    //} else {
                        lastCommand = command;
                    //}
                } else {

                    calculate(new BigDecimal(display.getText()));
                    lastCommand = command;
                    start = true;

                }
            }
        }
    }

    public void calculate(BigDecimal x) {
        if (lastCommand.equals("+")) {
            result = result.add(x);
        } else if (lastCommand.equals("-")) {
            result = result.subtract(x);
        } else if (lastCommand.equals("*")) {
            result = result.multiply(x);
        } else if (lastCommand.equals("÷")) {

            if (x.intValue() == 0) {
                result = BigDecimal.ZERO;
                display.setText("Error!");
                return;
            } else {
                result = result.divide(x);
            }

        } else if (lastCommand.equals("xn")) {

            result = new BigDecimal(Math.pow(result.doubleValue(), x.doubleValue()));

        } else if (lastCommand.equals("=")) {
            result = x;
        }

        if (result.compareTo(BigDecimal.ZERO) == 0) {
            result = BigDecimal.ZERO;
        }

        display.setText(result.toString());
    }
}
