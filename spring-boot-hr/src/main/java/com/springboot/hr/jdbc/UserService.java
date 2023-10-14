package com.springboot.hr.jdbc;

/**
 * @author huangran <huangran@kuaishou.com>
 * created on 2022-05-10
 */
//@Service
public class UserService {

//	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//	@Autowired
//	public void setNamedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
//		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
//	}
//
//	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
//		return this.namedParameterJdbcTemplate;
//	}
//
//	public List<User> queryUser(Collection<Integer> userIds) {
//		if (CollectionUtils.isEmpty(userIds)) {
//			return Collections.emptyList();
//		}
//		String sql = "select * from user where id in (:userIds)";
//		List<User> userList = namedParameterJdbcTemplate
//				.query(sql, Collections.singletonMap("userIds", userIds), new BeanPropertyRowMapper<>(User.class));
//		return CollectionUtils.isEmpty(userIds) ? Collections.emptyList() : userList;
//	}

}
