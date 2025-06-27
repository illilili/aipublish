package aipublish.infra;

import aipublish.config.kafka.KafkaProcessor;
import aipublish.domain.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointRepository pointRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverWelcomePointGranted_CreatePoint(@Payload WelcomePointGranted event) {
    if (!event.validate()) return;

    System.out.println("##### listener WelcomePointGranted : " + event.toJson());

    Point point = new Point();
    point.setUserId(event.getUserId());
    point.setBalance(event.getAmount());
    pointRepository.save(point);
}
}
//>>> Clean Arch / Inbound Adaptor
