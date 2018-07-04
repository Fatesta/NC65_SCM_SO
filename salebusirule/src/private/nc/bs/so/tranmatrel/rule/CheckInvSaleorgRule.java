package nc.bs.so.tranmatrel.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.ml.NCLangResOnserver;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.sale.MaterialSaleVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.so.tranmatrel.entity.TranMatRelBVO;
import nc.vo.so.tranmatrel.entity.TranMatRelHVO;
import nc.vo.so.tranmatrel.entity.TranMatRelVO;

/**
 * @description
 * ���۶������Ϲ�ϵ����ǰ��������Ƿ���䵽������֯
 * @scene
 * ���۶������Ϲ�ϵ�������޸ı���ǰ
 * @param
 * ��
 * @author gdsjw
 */
public class CheckInvSaleorgRule implements IRule<TranMatRelVO> {
  public CheckInvSaleorgRule() {
    //
  }

  @Override
  public void process(TranMatRelVO[] vos) {
    for (TranMatRelVO vo : vos) {
      // ����ǲ�ȫVO��У��ʱ������Ҫ������״̬
      this.checkInvSaleorg(vo);
    }
  }

  private void checkInvSaleorg(TranMatRelVO bill) {
    TranMatRelHVO header = bill.getParentVO();
    String pk_saleorg = header.getPk_org();
    List<String> materialvids = new ArrayList<String>();
    TranMatRelBVO[] items = bill.getChildrenVO();
    if ((items != null) && (items.length > 0)) {
      for (TranMatRelBVO item : items) {
        int vostatus = item.getStatus();
        if ((vostatus == VOStatus.DELETED) || (vostatus == VOStatus.UNCHANGED)) {
          // �����ɾ����û�仯����
          continue;
        }
        String material_v = item.getPk_material_v();
        if (!(PubAppTool.isNull(material_v))) {
          materialvids.add(material_v);
        }
      }
    }
    if (materialvids.size() == 0) {
      return;
    }
    String[] fields = new String[] {
      "pk_materialsale", "pk_material", "pk_group", "pk_org"
    };
    try {
      List<String> notsaleorgmaterialvids = new ArrayList<String>();
      Map<String, MaterialSaleVO> matsalMap =
          MaterialPubService.queryMaterialSaleInfoByPks(
              materialvids.toArray(new String[0]), pk_saleorg, fields);
      for (String materialvid : materialvids) {
        if ((matsalMap == null) || (!matsalMap.containsKey(materialvid))) {
          notsaleorgmaterialvids.add(materialvid);
        }
      }
      if (notsaleorgmaterialvids.size() > 0) {
        fields = new String[] {
          "code", "name"
        };
        MaterialVO[] materialvos =
            MaterialPubService.queryMaterialBaseInfoByPks(
                notsaleorgmaterialvids.toArray(new String[0]), fields);
        StringBuilder errmessage = new StringBuilder();
        /* errmessage.append("��ѡ����[");*//*�����޸�*/
        for (MaterialVO materialvo : materialvos) {
          errmessage.append(materialvo.getCode()).append("(")
              .append(materialvo.getName()).append("),");
        }
        errmessage.deleteCharAt(errmessage.length() - 1);
       /* errmessage.append("]��δ���䵽������֯�����������嶩���������Ϲ�ϵ��");*//*�����޸�*/
        /* throw new BusinessException(errmessage.toString());*//*�����޸�*/
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4006007_0", "04006007-0016", null, new String[]{errmessage.toString()})/*��ѡ����[{0}]��δ���䵽������֯�����������嶩���������Ϲ�ϵ��*/);/*�����޸�*/
      }
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
  }

}