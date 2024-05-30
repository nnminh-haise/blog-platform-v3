package com.example.javaee.service;

import com.example.javaee.dto.CreateSubscriberDto;
import com.example.javaee.dto.UpdateSubscriberDto;
import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Subscriber;
import com.example.javaee.repository.SubscriberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriberService {
    private SubscriberRepository subscriberRepository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public ServiceResponse<Subscriber> create(CreateSubscriberDto payload) {
        LocalDateTime timestamp = LocalDateTime.now();
        Subscriber newSubscriber = new Subscriber();
        newSubscriber.setEmail(payload.getEmail());
        newSubscriber.setFullName(payload.getFullName());
        newSubscriber.setCreateAt(timestamp);
        newSubscriber.setUpdateAt(timestamp);

        RepositoryResponse<Subscriber> response = this.subscriberRepository.create(newSubscriber);
        if (response.getError().equals(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }

        return ServiceResponse.ofSuccess(
                "Created new subscriber!", null, newSubscriber);
    }

    public List<Subscriber> findAll() {
        return this.subscriberRepository.findAll();
    }

    public Optional<Subscriber> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        return this.subscriberRepository.findById(id);
    }

    public ServiceResponse<Subscriber> update(UUID id, UpdateSubscriberDto payload) {
        Optional<Subscriber> targetingSubscriber = this.subscriberRepository.findById(id);
        if (!targetingSubscriber.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Subcriber not found", "Cannot find any subcriber with the given ID");
        }

        Subscriber buffer = targetingSubscriber.get();
        buffer.setEmail(payload.getEmail());
        buffer.setFullName(payload.getEmail());
        buffer.setUpdateAt(LocalDateTime.now());
        RepositoryResponse<Subscriber> response = this.subscriberRepository.update(buffer);
        if (response.getError().equals(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofUnknownServerError(response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Subcriber updated successfully", response.getDescription(), buffer);
    }

    public ServiceResponse<Subscriber> remove(UUID id) {
        Optional<Subscriber> targetingSubscriber = this.subscriberRepository.findById(id);
        if (!targetingSubscriber.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Subcriber not found", "Cannot find any subcriber with the given ID");
        }

        Subscriber buffer = targetingSubscriber.get();
        buffer.setDeleteAt(LocalDateTime.now());
        RepositoryResponse<Subscriber> response = this.subscriberRepository.update(buffer);
        if (response.getError().equals(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofUnknownServerError(response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Subcriber deleted successfully", response.getDescription(), buffer);
    }
}
