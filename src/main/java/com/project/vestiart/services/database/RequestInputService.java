package com.project.vestiart.services.database;

import com.project.vestiart.models.Idea;
import com.project.vestiart.models.RequestInput;
import com.project.vestiart.repositories.RequestInputRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void deleteRequestInput(Idea idea) { requestInputRepository.deleteRequestInputsByIdea_Id(idea.getId()); }
}
