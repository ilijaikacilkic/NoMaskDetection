 package com.levi9.imageprocessingservice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.levi9.imageprocessingservice.exception.InvalidImageDimensionsException;
import com.levi9.imageprocessingservice.exception.NoFileSubmittedException;
import com.levi9.imageprocessingservice.exception.UnsupportedFileTypeException;
import com.levi9.imageprocessingservice.rabbitMQ.AlertMQConfig;
import com.levi9.imageprocessingservice.rabbitMQ.AlertMessage;
import com.levi9.imageprocessingservice.util.ImageProcessingConstraints;

import lombok.RequiredArgsConstructor;
import nu.pattern.OpenCV;


@Service
@RequiredArgsConstructor
public class ImageProcessingService {
	
	private final RabbitTemplate rabbitTemplate;
	
	private final ImageProcessingConstraints constraints;
	
	private static final String[] supportedTypes = {"image/apng", "image/bmp", "image/x-portable-bitmap", "image/x-portable-graymap",
			"image/x-portable-pixmap", "image/x-portable-anymap", "image/tiff", "image/jpeg", "image/png", "image/webp"};
	
	@PostConstruct
	private void init() {
		OpenCV.loadLocally();
	}
		
	public void processImage(MultipartFile file, String uploader) throws IOException, UnsupportedFileTypeException, InvalidImageDimensionsException, NoFileSubmittedException {
		validateImage(file);
		
		Mat image =scaleImage( Imgcodecs.imdecode(new MatOfByte(file.getBytes()), Imgcodecs.IMREAD_UNCHANGED));

		Map<Mat,Rect> faces = faceDetection(imageSharpeing(image));
		
		if (faces.isEmpty()) return;
		
		List<Rect> facesWithoutMask=new ArrayList<Rect>();
		for (Map.Entry<Mat,Rect> face: faces.entrySet())	
			if (isUnmasked(face.getKey()))
				facesWithoutMask.add(face.getValue());

		if(!facesWithoutMask.isEmpty())
			sendAlert(drawRect(image,facesWithoutMask), uploader);
	}

	private Mat scaleImage(Mat image) {
		Mat mat=new Mat();

 		if (image.rows() > constraints.getMaxImgHeight() || image.cols() > constraints.getMaxImgWidth()) {
 			if(image.rows()<image.cols())
 				Imgproc.resize(image, mat, new Size(constraints.getMaxImgWidth(),(constraints.getMaxImgWidth()*image.rows())/image.cols()));
 			else
 				Imgproc.resize(image, mat, new Size((constraints.getMaxImgWidth()*image.cols())/image.rows(),constraints.getMaxImgWidth()));
 		}
 		else if (image.rows() < constraints.getMinImgHeight() || image.cols() < constraints.getMinImgWidth())
 			if(image.rows()>image.cols())
				Imgproc.resize(image, mat, new Size(constraints.getMinImgHeight(),(constraints.getMinImgHeight()*image.rows())/image.cols()));
			else
				Imgproc.resize(image, mat, new Size((constraints.getMinImgWidth()*image.cols())/image.rows(),constraints.getMinImgWidth()));
		else return image;
		
		return mat;	
	}
	
	private void validateImage(MultipartFile file) throws UnsupportedFileTypeException, IOException, InvalidImageDimensionsException, NoFileSubmittedException {
 		if (file.getSize() == 0)
 			throw new NoFileSubmittedException("No file submitted.");
		
 		if (!Arrays.asList(supportedTypes).contains(file.getContentType()))
 			throw new UnsupportedFileTypeException("Unsupported file type.");
 	}
 	
 	private Map<Mat,Rect> faceDetection(Mat mat) throws IOException {
 		MatOfRect mateOfReactAlt=faceDetectionHaarcascade(mat,"xml/haarcascade_frontalface_alt.xml");
 		MatOfRect mateOfReactAlt2=faceDetectionHaarcascade(mat,"xml/haarcascade_frontalface_alt2.xml");
 	
 		if(mateOfReactAlt.toArray().length == 0 && mateOfReactAlt2.toArray().length == 0) return new HashMap<Mat,Rect>();
 		
 		return cutFaces(mat, mateOfReactAlt, mateOfReactAlt2);
 	}
 	
 	private boolean isUnmasked(Mat face) {
 		MatOfRect mateOfReactNose=faceDetectionHaarcascade(face,"xml/haarcascade_mcs_nose.xml");
		MatOfRect mateOfReactMouth=faceDetectionHaarcascade(face,"xml/haarcascade_mcs_mouth.xml");
		
		if((mateOfReactMouth.toArray().length!=0 && mateOfReactNose.toArray().length!=0) || mateOfReactNose.toArray().length!=0)
 			 return true;
 		return false;
 	}
 	
 	private Mat drawRect(Mat original,List<Rect> facesWithoutMask) {

	 		for (Rect rect : facesWithoutMask)
	 	         Imgproc.rectangle(
	 	            original,
	 	            new Point(rect.x, rect.y),
	 	            new Point(rect.x + rect.width, rect.y + rect.height),
	 	            new Scalar(0, 0, 255),
	 	            3
	 	         );

		return original;
	}
 	
 	private void sendAlert(Mat face, String uploader) {
 		MatOfByte matOfByte = new MatOfByte();
	    Imgcodecs.imencode(".jpg", face, matOfByte);
	    byte[] byteArray = matOfByte.toArray();
	    
	    AlertMessage alert = new AlertMessage(new Date(), byteArray, uploader);
	    rabbitTemplate.convertAndSend(AlertMQConfig.EXCHANGE_NAME, AlertMQConfig.ROUTING_KEY, alert);
 	}
 	
 	private Mat imageSharpeing(Mat mat) {
 		Mat destination = new Mat(mat.rows(),mat.cols(),mat.type());
 		Imgproc.GaussianBlur(mat, destination, new Size(5,5), 25);
        Core.addWeighted(mat, 1.5, destination, -0.5, 0, destination);
        
        return destination;
 	}
 	
 	
 	private MatOfRect faceDetectionHaarcascade(Mat mat,String haarcascade) {
 		CascadeClassifier cc = new CascadeClassifier(haarcascade);	
 		MatOfRect faceDetection = new MatOfRect();
 		Mat gray = new Mat();
 		Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
 		Imgproc.equalizeHist(gray, gray);
 		
 		cc.detectMultiScale(gray, faceDetection);
 		
 		return faceDetection;
 	}
 	
 	private Map<Mat,Rect> cutFaces(Mat mat, MatOfRect faceDetectionAlt,MatOfRect faceDetectionAlt2)  {
 		Map<Mat,Rect> faces = new HashMap<Mat,Rect>();
 		
 		for(Rect rect : faceDetectionAlt.toList())
 			faces.put(new Mat(mat, rect),rect);
 		
 		for(Rect rect :faceDetectionAlt2.toList()) {
 			boolean contained = false;

 			for(Rect rectCheck : faceDetectionAlt.toList())
 				if(Math.abs(rect.x - rectCheck.x) < constraints.getRectOverlapThreshold() && Math.abs((rect.x + rect.width) - (rectCheck.x + rectCheck.width)) < constraints.getRectOverlapThreshold() &&
 						Math.abs(rect.y - rectCheck.y) < constraints.getRectOverlapThreshold() && Math.abs((rect.y + rect.height) - (rectCheck.y + rectCheck.height)) < constraints.getRectOverlapThreshold()) {
 					contained = true;
 					break;
 				}
 			
 			if(!contained)
 	 			faces.put(new Mat(mat, rect),rect);
 		}
 		return faces;
 	}
 }