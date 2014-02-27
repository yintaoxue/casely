package org.codeset.casely.druid_uc.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.codeset.casely.druid_uc.bean.Post;

public class PostDao {
	
	public List<Post> getPosts() {
		List<Post> list = new ArrayList<Post>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = PoolUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from posts");
			while (rs.next()) {
				String id = rs.getString("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Post p = new Post();
				p.setId(id);
				p.setTitle(title);
				p.setContent(content);
				list.add(p);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != rs) {
					rs.close();
				}
				if (null != stmt) {
					stmt.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static void main(String[] args) {
		
	}

}
