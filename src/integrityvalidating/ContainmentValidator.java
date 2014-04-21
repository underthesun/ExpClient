/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrityvalidating;

/**
 * 包含关系验证接口
 *
 * @see Validator
 * @author b1106
 */
public interface ContainmentValidator extends Validator {

    /**
     * 进行包含关系验证的
     *
     * @param order 收到的订单信息
     * @param orderRecv 收到的数据
     * @return 验证结果
     */
    boolean ctmValidate(Order order, Order orderRecv);
}
