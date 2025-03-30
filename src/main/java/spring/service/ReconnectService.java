package spring.service;

import spring.dto.ReconnectModelDTO;
import java.util.List;

public interface ReconnectService {
    List<ReconnectModelDTO> getOutOfTouchContacts();
} 