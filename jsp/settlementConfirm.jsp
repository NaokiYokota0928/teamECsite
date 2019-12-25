<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/spring.css">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/page-title.css">
<link rel="stylesheet" href="./css/message.css">
<link rel="stylesheet" href="./css/submit-btn.css">
<link rel="stylesheet" href="./css/vertical-table.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript" src="./js/setAction.js"></script>
<title>決済確認</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div id="contents">
		<h1>決済確認画面</h1>
		<!--宛先情報のリストが存在し、1件以上の宛先情報が入っている場合に表示 -->
		<s:if
			test="destinationInfoDTOList != null && destinationInfoDTOList.size() >0">
			<div class="info">宛先情報を選択してください。</div>
			<s:form id="form">
				<table class="vertical-table">
					<thead>
						<tr>
							<th><s:label value="#" /></th>
							<th><s:label value="姓" /></th>
							<th><s:label value="名" /></th>
							<th><s:label value="ふりがな" /></th>
							<th><s:label value="住所" /></th>
							<th><s:label value="電話番号" /></th>
							<th><s:label value="メールアドレス" /></th>
						</tr>
					</thead>
					<tbody>
						<!-- 	radioボタンに対応した宛先IDがActionにsetされる。 -->
						<!-- 	リストの最初のradioボタンをcheckedにする。 -->
						<!-- 	遷移先のActionに値を送るのでname="destinationId"をつける -->
						<s:iterator value="destinationInfoDTOList" status="st">
							<tr>
								<td><s:if test="#st.index ==0">
										<input type="radio" name="destinationId" checked="checked"
											value="<s:property value="destinationId" />" />
									</s:if> <s:else>
										<input type="radio" name="destinationId"
											value="<s:property value="destinationId" />" />
									</s:else></td>
								<td><s:property value="familyName" /></td>
								<td><s:property value="firstName" /></td>
								<!-- ひとつの項目にフルネームで表示 -->
								<td><s:property value="familyNameKana" /> <span> </span> <s:property
										value="firstNameKana" /></td>
								<td><s:property value="userAddress" /></td>
								<td><s:property value="telNumber" /></td>
								<td><s:property value="email" /></td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
				<!-- ひとつのフォームで複数の遷移先を配置。jsで設定 -->
				<div class="submit-btn-box">
					<s:submit value="決済" class="submit-btn"
						onClick="setAction('SettlementCompleteAction')" />
				</div>
				<div class="submit-btn-box">
					<s:submit value="削除" class="submit-btn"
						onClick="setAction('DeleteDestinationAction')" />
				</div>
			</s:form>
		</s:if>
		<!-- 	宛先情報のリストが存在しない、存在しても宛先情報が0件の場合に表示 -->
		<s:else>
			<div class="info">宛先情報がありません。</div>
		</s:else>
		<!-- リストの存在に関わらず表示するため独立して記述 -->
		<div class="submit-btn-box">
			<s:form action="CreateDestinationAction">
				<s:submit value="新規宛先登録" class="submit-btn" />
			</s:form>
		</div>
	</div>
</body>
</html>