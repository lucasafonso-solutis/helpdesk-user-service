package solutis.lucas.afonso.helpdesk.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import solutis.lucas.afonso.helpdesk.config.RabbitMQConfig;
import solutis.lucas.afonso.helpdesk.entities.UserRole;
import solutis.lucas.afonso.helpdesk.repository.UserRepository;

@Component
public class TechnicianValidationListener {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public TechnicianValidationListener(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.TECHNICIAN_VALIDATION_QUEUE)
    public TechnicianValidationResponse validate(String payload) throws Exception {
        TechnicianValidationRequest request = objectMapper.readValue(payload, TechnicianValidationRequest.class);
        if (request.technicianId() == null) {
            return new TechnicianValidationResponse(null);
        }

        return userRepository.findById(request.technicianId())
                .filter(user -> Boolean.TRUE.equals(user.getActive()))
                .filter(user -> UserRole.TECHNICIAN.equals(user.getUserRole()))
                .map(user -> new TechnicianValidationResponse(user.getId()))
                .orElseGet(() -> new TechnicianValidationResponse(null));
    }
}