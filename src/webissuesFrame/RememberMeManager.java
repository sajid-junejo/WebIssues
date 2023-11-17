/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webissuesFrame;

import java.util.prefs.Preferences;

public class RememberMeManager {
    
    public static void main(String[] args) {
        Loader l = new Loader();
        l.setVisible(true);
        try {
            for(int i=0;i<=100;i++){
                Thread.sleep(40);
                l.percnt.setText(Integer.toString(i)+"%");
                if(i==100){
                    l.dispose();
                }
            }
        } catch (Exception e) {
        }
    }
    
}

