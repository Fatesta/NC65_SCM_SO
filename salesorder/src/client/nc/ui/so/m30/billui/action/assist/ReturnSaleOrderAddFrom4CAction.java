package nc.ui.so.m30.billui.action.assist;

import java.awt.event.ActionEvent;

import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.pf.busiflow.PfButtonClickContext;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pubapp.uif2app.actions.AbstractReferenceAction;
import nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeFuncUtils;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.SOBillType;
import nc.vo.so.m30.entity.SaleOrderVO;

/**
 * 销售订单参照销售出库单退货操作
 * 
 * @since 6.0
 * @version 2011-7-20 上午11:21:44
 * @author fengjb
 */
public class ReturnSaleOrderAddFrom4CAction extends AbstractReferenceAction {

  private static final long serialVersionUID = -5130322362720789811L;

  private AbstractAppModel model;

  private BillForm editor;

  public AbstractAppModel getModel() {
    return this.model;
  }

  public void setModel(AbstractAppModel model) {
    this.model = model;
    model.addAppEventListener(this);
  }

  @Override
  public void setSourceBillName(String sourceBillName) {
    super.setSourceBillName(sourceBillName);
    this.setBtnName(NCLangRes.getInstance().getStrByID("4006011_0",
        "04006011-0232")/*参照销售出库单退货*/);
  }

  public BillForm getEditor() {
    return this.editor;
  }

  public void setEditor(BillForm editor) {
    this.editor = editor;
  }

  @Override
  protected boolean isActionEnable() {
    return this.model.getUiState() == UIState.NOT_EDIT;
  }

  @Override
  public void doAction(ActionEvent e) throws Exception {
    if (!SysInitGroupQuery.isICEnabled()) {
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4006011_0", "04006011-0470"));
      /*库存模块未启用，无法执行查询！*/
    }
    PfUtilClient.childButtonClickedNew(this.createPfButtonClickContext());
    if (PfUtilClient.isCloseOK()) {
      SaleOrderVO[] vos = (SaleOrderVO[]) PfUtilClient.getRetVos(false);

      // 显示到转单界面上
      this.getTransferViewProcessor().processBillTransfer(vos);
    }
  }

  private PfButtonClickContext createPfButtonClickContext() {
    PfButtonClickContext context = new PfButtonClickContext();
    context.setParent(this.getModel().getContext().getEntranceUI());
    context.setSrcBillType(this.getSourceBillType());
    context.setPk_group(this.getModel().getContext().getPk_group());
    context.setUserId(this.getModel().getContext().getPk_loginUser());
    // 如果该节点是由交易类型发布的，那么这个参数应该传交易类型，否则传单据类型
    String vtrantype =
        TrantypeFuncUtils.getTrantype(this.getModel().getContext());
    if (StringUtil.isEmptyWithTrim(vtrantype)) {
      context.setCurrBilltype(SOBillType.Order.getCode());
    }
    else {
      context.setCurrBilltype(vtrantype);
    }
    context.setUserObj(null);
    context.setSrcBillId(null);
    context.setBusiTypes(null);
    // 上面的参数在原来调用的方法中都有涉及，只不过封成了一个整结构，下面两个参数是新加的参数
    // 上游的交易类型集合
    context.setTransTypes(null);
    // 标志在交换根据目的交易类型分组时，查找目的交易类型的依据，有三个可设置值：1（根据接口定义）、
    // 2（根据流程配置）、-1（不根据交易类型分组）
    context.setClassifyMode(PfButtonClickContext.ClassifyByItfdef);
    return context;
  }

}
