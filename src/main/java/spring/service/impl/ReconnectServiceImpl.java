package spring.service.impl;

import domain.ReconnectDomainService;
import domain.ReconnectModel;
import spring.service.ReconnectService;
import spring.dto.ReconnectModelDTO;
import spring.mapper.DomainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReconnectServiceImpl implements ReconnectService {
    private final ReconnectDomainService reconnectDomainService;

    @Autowired
    public ReconnectServiceImpl(ReconnectDomainService reconnectDomainService) {
        this.reconnectDomainService = reconnectDomainService;
    }

    @Override
    public List<ReconnectModelDTO> getOutOfTouchContacts() {
        return reconnectDomainService.getOutOfTouchContactList().stream()
                .map(DomainMapper::toDTO)
                .collect(Collectors.toList());
    }
} 