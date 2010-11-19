package timesten;
import java.io.*;

class EraserThread implements Runnable {
   private boolean keepRunning = true;
 
   /**
    *@param The prompt displayed to the user
    */
   public EraserThread(String prompt) {
       System.out.print(prompt);
   }

   /**
    * Begin masking...display asterisks (*)
    */
   public void run () {
      while (keepRunning) {
         System.out.print("\010 ");
	 try {
	    Thread.currentThread().sleep(1);
         } catch(InterruptedException ie) {
            ie.printStackTrace();
         }
      }
   }

   /**
    * Instruct the thread to stop masking
    */
   public void stopMasking() {
      this.keepRunning = false;
   }
}
