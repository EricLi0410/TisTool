package com.framework.shiro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static boolean isStart = false;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!isStart) {  
			isStart = true;
			initParamMap();
	    }  
	}
	/**
	 * 初始化时加载
	 */
	private void initParamMap(){
		/*TbBizInfoMapper bizInfoMapper = (TbBizInfoMapper) SpringContextUtil.getBean("tbBizInfoMapper", TbBizInfoMapper.class);
		try {
			List<TbBizInfo> list = bizInfoMapper.questionList("X01");
		} catch (YTException e) {
			e.printStackTrace();
		}*/
			/*常见问题*/
//			BaseController.request().getSession().setAttribute("question", );
			/*首页支持单位*/
//			BaseController.request().getSession().setAttribute("cttypeX03",((TbLocalcomMapper) SpringContextUtil.getBean("tbLocalcomMapper", TbLocalcomMapper.class)).newList("X03"));
			/*资料管理*/
//			BaseController.request().getSession().setAttribute("fileName", ((TbAttachMapper)   SpringContextUtil.getBean("tbAttachMapper", TbAttachMapper.class)).imgList());
		
		/*TbParamTypeMapper mapper = (TbParamTypeMapper) SpringContextUtil.getBean("tbParamTypeMapper", TbParamTypeMapper.class);
		try {
			List<String> params = mapper.initCode();
			List<TbParamType> list = mapper.initParam();
			paramMap = new HashMap<String, List<TbParamType>>();
			for (String typeCode : params) {
				List<TbParamType> paramTypes = new ArrayList<TbParamType>();
				for (TbParamType type : list) {
					if(StringUtils.equals(typeCode, type.getTypeCode())){
						paramTypes.add(type);
						paramMap.put(typeCode, paramTypes);
					}
				}
			}
		} catch (YTException e) {
			e.printStackTrace();
		}*/
	}
}
