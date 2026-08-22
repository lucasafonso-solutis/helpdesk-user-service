package solutis.lucas.afonso.helpdesk.messaging;

public record TechnicianAssignmentEvent(Long ticketId, Long technicianId) {
}