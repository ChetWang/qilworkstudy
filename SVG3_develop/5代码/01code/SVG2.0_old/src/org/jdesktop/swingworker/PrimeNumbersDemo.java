

package org.jdesktop.swingworker;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PrimeNumbersDemo {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Prime Numbers Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        PrimeNumbersTask task = new PrimeNumbersTask(textArea, 100000);
        final JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(true);
        frame.add(progressBar, BorderLayout.NORTH);

        frame.setSize(500, 500);
        frame.setVisible(true);

        task.addPropertyChangeListener(
            new PropertyChangeListener() {
                public  void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        progressBar.setIndeterminate(false);
                        progressBar.setValue((Integer)evt.getNewValue());
                    }
                }
            });
        task.execute();

        return;
    }
    
    /**
     * Finds first N prime numbers.
     */
    static class PrimeNumbersTask extends SwingWorker<List<Integer>, Integer> {
        final int numbersToFind;
        //sorted list of consequent prime numbers
        private final List<Integer> primeNumbers;
        private final JTextArea textArea;

        PrimeNumbersTask(JTextArea textArea, int numbersToFind) {
            this.textArea = textArea;
            this.numbersToFind = numbersToFind;
            this.primeNumbers = new ArrayList<Integer>(numbersToFind);
        }

        @Override
        public List<Integer> doInBackground() {
            int number = 2;
            while(primeNumbers.size() < numbersToFind
                  && !isCancelled()) {
                if (isPrime(number)) {
                    primeNumbers.add(number);
                    setProgress(100 * primeNumbers.size() / numbersToFind);
                    publish(number);
                }
                number++;
            }
            return primeNumbers;
        }

        @Override
        protected void process(List<Integer> chunks) {
            StringBuilder strBuilder = new StringBuilder();
            for (int number : chunks) {
                strBuilder.append(number).append('\n');
            }
            textArea.append(strBuilder.toString());
        }
        private boolean isPrime(int number) {
            for (int prime : primeNumbers) {
                if (number % prime == 0) {
                    return false;
                }
            }
            return true;
        }
    }
}


