package fr.itris.glips.rtda.colorsblinkings;

import java.awt.Color;


import fr.itris.glips.library.color.SVGColorsManager;


public class ColorsAndBlinkingsToolkit {
    /**
     * Returns the string representation of the given color
     * @param color a color
     * @return the string representation of the given color
     */
    public static String getColorString(Color color){
        
        if(color==null){
            
            color=Color.black;
        }
        
        String	sr=Integer.toHexString(color.getRed()),
			        sg=Integer.toHexString(color.getGreen()),
			        sb=Integer.toHexString(color.getBlue());
        
        if(sr.length()==1){
            
            sr="0".concat(sr);
        }
        
        if(sg.length()==1){
            
            sg="0".concat(sg);
        }
        
        if(sb.length()==1){
            
            sb="0".concat(sb);
        }
        
        return (("#".concat(sr)).concat(sg)).concat(sb);
    }
    
    /**
     * returns the color corresponding to the given string
     * @param colorString a string representing a color
     * @return the color corresponding to the given string
     */
    public static Color getColor(String colorString){

        Color color=null;
        
        if(colorString==null){
            
            colorString="";
        }
        
        int pos=colorString.indexOf("/*");
        
        if(pos!=-1) {
        	
        	colorString=colorString.substring(0, pos);
        }
        
        try{color=Color.getColor(colorString);}catch (Exception ex){}
        
        if(color==null && colorString.length()==7){
            
            int r=0, g=0, b=0;
            
            try{
                r=Integer.decode("#"+colorString.substring(1,3)).intValue();
                g=Integer.decode("#"+colorString.substring(3,5)).intValue();
                b=Integer.decode("#"+colorString.substring(5,7)).intValue();
                
                color=new Color(r,g,b);
            }catch (Exception ex){}
            
        }else if(color==null && colorString.indexOf("rgb(")!=-1){
            
            String tmp=colorString.replaceAll("\\s*[rgb(]\\s*", "");
            
            tmp=tmp.replaceAll("\\s*[)]\\s*", "");
            tmp=tmp.replaceAll("\\s+", ",");
            tmp=tmp.replaceAll("[,]+", ",");
            
            int r=0, g=0, b=0;
            
            try{
                r=new Integer(tmp.substring(0, tmp.indexOf(","))).intValue();
                tmp=tmp.substring(tmp.indexOf(",")+1, tmp.length());
                
                g=new Integer(tmp.substring(0, tmp.indexOf(","))).intValue();
                tmp=tmp.substring(tmp.indexOf(",")+1, tmp.length());
                
                b=new Integer(tmp).intValue();
                
                color=new Color(r,g,b);
            }catch (Exception ex){}
            
        }else{
        	
        	color=SVGColorsManager.getColor(colorString);
        }

        return color;
    }
}
