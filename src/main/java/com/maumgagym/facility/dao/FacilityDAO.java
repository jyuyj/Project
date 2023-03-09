package com.maumgagym.facility.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maumgagym.facility.dto.FacilityTO;

@Repository
public class FacilityDAO {
	
	@Autowired
	private DataSource dataSource;
	
	public ArrayList<FacilityTO> facilityList() {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<FacilityTO> facilityLists = new ArrayList<FacilityTO>();
		
		try {
			conn = this.dataSource.getConnection();
			
			// 글 게시판의 title을 통한 업체 정보, 멤버 게시판의 address을 통한 주소 정보, 멤버쉽 게시판의 price를 통한 가격 정보, 이미지 게시판의 name을 통한 이미지 파일, 태그 게시판의 tag를 통한 글 태그를 가져옴
			// 카테고리 게시판의 seq는 1~9번(운동시설)으로 가져옴
			String sql = "SELECT b.seq, b.title, b.category_seq, m.address, ms.price, i.name, t.tag "
					+ "FROM board b LEFT OUTER JOIN member m ON( b.write_seq = m.seq )"
					+ "LEFT OUTER JOIN membership ms ON( b.seq = ms.board_seq )"
					+ "LEFT OUTER JOIN image i ON( b.seq = i.board_seq )"
					+ "LEFT OUTER JOIN tag t ON( b.seq = t.board_seq )"
					+ "LEFT OUTER JOIN category c ON( b.category_seq = c.seq )"
					+ "WHERE c.seq <= 9 && b.status=1 group BY b.seq ORDER BY b.seq DESC;";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				FacilityTO fto = new FacilityTO();
				
				fto.setB_seq( rs.getInt("b.seq") );
				fto.setTitle( rs.getString( "b.title" ) );
				fto.setCategory_seq( rs.getInt( "b.category_seq" ) );
				fto.setAddress( rs.getString( "m.address" ) );
				fto.setPrice( rs.getInt( "ms.price" ) );
				fto.setImage_name(rs.getString( "i.name" ) );
				fto.setTag( rs.getString( "t.tag" ) );
				
				facilityLists.add(fto);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println( "[에러] " + e.getMessage() );
		} finally {
			if( pstmt != null) try { pstmt.close(); } catch( SQLException e ) {}
			if( conn != null) try { conn.close(); } catch( SQLException e ) {}
			if( rs != null) try { rs.close(); } catch( SQLException e ) {}
		}
		
		return facilityLists; 
		
	}
	
	public FacilityTO selectfacilityBoard( FacilityTO fto ) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
		conn = dataSource.getConnection();
		
		String sql = "select seq from board "
				+ "		WHERE write_seq = ? "
				+ "			ORDER BY write_date DESC "
				+ "				LIMIT 0,1";
		
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, fto.getWrite_seq());
		
		rs = pstmt.executeQuery();
		
		if( rs.next() ) {
			fto.setB_seq( rs.getInt("seq") );
		}
		
		} catch( SQLException e) {
			System.out.println( "[에러]" + e.getMessage());
		} finally {
			if( pstmt != null) try { pstmt.close(); } catch( SQLException e ) {}
			if( conn != null) try { conn.close(); } catch( SQLException e ) {}
		}
		
		return fto;
	}
	
	public int insertfacilityBoard( FacilityTO fto ) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int flag = 1;
		
		try {
			
		conn = dataSource.getConnection();
		
		String sql = "insert into board values (0, ?, ?, ?, ?, now(), 1)";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1, fto.getCategory_seq() );
		pstmt.setString(2, fto.getTitle() );
		pstmt.setString(3, fto.getContent() );
		pstmt.setInt(4, fto.getWrite_seq() );
		
		if( pstmt.executeUpdate() == 1) {
			flag = 0;
		}
		
		} catch( SQLException e) {
			System.out.println( "[에러]" + e.getMessage());
		} finally {
			if( pstmt != null) try { pstmt.close(); } catch( SQLException e ) {}
			if( conn != null) try { conn.close(); } catch( SQLException e ) {}
		}
		
		return flag;
	}
	
	public int insertfacilityImage( FacilityTO fto ) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int flag = 1;
		
		try {
			
		conn = dataSource.getConnection();
		
		String sql = "insert into image values (0, ?, ?, ?)";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, fto.getImage_name() );
		pstmt.setDouble(2, fto.getImage_size() );
		pstmt.setInt(3, fto.getB_seq() );
		
		if( pstmt.executeUpdate() == 1) {
			flag = 0;
		} else {
			flag = 1;
		}
		
		} catch( SQLException e) {
			System.out.println( "[에러]" + e.getMessage());
		} finally {
			if( pstmt != null) try { pstmt.close(); } catch( SQLException e ) {}
			if( conn != null) try { conn.close(); } catch( SQLException e ) {}
		}
		
		return flag;
	}
	
	public int insertfacilityMembership( FacilityTO fto ) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int flag = 1;
		
		try {
			
		conn = dataSource.getConnection();
		
		String sql = "insert into membership values (0, ?, ?, ?, ?, ?)";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1, fto.getWrite_seq() );
		pstmt.setString(2, fto.getMs_name()  );
		pstmt.setInt(3, fto.getPrice() );
		pstmt.setInt(4, fto.getPeriod() );
		pstmt.setInt(5, fto.getB_seq() );
		
		if( pstmt.executeUpdate() == 1) {
			flag = 0;
		} else {
			flag = 1;
		}
		
		} catch( SQLException e) {
			System.out.println( "[에러]" + e.getMessage());
		} finally {
			if( pstmt != null) try { pstmt.close(); } catch( SQLException e ) {}
			if( conn != null) try { conn.close(); } catch( SQLException e ) {}
		}
		
		return flag;
	}


}

