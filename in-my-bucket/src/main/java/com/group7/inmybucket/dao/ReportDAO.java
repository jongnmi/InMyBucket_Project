package com.group7.inmybucket.dao;

import java.util.List;

import com.group7.inmybucket.dto.ReportDTO;

public interface ReportDAO {
	// 신고 테이블에 신고글 등록
	public int bucketReportInsert(ReportDTO dto);
	// 신고 목록
	public List<ReportDTO> bucketReportAllSelect();
	// 신고 보기
	public ReportDTO bucketReportSelect(int report_no);
	// 신고 처리상황 변경
	public int bucketReportEditUpdate(ReportDTO dto);
	// 신고 글 삭제
	public int bucketReportDelete(int report_no);
}
