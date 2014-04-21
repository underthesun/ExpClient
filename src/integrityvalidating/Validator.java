/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrityvalidating;

/**
 * 完整性验证接口
 *
 * @author b1106
 */
public interface Validator {
    /**
     * 进行完整性验证
     * @param order 收到的订单信息
     * @param orderRecv 收到的数据
     * @return 验证结果
     */
    boolean validate(Order order, Order orderRecv);
}
