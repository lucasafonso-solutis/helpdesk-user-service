package solutis.lucas.afonso.helpdesk.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String TECHNICIAN_VALIDATION_QUEUE = "user.technician.find";

    @Bean
    public DirectExchange helpdeskExchange(@Value("${helpdesk.rabbitmq.exchange}") String exchangeName) {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue technicianValidationQueue() {
        return QueueBuilder.durable(TECHNICIAN_VALIDATION_QUEUE).build();
    }

    @Bean
    public Binding technicianValidationBinding(Queue technicianValidationQueue,
            DirectExchange helpdeskExchange,
            @Value("${helpdesk.rabbitmq.routing-key}") String routingKey) {
        return BindingBuilder.bind(technicianValidationQueue)
                .to(helpdeskExchange)
                .with(routingKey);
    }

    @Bean
    public MessageConverter rabbitMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}