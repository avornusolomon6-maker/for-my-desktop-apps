/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pensionform;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author SK
 */
public class Retirement {
    
    private String birthdate;   
    private int months;

    // Constructor
    public Retirement(String birthdate, int months) {
        this.birthdate = birthdate;
        this.months = months;
    }

    // Method to get birthdate
    public String getBirthDate() {
        return birthdate;
    }

    // Method to calculate age 
    public int age() {
        int birthYear = Integer.parseInt(birthdate.substring(0, 4)); // take year part
        int currentYear = java.time.LocalDate.now().getYear();
        Calendar CurrentDate = Calendar.getInstance();
        CurrentDate.setTime(new Date());
        return currentYear - birthYear;
        //return CurrentDate
    }

    // Method to calculate pension right
    public double pensionRight() {
        if (months < 180) {
            return 0; 
        }

        double pension = 37.5; 
        int extraMonths = months - 180;
        pension += (extraMonths / 12) * 1.125; 

        if (pension > 60) {
            pension = 60;
        }
        return pension;
    }
    
}
