package nc.vo.so.m30.vochange;

import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.m30.pub.SaleOrderVOCalculator;
import nc.vo.so.m30.rule.DirectStoreRule;
import nc.vo.so.m30.rule.PayTermRule;
import nc.vo.so.pub.keyvalue.IKeyValue;
import nc.vo.so.pub.keyvalue.VOKeyValue;
import nc.vo.so.pub.rule.BodyValueRowRule;
import nc.vo.so.pub.rule.SOBuysellTriaRule;
import nc.vo.so.pub.rule.SOCalConditionRule;
import nc.vo.so.pub.rule.SOCountryInfoRule;
import nc.vo.so.pub.rule.SOCurrencyRule;
import nc.vo.so.pub.rule.SOExchangeRateRule;
import nc.vo.so.pub.rule.SOGlobalExchangeRate;
import nc.vo.so.pub.rule.SOGroupExchangeRate;
import nc.vo.so.pub.rule.SOProfitCenterValueRule;
import nc.vo.so.pub.rule.SOTaxInfoRule;
import nc.vo.so.pub.rule.SaleOrgRelationRule;

public class MZ3ToM30ChangeVOAdjust extends AbstractSaleOrderChangeVOAdjust {

  @Override
  protected void fillRefAddValue(SaleOrderVO[] vos) {
    super.fillRefAddValue(vos);

    for (SaleOrderVO billvo : vos) {

      IKeyValue keyValue = new VOKeyValue<SaleOrderVO>(billvo);

      // (1)�����տ�Э����Ϣ
      PayTermRule payTermRule = new PayTermRule(keyValue);
      payTermRule.setPayTermInfo();

      // (2)���������֯��������֯��������֯���
      BodyValueRowRule bodycouuitl = new BodyValueRowRule(keyValue);
      int[] rows = bodycouuitl.getMarNotNullRows();

      SaleOrgRelationRule orgrelrule = new SaleOrgRelationRule(keyValue);
      // ���������֯
      int[] sendstockrows =
          bodycouuitl.getValueNullRows(SaleOrderBVO.CSENDSTOCKORGVID);
      orgrelrule.setSendStockOrg(sendstockrows);
      // ������֯
      int[] trafficrows =
          bodycouuitl.getValueNullRows(SaleOrderBVO.CTRAFFICORGVID);

      // ��Դ����������ʱ�����տͻ��̶�������֯������ҵ��ί�й�ϵƥ�����Я�����������֯��Ӧ����֯���������ġ�
      orgrelrule.setFinanceStockOrg(rows,
          orgrelrule.getRelationOrg(rows));
      
      
      // add by zhangby5 for ��������ȡֵ����
      SOProfitCenterValueRule profitRule =
          new SOProfitCenterValueRule(keyValue);
      profitRule.setProfitCenterValue(SaleOrderBVO.CSPROFITCENTERVID,
          SaleOrderBVO.CSPROFITCENTERID);
      

      // ��֯�������
      SOCurrencyRule currule = new SOCurrencyRule(keyValue);
      currule.setCurrency(rows);

      SOExchangeRateRule exrule = new SOExchangeRateRule(keyValue);
      exrule.calcBodyExchangeRates(rows);

      // ���š�ȫ�ֻ��ʼ���
      SOGlobalExchangeRate globalraterule = new SOGlobalExchangeRate(keyValue);
      globalraterule.calcGlobalExchangeRate(rows);

      SOGroupExchangeRate groupraterule = new SOGroupExchangeRate(keyValue);
      groupraterule.calcGroupExchangeRate(rows);
      SaleOrderVOCalculator vocalcultor = new SaleOrderVOCalculator(billvo);
      int[] changerows = exrule.getRateChangeRow();
      vocalcultor.calculate(changerows, SaleOrderBVO.NEXCHANGERATE);
      
      /******* ���ù��ҡ��������ͺ�����ó��\ѯ˰(61) ********/
      SOCountryInfoRule countryrule = new SOCountryInfoRule(keyValue);
      countryrule.setCountryInfo(rows);
      // ��������
      SOBuysellTriaRule buyflgrule = new SOBuysellTriaRule(keyValue);
      buyflgrule.setBuysellAndTriaFlag(rows);

      int[] buychgrows = buyflgrule.getBuysellChgRow();
      vocalcultor.calculate(buychgrows, SOCalConditionRule.getCalPriceKey());
      // ѯ˰
      SOTaxInfoRule taxInfo = new SOTaxInfoRule(keyValue);
      taxInfo.setTaxInfoByBodyPos(rows);
      int[] taxchgrows = taxInfo.getTaxChangeRows();
      vocalcultor.calculate(taxchgrows, SaleOrderBVO.NTAXRATE);
      /******* ���ù��ҡ��������ͺ�����ó��\ѯ˰(61) ********/

      // ֱ�˲�
      DirectStoreRule dirstore = new DirectStoreRule(keyValue);
      dirstore.setSendStordoc(rows);
      // ȡĬ��������֯Ҫ����ȡ�ֿ����
      orgrelrule.setTrafficOrg(trafficrows);

    }

  }

  @Override
  protected String getSrcBillTypeCode() {
    return CTBillType.SaleDaily.getCode();
  }
}