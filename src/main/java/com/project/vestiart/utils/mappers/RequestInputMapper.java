package com.project.vestiart.utils.mappers;

import com.project.vestiart.dto.RequestInputDTO;
import com.project.vestiart.enums.TypeEnum;
import com.project.vestiart.input.RequestInput;
import org.springframework.stereotype.Service;

@Service
public class RequestInputMapper {

    public RequestInput mapRequestInputDTOintiRequestInput (RequestInputDTO requestInputDTO){

        return RequestInput.builder()
                .person(requestInputDTO.getPerson())
                .reference(requestInputDTO.getReference())
                .type(TypeEnum.findTypeEnumByType(requestInputDTO.getType()).getId())
                .build();

    }

}
