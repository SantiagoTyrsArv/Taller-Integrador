package u.tallerin.service;

import u.tallerin.domain.entity.Service;

import java.util.List;

public interface ServiceCatalogService {

    Service registerService(Service service);

    List<Service> listAll();

    Service getById(Long id);
}
