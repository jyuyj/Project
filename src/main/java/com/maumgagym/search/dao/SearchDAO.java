package com.maumgagym.search.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maumgagym.search.dto.SearchTO;

@Repository
public class SearchDAO {
	
	@Autowired
	private DataSource dataSource;

	public ArrayList<SearchTO> searchResult() {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<SearchTO> searchResluts = new ArrayList<SearchTO>();
		
		try {
			conn = this.dataSource.getConnection();
			
			// 글 게시판의 title을 통한 업체 정보, 멤버 게시판의 address을 통한 주소 정보, 멤버쉽 게시판의 price를 통한 가격 정보, 이미지 게시판의 name을 통한 이미지 파일, 태그 게시판의 tag를 통한 글 태그를 가져옴
			// 카테고리 게시판의 seq는 1~9번(운동시설)으로 가져옴
			String sql = "SELECT b.seq, b.title, b.category_seq, c.topic, m.address, ms.price, i.name, t.tag "
					+ "FROM board b LEFT OUTER JOIN member m ON( b.write_seq = m.seq ) "
					+ "LEFT OUTER JOIN membership ms ON( b.seq = ms.board_seq ) "
					+ "LEFT OUTER JOIN image i ON( b.seq = i.board_seq ) "
					+ "LEFT OUTER JOIN tag t ON( b.seq = t.board_seq ) "
					+ "LEFT OUTER JOIN category c ON( b.category_seq = c.seq ) "
					+ "WHERE c.seq <= 9 && b.status=1 group BY b.seq;";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				SearchTO sto = new SearchTO();
				sto.setB_seq( rs.getInt("b.seq") );
				sto.setTitle( rs.getString( "b.title" ) );
				sto.setCategory_seq( rs.getInt( "b.category_seq" ) );
				sto.setAddress( rs.getString( "m.address" ) );
				sto.setPrice( rs.getInt( "ms.price" ) );
				sto.setImage_name(rs.getString( "i.name" ) );
				sto.setTag( rs.getString( "t.tag" ) );
				sto.setTopic( rs.getString( "c.topic" ) );
				
				searchResluts.add(sto);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println( "[에러] " + e.getMessage() );
		} finally {
			if(rs != null) try {rs.close();} catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close();} catch(SQLException e) {}
			if(conn != null) try {conn.close();} catch(SQLException e) {}
		}
		    
		return searchResluts;
	}
}
