package com.group7.inmybucket.controller;

import java.util.Comparator;

import com.group7.inmybucket.vo.RegisterVO;

public class MyObjectComparator  implements Comparator<RegisterVO> {

	@Override
	public int compare(RegisterVO o1, RegisterVO o2) {
		RegisterVO obj1 = (RegisterVO) o1;
		RegisterVO obj2 = (RegisterVO) o2;
        return obj1.getUsername().compareTo(obj2.getUsername());
	}

}
