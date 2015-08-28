package com.cardpay.pccredit.report.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.common.PieJsonData;
import com.cardpay.pccredit.report.dao.StatisticalCommonDao;
import com.cardpay.pccredit.report.model.NameValueRecord;

/**
 * @author chenzhifang
 *
 * 2014-12-18下午3:38:35
 */
@Service
public class StatisticalCommonService {
	@Autowired
	private StatisticalCommonDao statisticalCommonDao;
	
	/**
     * 统计当前进件状况
     * @param filter
     * @return
     */
	public List<NameValueRecord> statisticalApplicationStatus(){
		return statisticalCommonDao.statisticalApplicationStatus();
	}
	
	/**
     * 统计当前进件状况json
     * @param filter
     * @return
     */
	public String getApplicationStatusJson(){
		List<PieJsonData> pList = getPieJsonData(statisticalApplicationStatus());
		PieJsonData pieJsonData = pList.get(0);
		pieJsonData.setSliced(true);
		pieJsonData.setSelected(true);
		
		return JSONArray.fromObject(pList).toString();
	}
	
	/**
     * 统计当前贷款状况
     * @param filter
     * @return
     */
	public List<NameValueRecord> statisticalCreditStatus(){
		return statisticalCommonDao.statisticalCreditStatus();
	}
	
	/**
     * 统计当前贷款状况json
     * @param filter
     * @return
     */
	public String getCreditStatusJson(){
		List<PieJsonData> pList = getPieJsonData(statisticalCreditStatus());
		PieJsonData pieJsonData = pList.get(1);
		pieJsonData.setSliced(true);
		pieJsonData.setSelected(true);
		
		DecimalFormat df = new DecimalFormat("####.0000");
		for(int i =0; i < pList.size(); i++){
			pieJsonData = pList.get(i);
			pieJsonData.setY(Double.valueOf(df.format(pieJsonData.getY())));
		}
		
		return JSONArray.fromObject(pList).toString();
	}
	
	/**
     * 统计当前卡片状况
     * @param filter
     * @return
     */
	public List<NameValueRecord> statisticalCardStatus(){
		return statisticalCommonDao.statisticalCardStatus();
	}
	
	/**
     * 统计当前卡片状况柱状图标签
     * @param filter
     * @return
     */
	public String getCardStatusCategoriesJson(List<NameValueRecord> records){
		List<String> list = new ArrayList<String>();
		for(NameValueRecord nameValueRecord : records){
			list.add(nameValueRecord.getName());
		}
		return JSONArray.fromObject(list).toString();
	}
	
	/**
     * 统计当前卡片状况柱状图标签
     * @param filter
     * @return
     */
	public String getCardStatusValuesJson(List<NameValueRecord> records){
		List<Double> list = new ArrayList<Double>();
		for(NameValueRecord nameValueRecord : records){
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				list.add(Double.valueOf(nameValueRecord.getValue()));
			}else{
				list.add(0d);
			}
			
		}
		return JSONArray.fromObject(list).toString();
	}
	
	public List<PieJsonData> getPieJsonData(List<NameValueRecord> list){
		List<PieJsonData> pList= new ArrayList<PieJsonData>();
		for(NameValueRecord nameValueRecord : list){
			PieJsonData pieJsonData = new PieJsonData();
			pieJsonData.setName(nameValueRecord.getName());
			
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				pieJsonData.setY(Double.valueOf(nameValueRecord.getValue()));
			}else{
				pieJsonData.setY(0);
			}
			pieJsonData.setSliced(false);
			pieJsonData.setSelected(false);
			pList.add(pieJsonData);
		}
		return pList;
	}
}
