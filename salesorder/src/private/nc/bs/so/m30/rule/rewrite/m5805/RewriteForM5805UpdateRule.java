package nc.bs.so.m30.rule.rewrite.m5805;

import java.util.List;
import java.util.Map;

import nc.bs.so.m30.maintain.util.RewriteBillUtil;
import nc.impl.pubapp.bill.rewrite.BillRewriter;
import nc.impl.pubapp.bill.rewrite.RewritePara;
import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.vo.so.m30.entity.SaleOrderVO;

/**
 * @author zhangjjs
 * @date 2018-3-16
 */
public class RewriteForM5805UpdateRule implements ICompareRule<SaleOrderVO> {

	@Override
	public void process(SaleOrderVO[] vo, SaleOrderVO[] originvo) {
		// ���û�д��ֱ�ӽ��л�д����
		RewriteBillUtil rewriteUtil = new RewriteBillUtil();
		BillRewriter srctool = rewriteUtil.getSrc5805BillRewriter();
		Map<String, List<RewritePara>> rwParaMap = srctool.splitForUpdate(vo, originvo);
		// ��д������ϸ��
		if (null != rwParaMap) {
			List<RewritePara> paramList = rwParaMap.get("5805");
			if (null != paramList && !paramList.isEmpty()) {
				rewriteUtil.reWriteSrc5805(paramList);
			}
		}
	}

}