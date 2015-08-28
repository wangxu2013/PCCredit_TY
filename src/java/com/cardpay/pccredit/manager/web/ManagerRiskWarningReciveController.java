package com.cardpay.pccredit.manager.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.manager.filter.ManagerRiskWarningFilter;
import com.cardpay.pccredit.manager.model.ManagerRiskWarning;
import com.cardpay.pccredit.manager.service.ManagerRiskWarningService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;
import com.wicresoft.util.web.RequestHelper;

/**
 * 客户经理警示书（客户经理）类的描述
 * 
 * @author 王海东
 * @created on 2014-11-12
 * 
 * @version $Id:$
 */
@Controller
@RequestMapping("/manager/managerriskcheck/*")
@JRadModule("manager.managerriskcheck")
public class ManagerRiskWarningReciveController extends BaseController {

	@Autowired
	private ManagerRiskWarningService managerRiskWarningService;

	/**
	 * 浏览页面
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView browse(@ModelAttribute ManagerRiskWarningFilter filter, HttpServletRequest request) {
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		filter.setCustomerManagerId(loginId);
		QueryResult<ManagerRiskWarningForm> result = managerRiskWarningService.findReciveManagerRiskWarningByFilter(filter);
		JRadPagedQueryResult<ManagerRiskWarningForm> pagedResult = new JRadPagedQueryResult<ManagerRiskWarningForm>(filter, result);
		JRadModelAndView mv = new JRadModelAndView("manager/manager_risk_warning/manager_risk_recive_browse", request);
		mv.addObject("loginId", loginId);
		mv.addObject(PAGED_RESULT, pagedResult);

		return mv;
	}

	/**
	 * 修改页面
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "change.page")
//	@JRadOperation(JRadOperation.CHANGE)
	public AbstractModelAndView change(@ModelAttribute ManagerRiskWarningFilter filter, HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/manager/manager_risk_warning/manager_risk_recive_feedback", request);

		String riskId = RequestHelper.getStringValue(request, ID);
		if (StringUtils.isNotEmpty(riskId)) {
			ManagerRiskWarning managerRiskWarning = managerRiskWarningService.findManagerRiskWarningById(riskId);
			mv.addObject("managerRiskWarning", managerRiskWarning);
		}
		mv.addObject("id", riskId);
		return mv;
	}

	/**
	 * 执行反馈的修改
	 * 
	 * @param managerRiskWarningForm
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updatefeedback.json")
	//@JRadOperation(JRadOperation.CHANGE)
	public JRadReturnMap updatefeedback(@ModelAttribute ManagerRiskWarningForm managerRiskWarningForm, HttpServletRequest request) {

		JRadReturnMap returnMap = WebRequestHelper.requestValidation(getModuleName(), managerRiskWarningForm);
		if (returnMap.isSuccess()) {
			try {
				String riskId = request.getParameter("id");
				String feedback = request.getParameter("feedback");
				ManagerRiskWarning managerRiskWarning = managerRiskWarningForm.createModel(ManagerRiskWarning.class);
				managerRiskWarning.setFeedback(feedback);
				managerRiskWarning.setId(riskId);
				managerRiskWarning.setFeedbackTime(new Date());
				managerRiskWarningService.updateFeedBackById(riskId, feedback);
				returnMap.put(RECORD_ID, managerRiskWarning.getId());
				returnMap.addGlobalMessage(CHANGE_SUCCESS);
			} catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}
		return returnMap;
	}

	/**
	 * 显示页面
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "display.page")
	@JRadOperation(JRadOperation.DISPLAY)
	public AbstractModelAndView display(@ModelAttribute ManagerRiskWarningFilter filter, HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/manager/manager_risk_warning/manager_risk_recive_display", request);

		String riskId = RequestHelper.getStringValue(request, ID);
		if (StringUtils.isNotEmpty(riskId)) {
			ManagerRiskWarning managerRiskWarning = managerRiskWarningService.findManagerRiskWarningById(riskId);
			mv.addObject("managerRiskWarning", managerRiskWarning);
			mv.addObject("id", riskId);
		}
		return mv;
	}

}
