package u.tallerin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import u.tallerin.domain.entity.Service;
import u.tallerin.exception.ResourceNotFoundException;
import u.tallerin.repository.ServiceRepository;
import u.tallerin.service.ServiceCatalogService;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    private final ServiceRepository serviceRepository;

    @Override
    public Service registerService(Service service) {
        return serviceRepository.save(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> listAll() {
        return serviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Service getById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + id));
    }
}
