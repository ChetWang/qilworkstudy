/*
 * Text.java
 *
 * Created on 2007年4月3日, 上午12:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans;

import javax.swing.SwingWorker;

public class Text implements Comparable<Text>{

    private int x;
    private int y;
    private int width;
    private int height;

    private String text;
    private boolean visible;

    private transient SwingWorker lookup;
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        if(!visible && lookup!=null)
            lookup.cancel(true);
        this.visible = visible;
    }

    public Text(String text) {
        this.text = text;
    }

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getText() {
        return text;
    }

    void setLookup(SwingWorker lookup) {
        this.lookup = lookup;
    }
    public boolean equals(Object o){
        if(o==null)
            return false;
        if(!(o instanceof Text))
            return false;
        Text another=(Text)o;
        if(text.equals(another.text))
            return another.x==x&&another.y==y&&another.width==width&&another.height==height;
        else
            return false;           
    }
    public int compareTo(Text o) {
        if(o==null)
            return 1;
        Text another=(Text)o;
        if(y<another.y){
            return -1;
        }else if(y==another.y){
            if(x<another.x)
                return -1;
            else if(x==another.x)
                return 0;
            else
                return 1;
        }else{
            return 1;
        }
    }
    public boolean contains(int x, int y){
        return y >= this.y && y <= this.y + this.height && x >= this.x && x <= this.x + this.width;
    }
}
