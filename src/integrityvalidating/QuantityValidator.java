/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrityvalidating;

/**
 *
 * @author b1106
 */
public interface QuantityValidator extends Validator{
    
    boolean qtyValidate(Order order, Order orderRecv);
}
