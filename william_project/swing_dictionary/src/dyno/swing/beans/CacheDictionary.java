/*
 * CachedDictionary.java
 *
 * Created on July 6, 2007, 12:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.WeakHashMap;
import javax.swing.JToolTip;
import javax.swing.SwingWorker;

/**
 *
 * @author William Chen
 */
public class CacheDictionary extends WeakHashMap<String, String> implements Dictionary{
    private static CacheDictionary cache;
    private static Object[][]suffixes={
        {"s",1},
        {"es",1},
        {"es",2},
        {"ing",3},
        {"ed",1},
        {"ed",2},
        {"d",1},
        {"er",1},
        {"er",2},
        {"est",2},
        {"est",3},
        {"ly",2},
        {"y",1},
    };
    static {
        cache = new CacheDictionary();
    }
    private static File getJavaHome(){
        String javaHome=System.getProperty("java.home");
        return new File(javaHome);
    }
    private static File getJavaLibHome(){
        return new File(getJavaHome(), "lib");
    }
    private static File getJavaDictionaryHome(){
        return new File(getJavaLibHome(), "dict");
    }
    
    private static File getWordFile(String word){
        return new File(getJavaDictionaryHome(), word);
    }
    
    static CacheDictionary getInstance() {
        if(cache == null)
            cache = new CacheDictionary();
        return cache;
    }
    
    /** Creates a new instance of CachedDictionary */
    public CacheDictionary() {
    }
    
    public String translate(JToolTip tip, String word) {
        String translation = getInstance().get(word);
        if (translation != null)
            return getTip(tip, word, translation);
        else
            return null;
    }
    private static String LOW_COLOR=null;
    private static String TOP_COLOR=null;
    private static String MID_COLOR=null;
    private static void intializeColor(JToolTip tip){
        TOP_COLOR=getHTMLColor(128, 0, 128);
        MID_COLOR=getHTMLColor(128, 128, 0);
        LOW_COLOR=getHTMLColor(0, 128, 128);
    }
    static String getTip(JToolTip tip, String word, String translation){
        if(TOP_COLOR==null){
            intializeColor(tip);
        }
        return "<html><body><div align='center' color='"+TOP_COLOR+"'><b>" + word + "</b></div>" + translation + "</body></html>";
    }
    static String getTranslation(JToolTip tip, String root, String text){
        if(MID_COLOR==null){
            intializeColor(tip);
        }
        if(LOW_COLOR==null){
            intializeColor(tip);
        }
        return "<div color='"+MID_COLOR+"'><i>"+root+"</i></div><div color='"+LOW_COLOR+"'>"+text+"</div>";
    }
    private static String getHTMLColor(int r, int g, int b){
        return "#"+getHexString(r)+getHexString(g)+getHexString(b);
    }
    private static String getHexString(int d){
        String hex=Integer.toHexString(d);
        while(hex.length()<2)
            hex="0"+hex;
        return hex;
    }
    public void asynchronizedTranslate(Clock clock, Text text, Component src, JToolTip tip, int x, int y) {
        tip.setTipText(getTip(tip, text.getText(), "<div color='#008080'>请稍候，查询中...</div>"));
        new OnlineLookup(clock, text, src, tip, x, y).execute();
    }
    
    class OnlineLookup extends SwingWorker<String, Object>{
        private Text text;
        private Component src;
        private String word;
        private JToolTip tip;
        private int x, y;
        private Clock clock;
        public OnlineLookup(Clock clock, Text text, Component src, JToolTip tip, int x, int y){
            this.clock=clock;
            this.text=text;
            this.word=text.getText().toLowerCase();
            this.src=src;
            this.tip=tip;
            this.x=x;
            this.y=y;
            text.setLookup(this);
        }
        protected String doInBackground() throws Exception {
            String result=search_as_word(word);
            if(result!=null)
                return result;
            for(int i=0;i<suffixes.length;i++){
                String suffix=(String)suffixes[i][0];
                int trimming=(Integer)suffixes[i][1];
                if(word.endsWith(suffix)){
                    result=search_trimming(word, trimming);
                    if(result!=null)
                        return result;
                }
            }
            return null;
        }
        private String search_trimming(String w, int trail){
            w=w.substring(0, w.length()-trail);
            String result=search_as_word(w);
            if(result!=null)
                return result;
            if(w.endsWith("i")){
                String tmpw=w.substring(0,w.length()-1)+"y";
                result=search_as_word(tmpw);
                if(result!=null)
                    return result;
            }
            if(w.length()>1){
                char lastc=w.charAt(w.length()-1);
                char lastbut=w.charAt(w.length()-2);
                if(lastc==lastbut){
                    String tmpw=w.substring(0, w.length()-1);
                    result=search_as_word(tmpw);
                    if(result!=null)
                        return result;
                }
            }
            result=search_as_word(w+"e");
            return result;
        }
        private String search_as_word(String w){
            File wordFile=getWordFile(w);
            if(wordFile.exists()){
                String content=readContent(wordFile);
                if(content==null)
                    return null;
                else{
                    content=getTranslation(tip, w, content);
                    getInstance().put(text.getText(), content);
                    return content;
                }
            }else
                return null;
        }
        private String readContent(File wordFile){
            BufferedReader reader=null;
            try{
                reader=new BufferedReader(new FileReader(wordFile));
                String content="";
                String line;
                while((line=reader.readLine())!=null){
                    line=line.trim();
                    if(line.length()==0)
                        continue;
                    content+=line;
                }
                return content;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }finally{
                if(reader!=null){try{reader.close();}catch(Exception e){}}
            }
        }
        protected void done() {
            try {
                if(!isCancelled()){
                    String result=get();
                    if(result!=null)
                        getInstance().put(word, result);
                    if(tip.isVisible()){
                        if(result==null){
                            result="<div color='#008080'>查找不到该单词</div>";
                            tip.setTipText(getTip(tip, text.getText(), result));
                        }else{
                            DictPopupManager.hide();
                            DictPopupManager.show(clock, text, src, x, y);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }finally{
                text.setLookup(null);
            }
        }
    }
}
