package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.request.EventRequest;
import bf.gov.mtdpce.dto.response.EventResponse;
import bf.gov.mtdpce.dto.response.PaginatedResponse;
import bf.gov.mtdpce.entity.Event;
import bf.gov.mtdpce.entity.EventStatus;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    public PaginatedResponse<EventResponse> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventRepository.findByIsPublicTrueOrderByStartDateDesc(pageable);
        
        List<EventResponse> events = eventPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return new PaginatedResponse<>(events, page, size, eventPage.getTotalElements());
    }
    
    public List<EventResponse> getUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusMonths(3);
        return eventRepository.findUpcomingEvents(now, end).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé avec l'id: " + id));
        return mapToResponse(event);
    }
    
    public EventResponse createEvent(EventRequest request) {
        Event event = new Event();
        mapRequestToEntity(request, event);
        Event saved = eventRepository.save(event);
        return mapToResponse(saved);
    }
    
    public EventResponse updateEvent(Long id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé avec l'id: " + id));
        mapRequestToEntity(request, event);
        Event updated = eventRepository.save(event);
        return mapToResponse(updated);
    }
    
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Événement non trouvé avec l'id: " + id);
        }
        eventRepository.deleteById(id);
    }
    
    public PaginatedResponse<EventResponse> searchEvents(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventRepository.searchEvents(query, pageable);
        
        List<EventResponse> events = eventPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return new PaginatedResponse<>(events, page, size, eventPage.getTotalElements());
    }
    
    private void mapRequestToEntity(EventRequest request, Event event) {
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setContent(request.getContent());
        event.setStatut(request.getStatut());
        event.setEndDate(request.getEndDate());
        event.setLocation(request.getLocation());
        event.setImageUrl(request.getImageUrl());
        event.setCategory(request.getCategory());
        event.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : true);
        event.setMaxParticipants(request.getMaxParticipants());
        event.setRegistrationRequired(request.getRegistrationRequired() != null ? request.getRegistrationRequired() : false);
    }
    
    private EventResponse mapToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setContent(event.getContent());
        response.setStatut(event.getStatut());
        response.setStartDate(event.getStartDate());
        response.setEndDate(event.getEndDate());
        response.setLocation(event.getLocation());
        response.setImageUrl(event.getImageUrl());
        response.setCategory(event.getCategory());
        response.setIsPublic(event.getIsPublic());
        response.setMaxParticipants(event.getMaxParticipants());
        response.setCurrentParticipants(event.getCurrentParticipants());
        response.setRegistrationRequired(event.getRegistrationRequired());
        response.setIsRegistrationOpen(event.getMaxParticipants() == null || 
                event.getCurrentParticipants() < event.getMaxParticipants());
        response.setStatus(event.getStatus().name());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());
        if (event.getCreatedBy() != null) {
            response.setCreatedBy(event.getCreatedBy().getUsername());
        }
        return response;
    }
}
