package com.project.vestiart.services.database;

import com.project.vestiart.models.Idea;
import com.project.vestiart.models.RequestInput;
import com.project.vestiart.repositories.IRequestInputRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestInputService {

    private final IRequestInputRepository IRequestInputRepository;

    public RequestInputService(IRequestInputRepository IRequestInputRepository) {
        this.IRequestInputRepository = IRequestInputRepository;
    }

    public void saveRequestInput(RequestInput requestInput)
    {
        IRequestInputRepository.save(requestInput);
    }

    @Transactional
    public void deleteRequestInput(Idea idea) { IRequestInputRepository.deleteRequestInputsByIdea_Id(idea.getId()); }
}
