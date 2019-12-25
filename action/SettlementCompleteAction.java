package com.internousdev.spring.action;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.spring.dao.CartInfoDAO;
import com.internousdev.spring.dao.PurchaseHistoryInfoDAO;
import com.internousdev.spring.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class SettlementCompleteAction extends ActionSupport implements SessionAware {

	private String destinationId;
	private Map<String, Object> session;

	public String execute() throws SQLException {

		boolean deleteCartItemInfoBoolean = false;

		// セッションのログインフラグを取得し、数値が1であれば処理続行
		// 値が0あるいはnullならばloginErrorを返す
		String tempLogined = String.valueOf(session.get("loginFlg"));
		int logined = "null".equals(tempLogined) ? 0 : Integer.parseInt(tempLogined);
		if (logined != 1) {
			return "loginError";
		}

		// セッションよりuserIdを取得し、それを引数にgetProductInfoinCartを実行
		// 実行の返り値としてcartInfoListを取得
		String userId = session.get("userId").toString();

		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		List<CartInfoDTO> cartInfoList = cartInfoDAO.getProductInfoinCart(userId);

		// cartInfoListに入っているprodoctId,count,productPriceに加え、
		// userId,destinationIdを商品購入履歴テーブルに登録する
		// リストの数値を引数に使うために拡張for文を使用
		// countで返り値を取得する
		PurchaseHistoryInfoDAO purchaseHistoryInfoDAO = new PurchaseHistoryInfoDAO();
		int count = 0;
		for (CartInfoDTO dto : cartInfoList) {
			count += purchaseHistoryInfoDAO.insertPurchaseHistoryInfo(userId, dto.getProductId(), dto.getProductCount(),
					dto.getPrice(), Integer.parseInt(destinationId));
		}

		// if文で商品購入履歴テーブルへのinsertの成否判定
		// count>0ならテーブル書き込み成功しているので、カート情報削除を実行する
		// それ以外の場合はinsertが失敗しているのでERRORを返す
		// カート情報削除が成功すればtrueが返ってくるので、if文で判定しSUCCESSを返す
		// falseならば書き込み失敗なのでERRORを返す
		if (count > 0) {
			deleteCartItemInfoBoolean = cartInfoDAO.deleteCartItemBuySuccess(userId);
			if (deleteCartItemInfoBoolean) {
				return SUCCESS;
			}
		}
		return ERROR;
	}

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
