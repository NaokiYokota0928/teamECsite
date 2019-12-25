package com.internousdev.spring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.spring.dto.DestinationInfoDTO;
import com.internousdev.spring.util.DBConnector;

/**
 * 宛先情報テーブル(destination_info)に対してSQL文を送るためのクラス
 * 行う操作はINSERT(宛先情報登録機能で使用)、SELECTとDELETE(共に決済機能で使用)の３つ
 */
public class DestinationInfoDAO {

	/**
	 * 宛先情報登録機能で使用
	 * 宛先情報テーブル(destination_info)に登録したい値を引数にして宛先情報テーブル(destination_info)にアクセス
	 * →登録に成功すれば１/取得失敗時は０をActionに返す
	 */
	public int createDestinationInfo(String userId, String familyName, String firstName, String familyNameKana,
			String firstNameKana, String userAddress, String telNumber, String email) throws SQLException {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		int result = 0;
		String sql = "INSERT INTO destination_info" + "(user_id," + "family_name," + "first_name," + "family_name_kana,"
				+ "first_name_kana," + "user_address," + "tel_number," + "email," + "regist_date," + "update_date)"
				+ "VALUES(?,?,?,?,?,?,?,?,now(),now())";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, familyName);
			ps.setString(3, firstName);
			ps.setString(4, familyNameKana);
			ps.setString(5, firstNameKana);
			ps.setString(6, userAddress);
			ps.setString(7, telNumber);
			ps.setString(8, email);

			result = ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return result;
	}

	/**
	 * 決済機能で使用 入力されたユーザーIDを引数にして宛先情報テーブル(destination_info)にアクセス
	 * →各宛先情報を取得する/取得失敗時は宛先情報なしメッセージを返す
	 */
	public List<DestinationInfoDTO> getDestinationInfo(String userId) throws SQLException{
		DBConnector dbConnector = new DBConnector();
		Connection con = dbConnector.getConnection();
		List<DestinationInfoDTO> destinationInfoDTOList = new ArrayList<DestinationInfoDTO>();

		String sql = "SELECT id, family_name, first_name, family_name_kana, first_name_kana, user_address, tel_number, email "
				+ "FROM destination_info WHERE user_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				DestinationInfoDTO destinationInfoDTO = new DestinationInfoDTO();
				destinationInfoDTO.setDestinationId(rs.getInt("id"));
				destinationInfoDTO.setFamilyName(rs.getString("family_name"));
				destinationInfoDTO.setFirstName(rs.getString("first_name"));
				destinationInfoDTO.setFamilyNameKana(rs.getString("family_name_kana"));
				destinationInfoDTO.setFirstNameKana(rs.getString("first_name_kana"));
				destinationInfoDTO.setUserAddress(rs.getString("user_address"));
				destinationInfoDTO.setEmail(rs.getString("email"));
				destinationInfoDTO.setTelNumber(rs.getString("tel_number"));
				destinationInfoDTOList.add(destinationInfoDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
				con.close();
		}
		return destinationInfoDTOList;
	}

	/**
	 * 決済機能で使用 ユーザーIDと宛先IDを引数にして宛先情報テーブル(destination_info)の該当レコード(行)を削除
	 * →削除成功なら１/削除失敗時は０を返す
	 */
	public int deleteDestinationInfo(int destinationId, String userId) throws SQLException {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 0;
		String sql = "DELETE FROM destination_info WHERE id=? AND user_id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, destinationId);
			ps.setString(2, userId);
			count = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return count;
	}
}
