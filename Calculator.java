import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculator");
        frame.setSize(600, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JTextField display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setHorizontalAlignment(SwingConstants.LEFT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 4, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(220, 220, 220));

        String[] buttons = {
            "√", "x²", "C", "←",
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+"
        };

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane historyScroll = new JScrollPane(historyArea);

        JButton clearHistoryButton = new JButton("Clear");
        clearHistoryButton.addActionListener(e -> historyArea.setText(""));

        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setPreferredSize(new Dimension(200, 0));
        historyPanel.setBorder(BorderFactory.createTitledBorder("History"));
        historyPanel.add(historyScroll, BorderLayout.CENTER);
        historyPanel.add(clearHistoryButton, BorderLayout.SOUTH);

        frame.add(historyPanel, BorderLayout.EAST);

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusPainted(false);
            button.setBackground(new Color(200, 200, 255));
            button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            panel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String cmd = e.getActionCommand();
                    String current = display.getText();

                    try {
                        switch (cmd) {
                            case "C":
                                display.setText("0");
                                break;
                            case "←":
                                if (!current.isEmpty()) {
                                    display.setText(current.length() == 1 ? "0" :
                                        current.substring(0, current.length() - 1));
                                }
                                break;
                            case "√":
                                double val = Double.parseDouble(current);
                                double sqrtResult = Math.sqrt(val);
                                String sqrtFormatted = formatResult(sqrtResult);
                                historyArea.append("√" + current + " = " + sqrtFormatted + "\n");
                                display.setText(sqrtFormatted);
                                break;
                            case "x²":
                                double base = Double.parseDouble(current);
                                double sqrResult = base * base;
                                String sqrFormatted = formatResult(sqrResult);
                                historyArea.append(current + "² = " + sqrFormatted + "\n");
                                display.setText(sqrFormatted);
                                break;
                            case "=":
                                char operator = ' ';
                                int pos = -1;
                                for (int i = 0; i < current.length(); i++) {
                                    if ("+-*/".indexOf(current.charAt(i)) != -1) {
                                        operator = current.charAt(i);
                                        pos = i;
                                        break;
                                    }
                                }

                                if (pos == -1) throw new Exception("No operator");

                                double a = Double.parseDouble(current.substring(0, pos));
                                double b = Double.parseDouble(current.substring(pos + 1));
                                double result = 0;

                                switch (operator) {
                                    case '+': result = a + b; break;
                                    case '-': result = a - b; break;
                                    case '*': result = a * b; break;
                                    case '/': result = b != 0 ? a / b : 0; break;
                                }

                                String formattedResult = formatResult(result);
                                historyArea.append(current + " = " + formattedResult + "\n");
                                display.setText(formattedResult);
                                break;
                            default:
                                if (current.equals("0") || current.equals("Error")) {
                                    display.setText(cmd);
                                } else {
                                    display.setText(current + cmd);
                                }
                        }
                    } catch (Exception ex) {
                        display.setText("Error");
                    }
                }
            });
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static String formatResult(double result) {
        return (result == (int) result) ? String.valueOf((int) result) : String.format("%.2f", result);
    }
}
