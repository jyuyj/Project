package com.maumgagym.facility.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityTO {

	//board
	int b_seq;
	int category_seq;
	String title;
	String content;
	int write_seq;
	String write_date;
	int status;
	
	//member
	int m_seq;
	String address;
	
	//membership
	int price;
	
	//tag
	String tag;
	
	//image
	String image_name;
	double image_size;
	
	
}
