package org.verwandlung.voj.web.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

import org.verwandlung.voj.web.model.JudgeResult;

/**
 * JudgeResult Data Access Object.
 * 
 * @author Haozhe Xie
 */
@CacheNamespace(implementation = org.mybatis.caches.ehcache.EhcacheCache.class)
public interface JudgeResultMapper {
	/**
	 * 通过评测结果的唯一标识符获取评测结果对象.
	 * @param judgeResultId - 评测结果的唯一标识符
	 * @return 预期的评测结果对象或空引用
	 */
	public JudgeResult getJudgeResultUsingId(@Param("judgeResultId") int judgeResultId);
	
	/**
	 * 通过评测结果的别名获取评测结果对象.
	 * @param judgeResultSlug - 评测结果的别名
	 * @return 预期的评测结果对象或空引用
	 */
	public JudgeResult getJudgeResultUsingSlug(@Param("judgeResultSlug") String judgeResultSlug);
}
