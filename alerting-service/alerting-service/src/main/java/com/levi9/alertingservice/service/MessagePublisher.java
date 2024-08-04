package com.levi9.alertingservice.service;
import org.springframework.stereotype.Service;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.levi9.alertingservice.model.Message;



@Service
public class MessagePublisher {    
    
    private static final String TOPIC_ARN = "arn:aws:sns:us-east-2:752936117235:DetectedMaskTopic";    
    private final AmazonSNSClient amazonSNSClient;
    
    public MessagePublisher(AmazonSNSClient amazonSNSClient) {
        this.amazonSNSClient = amazonSNSClient;        
    }
  
    public void  publish(Message message) {
    	final PublishRequest publishRequest = new PublishRequest(TOPIC_ARN, "There is an employe that is not wearing a mask! alert ID: " + message.getId(), "New alert!");
        amazonSNSClient.publish(publishRequest);
    }
    
} 
