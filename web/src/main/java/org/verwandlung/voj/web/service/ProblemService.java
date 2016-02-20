package org.verwandlung.voj.web.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.verwandlung.voj.web.mapper.CheckpointMapper;
import org.verwandlung.voj.web.mapper.ProblemCategoryMapper;
import org.verwandlung.voj.web.mapper.ProblemMapper;
import org.verwandlung.voj.web.mapper.ProblemTagMapper;
import org.verwandlung.voj.web.model.Checkpoint;
import org.verwandlung.voj.web.model.Problem;
import org.verwandlung.voj.web.model.ProblemCategory;
import org.verwandlung.voj.web.model.ProblemTag;
import org.verwandlung.voj.web.util.SlugifyUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 试题类(Problem)的业务逻辑层.
 * @author Haozhe Xie
 */
@Service
@Transactional
public class ProblemService {
    /**
     * 获取试题的起始编号.
     * @return 试题的起始编号
     */
    public long getFirstIndexOfProblems() {
        return problemMapper.getLowerBoundOfProblems();
    }
    
    /**
     * 获取试题的结束编号.
     * @param isPublicOnly - 是否只筛选公开试题
     * @param offset - 试题唯一标识符的起始序号
     * @param limit - 每次加载试题的数量
     * @return 试题的结束编号
     */
    public long getLastIndexOfProblems(boolean isPublicOnly, long offset, int limit) {
        return problemMapper.getUpperBoundOfProblemsWithLimit(isPublicOnly, offset, limit);
    }
    
    /**
     * 通过试题的唯一标识符获取试题的详细信息.
     * @param problemId - 试题的唯一标识符
     * @return 试题的详细信息
     */
    public Problem getProblem(long problemId) {
        return problemMapper.getProblem(problemId);
    }
    
    /**
     * 获取公开的试题列表.
     * @param offset - 试题唯一标识符的起始序号
     * @param keyword - 关键字
     * @param problemCategorySlug - 试题分类的唯一英文缩写
     * @param isPublicOnly - 是否只筛选公开试题
     * @param limit - 每次加载试题的数量
     * @return 试题列表(List<Problem>对象)
     */
    public List<Problem> getProblemsUsingFilters(long offset, String keyword, String problemCategorySlug, boolean isPublicOnly, int limit) {
        ProblemCategory problemCategory = problemCategoryMapper.getProblemCategoryUsingCategorySlug(problemCategorySlug);
        int problemCategoryId = 0;
        if ( problemCategory != null ) {
            problemCategoryId = problemCategory.getProblemCategoryId();
        }
        return problemMapper.getProblemsUsingFilters(keyword, problemCategoryId, isPublicOnly, offset, limit);
    }
    
    /**
     * 获取试题的总数量.
     * @param keyword - 关键字
     * @param problemCategorySlug - 试题分类的唯一英文缩写
     * @param isPublicOnly - 是否只筛选公开试题
     * @return 试题的总数量
     */
    public long getNumberOfProblemsUsingFilters(String keyword, String problemCategorySlug, boolean isPublicOnly) {
        ProblemCategory problemCategory = problemCategoryMapper.getProblemCategoryUsingCategorySlug(problemCategorySlug);
        int problemCategoryId = 0;
        if ( problemCategory != null ) {
            problemCategoryId = problemCategory.getProblemCategoryId();
        }
        return problemMapper.getNumberOfProblemsUsingFilters(keyword, problemCategoryId, isPublicOnly);
    }
    
    /**
     * 获取试题的分类列表.
     * @param problemId - 试题的唯一标识符.
     * @return 包含试题分类的列表
     */
    public List<ProblemCategory> getProblemCategoriesUsingProblemId(long problemId) {
    	return problemCategoryMapper.getProblemCategoriesUsingProblemId(problemId);
    }
    
    /**
     * 获取试题的标签列表.
     * @param problemId - 试题的唯一标识符.
     * @return 包含试题标签的列表
     */
    public List<ProblemTag> getProblemTagsUsingProblemId(long problemId) {
    	return problemTagMapper.getProblemTagUsingProblemId(problemId);
    }
    
    /**
     * 获取全部的试题分类.
     * @return 包含全部试题分类的列表
     */
    public List<ProblemCategory> getProblemCategories() {
        return problemCategoryMapper.getProblemCategories();
    }
    
    /**
     * [此方法仅供管理员使用]
     * 获取全部试题的总数量.
     * @return 全部试题的总数量
     */
    public long getNumberOfProblems() {
        return problemMapper.getNumberOfProblems();
    }
    
    /**
     * [此方法仅供管理员使用]
     * 获取系统中全部试题测试点的数量(包括私有试题).
     * @return 系统中全部试题测试点的数量
     */
    public long getNumberOfCheckpoints() {
        return checkpointMapper.getNumberOfCheckpoints();
    }
    
    /**
     * [此方法仅供管理员使用]
     * 获取某个试题的测试数据集.
     * @param problemId - 试题的唯一标识符
     * @return 某个试题的测试数据列表
     */
    public List<Checkpoint> getCheckpointsUsingProblemId(long problemId) {
    	List<Checkpoint> checkpoints = checkpointMapper.getCheckpointsUsingProblemId(problemId);
    	return checkpoints;
    }
    
    /**
     * [此方法仅供管理员使用]
     * 创建试题.
     * @param problemName - 试题名称
     * @param timeLimit - 时间限制
     * @param memoryLimit - 内存占用限制
     * @param description - 试题描述
     * @param hint - 试题提示
     * @param inputFormat - 输入格式
     * @param outputFormat - 输出格式
     * @param inputSample - 输入样例
     * @param outputSample - 输出样例
     * @param testCases - 测试用例(JSON 格式)
     * @param problemCategories - 试题分类(JSON 格式)
     * @param problemTags - 试题标签((JSON 格式)
     * @param isPublic - 试题是否公开
     * @param isExactlyMatch - 测试点是否精确匹配
     * @return 包含试题创建结果的Map<String, Object>对象
     */
    public Map<String, Object> createProblem(String problemName, int timeLimit, int memoryLimit, 
    		String description, String hint, String inputFormat, String outputFormat, 
    		String inputSample, String outputSample, String testCases, String problemCategories, 
    		String problemTags, boolean isPublic, boolean isExactlyMatch) {
    	Problem problem = new Problem(isPublic, problemName, timeLimit, memoryLimit, 
    	        description, inputFormat, outputFormat, inputSample, outputSample, hint);
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) getProblemCreationResult(problem);
        
        if ( (boolean) result.get("isSuccessful") ) {
            problemMapper.createProblem(problem);
            
            long problemId = problem.getProblemId();
            createTestCases(problemId, testCases, isExactlyMatch);
            createProblemCategories(problemId, problemCategories);
            createProblemTags(problemId, problemTags);
            
            result.put("problemId", problemId);
        }
        return result;
    }
    
    /**
     * 检查试题信息是否合法.
     * @param problem - 待创建的试题
     * @return 包含试题创建结果的Map<String, Boolean>对象
     */
    private Map<String, ? extends Object> getProblemCreationResult(Problem problem) {
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("isProblemNameEmpty", problem.getProblemName().isEmpty());
        result.put("isProblemNameLegal", isProblemNameLegal(problem.getProblemName()));
        result.put("isTimeLimitLegal", problem.getTimeLimit() > 0);
        result.put("isMemoryLimitLegal", problem.getMemoryLimit() > 0);
        result.put("isDescriptionEmpty", problem.getDescription().isEmpty());
        result.put("isInputFormatEmpty", problem.getInputFormat().isEmpty());
        result.put("isOutputFormatEmpty", problem.getOutputFormat().isEmpty());
        result.put("isInputSampleEmpty", problem.getSampleInput().isEmpty());
        result.put("isOutputSampleEmpty", problem.getSampleOutput().isEmpty());
        
        boolean isSuccessful = !result.get("isProblemNameEmpty")  &&  result.get("isProblemNameLegal") &&
                                result.get("isTimeLimitLegal")    &&  result.get("isMemoryLimitLegal") &&
                               !result.get("isDescriptionEmpty")  && !result.get("isInputFormatEmpty") &&
                               !result.get("isOutputFormatEmpty") && !result.get("isInputSampleEmpty") &&
                               !result.get("isOutputSampleEmpty");
        result.put("isSuccessful", isSuccessful);
        return result;
    }
    
    /**
     * [此方法仅供管理员使用]
     * 编辑试题.
     * @param problemId - 试题唯一标识符
     * @param problemName - 试题名称
     * @param timeLimit - 时间限制
     * @param memoryLimit - 内存占用限制
     * @param description - 试题描述
     * @param hint - 试题提示
     * @param inputFormat - 输入格式
     * @param outputFormat - 输出格式
     * @param inputSample - 输入样例
     * @param outputSample - 输出样例
     * @param testCases - 测试用例(JSON 格式)
     * @param problemCategories - 试题分类(JSON 格式)
     * @param problemTags - 试题标签((JSON 格式)
     * @param isPublic - 试题是否公开
     * @param isExactlyMatch - 测试点是否精确匹配
     * @return 包含试题创建结果的Map<String, Object>对象
     */
    public Map<String, Boolean> editProblem(long problemId, String problemName, int timeLimit, 
            int memoryLimit, String description, String hint, String inputFormat, String outputFormat, 
            String inputSample, String outputSample, String testCases, String problemCategories, 
            String problemTags, boolean isPublic, boolean isExactlyMatch) {
        Problem problem = new Problem(problemId, isPublic, problemName, timeLimit, memoryLimit, 
                description, inputFormat, outputFormat, inputSample, outputSample, hint);
        Map<String, Boolean> result = getProblemEditResult(problem);
        
        if ( result.get("isSuccessful") ) {
            problemMapper.updateProblem(problem);
            
            updateTestCases(problemId, testCases, isExactlyMatch);
            updateProblemCategories(problemId, problemCategories);
            updateProblemTags(problemId, problemTags);
        }
        return result;
    }
    
    /**
     * 检查试题信息是否合法.
     * @param problem - 待编辑的试题
     * @return 包含试题编辑结果的Map<String, Boolean>对象
     */
    private Map<String, Boolean> getProblemEditResult(Problem problem) {
        @SuppressWarnings("unchecked")
        Map<String, Boolean> result = (Map<String, Boolean>) getProblemCreationResult(problem);
        
        long problemId = problem.getProblemId();
        if ( problemMapper.getProblem(problemId) != null ) {
            result.put("isProblemExists", true);
        } else {
            result.put("isProblemExists", false);
            result.put("isSuccessful", false);
        }
        return result;
    }
    
    /**
     * 创建测试用例.
     * @param problemId - 试题的唯一标识符
     * @param testCases - 测试用例 (JSON格式)
     * @param isExactlyMatch - 是否精确匹配测试用例
     */
    private void createTestCases(long problemId, String testCases, boolean isExactlyMatch) {
    	JSONArray jsonArray = JSON.parseArray(testCases);
    	
    	for ( int i = 0; i < jsonArray.size(); ++ i ) {
    	    JSONObject testCase = jsonArray.getJSONObject(i);
    	    
    	    int score = 100 / jsonArray.size();
    	    if ( i == jsonArray.size() - 1 ) {
    	        score = 100 - score * i;
    	    }
    	    String input = testCase.getString("input");
    	    String output = testCase.getString("output");
    	    
    	    Checkpoint checkpoint = new Checkpoint(problemId, i, isExactlyMatch, score, input, output);
    	    checkpointMapper.createCheckpoint(checkpoint);
    	}
    }
    
    /**
     * 更新测试用例.
     * 首先删除该试题的全部的测试用例, 然后创建测试用例.
     * @param problemId - 试题的唯一标识符
     * @param testCases - 测试用例 (JSON格式)
     * @param isExactlyMatch - 是否精确匹配测试用例
     */
    private void updateTestCases(long problemId, String testCases, boolean isExactlyMatch) {
        checkpointMapper.deleteCheckpoint(problemId);
        createTestCases(problemId, testCases, isExactlyMatch);
    }
    
    /**
     * 创建试题所属分类.
     * @param problemId - 试题的唯一标识符
     * @param problemCategories - 试题分类唯一英文缩写的JSON数组
     */
    private void createProblemCategories(long problemId, String problemCategories) {
        JSONArray jsonArray = JSON.parseArray(problemCategories);
        
        if ( jsonArray.size() == 0 ) {
            jsonArray.add("uncategorized");
        }
        for ( int i = 0; i < jsonArray.size(); ++ i ) {
            String problemCategorySlug = jsonArray.getString(i);
            ProblemCategory pc = problemCategoryMapper.getProblemCategoryUsingCategorySlug(problemCategorySlug);
            
            problemCategoryMapper.createProblemCategoryRelationship(problemId, pc);
        }
    }
    
    /**
     * 更新试题所属分类.
     * 首先删除该试题的全部分类, 然后重新创建分类关系.
     * @param problemId - 试题的唯一标识符
     * @param problemCategories - 试题分类唯一英文缩写的JSON数组
     */
    private void updateProblemCategories(long problemId, String problemCategories) {
        problemCategoryMapper.deleteProblemCategoryRelationship(problemId);
        createProblemCategories(problemId, problemCategories);
    }
    
    /**
     * 创建试题标签.
     * @param problemId - 试题的唯一标识符
     * @param problemTags - 试题标签的JSON数组
     */
    private void createProblemTags(long problemId, String problemTags) {
        Set<String> problemTagSlugs = new HashSet<String>();
        JSONArray jsonArray = JSON.parseArray(problemTags);
        
        for ( int i = 0; i < jsonArray.size(); ++ i ) {
            String problemTagName = jsonArray.getString(i);
            String problemTagSlug = SlugifyUtils.getSlug(problemTagName);
            
            ProblemTag pt = problemTagMapper.getProblemTagUsingTagSlug(problemTagSlug);
            if ( pt == null ) {
                pt = new ProblemTag(problemTagSlug, problemTagName);
                problemTagMapper.createProblemTag(pt);
            }
            // Fix Bug: Two tags have different tag name but the same tag slug.
            // Example: Hello World / Hello-World
            if ( !problemTagSlugs.contains(problemTagSlug) ) {
                problemTagMapper.createProblemTagRelationship(problemId, pt);
                problemTagSlugs.add(problemTagSlug);
            }
        }
    }
    
    /**
     * 更新试题标签.
     * 首先删除该试题的全部标签, 然后重新创建标签与试题的关系.
     * @param problemId - 试题的唯一标识符
     * @param problemTags - 试题标签的JSON数组
     */
    private void updateProblemTags(long problemId, String problemTags) {
        problemTagMapper.deleteProblemTagRelationship(problemId);
        createProblemTags(problemId, problemTags);
    }
    
    /**
     * 检查试题名称的合法性.
     * @param problemName - 试题名称
     * @return 试题名称是否合法
     */
    private boolean isProblemNameLegal(String problemName) {
    	return problemName.length() <= 128;
    }
    
    /**
     * [此方法仅供管理员使用]
     * 删除指定的试题.
     * @param problemId - 试题的唯一标识符
     */
    public void deleteProblem(long problemId) {
    	problemMapper.deleteProblem(problemId);
    }
    
    /**
     * 自动注入的ProblemMapper对象.
     */
    @Autowired
    private ProblemMapper problemMapper;
    
    /**
     * 自动注入的ProblemCategoryMapper对象.
     */
    @Autowired
    private ProblemCategoryMapper problemCategoryMapper;
    
    /**
     * 自动注入的ProblemTagMapper对象.
     */
    @Autowired
    private ProblemTagMapper problemTagMapper;
    
    /**
     * 自动注入的CheckpointMapper对象.
     */
    @Autowired
    private CheckpointMapper checkpointMapper;
}
