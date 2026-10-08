package pensionform;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author SK
 */
public class PensionForm extends JFrame{

    /**
     * @param args the command line arguments
     */
    JTextField birthTxt, monthsTxt, yr1Txt, yr2Txt, yr3Txt, avgTxt;
    JComboBox<String> ssnitTxt;
    JRadioButton pndcBtn, actBtn;
    JButton estimateBtn, resetBtn, infoBtn, exitBtn;
    private static final String HISTORY_FILE = System.getenv("APPDATA")+File.separator + "PensionForm" + File.separator;
    
    
    // Check if already exists in combo box
    private boolean alreadyInBox(String text) {
        for (int i = 0; i < ssnitTxt.getItemCount(); i++) {
            if (ssnitTxt.getItemAt(i).equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }

    // Save search text to file
    private void saveToFile(String text) {
        try {
            File file = new File(HISTORY_FILE + "search_history.txt");
            file.getParentFile().mkdirs(); // ✅ Create folders if missing
            try (FileWriter fw = new FileWriter(file, true)) {
                fw.write(text + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Load search history from file
    private void loadHistory() {
        File file = new File(HISTORY_FILE + "search_history.txt");
        ssnitTxt.addItem("");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        ssnitTxt.addItem(line);
                    }
                }
            } catch (IOException e) {
               System.out.println(e.getMessage());
            }
        }
    }
    
    public PensionForm() {
        setTitle("Pension Estimator");
        setSize(500, 650);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(220, 220, 220));
        //setUndecorated(true);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
        
            @Override
            public void windowOpened(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                resetBtn.setEnabled(true);
                pndcBtn.setSelected(true);
            }           
        });

        JLabel title = new JLabel("Pension Estimator", JLabel.CENTER);
        title.setBounds(20, 10, 460, 30);
        title.setForeground(new Color(0, 0, 0));
        title.setFont(new Font("Arial", Font.BOLD, 20));
        
        // SSNIT
        JLabel ssnitLbl = new JLabel("SSNIT No:",JLabel.CENTER);
        ssnitLbl.setBounds(20, 70, 80, 30);
        ssnitLbl.setFont(new Font("Arial", Font.BOLD, 14));
        
        ssnitTxt = new JComboBox<>();
        ssnitTxt.setBounds(120, 70, 250, 30);     
        ssnitTxt.setEditable(true);
        loadHistory(); // Load old searches from file

        // Add key listener for Enter key
        ssnitTxt.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String text = ((JTextField) ssnitTxt.getEditor().getEditorComponent()).getText().trim();
                    if (!text.isEmpty() && !alreadyInBox(text)) {
                        ssnitTxt.addItem(text);
                        saveToFile(text);
                       
                    }
                }
            }
        });
        ssnitTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            
            @Override
            public void focusLost(FocusEvent e) {
                if (!ssnitTxt.getSelectedItem().toString().isEmpty() && ssnitTxt.getSelectedItem().toString().length() == 13 && Character.isLetter(ssnitTxt.getSelectedItem().toString().charAt(0))) {
                    
                    char centuryFlag = ssnitTxt.getSelectedItem().toString().charAt(1);
                int yearPrefix = (centuryFlag == '0') ? 1900 : 2000;

                int year = yearPrefix + Integer.parseInt(ssnitTxt.getSelectedItem().toString().substring(2, 4));
                int month = Integer.parseInt(ssnitTxt.getSelectedItem().toString().substring(4, 6));
                int day = Integer.parseInt(ssnitTxt.getSelectedItem().toString().substring(6, 8));

                String birthdate = String.format("%04d-%02d-%02d", year, month, day);
                birthTxt.setText(birthdate);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Invalid SSNIT Number! Must be 13 characters and start with a letter.");
                ssnitTxt.requestFocus();
            }
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        });

        // Birthdate
        JLabel birthLbl = new JLabel("Birth Date:", JLabel.CENTER);
        birthLbl.setBounds(20, 110, 80, 30);
        birthLbl.setFont(new Font("Arial", Font.BOLD, 14));
        
        birthTxt = new JTextField();
        birthTxt.setBounds(120, 110, 250, 30);     
        birthTxt.setEnabled(false);
        
        //Panel for border around radio buttons 
        JPanel panel = new JPanel();
        panel.setBounds(20, 180, 460, 75);
        panel.setBackground(new Color(220,220,220));
        panel.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 255), 2));
        
        // Radio buttons with title
        JLabel radioLabel = new JLabel("Pension Scheme", JLabel.LEFT);
        radioLabel.setBounds(30, 170, 150, 20);
        radioLabel.setFont(new Font("Arial", Font.BOLD, 14));
        radioLabel.setBackground(new Color(220,220,220));
        
        pndcBtn = new JRadioButton("PNDC Law 247");
        pndcBtn.setBounds(50, 210, 190, 30);
        pndcBtn.setSelected(true);
        pndcBtn.setFont(new Font("Arial", Font.BOLD, 14));
        pndcBtn.setHorizontalAlignment(SwingConstants.CENTER);
        pndcBtn.setVerticalAlignment(SwingConstants.CENTER);
        pndcBtn.setBackground(new Color(220,220,220));
        
        actBtn = new JRadioButton("Act 766");
        actBtn.setBounds(260, 210, 190, 30);
        actBtn.setFont(new Font("Arial", Font.BOLD, 14));
        actBtn.setHorizontalAlignment(SwingConstants.CENTER);
        actBtn.setVerticalAlignment(SwingConstants.CENTER);
        actBtn.setBackground(new Color(220,220,220));
        
        ButtonGroup group = new ButtonGroup();
        group.add(pndcBtn); group.add(actBtn);
                
        // Months
        JLabel monthsLbl = new JLabel("Number Of Months Contributed:", JLabel.CENTER);
        monthsLbl.setBounds(20, 280, 460, 20);
        monthsLbl.setFont(new Font("Arial", Font.BOLD, 14));
        
        monthsTxt = new JTextField();
        monthsTxt.setBounds(20, 305, 460, 30);
        monthsTxt.setFont(new Font("Sans Serif", Font.PLAIN, 15));
        monthsTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                
                if (!monthsTxt.getText().trim().isEmpty()) {
                    int months = Integer.parseInt(monthsTxt.getText().trim());
                    if (pndcBtn.isSelected()) {
                        if (months > 239) {
                            
                        }else{ JOptionPane.showMessageDialog(null, "You don't qualify for a full pension"); 
                            SwingUtilities.invokeLater((() -> {monthsTxt.requestFocusInWindow();})); }
                        
                    }else if(actBtn.isSelected()){
                        
                        if (months > 179 ) {
                            
                        }else{ JOptionPane.showMessageDialog(null, "You don't qualify for a full pension"); 
                            monthsTxt.requestFocus();}
                    }
                }
            }
        });
        

        // Years
        JLabel yr1Lbl = new JLabel("Year 1:", JLabel.CENTER);
        yr1Lbl.setBounds(20, 355, 80, 30);
        yr1Lbl.setFont(new Font("Arial", Font.BOLD, 14));
        add(yr1Lbl);
        
        yr1Txt = new JTextField("0"); 
        yr1Txt.setBounds(120, 355, 250, 30); 
        yr1Txt.setFont(new Font("Sans Serif", Font.PLAIN, 15));
        add(yr1Txt);
        
        JLabel yr1Lb2 = new JLabel("Year 2:", JLabel.CENTER);
        yr1Lb2.setBounds(20, 395, 80, 30);
        yr1Lb2.setFont(new Font("Arial", Font.BOLD, 14));
        add(yr1Lb2);
        
        yr2Txt = new JTextField("0"); 
        yr2Txt.setBounds(120, 395, 250, 30); 
        yr2Txt.setFont(new Font("Sans Serif", Font.PLAIN, 15));
        add(yr2Txt);
        
        JLabel yr1Lb3 = new JLabel("Year 3:", JLabel.CENTER);
        yr1Lb3.setBounds(20, 435, 80, 30);
        yr1Lb3.setFont(new Font("Arial", Font.BOLD, 14));
        add(yr1Lb3);
        
        yr3Txt = new JTextField("0"); 
        yr3Txt.setBounds(120, 435, 250, 30);
        yr3Txt.setFont(new Font("Sans Serif", Font.PLAIN, 15));
        add(yr3Txt);

        // Average
        avgTxt = new JTextField();
        avgTxt.setBounds(20, 485, 460, 55);
        avgTxt.setFont(new Font("Sans Serif", Font.BOLD, 17));
        avgTxt.setBackground(new Color(220,220,220));
        avgTxt.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 255), 2));
        avgTxt.setText("Average Salary");
        avgTxt.setHorizontalAlignment(SwingConstants.CENTER);
        avgTxt.setEditable(false);
        avgTxt.setFocusable(false);
        add(avgTxt);

        // Buttons
        estimateBtn = new JButton("Estimate");
        estimateBtn.setBounds(20, 570, 100, 30);
        getContentPane().add(estimateBtn);

        resetBtn = new JButton("Reset");
        resetBtn.setBounds(140, 570, 100, 30);
        resetBtn.setEnabled(false);
        getContentPane().add(resetBtn);

        infoBtn = new JButton("Info");
        infoBtn.setBounds(260, 570, 100, 30);
        getContentPane().add(infoBtn);

        exitBtn = new JButton("Exit");
        exitBtn.setBounds(380, 570, 100, 30);
        getContentPane().add(exitBtn);

        // Events
        resetBtn.addActionListener(e -> resetForm());
        
        estimateBtn.addActionListener(e -> estimatePension());
        
        infoBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Name: Avornu Solomon\nIndex No: 0320080247\nTel: 0506081135", "Programmer", JOptionPane.INFORMATION_MESSAGE));
        
        exitBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want \nto close the application?","Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        add(title);
        add(ssnitLbl);
        add(ssnitTxt);
        add(birthLbl);
        add(birthTxt);
        
        add(radioLabel);
        add(pndcBtn); 
        add(actBtn);
        add(panel);
        
        add(monthsLbl);
        add(monthsTxt);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Method to reset the form
    private void resetForm() {
        ssnitTxt.setSelectedIndex(0);
        birthTxt.setText("");
        monthsTxt.setText("");
        yr1Txt.setText("€0.00");
        yr2Txt.setText("€0.00");
        yr3Txt.setText("€0.00");
        avgTxt.setText("");
        pndcBtn.setSelected(true);
    }

    // Method to estimate Pension
    private void estimatePension() {
        try {
            if (birthTxt.getText().isEmpty() || monthsTxt.getText().isEmpty()
                || yr1Txt.getText().isEmpty() || yr2Txt.getText().isEmpty() || yr3Txt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are mandatory");
                return;
            }

            String birthdate = birthTxt.getText();
            int months = Integer.parseInt(monthsTxt.getText());
            double y1 = Double.parseDouble(yr1Txt.getText());
            double y2 = Double.parseDouble(yr2Txt.getText());
            double y3 = Double.parseDouble(yr3Txt.getText());

            MyRetirement mr = new MyRetirement(birthdate, months, y1, y2, y3);

            if (mr.age() < 55) {
                JOptionPane.showMessageDialog(this, "You don't qualify for full pension");
                return;
            }

            double avg = mr.avgeSalary();
            double annual = mr.annualPension();
            double monthly = mr.monthlyPension();

            avgTxt.setText(String.format("%.2f", avg));
            JOptionPane.showMessageDialog(this, "Annual: " + annual + "\nMonthly: " + monthly + "\nRight: " + mr.pensionRight());

            estimateBtn.setEnabled(false);
            resetBtn.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new PensionForm();
    }
    
}
