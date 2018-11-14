/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oscilloscope;

import java.util.EventListener;

/**
 *
 * @author Revison
 */
public interface KnobListener extends EventListener {
    void knobChanged(KnobEvent event);
}