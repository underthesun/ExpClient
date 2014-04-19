/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrityvalidating;

/**
 *
 * @author b1106
 */
public interface Validator {

    boolean validate(Order order, Order orderRecv);
}
