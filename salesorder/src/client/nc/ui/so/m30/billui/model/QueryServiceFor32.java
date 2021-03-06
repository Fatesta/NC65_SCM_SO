/**
 * 
 */
package nc.ui.so.m30.billui.model;

import nc.bs.framework.common.NCLocator;
import nc.itf.so.m30.self.ISaleOrderMaintain;
import nc.ui.pubapp.uif2app.query2.model.IRefQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.m30.entity.SaleOrderVO;

/**
 * @author gdsjw
 * 
 */
public class QueryServiceFor32 implements IRefQueryService {

  @Override
  public Object[] queryByQueryScheme(IQueryScheme queryScheme) throws Exception {
    SaleOrderVO[] rets = null;
    ISaleOrderMaintain service =
        NCLocator.getInstance().lookup(ISaleOrderMaintain.class);
    try {
      rets = service.querySaleOrderFor32(queryScheme);
    }
    catch (BusinessException e) {

      ExceptionUtils.wrappException(e);
    }
    return rets;
  }

  @Override
  public SaleOrderVO[] queryByWhereSql(String whereSql) throws Exception {
    // SaleOrderVO[] rets = null;
    // ISaleOrderMaintain service =
    // NCLocator.getInstance().lookup(ISaleOrderMaintain.class);
    // try {
    // rets = service.querySaleOrderFor32(whereSql);
    // }
    // catch (BusinessException e) {
    //
    // ExceptionUtils.wrappException(e);
    // }
    return null;
  }

}
