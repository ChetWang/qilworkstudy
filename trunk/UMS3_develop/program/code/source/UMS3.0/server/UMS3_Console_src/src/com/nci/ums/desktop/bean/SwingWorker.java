package com.nci.ums.desktop.bean;

import javax.swing.SwingUtilities;

public abstract class SwingWorker {
    private Object value;  // see getValue(), setValue()
    /** 
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
    private static class ThreadVar {
        private Thread thread;
        ThreadVar(Thread t) { thread = t; }
        synchronized Thread get() { return thread; }
        synchronized void clear() { thread = null; }
    }

    private ThreadVar threadVar;

    /** 
     * ��ȡSwingWorker�̲߳�����ֵ�����construct��ûִ���꣬����null
     */
    protected synchronized Object getValue() { 
        return value; 
    }

    /** 
     * ����SwingWorker������Objectֵ
     */
    private synchronized void setValue(Object x) { 
        value = x; 
    }

    /** 
     * ��ʼ��̨���㣬���ص�Objectֵ����ͨ��<code>get</code>�������
     */
    public abstract Object construct();

    /**
     * ��construct()����ִ���겢���غ�ִ��finished����
     */
    public void finished() {
    }

    /**
     * A new method that interrupts the worker thread.  Call this method
     * to force the worker to stop what it's doing.
     */
    public void interrupt() {
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }

    /**
     * Return the value created by the <code>construct</code> method.  
     * Returns null if either the constructing thread or the current
     * thread was interrupted before a value was produced.
     * 
     * @return the value created by the <code>construct</code> method
     */
    public Object get() {
        while (true) {  
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // propagate
                return null;
            }
        }
    }


    /**
     * SwingWorker���캯����ִ����construct()����������˳�
     */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {
           public void run() { finished(); }
        };

        Runnable doConstruct = new Runnable() { 
            public void run() {
                try {
                    setValue(construct());
                }
                finally {
                    threadVar.clear();
                }

                SwingUtilities.invokeLater(doFinished);
            }
        };

        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }

    /**
     * ����SwingWorker�߳�
     */
    public void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
}
