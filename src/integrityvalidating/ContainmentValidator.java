/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrityvalidating;

/**
 *
 * @author b1106
 */
public interface ContainmentValidator extends Validator{
    
    boolean ctmValidate(Order order, Order orderRecv);
}
