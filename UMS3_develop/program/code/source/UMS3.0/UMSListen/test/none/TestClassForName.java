/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package none;

/**
 *
 * @author Qil.Wong
 */
public class TestClassForName {
    
    public static void main(String args[]){
        try {
            String s ="com.nci.ums.v3.service.ServiceInfo";
            Class c = Class.forName(s);
            c.newInstance();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
