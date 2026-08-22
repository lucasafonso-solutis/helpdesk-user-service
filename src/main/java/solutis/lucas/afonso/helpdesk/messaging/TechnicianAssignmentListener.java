package solutis.lucas.afonso.helpdesk.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import solutis.lucas.afonso.helpdesk.config.RabbitMQConfig;
import solutis.lucas.afonso.helpdesk.entities.UserRole;
import solutis.lucas.afonso.helpdesk.repository.UserRepository;

@Component
public class TechnicianAssignmentListener {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String responseRoutingKey;

    public TechnicianAssignmentListener(UserRepository userRepository, ObjectMapper objectMapper,
            RabbitTemplate rabbitTemplate,
            @Value("${helpdesk.rabbitmq.exchange}") String exchange,
            @Value("${helpdesk.rabbitmq.response-routing-key}") String responseRoutingKey) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.responseRoutingKey = responseRoutingKey;
    }

    @RabbitListener(queues = RabbitMQConfig.TECHNICIAN_ASSIGNMENT_QUEUE)
    public void assign(String payload) throws Exception {
        TechnicianAssignmentEvent event = objectMapper.readValue(payload, TechnicianAssignmentEvent.class);
        boolean accepted = event.ticketId() != null && event.technicianId() != null
                && userRepository.findById(event.technicianId())
                        .filter(user -> Boolean.TRUE.equals(user.getActive()))
                        .filter(user -> UserRole.TECHNICIAN.equals(user.getUserRole()))
                        .isPresent();

        TechnicianAssignmentResult result = new TechnicianAssignmentResult(
                event.ticketId(), event.technicianId(), accepted);
        rabbitTemplate.convertAndSend(exchange, responseRoutingKey, objectMapper.writeValueAsString(result));
    }
}