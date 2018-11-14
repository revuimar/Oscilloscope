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
import java.util.EventObject;

public class KnobEvent extends EventObject {
    int value;
    KnobEvent(Knob knob, int value) {
        super(knob);
        this.value = value;
    }
    public int getValue() {return value;}
}


