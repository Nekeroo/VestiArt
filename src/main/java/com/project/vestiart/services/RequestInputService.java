package com.project.vestiart.services;

import com.project.vestiart.input.RequestInput;
import com.project.vestiart.repositories.RequestInputRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestInputService {

    private final RequestInputRepository requestInputRepository;

    public RequestInputService(RequestInputRepository requestInputRepository) {
        this.requestInputRepository = requestInputRepository;
    }

    public void saveRequestInput(RequestInput requestInput)
    {
        requestInputRepository.save(requestInput);
    }
}
