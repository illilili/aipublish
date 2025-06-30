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
    AiBookProcessorRepository aiBookProcessorRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubmittedEvent'"
    )
    public void wheneverBookSubmittedEvent_TriggerAiPublishingOnBookSubmit(
        @Payload BookSubmittedEvent bookSubmittedEvent
    ) {
        BookSubmittedEvent event = bookSubmittedEvent;
        System.out.println(
            "\n\n##### listener TriggerAiPublishingOnBookSubmit : " +
            bookSubmittedEvent +
            "\n\n"
        );

        // Comments //
        //작가가 출간 요청을 제출하면 AI 자동화 프로세스를 시작하여 요약, 표지 생성, 포맷 변환, 카테고리 분류, 요금 산정 등 일련의 자동화 작업을 수행하기 위함입니다.

        // Sample Logic //

        StartAiPublishingCommand command = new StartAiPublishingCommand();
        //command.setBookId("???");
        // AiBookProcessor.startAiPublishing(command);
    }
}
//>>> Clean Arch / Inbound Adaptor
