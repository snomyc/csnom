package com.snomyc.common.base.core.persistence;

import com.snomyc.common.util.CastUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 
 * 类名称：PagingHibernateJdbcDao<br>
 * 类描述：基于Hibernate的原生sql分页查询支持多数据库<br>
 * @version v1.0
 *
 */
@Repository
public class PagingHibernateJdbcDao {

    @PersistenceContext
    private EntityManager em;

    // 避免影响遗留代码，开放此方法
    public Query createSQLQuery(String sql) {
        // 得到Hibernate Session
        Session session = em.unwrap(Session.class);
        return session.createSQLQuery(sql);
    }


    /**
     * 批量插入
     * @param list
     */
	 @Transactional
	 public void batchInsert(@SuppressWarnings("rawtypes") List list){
		 for (int i = 0; i < list.size(); i++) {
			 em.persist(list.get(i));
			 if (i % 30 == 0) {
				 em.flush();
				 em.clear();
			 }
		  }
	 }
	 @Transactional
	 public void batchInsertObject(Object object){
			 em.persist(object);
	 }
	 /**
	  * 批量更新
	  * @param list
	  */
	 @Transactional
	 public void batchUpdate(@SuppressWarnings("rawtypes") List list){
		 for (int i = 0; i < list.size(); i++) {
			 em.merge(list.get(i));
			 if (i % 30 == 0) {
				 em.flush();
				 em.clear();
			 }
		 }
	 }

    /**
     * count查询
     * 
     * @param sql 查询语句
     * @return 结果集行数
     */
    public long count(String sql) {
        sql = createCountSql(sql);
        return count(createSQLQuery(sql));
    }

    /**
     * 带参数count查询
     * 
     * @param sql 查询语句，参数用 ? 代替
     * @param args 查询参数
     * @return 结果集行数
     */
    public long count(String sql, Object[] args) {
        sql = createCountSql(sql);
        Query query = createSQLQuery(sql);
        query = setParameters(query, args);
        return count(query);
    }

    /**
     * 查询结果集
     *
     * @param sql 查询语句
     * @param clazz 返回domain实体的class
     * @return 分页实体
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findList(String sql, Class<T> clazz) {
        List<T> results;
        Query query = createSQLQuery(sql);
        query.setResultTransformer(new EntityResultTransformer(clazz));
        results = query.list();
        return results;
    }
    @SuppressWarnings("unchecked")
    public <T> List<T> findList(String sql , Object[] args, Class<T> clazz){
    	Query query = createSQLQuery(sql);
    	 query = setParameters(query, args);
    	 query.setResultTransformer(new EntityResultTransformer(clazz));
    	List<T> results =query.list();
    	return results;
    }

    /**
     * 分页查询：查询结果集
     * 
     * @param sql 查询语句
     * @param page 分页条件
     * @param clazz 返回domain实体的class
     * @return 分页实体
     */
    @SuppressWarnings("unchecked")
    public <T> Page<T> findPage(String sql, Pageable page, Class<T> clazz) {
        long count = count(sql);
        List<T> results;
        if (count == 0) {
            results = null;
        } else {
            Query query = createSQLQuery(sql);
            query = setPageable(query, page);
            query.setResultTransformer(new EntityResultTransformer(clazz));
            results = query.list();
        }
        return wrapResult(results, page, count);
    }

    /**
     * 分页查询：查询结果集
     * 
     * @param sql 查询语句，参数用 ? 代替
     * @param args 查询参数
     * @param page 分页条件
     * @param clazz 返回domain实体的class
     * @return 分页实体
     */
    @SuppressWarnings("unchecked")
    public <T> Page<T> findPage(String sql, Object[] args, Pageable page, Class<T> clazz) {
        long count = count(sql, args);
        List<T> results;
        if (count == 0) {
            results = null;
        } else {
            Query query = createSQLQuery(sql);
            query = setParameters(query, args);
            query = setPageable(query, page);
            query.setResultTransformer(new EntityResultTransformer(clazz));
            results = query.list();
        }
        return wrapResult(results, page, count);
    }

    /**
     * 分页查询：查询结果集
     * 
     * @param sql 查询语句
     * @param page 分页条件
     * @return 分页实体
     */
    @SuppressWarnings("unchecked")
    public Page<Map<String, Object>> findPage(String sql, Pageable page) {
        long count = count(sql);
        List<Map<String, Object>> results;
        if (count == 0) {
            results = null;
        }
        Query query = createSQLQuery(sql);
        query = setPageable(query, page);
        query.setResultTransformer(new MapResultTransformer());
        results = query.list();
        return wrapResult(results, page, count);
    }

    /**
     * 分页查询：查询结果集
     * @param sql 查询语句
     * @param countSql 查询总数语句
     * @param page 分页条件
     * @return 分页实体
     */
    @SuppressWarnings("unchecked")
    public Page<Map<String, Object>> findByPage(String sql, String countSql, Pageable page) {
        long count = count(countSql);
        List<Map<String, Object>> results;
        if (count == 0) {
            results = null;
        }
        Query query = createSQLQuery(sql);
        query = setPageable(query, page);
        query.setResultTransformer(new MapResultTransformer());
        results = query.list();
        return wrapResult(results, page, count);
    }

    /**
     * 分页查询：查询结果集
     * 
     * @param sql 查询语句，参数用 ? 代替
     * @param args 查询参数
     * @param page 分页条件
     * @return 分页实体
     */
    @SuppressWarnings("unchecked")
    public Page<Map<String, Object>> findPage(String sql, Object[] args, Pageable page) {
        long count = count(sql, args);
        List<Map<String, Object>> results;
        if (count == 0) {
            results = null;
        } else {
            Query query = createSQLQuery(sql);
            query = setPageable(query, page);
            query = setParameters(query, args);
            query.setResultTransformer(new MapResultTransformer());
            results = query.list();
        }
        return wrapResult(results, page, count);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findMap(String sql, Object[] args) {
    	  List<Map<String, Object>> results;
    		Query query = createSQLQuery(sql);
    		query = setParameters(query, args);
    		query.setResultTransformer(new MapResultTransformer());
    		results = query.list();
    	return results;
    }

    // 创建count语句
    private String createCountSql(String sql) {
        StringBuilder countSql = new StringBuilder();
        countSql.append("select count(1) from (");
        countSql.append(sql);
        countSql.append(") t");
        return countSql.toString();
    }

    // 简单封装count查询
    private long count(Query query) {
        return CastUtil.castLong(query.uniqueResult());
    }

    // 设置查询参数
    private Query setParameters(Query query, Object[] args) {
        if (args != null && args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                query.setParameter(i, args[i]);
            }
        }
        return query;
    }

    // 设置分页参数
    private Query setPageable(Query query, Pageable page) {
        query.setFirstResult(page.getOffset());
        query.setMaxResults(page.getPageSize());
        return query;
    }

    // 将查询结果集封装为标准格式
    private <T> Page<T> wrapResult(List<T> result, Pageable page, long count) {
        if (result == null) {
            result = Collections.emptyList();
        }
        Page<T> thisPage = new PageImpl<>(result, page, count);
        return thisPage;
    }
    
    

}
