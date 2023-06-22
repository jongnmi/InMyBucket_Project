package com.group7.inmybucket.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.group7.inmybucket.dto.BucketDTO;
import com.group7.inmybucket.dto.ImageFileDTO;
import com.group7.inmybucket.vo.LikeVO;


public interface BucketDAO {
	// 버킷리스트 등록
	public int bucketInsert(BucketDTO dto);
	// 버킷리스트 등록 실패시 삭제
	public int bucketDelete(int bucket_no);
	// 이미지파일 등록
	public int imageFileInsert(List<ImageFileDTO> filelist);
	// 버킷리스트 목록
	public List<BucketDTO> bucketAllSelect();
	// 버킷리스트 보기
	public BucketDTO bucketSelect(int bucket_no);
	// 이미지 선택
	public List<ImageFileDTO> imageFileSelect(int bucket_no);
	// 원글 가져오기(수정폼) 
	public BucketDTO bucketEditSelect(int bucket_no, String userid);
	// 수정된 이미지 파일 리스트
	public List<String> imageFilenameList(int bucket_no);
	// 수정후 버킷리스트 업데이트
	public int bucketEditUpdate(BucketDTO dto);
	// 수정후 지원진것들 삭제하기
	public int bucketEditDelete(int bucket_no, String userid);
	// 이미지 삭제
	public int imageFileDelete(int bucket_no);
	// 이미지 업데이트
	public int imageFileEditInsert(List<ImageFileDTO> imageDTOList);
	// 원글정보 가져오기(신고폼)
	public BucketDTO bucketReportSelect(int bucket_no, String userid);
	// 좋아요 추가
	public int bucketSaveLike(LikeVO vo); 
	// 좋아요 +1
	public int bucketLikeUp(LikeVO vo); 
	// 좋아요 삭제
	public int bucketRemoveLike(LikeVO vo);
	// 좋아요 -1
	public int bucketLikeDown(LikeVO vo);
	// 좋아요수 가져오기
	public int bucketLikeCount(LikeVO vo);
	// 좋아요 했으면 like_no 리턴
	public Integer isLike(LikeVO vo);
	// 댓글 수
	public int commentCount(int bucket_no);
	// 그룹 멤버 닉네임 리스트
	public List<String> groupMemberNick(int bucket_no);
	// 그룹 참여요청 여부
	public int isGroupRequest(int bucket_no, String userid);
	// 그룹 참여요청
	public int groupQueueInsert(int bucket_no, String userid);
	// 그룹시 자신을 그룹멤버로 추가
	public int groupMemberInsert(BucketDTO dto);
}
