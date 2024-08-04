package com.levi9.imageprocessingservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class ImageProcessingConstraints {

	@Getter
	@Value("${constants.minImgWidth}")
	private int minImgWidth;
	@Getter
	@Value("${constants.minImgHeight}")
	private int minImgHeight;
	@Getter
	@Value("${constants.maxImgWidth}")
	private int maxImgWidth;
	@Getter
	@Value("${constants.maxImgHeight}")
	private int maxImgHeight;
	@Getter
	@Value("${constants.rectOverlapThreshold}")
	private int rectOverlapThreshold;
}
