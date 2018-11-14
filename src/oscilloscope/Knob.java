/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oscilloscope;
/**
 *
 * @author Revison
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Knob extends JComponent {
    public static final int TICK_MODE = 0;
    public static final int SWITCH_MODE = 1;
    double minValue, value,previousValue, maxValue;
    int radius,mode;
  
    public Knob( ) { this(0, 100, 0,TICK_MODE); }
    public Knob(double minValue, double maxValue, double value,int mode) {
        this.minValue = minValue; 
        this.maxValue = maxValue;
        this.value = value;
        this.previousValue = value;
        this.mode = mode;
        setForeground(Color.decode("#A9A9A9"));
        initialiseListeners();
        
    }
    public Knob(double minValue, double maxValue, double value){
        this(minValue, maxValue, value,SWITCH_MODE);
    }
    private void initialiseListeners(){
        this.addMouseListener(new MouseAdapter( ) {
            @Override
            public void mousePressed(MouseEvent e) { 
            rotate(e); 
            //System.out.println(getValue());
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter( ) {
            @Override
            public void mouseDragged(MouseEvent e) {
                rotate(e);
                //System.out.println(getValue());
            }
        
        });
    }
    public double getValue(){
        return value;
    }
    public void setValue(double value) {
        firePropertyChange( "value", this.value, value );
        this.previousValue = this.value;
        this.value = value;
        fireEvent();
      }
    protected void rotate(MouseEvent event) {
        int y = event.getY( );
        int x = event.getX( );
        double angle = Math.atan((y - radius) / (x - radius));
        double newValue = (angle / (2 * Math.PI) * (maxValue - minValue));
        if (x < radius){setValue(newValue + maxValue / 2);}
        else if (y < radius){setValue(newValue + maxValue);}
        else {setValue(newValue);}
    }
    private void drawKnobCircles(Graphics g, int x, int y, int radius){
        g.setColor(getForeground());
        g.fillOval(x, y, radius * 2, radius * 2);
        g.setColor(getForeground().brighter());
        g.drawArc(x, y, radius * 2, radius * 2, 45, 180);
        g.setColor(getForeground().darker());
        g.drawArc(x, y, radius * 2, radius * 2, 225, 180);
    }
    @Override
    public void paintComponent(Graphics graphic) {
        Graphics2D gObject = (Graphics2D)graphic;
        int tick = 10;
        radius = getSize().width / 2 - tick;
        gObject.setPaint(getForeground().darker());
        gObject.drawLine(radius * 2 + tick / 2, radius, radius * 2 + tick, radius);
        drawKnobCircles(gObject, 0, 0, radius);
        int knobRadius = radius / 7;
        double currentAngle = value * (2 * Math.PI) / (maxValue - minValue);
        int x = (int)(Math.cos(currentAngle) * (radius - knobRadius * 3)),
        y = (int)(Math.sin(currentAngle) * (radius - knobRadius * 3));
        gObject.setStroke(new BasicStroke(1));
        drawKnobCircles(gObject, x + radius - knobRadius, y + radius - knobRadius, knobRadius);
        }
    
    @Override
    public Dimension getPreferredSize( ) {
        return new Dimension(120, 120);
    }

    public void addKnobListener(KnobListener listener) {
        listenerList.add( KnobListener.class, listener );
    }
    public void removeKnobListener(KnobListener listener) {
         listenerList.remove( KnobListener.class, listener );
    }
    void fireEvent( ) {
        Object[] listeners = listenerList.getListenerList( );
            for ( int i = 0; i < listeners.length; i += 2 ){
                if ( listeners[i] == KnobListener.class ){
                    ((KnobListener)listeners[i + 1])
                        .knobChanged(new KnobEvent(this,(int)value));
                    repaint();
            }
        }
    }
  
}