package com.internousdev.spring.action;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.spring.dao.DestinationInfoDAO;
import com.internousdev.spring.dto.DestinationInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class SettlementConfirmAction extends ActionSupport implements SessionAware {

	private Map<String, Object> session;
	private List<DestinationInfoDTO> destinationInfoDTOList;
	String userId;

	public String execute() throws SQLException {

		// セッションのログインフラグを取得し、数値が1であれば処理続行
		// 値が0あるいはnullならばloginErrorを返す
		String tempLogined = String.valueOf(session.get("loginFlg"));
		int logined = "null".equals(tempLogined) ? 0 : Integer.parseInt(tempLogined);
		if (logined != 1) {
			return "loginError";
		}

		String userId = session.get("userId").toString();

		// 宛先情報テーブルからユーザーIDに紐付いた宛先情報を取得
		// DAOでリスト化してるのでリストで宣言
		DestinationInfoDAO destinationInfoDAO = new DestinationInfoDAO();
		destinationInfoDTOList = destinationInfoDAO.getDestinationInfo(userId);

		// settlementConfirm.jspへ遷移
		// 下部にあるgetterでリストをjspに受け渡し
		return SUCCESS;
	}

	public List<DestinationInfoDTO> getDestinationInfoDTOList() {
		return destinationInfoDTOList;
	}

	public void setDestinationInfoDTOList(List<DestinationInfoDTO> destinationInfoDTOList) {
		this.destinationInfoDTOList = destinationInfoDTOList;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
