package nc.pubitf.so.m30.so.balance;

import nc.vo.pub.BusinessException;

/**
 * ����������д���۶�������ӿ�
 * 
 * @since 6.0
 * @version 2011-5-28 ����01:01:26
 * @author ��־ΰ
 */
public interface IRewrite30ForBalance {

  /**
   * �տ������д���۶���ʵ���տ���
   * 
   * @param paras RewriteBalancePara[]
   * @throws BusinessException
   */
  void rewrite30ReceivedMnyForBalance(RewriteBalancePara[] paras)
      throws BusinessException;
}