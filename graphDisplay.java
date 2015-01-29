/* graphDisplay.java
 * 
 * Last edited: 1/21/2015
 * 
 * Generates a bar graph display specifically modified to suit 
 * SolverEngine.java methods.
 * 
 * Graph display template obtained from: 
 * Copyright 2009 - 12 Demo Source and Support. All rights reserved.
 * http://www.java2s.com/Code/Java/2D-Graphics-GUI/Simplebarchart.htm
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class graphDisplay extends JPanel {
  private double[] values;

  private String[] names;
  private int tempheight = 0;
  private String title;
  public graphDisplay(double[] v, String[] n, String t) {
	for(int i = 0; i < n.length; i++){
		n[i] = n[i].toUpperCase();
	}
    names = n;
    values = v;
    title = t;
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (values == null || values.length == 0)
      return;
    double minValue = 0;
    double maxValue = 0;
    for (int i = 0; i < values.length; i++) {
      if (minValue > values[i])
        minValue = values[i];
      if (maxValue < values[i])
        maxValue = values[i];
    }
    
    Dimension d = getSize();
    int clientWidth = d.width - 50;
    int clientHeight = d.height;
    int barWidth = clientWidth / values.length;

    Font titleFont = new Font("ARIAL", Font.BOLD, 18);
    FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
    Font labelFont = new Font("Arial", Font.PLAIN, 15);
    FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

    int titleWidth = titleFontMetrics.stringWidth(title);
    int y = titleFontMetrics.getAscent();
    int x = (clientWidth - titleWidth) / 2;
    g.setFont(titleFont);
    g.drawString(title, x, y);

    int top = titleFontMetrics.getHeight();
    int bottom = labelFontMetrics.getHeight();
    if (maxValue == minValue)
      return;
    double scale = (clientHeight - top - bottom) / (maxValue - minValue);
    y = clientHeight - labelFontMetrics.getDescent();
    g.setFont(labelFont);

    for (int i = 0; i < values.length; i++) {
      int valueX = i * barWidth + 1;
      int valueY = top;
      
      int height = (int) (values[i] * scale);
      if (values[i] >= 0)
        valueY += (int) ((maxValue - values[i]) * scale);
      else {
        valueY += (int) (maxValue * scale);
        height = -height;
      }
      int ylabel = height; 

      if(valueY - tempheight > 15){
    	  g.setColor(Color.black);
          g.drawString((Integer.toString((int) values[i])), 0, valueY + 5);
          g.setColor(Color.gray);
          g.drawString("_____________________________________________________", 38, valueY - 2);
          g.setColor(Color.black);
          g.drawString("__", 38, valueY - 2);

          tempheight = valueY;
          }
      
      g.setColor(new Color(102,178,255));
      if(i == 0){
    	  g.setColor(new Color(178,255,102));
      }
      
      
      if(values[i] < values[1] / 2){
      	 g.setColor(new Color(0,128,255));
        }
      if(values[i] < values[1] / 3){
     	 g.setColor(new Color(0,80,160));
       }
      
      if(values[i] < values[1] / 5){
    	 g.setColor(new Color(0, 51, 102));
      }
      
      
      
      g.fillRect(valueX + 50, valueY, barWidth - 2, height);
      g.setColor(Color.black);
      g.drawRect(valueX + 50, valueY, barWidth - 2, height);
      int labelWidth = labelFontMetrics.stringWidth(names[i]);
      x = i * barWidth + (barWidth - labelWidth) / 2;
      
      
      
      g.setColor(Color.black);
      g.setFont(labelFont);
      g.drawString(names[i], x + 50, y);
        
    }
  }
}
