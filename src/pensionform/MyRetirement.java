/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pensionform;

/**
 *
 * @author SK
 */
public class MyRetirement extends Retirement{
    
    private double yr1, yr2, yr3;

    // Constructor
    public MyRetirement(String birthdate, int months, double yr1, double yr2, double yr3) {
        super(birthdate, months);
        this.yr1 = yr1;
        this.yr2 = yr2;
        this.yr3 = yr3;
    }

    // Average salary
    public double avgeSalary() {
        return (yr1 + yr2 + yr3) / 3;
    }

    // Annual pension
    public double annualPension() {
        return avgeSalary() * (pensionRight() / 100);
    }

    // Monthly pension
    public double monthlyPension() {
        return annualPension() / 12;
    }
    
}
